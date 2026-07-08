package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Lesson
import com.example.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    viewModel: AppViewModel,
    lesson: Lesson,
    onNavigateBack: () -> Unit
) {
    // Current Mode: 0 = Reading Slides, 1 = Quiz, 2 = Results Summary
    var currentMode by remember { mutableStateOf(0) }
    var currentSlideIndex by remember { mutableStateOf(0) }
    
    // Quiz states
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var scoreCount by remember { mutableStateOf(0) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFDFBFF), // Off-white/light lavender grey
            Color(0xFFEADDFF)  // Soft light lavender
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(lesson.title, fontWeight = FontWeight.Bold, color = Color(0xFF001C38)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("lesson_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF001C38))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
        ) {
            when (currentMode) {
                0 -> {
                    // SLIDES READING MODE
                    SlideReaderSection(
                        lesson = lesson,
                        currentSlideIndex = currentSlideIndex,
                        onPrevSlide = { if (currentSlideIndex > 0) currentSlideIndex-- },
                        onNextSlide = {
                            if (currentSlideIndex < lesson.contentPages.size - 1) {
                                currentSlideIndex++
                            } else {
                                // Transition to Quiz mode
                                currentMode = 1
                            }
                        }
                    )
                }
                1 -> {
                    // ACTIVE QUIZ INTERACTION MODE
                    QuizSection(
                        lesson = lesson,
                        currentQuestionIndex = currentQuestionIndex,
                        selectedOptionIndex = selectedOptionIndex,
                        isAnswerSubmitted = isAnswerSubmitted,
                        onOptionSelect = { index ->
                            if (!isAnswerSubmitted) {
                                selectedOptionIndex = index
                            }
                        },
                        onSubmitAnswer = {
                            if (selectedOptionIndex != null) {
                                isAnswerSubmitted = true
                                val correctIdx = lesson.quizQuestions[currentQuestionIndex].correctAnswerIndex
                                if (selectedOptionIndex == correctIdx) {
                                    scoreCount++
                                }
                            }
                        },
                        onNextQuestion = {
                            if (currentQuestionIndex < lesson.quizQuestions.size - 1) {
                                currentQuestionIndex++
                                selectedOptionIndex = null
                                isAnswerSubmitted = false
                            } else {
                                // Finalize lesson and submit score to VM
                                viewModel.completeLesson(lesson, scoreCount, lesson.quizQuestions.size)
                                currentMode = 2
                            }
                        }
                    )
                }
                2 -> {
                    // QUIZ CELEBRATIVE RESULT SUMMARY MODE
                    QuizCelebrationSection(
                        lesson = lesson,
                        score = scoreCount,
                        onFinishQuest = onNavigateBack
                    )
                }
            }
        }
    }
}

@Composable
fun SlideReaderSection(
    lesson: Lesson,
    currentSlideIndex: Int,
    onPrevSlide: () -> Unit,
    onNextSlide: () -> Unit
) {
    val page = lesson.contentPages[currentSlideIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Page Progress Indicator Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(lesson.contentPages.size) { index ->
                val isActive = index == currentSlideIndex
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(8.dp)
                        .width(if (isActive) 24.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (isActive) Color(0xFF6750A4) else Color(0xFFEADDFF))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Slide Content Card
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Large visual Emoji Clue
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEADDFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = page.visualHintEmoji, fontSize = 56.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = page.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF001C38),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = page.body,
                    fontSize = 15.sp,
                    color = Color(0xFF44474E),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Button(
                onClick = onPrevSlide,
                enabled = currentSlideIndex > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF001C38)
                ),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = 0.15f)),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("slide_back")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back", fontWeight = FontWeight.Bold)
            }

            // Next / Play Quiz Button
            Button(
                onClick = onNextSlide,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6750A4),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("slide_next")
            ) {
                val isLast = currentSlideIndex == lesson.contentPages.size - 1
                Text(
                    text = if (isLast) "Start Quiz! ✏️" else "Next Slide",
                    fontWeight = FontWeight.Bold
                )
                if (!isLast) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }
}

