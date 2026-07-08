package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    data class Success(val user: User) : AuthState
    data class Error(val message: String) : AuthState
}

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = UserRepository(db.userDao())

    // App state flows
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userProgress = MutableStateFlow<List<Progress>>(emptyList())
    val userProgress: StateFlow<List<Progress>> = _userProgress.asStateFlow()

    private val _selectedLesson = MutableStateFlow<Lesson?>(null)
    val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

    // Loaded lessons
    val lessons: List<Lesson> = repository.getLessons()

    init {
        // Collect progress automatically when user changes
        viewModelScope.launch {
            _currentUser.collectLatest { user ->
                if (user != null) {
                    repository.getProgressForUser(user.id).collect { progressList ->
                        _userProgress.value = progressList
                    }
                } else {
                    _userProgress.value = emptyList()
                }
            }
        }
    }

    fun login(username: String, passwordText: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (username.isBlank() || passwordText.isBlank()) {
                _authState.value = AuthState.Error("Fields cannot be empty!")
                return@launch
            }

            val user = withContext(Dispatchers.IO) {
                repository.getUserByUsername(username.lowercase().trim())
            }

            if (user == null) {
                _authState.value = AuthState.Error("Username not found!")
            } else if (user.passwordHash != passwordText) {
                _authState.value = AuthState.Error("Incorrect password!")
            } else {
                // Check daily streak
                val updatedUser = updateStreak(user)
                withContext(Dispatchers.IO) {
                    repository.updateUser(updatedUser)
                }
                _currentUser.value = updatedUser
                _authState.value = AuthState.Success(updatedUser)
            }
        }
    }

    fun register(name: String, username: String, passwordText: String, avatar: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (name.isBlank() || username.isBlank() || passwordText.isBlank()) {
                _authState.value = AuthState.Error("Please fill in all fields!")
                return@launch
            }

            val cleanedUsername = username.lowercase().trim()
            val existing = withContext(Dispatchers.IO) {
                repository.getUserByUsername(cleanedUsername)
            }

            if (existing != null) {
                _authState.value = AuthState.Error("Username is already taken!")
            } else {
                val newUser = User(
                    name = name.trim(),
                    username = cleanedUsername,
                    passwordHash = passwordText,
                    avatarEmoji = avatar,
                    points = 50, // Starter bonus!
                    coins = 10,  // Starter bonus!
                    streak = 1,
                    lastActiveTimestamp = System.currentTimeMillis()
                )
                val userId = withContext(Dispatchers.IO) {
                    repository.registerUser(newUser)
                }
                val registeredUser = newUser.copy(id = userId.toInt())
                _currentUser.value = registeredUser
                _authState.value = AuthState.Success(registeredUser)
            }
        }
    }

    fun selectLesson(lesson: Lesson?) {
        _selectedLesson.value = lesson
    }

    fun completeLesson(lesson: Lesson, correctAnswers: Int, totalQuestions: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            // Calculate stars
            val ratio = correctAnswers.toDouble() / totalQuestions.toDouble()
            val stars = when {
                ratio >= 1.0 -> 3
                ratio >= 0.6 -> 2
                ratio >= 0.3 -> 1
                else -> 0
            }

            // Gamified bonuses: perfect score gets extra!
            var earnedPoints = lesson.pointsReward
            var earnedCoins = lesson.coinsReward
            if (stars == 3) {
                earnedPoints += 50 // Perfect score bonus
                earnedCoins += 10  // Perfect score bonus
            }

            // Save or Update Progress
            val existingProgress = withContext(Dispatchers.IO) {
                repository.getProgressForUserAndLesson(user.id, lesson.id)
            }

            // Only award points if it's the first time completing, or if they improved their star score
            val shouldReward = existingProgress == null || stars > existingProgress.starsEarned

            if (shouldReward) {
                val actualPointsGained = if (existingProgress == null) earnedPoints else (earnedPoints / 2) // half reward on replay improvement
                val actualCoinsGained = if (existingProgress == null) earnedCoins else (earnedCoins / 2)

                val updatedUser = user.copy(
                    points = user.points + actualPointsGained,
                    coins = user.coins + actualCoinsGained
                )
                withContext(Dispatchers.IO) {
                    repository.updateUser(updatedUser)
                }
                _currentUser.value = updatedUser
            }

            val newProgress = Progress(
                id = existingProgress?.id ?: 0,
                userId = user.id,
                lessonId = lesson.id,
                starsEarned = maxOf(stars, existingProgress?.starsEarned ?: 0),
                score = ((correctAnswers.toDouble() / totalQuestions) * 100).toInt(),
                isCompleted = true,
                completedAtTimestamp = System.currentTimeMillis()
            )

            withContext(Dispatchers.IO) {
                repository.saveProgress(newProgress)
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    fun clearAuthError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Idle
        }
    }

    // Helper to calculate & update streak
    private fun updateStreak(user: User): User {
        val now = System.currentTimeMillis()
        val lastActive = user.lastActiveTimestamp
        
        val diffMs = now - lastActive
        val oneDayMs = 24 * 60 * 60 * 1000L
        
        return when {
            diffMs < oneDayMs -> {
                // Activated within the same day, preserve current streak
                user.copy(lastActiveTimestamp = now)
            }
            diffMs in oneDayMs..(2 * oneDayMs) -> {
                // Consecutive day! Increment streak and award streak coin bonus
                user.copy(
                    streak = user.streak + 1,
                    coins = user.coins + 5, // streak bonus coins!
                    lastActiveTimestamp = now
                )
            }
            else -> {
                // Missed a day, reset streak to 1
                user.copy(streak = 1, lastActiveTimestamp = now)
            }
        }
    }
}
