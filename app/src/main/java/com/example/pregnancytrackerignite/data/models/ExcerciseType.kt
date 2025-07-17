package com.example.pregnancytrackerignite.data.models

import com.example.pregnancytrackerignite.R

data class ExerciseType(
    val name: String,
    val image: Int,
    val description: String,
    val videoName: Int
)

val dataExercises by lazy {
    listOf(
        ExerciseType(
            "Warrior Pose",
            R.drawable.exercise1,
            "The Warrior Pose is a powerful standing posture that strengthens the legs, opens the hips and chest, and energizes the entire body.\n• To perform the pose, begin by standing tall, then step one foot back, turning the back foot slightly outward.\n• Bend your front knee while keeping the back leg straight. Extend your arms overhead or parallel to the floor, keeping your gaze forward. This pose helps in building stamina and balance.\n" +
                    "Hold this posture for 20-30 seconds on each side, focusing on maintaining steady breaths.\n• As you grow more comfortable, you can gradually extend the duration to 1 minute per side. This exercise engages your core, boosts concentration, and promotes inner strength.",
            R.raw.warrior_pose
        ),
        ExerciseType(
            "Tree Pose",
            R.drawable.exercise2,
            "Tree Pose is a balancing posture that cultivates focus and stability while strengthening the legs.\n• Start by standing tall, and shift your weight onto one foot. Slowly bring the opposite foot to rest on the inner thigh or calf (avoid placing it on the knee).\n• Bring your palms together in a prayer position at your chest or extend your arms upward like tree branches.\n• Engage your core and maintain a steady gaze on a fixed point to keep your balance.\n" +
                    "Hold this pose for 30-60 seconds on each leg. The Tree Pose helps improve balance, posture, and mental clarity. With consistent practice, it enhances your ability to stay grounded and centered.",
            R.raw.tree_pose
        ),
        ExerciseType(
            "Downward-Facing Dog",
            R.drawable.exercise3,
            "Downward-Facing Dog is a foundational pose in yoga, stretching and strengthening various parts of the body.\n• Begin on your hands and knees in a tabletop position. Spread your fingers wide, press firmly into the mat, and lift your hips towards the ceiling, forming an inverted \"V\" shape. Keep your feet hip-width apart and your knees slightly bent or straight if comfortable.\n• Relax your head between your arms, and gaze toward your navel.\n" +
                    "Hold this pose for 1-3 minutes, taking deep, steady breaths.\n• Downward Dog stretches the hamstrings, calves, and spine while strengthening the arms, shoulders, and core.\n• It helps reduce tension, improves circulation, and boosts overall body awareness.",
            R.raw.downward_facing_dog
        ),
        ExerciseType(
            "Cat-Cow Pose",
            R.drawable.exercise4,
            "The Cat-Cow sequence is a gentle flow between two poses that warms up the spine and increases flexibility.\n• Start on your hands and knees in a tabletop position, with wrists directly under shoulders and knees under hips.\n• Inhale and arch your back (Cow Pose), lifting your head and tailbone upwards while allowing your belly to drop toward the floor.\n• Exhale and round your spine (Cat Pose), tucking your chin and drawing your belly inwards as you press through your hands.\n" +
                    "Flow through this sequence for 5-10 rounds, moving with your breath.\n• This dynamic movement promotes spinal mobility, massages internal organs, and helps relieve tension in the back and neck.\n• It's great as a warm-up or for stress relief.",
            R.raw.cat_cow_pose
        ),
        ExerciseType(
            "Tabletop Pose",
            R.drawable.exercise5,
            "Tabletop Pose serves as a neutral starting point for various yoga postures, but it can also be used for a seated twist to release tension in the spine.\n• Sit with your legs bent to one side and both knees on the ground. Place one hand behind you for support and the other on the opposite knee to gently twist your torso.\n• Keep your spine long as you twist, and take deep breaths as you rotate your gaze over the shoulder.\n" +
                    "• Hold this twist for 20-30 seconds on each side. Bharadvaja's Twist helps detoxify the body by stimulating the digestive organs and improving spinal flexibility.\n• It's a calming pose, often practiced toward the end of a session to release tension from the spine and shoulders.",
            R.raw.tabletop_pose
        ),
        ExerciseType(
            "Child's Pose",
            R.drawable.exercise6,
            "Child's Pose is a restorative yoga posture that promotes relaxation and relieves tension in the lower back and hips. To perform this pose, kneel on the floor, bring your big toes together, and sit back on your heels. Spread your knees apart, extend your arms forward, and rest your forehead on the mat. Breathe deeply and relax into the stretch.\n" +
                    "This pose gently stretches the spine and shoulders while calming the mind. It is especially helpful for releasing tightness in the hips and reducing stress. Regular practice of Child’s Pose can enhance flexibility and provide a sense of peace and comfort during pregnancy.",
            R.raw.child_pose
        ),
        ExerciseType(
            "Stated Forward Bend",
            R.drawable.exercise7,
            "The Seated Forward Bend is a simple yet effective pose for stretching the hamstrings and lower back. Sit on the floor with your legs extended straight in front of you, keeping your spine tall. Inhale to lengthen the spine, then exhale as you gently fold forward from the hips, reaching for your feet or ankles.\n" +
                    "This posture improves flexibility in the lower body while relieving tension in the back. It also supports digestion and helps ease bloating. This calming pose can be performed during quiet moments to encourage relaxation and mindfulness.",
            R.raw.stated_forward_bend
        ),
        ExerciseType(
            "Butterfly Pose",
            R.drawable.exercise8,
            "Butterfly Pose is ideal for opening the hips and stretching the inner thighs. Sit with your back straight, bring the soles of your feet together, and let your knees gently fall outward. Hold your feet with your hands and sit tall, breathing deeply as you relax.\n" +
                    "This pose strengthens the pelvic floor and helps improve circulation in the lower body. Regular practice can ease discomfort in the groin and lower back, making it a beneficial addition to any prenatal yoga routine.",
            R.raw.butterfly_pose
        ),
        ExerciseType(
            "Bridge Pose",
            R.drawable.exercise9,
            "Bridge Pose is a gentle backbend that strengthens the lower body and improves flexibility. Lie on your back with your knees bent and feet flat on the floor. Place your arms at your sides, palms down, and press through your feet to lift your hips toward the ceiling.\n" +
                    "This pose engages the glutes and hamstrings while opening the chest and stretching the spine. It helps relieve lower back tension and supports overall relaxation. Practicing this pose regularly can also improve posture and stability.",
            R.raw.bridge_pose
        ),
        ExerciseType(
            "Pelvic Tilts Pose",
            R.drawable.exercise10,
            "Pelvic Tilts are a simple yet powerful exercise to strengthen the core and alleviate back pain. Lie on your back with knees bent and feet flat on the floor. Inhale as you arch your lower back slightly, then exhale and flatten it against the floor by tucking your pelvis.\n" +
                    "This gentle movement strengthens the pelvic floor and helps improve posture. It is particularly beneficial during pregnancy to relieve pressure on the lower back and enhance body awareness.",
            R.raw.pelvic_tilts_pose
        ),
        ExerciseType(
            "Squats Pose",
            R.drawable.exercise11,
            "Squats are a great way to strengthen the legs and prepare the body for labor. Stand with your feet slightly wider than hip-width apart, toes pointing out. Lower your hips into a squat position, keeping your chest lifted and knees aligned with your toes.\n" +
                    "This exercise improves hip flexibility and tones the lower body. Squats also help strengthen the pelvic floor, which is essential for a smooth delivery. Perform them slowly and gently for best results.",
            R.raw.squats_pose
        ),
        ExerciseType(
            "Standing Side Stretch Pose",
            R.drawable.exercise12,
            "The Standing Side Stretch is a gentle exercise to open the sides of the body and improve flexibility. Stand tall with feet hip-width apart, raise one arm overhead, and lean toward the opposite side. Hold for a few breaths, then switch sides.\n" +
                    "This pose stretches the obliques and intercostal muscles while promoting deep breathing. It helps reduce tension in the torso and encourages better posture, making it an excellent addition to a prenatal exercise routine.",
            R.raw.standing_side_stretch_pose
        ),
        ExerciseType(
            "Cobbler’s Pose",
            R.drawable.exercise13,
            "Cobbler’s Pose is a relaxing posture that stretches the hips and inner thighs. Sit on the floor with a straight back, bring the soles of your feet together, and let your knees fall outward. Hold your feet and gently press your knees closer to the ground.\n" +
                    "This pose improves blood circulation in the pelvic area and strengthens the pelvic floor. It also helps relieve tightness in the hips and lower back, promoting relaxation and ease.\n",
            R.raw.cobbler_pose
        ),
        ExerciseType(
            "Leg Raises",
            R.drawable.exercise14,
            "Modified Leg Raises help strengthen the legs and core with minimal impact. Lie on your side with legs straight and stacked. Lift the top leg slowly to hip height and lower it back down. Repeat on both sides.\n" +
                    "This exercise enhances stability and strengthens the hips and thighs. It is a gentle way to maintain lower body strength and improve balance during pregnancy.",
            R.raw.leg_raise_pose
        ),
        ExerciseType(
            "Side-Lying Leg Lifts",
            R.drawable.exercise15,
            "Side-Lying Leg Lifts are effective for toning the lower body and improving mobility. Lie on your side with your legs slightly bent for stability. Lift the top leg slowly, keeping it straight, then lower it back down.\n" +
                    "This low-impact exercise strengthens the hips and glutes while reducing stiffness. It is a great choice for maintaining flexibility and ease in the lower body throughout pregnancy.",
            R.raw.side_lying_leg_lift_pose
        )
    )
}
