package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Lesson
import com.example.data.Progress
import com.example.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: AppViewModel,
    onSelectLesson: (Lesson) -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Lessons, 1: Daily Progress

    // Geometric Balance backgrounds
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFDFBFF), // Off-white/light lavender grey
            Color(0xFFFDFBFF)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFD3E4FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("S", color = Color(0xFF001C38), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Sapien School",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF001C38),
                                fontSize = 16.sp,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = "AMETHI CAMPUS",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF44474E),
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onLogout, modifier = Modifier.testTag("dashboard_logout_button")) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color(0xFFBA1A1A))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Elegant navigation bar with colored active pills
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 1.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = "Lessons") },
                    label = { Text("Lessons", fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF001C38),
                        selectedTextColor = Color(0xFF001C38),
                        indicatorColor = Color(0xFFD3E4FF),
                        unselectedIconColor = Color(0xFF44474E),
                        unselectedTextColor = Color(0xFF44474E)
                    ),
                    modifier = Modifier.testTag("tab_lessons")
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "My Progress") },
                    label = { Text("Progress", fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF21005D),
                        selectedTextColor = Color(0xFF21005D),
                        indicatorColor = Color(0xFFEADDFF),
                        unselectedIconColor = Color(0xFF44474E),
                        unselectedTextColor = Color(0xFF44474E)
                    ),
                    modifier = Modifier.testTag("tab_progress")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
        ) {
            val user = currentUser
            if (user != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Quick stats header (Avatar + Points + Streak)
                    StudentProfileHeader(
                        avatarEmoji = user.avatarEmoji,
                        name = user.name,
                        points = user.points,
                        coins = user.coins,
                        streak = user.streak
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (selectedTab == 0) {
                        // Lessons List Screen
                        LessonsTab(
                            lessons = viewModel.lessons,
                            progressList = userProgress,
                            onSelectLesson = onSelectLesson
                        )
                    } else {
                        // Progress / Gamification Dashboard Screen
                        ProgressTab(
                            points = user.points,
                            coins = user.coins,
                            streak = user.streak,
                            lessons = viewModel.lessons,
                            progressList = userProgress
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun StudentProfileHeader(
    avatarEmoji: String,
    name: String,
    points: Int,
    coins: Int,
    streak: Int
) {
    Card(
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cute circular avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEADDFF)), // Lavender from theme
                contentAlignment = Alignment.Center
            ) {
                Text(text = avatarEmoji, fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hello, $name! 👋",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF001C38), // Midnight Blue
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Sapien Scholar • Amethi",
                    fontSize = 12.sp,
                    color = Color(0xFF44474E),
                    fontWeight = FontWeight.Bold
                )
            }

            // Quick Stats Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Streak
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFDBCB)) // Peach
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔥", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "$streak",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF311100)
                    )
                }

                // Points
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8DEF8)) // Lavender progress background
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⭐", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "$points",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF21005D)
                    )
                }

                // Coins
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFD3E4FF)) // Soft sky blue
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🪙", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "$coins",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF001C38)
                    )
                }
            }
        }
    }
}

@Composable
fun LessonsTab(
    lessons: List<Lesson>,
    progressList: List<Progress>,
    onSelectLesson: (Lesson) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Pick a Learning Quest! 📚🗺️",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001C38),
            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(lessons) { lesson ->
                val progress = progressList.find { it.lessonId == lesson.id }
                val completed = progress != null

                LessonRowCard(
                    lesson = lesson,
                    isCompleted = completed,
                    stars = progress?.starsEarned ?: 0,
                    score = progress?.score ?: 0,
                    onClick = { onSelectLesson(lesson) }
                )
            }
        }
    }
}