@Composable
fun QuizSection(
    lesson: Lesson,
    currentQuestionIndex: Int,
    selectedOptionIndex: Int?,
    isAnswerSubmitted: Boolean,
    onOptionSelect: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val question = lesson.quizQuestions[currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Question tracker
        Text(
            text = "Question ${currentQuestionIndex + 1} of ${lesson.quizQuestions.size}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001C38)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Question Container Card
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = question.question,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001C38),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Options list
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            question.options.forEachIndexed { index, option ->
                val isSelected = selectedOptionIndex == index
                val isCorrect = index == question.correctAnswerIndex

                val cardColor = when {
                    isAnswerSubmitted && isCorrect -> Color(0xFFC2F0CB) // Green for correct answer
                    isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFFFDBCB) // Peach for wrong selected
                    isSelected -> Color(0xFFD3E4FF) // Sky blue highlight for select
                    else -> Color.White
                }

                val borderColor = when {
                    isAnswerSubmitted && isCorrect -> Color(0xFF072711)
                    isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFF311100)
                    isSelected -> Color(0xFF6750A4)
                    else -> Color(0xFFEADDFF)
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    border = BorderStroke(2.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isAnswerSubmitted) { onOptionSelect(index) }
                        .testTag("option_$index")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Letter badge: A, B, C, D
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isSelected || (isAnswerSubmitted && isCorrect)) Color.White else Color(0xFFEADDFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ('A' + index).toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF001C38)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = option,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF001C38),
                            modifier = Modifier.weight(1f)
                        )

                        if (isAnswerSubmitted) {
                            if (isCorrect) {
                                Icon(Icons.Default.Check, contentDescription = "Correct", tint = Color(0xFF072711))
                            } else if (isSelected) {
                                Icon(Icons.Default.Close, contentDescription = "Wrong", tint = Color(0xFF311100))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quiz feedback/explanation pane
        AnimatedVisibility(
            visible = isAnswerSubmitted,
            enter = fadeIn() + expandVertically()
        ) {
            val isCorrect = selectedOptionIndex == question.correctAnswerIndex
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect) Color(0xFFC2F0CB) else Color(0xFFFFDBCB)
                ),
                border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = 0.1f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isCorrect) "Excellent Job! 🎉" else "Nice Try! ✨",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isCorrect) Color(0xFF072711) else Color(0xFF311100)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = question.explanation,
                        fontSize = 13.sp,
                        color = if (isCorrect) Color(0xFF072711).copy(alpha = 0.8f) else Color(0xFF311100).copy(alpha = 0.8f),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action controls
        if (!isAnswerSubmitted) {
            Button(
                onClick = onSubmitAnswer,
                enabled = selectedOptionIndex != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_answer_button")
            ) {
                Text("Check My Answer! 🔍", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        } else {
            Button(
                onClick = onNextQuestion,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("next_question_button")
            ) {
                val isLast = currentQuestionIndex == lesson.quizQuestions.size - 1
                Text(
                    text = if (isLast) "See My Score! 🏆" else "Next Question ➡️",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun QuizCelebrationSection(
    lesson: Lesson,
    score: Int,
    onFinishQuest: () -> Unit
) {
    val total = lesson.quizQuestions.size
    val ratio = score.toDouble() / total.toDouble()
    val stars = when {
        ratio >= 1.0 -> 3
        ratio >= 0.6 -> 2
        ratio >= 0.3 -> 1
        else -> 0
    }

    // High contrast light theme outcome box
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🎉 Quest Completed! 🎉", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color(0xFF001C38))

        Spacer(modifier = Modifier.height(16.dp))

        // Celebrative Card
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dynamic Star graphics
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    repeat(3) { index ->
                        val isStarFilled = index < stars
                        Text(
                            text = if (isStarFilled) "⭐" else "☆",
                            fontSize = 48.sp,
                            color = if (isStarFilled) Color(0xFFFFD54F) else Color.LightGray
                        )
                    }
                }

                Text(
                    text = "Score: $score / $total",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF001C38)
                )

                Text(
                    text = when (stars) {
                        3 -> "Fantastic! Perfect Score! 🏆"
                        2 -> "Splendid! You did amazing! 🌟"
                        1 -> "Good job! Keep learning! 👍"
                        else -> "Nice try! Let's read again and get higher scores! 💪"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001C38),
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                    textAlign = TextAlign.Center
                )

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                // Rewards Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "⭐", fontSize = 28.sp)
                        Text(
                            text = "+${lesson.pointsReward}${if (stars == 3) " +50 Bonus!" else ""}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6750A4)
                        )
                        Text(text = "Points Earned", fontSize = 10.sp, color = Color(0xFF44474E))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🪙", fontSize = 28.sp)
                        Text(
                            text = "+${lesson.coinsReward}${if (stars == 3) " +10 Bonus!" else ""}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF001C38)
                        )
                        Text(text = "Coins Earned", fontSize = 10.sp, color = Color(0xFF44474E))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Finish Quest button
        Button(
            onClick = onFinishQuest,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
            shape = RoundedCornerShape(26.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("finish_quest_button")
        ) {
            Text("Finish Quest! 🏆", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
