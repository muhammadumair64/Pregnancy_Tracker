package com.example.pregnancytrackerignite.data.utils

import com.example.pregnancytrackerignite.data.models.GenderPredictionQuestion

object GenderPredictionUtils {
    var questions = ArrayList<GenderPredictionQuestion>()

    init {
        initQuestions()
    }

    private fun initQuestions() {
        questions.apply {
            clear()
            add(
                GenderPredictionQuestion(
                    "Do you have morning sickness?",
                    "Very intense",
                    "Mild",
                    "None at all",
                    "Only in the evening"
                )
            )

            add(
                GenderPredictionQuestion(
                    "Are you craving sweet, salty, or other types of foods?",
                    "Sweet (chocolates, candies)",
                    "Salty (chips, snacks)",
                    "Sour (pickles, lemons)",
                    "Spicy (hot peppers, curry)"
                )
            )

            add(
                GenderPredictionQuestion(
                    "What is your baby's heart rate (if known)?",
                    "Above 160 beats per minute",
                    "140-160 beats per minute",
                    "120-140 beats per minute",
                    "Below 120 beats per minute"
                )
            )

            add(
                GenderPredictionQuestion(
                    "Are your hands dry or soft during pregnancy?",
                    "Very dry", "Slightly dry", "Normal", "Extremely soft"
                )
            )

            add(
                GenderPredictionQuestion(
                    "Have you experienced mood swings?",
                    "Very often", "Sometimes", "Rarely", "Never"
                )
            )

            add(
                GenderPredictionQuestion(
                    "How does your skin look during pregnancy?",
                    "Clear and glowing", "Normal", "Breaking out slightly", "Lots of breakouts"
                )
            )

            add(
                GenderPredictionQuestion(
                    "What do you think the gender is?",
                    "Boy", "Girl", "Not sure", "I don’t want to guess"
                )
            )

            add(
                GenderPredictionQuestion(
                    "What side do you sleep on most?",
                    "Left side", "Right side", "Back", "Stomach"
                )
            )

            add(
                GenderPredictionQuestion(
                    "Have you noticed any change in your feet size?",
                    "They have grown larger",
                    "They have stayed the same",
                    "They have swollen slightly",
                    "They feel smaller"
                )
            )

            add(
                GenderPredictionQuestion(
                    "How is your hair during pregnancy?",
                    "Shiny and thick", "Dull and thin", "The same as before", "Frizzy or curly"
                )
            )

            add(
                GenderPredictionQuestion(
                    "Have you noticed any increase in clumsiness?",
                    "Yes, a lot more clumsy",
                    "Just a little more",
                    "Not at all",
                    "I’m always clumsy"
                )
            )

            add(
                GenderPredictionQuestion(
                    "What are you craving the most in drinks?",
                    "Water", "Juice", "Tea or coffee", "Soda or fizzy drinks"
                )
            )

            add(
                GenderPredictionQuestion(
                    "Have you noticed any changes in your nose size?",
                    "It’s bigger",
                    "It’s smaller",
                    "No change",
                    "It looks the same but feels different"
                )
            )

            add(
                GenderPredictionQuestion(
                    "How is your sleep pattern?",
                    "Sleeping more than usual",
                    "Having trouble sleeping",
                    "Normal sleep",
                    "Sleeping at odd hours"
                )
            )

            add(
                GenderPredictionQuestion(
                    "What kind of kicks do you feel from your baby?",
                    "Strong and frequent", "Light and gentle", "Irregular", "Barely noticeable"
                )
            )

            add(
                GenderPredictionQuestion(
                    "Do you notice any difference in the color of your urine?",
                    "Light yellow", "Dark yellow", "No change", "Cloudy or clear"
                )
            )

            add(
                GenderPredictionQuestion(
                    "What do you dream about the most?",
                    "A baby boy", "A baby girl", "Nothing in particular", "Other things entirely"
                )
            )

            add(
                GenderPredictionQuestion(
                    "What type of ring test result do you get?",
                    "Swings back and forth", "Moves in circles", "Stays still", "Falls off quickly"
                )
            )

            add(
                GenderPredictionQuestion(
                    "Do you have headaches more often during pregnancy?",
                    "Frequently", "Occasionally", "Rarely", "Never"
                )
            )

            add(
                GenderPredictionQuestion(
                    "How is your partner gaining weight during your pregnancy?",
                    "Gaining a lot of weight",
                    "Gaining a little weight",
                    "No change",
                    "Losing weight"
                )
            )

//            add(GenderPredictionQuestion("Have you experienced more heartburn than usual?",
//                "All the time", "Occasionally", "Rarely", "Never"))
//
//            add(GenderPredictionQuestion("What color are your nipples now?",
//                "Much darker", "Slightly darker", "No change", "Lighter"))
//
//            add(GenderPredictionQuestion("How would you describe your pregnancy glow?",
//                "Glowing like never before", "Slightly glowing", "No glow", "Dull skin"))
//
//            add(GenderPredictionQuestion("What do you notice about your legs?",
//                "More hair growth", "Less hair growth", "No change in hair growth", "Smoother skin"))
//
//            add(GenderPredictionQuestion("How are your emotions during pregnancy?",
//                "Very calm and relaxed", "A bit more emotional than usual", "Extremely moody", "No noticeable change"))
//
//            add(GenderPredictionQuestion("What does your intuition tell you about the baby's gender?",
//                "Boy", "Girl", "No strong feeling", "Changing my mind all the time"))
//
//            add(GenderPredictionQuestion("How are your cravings for meat?",
//                "Craving it all the time", "Slightly more interested in meat", "Not interested in meat at all", "Same as before pregnancy"))
//
//            add(GenderPredictionQuestion("How is your energy level during the day?",
//                "Full of energy", "Very tired", "Energized in the morning, tired by evening", "Low energy all day"))
//
//            add(GenderPredictionQuestion("What is the shape of your belly?",
//                "Round like a basketball", "Wide like a watermelon", "Small and compact", "Big and spread out"))
//
//            add(GenderPredictionQuestion("What’s your skin temperature like during pregnancy?",
//                "Always cold", "Always warm", "Changes frequently", "Normal"))

//            add(GenderPredictionQuestion("How do you feel about spicy food during pregnancy?",
//                "Craving it constantly", "Avoiding it entirely", "Eating occasionally", "No change in preference"))
//
//            add(GenderPredictionQuestion("How much sleep are you getting?",
//                "More than 8 hours", "Less than 5 hours", "Around 6-7 hours", "Sleep varies a lot"))
//
//            add(GenderPredictionQuestion("Have you noticed changes in your hair color?",
//                "Darker", "Lighter", "No change", "Turning gray"))
//
//            add(GenderPredictionQuestion("How is your sense of smell during pregnancy?",
//                "Extremely sensitive", "Duller than usual", "No noticeable change", "Changes from day to day"))
//
//            add(GenderPredictionQuestion("How would you describe your back pain?",
//                "Severe lower back pain", "Mild discomfort", "Upper back pain", "No pain at all"))
//
//            add(GenderPredictionQuestion("What kind of dreams are you having most frequently?",
//                "Vivid dreams", "Nightmares", "Dreams about the baby", "No dreams at all"))
//
//            add(GenderPredictionQuestion("How are you carrying the baby?","High and round","Low and wide","High and pointed","Low and narrow"))
//
//            add(GenderPredictionQuestion("Have your eating habits changed significantly?",
//                "Eating a lot more", "Eating less than usual", "Eating the same amount", "Eating at odd times"))
//
//
//            add(GenderPredictionQuestion("Do you have a metallic taste in your mouth?",
//                "Yes, constantly", "Occasionally", "Rarely", "Never"))
//
//            add(GenderPredictionQuestion("How does your belly button look during pregnancy?",
//                "Popped out", "Flat", "Same as before", "Sinking inwards"))
        }
    }
}