@Composable
fun LessonRowCard(
    lesson: Lesson,
    isCompleted: Boolean,
    stars: Int,
    score: Int,
    onClick: () -> Unit
) {
    // Beautiful Geometric Balance thematic color pairs
    val (cardBg, cardText) = when (lesson.category) {
        "Math" -> Pair(Color(0xFFD3E4FF), Color(0xFF001C38))
        "Science" -> Pair(Color(0xFFC2F0CB), Color(0xFF072711))
        "English" -> Pair(Color(0xFFFFDBCB), Color(0xFF311100))
        else -> Pair(Color(0xFFEADDFF), Color(0xFF21005D))
    }

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = 0.1f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("lesson_card_${lesson.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Playful circular emoji icon inside a white bubble to match the buttons in the HTML template!
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .shadow(1.dp, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = lesson.iconEmoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Category Chip & Difficulty styled perfectly
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.5f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = lesson.category.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = cardText
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.3f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = lesson.difficulty,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = cardText.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = lesson.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = cardText
                )

                Text(
                    text = lesson.description,
                    fontSize = 12.sp,
                    color = cardText.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Stars earned row if completed
                if (isCompleted) {
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        repeat(3) { index ->
                            val isStarFilled = index < stars
                            Text(
                                text = if (isStarFilled) "⭐" else "☆",
                                fontSize = 16.sp,
                                color = if (isStarFilled) Color(0xFFFBC02D) else cardText.copy(alpha = 0.3f)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Score: $score%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = cardText
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎁 +${lesson.pointsReward} Points",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = cardText
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Start Lesson",
                    tint = cardText,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun ProgressTab(
    points: Int,
    coins: Int,
    streak: Int,
    lessons: List<Lesson>,
    progressList: List<Progress>
) {
    // Calculate dynamically
    val completedCount = progressList.count { it.isCompleted }
    val totalCount = lessons.size
    val completionPercentage = if (totalCount > 0) (completedCount.toFloat() / totalCount.toFloat()) else 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Daily Progress Circle Meter Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8DEF8)),
                border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Sapien Scholar Daily Progress",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF21005D)
                            )
                            Text(
                                text = "$points XP",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF21005D)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.5f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "LEVEL ${1 + points / 100}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF21005D)
                            )
                        }
                    }

                    // Linear progress gauge precisely mimicking the geometric design!
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.4f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = completionPercentage)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(Color(0xFF6750A4))
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Completed: $completedCount of $totalCount Quests",
                            fontSize = 12.sp,
                            color = Color(0xFF49454F),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Daily Active Streak: $streak ${if (streak > 1) "🔥" else "🌱"}",
                            fontSize = 12.sp,
                            color = Color(0xFF49454F)
                        )
                        Text(
                            text = "Total Badges Earned: ${getBadgesCount(points, progressList)}",
                            fontSize = 12.sp,
                            color = Color(0xFF49454F)
                        )
                    }
                }
            }
        }

        // Unlocked Badges Row Section
        item {
            Text(
                text = "My Incredible Badges 🏆🛡️",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF001C38)
            )
        }

        item {
            // Display Grid of Badges
            val badges = getBadgesList(points, progressList)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                badges.chunked(2).forEach { rowBadges ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowBadges.forEach { badge ->
                            BadgeCard(
                                badgeName = badge.name,
                                icon = badge.icon,
                                desc = badge.desc,
                                isUnlocked = badge.isUnlocked,
                                color = badge.color,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowBadges.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeCard(
    badgeName: String,
    icon: String,
    desc: String,
    isUnlocked: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color.White else Color(0xFFF1F3F5)
        ),
        border = BorderStroke(1.dp, Color(0xFF74777F).copy(alpha = if (isUnlocked) 0.15f else 0.05f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) color.copy(alpha = 0.2f) else Color.LightGray.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = badgeName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = if (isUnlocked) Color(0xFF001C38) else Color(0xFF74777F),
                textAlign = TextAlign.Center
            )

            Text(
                text = desc,
                fontSize = 10.sp,
                color = if (isUnlocked) Color.Gray else Color.LightGray,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

// Helpers for Badge mapping
data class Badge(
    val name: String,
    val icon: String,
    val desc: String,
    val isUnlocked: Boolean,
    val color: Color
)

fun getBadgesCount(points: Int, progressList: List<Progress>): Int {
    return getBadgesList(points, progressList).count { it.isUnlocked }
}

fun getBadgesList(points: Int, progressList: List<Progress>): List<Badge> {
    val isMathUnlocked = progressList.any { it.lessonId == "math_addition" && it.starsEarned >= 2 }
    val isScienceUnlocked = progressList.any { it.lessonId == "science_water_cycle" && it.starsEarned >= 2 }
    val isEnglishUnlocked = progressList.any { it.lessonId == "english_nouns_verbs" && it.starsEarned >= 2 }
    val isGeographyUnlocked = progressList.any { it.lessonId == "geography_india" && it.starsEarned >= 2 }

    return listOf(
        Badge(
            name = "Fast Starter",
            icon = "🏁",
            desc = "Earned by creating your learning profile!",
            isUnlocked = points >= 50,
            color = Color(0xFF4CAF50)
        ),
        Badge(
            name = "Math Wizard",
            icon = "➕",
            desc = "Earn 2+ stars in Math Magic quest",
            isUnlocked = isMathUnlocked,
            color = Color(0xFFE91E63)
        ),
        Badge(
            name = "Science Whiz",
            icon = "💧",
            desc = "Earn 2+ stars in Magical Water Cycle",
            isUnlocked = isScienceUnlocked,
            color = Color(0xFF2196F3)
        ),
        Badge(
            name = "Grammar Star",
            icon = "🔤",
            desc = "Earn 2+ stars in Nouns & Verbs Playground",
            isUnlocked = isEnglishUnlocked,
            color = Color(0xFF9C27B0)
        ),
        Badge(
            name = "Bharat Explorer",
            icon = "🇮🇳",
            desc = "Earn 2+ stars in Incredible India quest",
            isUnlocked = isGeographyUnlocked,
            color = Color(0xFFFF9800)
        ),
        Badge(
            name = "Super Scholar",
            icon = "🎓",
            desc = "Complete all 4 elementary learning quests",
            isUnlocked = progressList.count { it.isCompleted } >= 4,
            color = Color(0xFF3F51B5)
        )
    )
}
