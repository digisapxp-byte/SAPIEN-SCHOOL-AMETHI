package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val name: String,
    val passwordHash: String, // In a real app we'd hash, here simple password check is safe for local student app
    val avatarEmoji: String = "👦",
    val points: Int = 0,
    val coins: Int = 0,
    val streak: Int = 1,
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "progress")
data class Progress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val lessonId: String,
    val starsEarned: Int, // 1, 2, or 3 stars
    val score: Int,       // Percentage or points scored
    val isCompleted: Boolean = true,
    val completedAtTimestamp: Long = System.currentTimeMillis()
)

// In-Memory models for interactive lessons
data class Lesson(
    val id: String,
    val title: String,
    val category: String, // Math, Science, English, GK
    val difficulty: String, // Easy, Medium, Fun
    val description: String,
    val contentPages: List<LessonPage>,
    val quizQuestions: List<QuizQuestion>,
    val pointsReward: Int = 100,
    val coinsReward: Int = 10,
    val iconEmoji: String
)

data class LessonPage(
    val title: String,
    val body: String,
    val visualHintEmoji: String,
    val interactiveTask: String? = null // Optional quick check
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)
