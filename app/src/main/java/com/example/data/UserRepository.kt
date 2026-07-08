package com.example.data

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getUserById(id: Int): Flow<User?> {
        return userDao.getUserById(id)
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun registerUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    fun getProgressForUser(userId: Int): Flow<List<Progress>> {
        return userDao.getProgressForUser(userId)
    }

    suspend fun getProgressForUserAndLesson(userId: Int, lessonId: String): Progress? {
        return userDao.getProgressForUserAndLesson(userId, lessonId)
    }

    suspend fun saveProgress(progress: Progress): Long {
        return userDao.insertProgress(progress)
    }

    fun getLessons(): List<Lesson> {
        return LessonData.lessons
    }
}
