package com.example.data

object LessonData {
    val lessons = listOf(
        Lesson(
            id = "math_addition",
            title = "Math Magic: Count & Add",
            category = "Math",
            difficulty = "Easy",
            description = "Learn how to join numbers together with juicy apples and jumps!",
            iconEmoji = "➕",
            pointsReward = 100,
            coinsReward = 10,
            contentPages = listOf(
                LessonPage(
                    title = "What is Adding?",
                    body = "Adding means putting things together to find out how many there are in total! \n\nImagine you have 2 red apples 🍎🍎 and your friend gives you 3 green apples 🍏🍏🍏. Let's count them all together: 1, 2, 3, 4, 5! \n\nSo, 2 apples plus 3 apples equals 5 apples! 🍎🍎 + 🍏🍏🍏 = 🖐️ apples!",
                    visualHintEmoji = "🍎"
                ),
                LessonPage(
                    title = "The Magic Number Line",
                    body = "You can also add numbers by jumping forward like a happy frog! 🐸 \n\nImagine a number line. If you start at 4 and jump forward 2 steps: \n\nFirst jump: 5 \nSecond jump: 6! \n\nYou landed on 6! So, 4 + 2 = 6! Jumping makes addition super easy and fun!",
                    visualHintEmoji = "🐸"
                ),
                LessonPage(
                    title = "Double the Fun!",
                    body = "Let's learn a special trick: Doubles! \n\nWhen we add a number to itself, it's called a double! \n• 1 + 1 = 2 (Two eyes! 👀) \n• 2 + 2 = 4 (Four wheels on a car! 🚗) \n• 3 + 3 = 6 (Six legs on an insect! 🐜) \n• 4 + 4 = 8 (Eight legs on a spider! 🕷️) \n• 5 + 5 = 10 (Ten fingers on your hands! 👐)",
                    visualHintEmoji = "👐"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is 3 + 4?",
                    options = listOf("5", "6", "7", "8"),
                    correctAnswerIndex = 2,
                    explanation = "If you start at 3 and count 4 more (4, 5, 6, 7), you get 7!"
                ),
                QuizQuestion(
                    question = "If you have 5 yummy candies 🍬 and buy 2 more, how many do you have now?",
                    options = listOf("5", "6", "7", "8"),
                    correctAnswerIndex = 2,
                    explanation = "5 candies + 2 candies = 7 candies!"
                ),
                QuizQuestion(
                    question = "A spider has 4 legs on the left and 4 legs on the right. How many legs in total?",
                    options = listOf("6", "8", "10", "4"),
                    correctAnswerIndex = 1,
                    explanation = "This is a double! 4 + 4 = 8 legs!"
                )
            )
        ),
        Lesson(
            id = "science_water_cycle",
            title = "Magical Water Cycle",
            category = "Science",
            difficulty = "Medium",
            description = "Follow droplets of water as they fly to the sky and fall as cool rain!",
            iconEmoji = "💧",
            pointsReward = 120,
            coinsReward = 15,
            contentPages = listOf(
                LessonPage(
                    title = "Water Can Fly! (Evaporation)",
                    body = "Did you know water can change into invisible air and fly? 🌤️ \n\nWhen the warm sun shines down on puddles, rivers, and oceans, it heats the water up. The liquid water turns into invisible steam called water vapor and rises high into the sky! \n\nThis magical warming-up step is called Evaporation!",
                    visualHintEmoji = "🌤️"
                ),
                LessonPage(
                    title = "Making Fluffy Clouds (Condensation)",
                    body = "Up in the sky, it is very cold! ❄️ \n\nWhen the warm, invisible water vapor reaches the chilly sky, it cools down and turns back into tiny drops of liquid water. \n\nThese tiny drops hold hands and gather together to form big, beautiful, fluffy clouds! \n\nThis cooling-down step is called Condensation!",
                    visualHintEmoji = "☁️"
                ),
                LessonPage(
                    title = "Falling Down! (Precipitation)",
                    body = "As more and more water drops gather inside the clouds, they become extremely heavy! The cloud cannot hold them anymore. \n\nSo, they fall back down to Earth as Rain, Snow, or Hail! 🌧️❄️ \n\nThis falling-down step is called Precipitation! It refills our rivers and waters the beautiful trees and flowers!",
                    visualHintEmoji = "🌧️"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is it called when the sun heats up water and makes it rise into the sky?",
                    options = listOf("Condensation", "Evaporation", "Precipitation", "Drinking"),
                    correctAnswerIndex = 1,
                    explanation = "Evaporation is when liquid water heats up and rises as vapor!"
                ),
                QuizQuestion(
                    question = "How are fluffy clouds formed in the sky?",
                    options = listOf("By smoke from fires", "By water vapor cooling down (Condensation)", "By dust blowing", "By airplanes"),
                    correctAnswerIndex = 1,
                    explanation = "Condensation is water vapor cooling down to form cloud droplets!"
                ),
                QuizQuestion(
                    question = "What is the fancy scientific name for Rain or Snow falling from the sky?",
                    options = listOf("Evaporation", "Precipitation", "Condensation", "Waterfall"),
                    correctAnswerIndex = 1,
                    explanation = "Precipitation is water falling back to earth as rain, snow, or hail!"
                )
            )
        ),
        Lesson(
            id = "english_nouns_verbs",
            title = "Nouns & Verbs Playground",
            category = "English",
            difficulty = "Easy",
            description = "Meet naming words and action words that bring sentences to life!",
            iconEmoji = "🔤",
            pointsReward = 100,
            coinsReward = 10,
            contentPages = listOf(
                LessonPage(
                    title = "What is a Noun?",
                    body = "A Noun is a naming word! It names everything we can see, touch, or visit! \n\nNouns can be: \n• People: Teacher 👩‍🏫, Sister 👧, Doctor 🩺 \n• Places: Amethi 🏛️, School 🏫, Park 🏞️ \n• Animals: Lion 🦁, Dog 🐕, Chimp 🐒 \n• Things: Book 📘, Pencil ✏️, Toy 🧸",
                    visualHintEmoji = "🦁"
                ),
                LessonPage(
                    title = "What is a Verb?",
                    body = "A Verb is an action word! It tells us what someone or something is doing! \n\nIf you can do it, it's a verb! \n• Running: 'The boy runs!' 🏃‍♂️ \n• Reading: 'She reads a book!' 📖 \n• Jumping: 'A frog jumps!' 🐸 \n• Eating: 'We eat mangoes!' 🥭 \n• Sleeping: 'The baby sleeps!' 😴",
                    visualHintEmoji = "🏃‍♂️"
                ),
                LessonPage(
                    title = "Putting Them Together!",
                    body = "Let's build a sentence using both! \n\nLook at this sentence: \n'The bird 🐦 sings 🎶' \n\n• Bird is the Noun (the animal). \n• Sings is the Verb (the action it is doing). \n\nYou can spot nouns and verbs everywhere you read!",
                    visualHintEmoji = "🐦"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which of these words is a Noun (naming word)?",
                    options = listOf("Run", "Sing", "Elephant", "Happy"),
                    correctAnswerIndex = 2,
                    explanation = "Elephant is an animal name, so it is a Noun!"
                ),
                QuizQuestion(
                    question = "Which of these words is a Verb (action word)?",
                    options = listOf("Jump", "School", "Pencil", "Yellow"),
                    correctAnswerIndex = 0,
                    explanation = "Jump is an action you can do, so it is a Verb!"
                ),
                QuizQuestion(
                    question = "In 'The dog barked at the mailman', which word is the action (Verb)?",
                    options = listOf("Dog", "Barked", "Mailman", "The"),
                    correctAnswerIndex = 1,
                    explanation = "Barked is the action the dog did, making it the Verb!"
                )
            )
        ),
        Lesson(
            id = "geography_india",
            title = "Incredible India & Amethi",
            category = "GK",
            difficulty = "Fun",
            description = "Explore our beautiful country India, its symbols, and our hometown Amethi!",
            iconEmoji = "🇮🇳",
            pointsReward = 150,
            coinsReward = 20,
            contentPages = listOf(
                LessonPage(
                    title = "Our Beautiful Nation",
                    body = "India is a gorgeous, colorful country in South Asia! 🇮🇳 \n\nIn the north, we have massive mountains covered in pure snow called the Himalayas. In the south, we have warm beaches and the deep blue Indian Ocean. \n\nThe capital city of India is New Delhi, where the historic Red Fort is located!",
                    visualHintEmoji = "🇮🇳"
                ),
                LessonPage(
                    title = "National Pride Symbols",
                    body = "India has awesome symbols that represent our pride: \n• National Animal: The majestic Bengal Tiger 🐅 \n• National Bird: The beautiful, colorful Peacock 🦚 \n• National Flower: The pure and lovely Lotus 🪷 \n• National Fruit: The sweet, golden Mango 🥭 (Everyone's favorite!)",
                    visualHintEmoji = "🦚"
                ),
                LessonPage(
                    title = "Our Home: Uttar Pradesh & Amethi",
                    body = "Sapien School is proudly located in Amethi! Amethi is a beautiful district in the state of Uttar Pradesh (UP). \n\nUttar Pradesh is India's most populous state and is famous for: \n• The Taj Mahal in Agra (one of the 7 Wonders of the World!) \n• The holy and wide Ganga River 🌊 \n• Warm and friendly people!",
                    visualHintEmoji = "🏛️"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the capital of India?",
                    options = listOf("Mumbai", "New Delhi", "Lucknow", "Kolkata"),
                    correctAnswerIndex = 1,
                    explanation = "New Delhi is the official capital of India!"
                ),
                QuizQuestion(
                    question = "What is the National Bird of India?",
                    options = listOf("Sparrow", "Eagle", "Peacock", "Parrot"),
                    correctAnswerIndex = 2,
                    explanation = "The Peacock with its beautiful feathers is our National Bird!"
                ),
                QuizQuestion(
                    question = "Which Indian state is Amethi located in?",
                    options = listOf("Bihar", "Uttar Pradesh", "Madhya Pradesh", "Punjab"),
                    correctAnswerIndex = 1,
                    explanation = "Amethi is located in the vibrant state of Uttar Pradesh!"
                )
            )
        )
    )
}
