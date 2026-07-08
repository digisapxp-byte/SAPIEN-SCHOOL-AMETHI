package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.Lesson
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppViewModel

sealed class Screen {
    object Welcome : Screen()
    object Login : Screen()
    object Register : Screen()
    object Dashboard : Screen()
    data class LessonDetail(val lesson: Lesson) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
                val viewModel: AppViewModel = viewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Beautiful smooth fade transition between views
                    Crossfade(
                        targetState = currentScreen,
                        label = "screen_transitions"
                    ) { screen ->
                        when (screen) {
                            is Screen.Welcome -> {
                                WelcomeScreen(
                                    onNavigateToLogin = { currentScreen = Screen.Login },
                                    onNavigateToRegister = { currentScreen = Screen.Register }
                                )
                            }
                            is Screen.Login -> {
                                LoginScreen(
                                    viewModel = viewModel,
                                    onNavigateBack = { currentScreen = Screen.Welcome },
                                    onNavigateToRegister = { currentScreen = Screen.Register },
                                    onLoginSuccess = { currentScreen = Screen.Dashboard }
                                )
                            }
                            is Screen.Register -> {
                                RegisterScreen(
                                    viewModel = viewModel,
                                    onNavigateBack = { currentScreen = Screen.Welcome },
                                    onRegisterSuccess = { currentScreen = Screen.Dashboard }
                                )
                            }
                            is Screen.Dashboard -> {
                                DashboardScreen(
                                    viewModel = viewModel,
                                    onSelectLesson = { lesson -> currentScreen = Screen.LessonDetail(lesson) },
                                    onLogout = {
                                        viewModel.logout()
                                        currentScreen = Screen.Welcome
                                    }
                                )
                            }
                            is Screen.LessonDetail -> {
                                LessonDetailScreen(
                                    viewModel = viewModel,
                                    lesson = screen.lesson,
                                    onNavigateBack = { currentScreen = Screen.Dashboard }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
