package com.example.pregnancytrackerignite.data.utils

data class Meal(
    val name: String,
    val prepTime: String,
    val cookTime: String,
    val serving: Int,
    val ingredients: List<String>,
    val recipe: List<String>,
    val imageUrl: String
)

data class DietPlan(
    val breakfast: Meal,
    val snack1: Meal,
    val lunch: Meal,
    val snack2: Meal,
    val dinner: Meal
)

object weakDietPlan {
    var planList = ArrayList<DietPlan>()
    init {
        planList.add(dietPlanDay1)
        planList.add(dietPlanDay2)
        planList.add(dietPlanDay3)
        planList.add(dietPlanDay4)
        planList.add(dietPlanDay5)
        planList.add(dietPlanDay6)
        planList.add(dietPlanDay7)
    }
}

val dietPlanDay1 =
    DietPlan(
    breakfast = Meal(
        name = "Oatmeal with Berries",
        prepTime = "5 min",
        cookTime = "10 min",
        serving = 1,
        ingredients = listOf(
            "1/2 cup rolled oats",
            "1 cup water or milk",
            "1/4 cup mixed berries (blueberries, strawberries, raspberries)",
            "1 tsp honey (optional)",
            "1 tbsp flax seeds",
            "1/4 cup almond butter (optional)",
            "1 tbsp chia seeds (optional)",
            "1 tbsp pumpkin seeds (optional)",
            "1 tbsp shredded coconut (optional)",
            "1 tsp cinnamon (optional)"
        ),
        recipe = listOf(
            "In a pot, bring the water or milk to a boil.",
            "Add the oats and reduce the heat to medium-low.",
            "Simmer for 5-7 minutes, stirring occasionally until the oats are soft and cooked.",
            "Once cooked, add honey if desired and stir.",
            "Top with mixed berries, flax seeds, chia seeds, and a spoonful of almond butter for extra protein and healthy fats.",
            "Add a sprinkle of cinnamon for flavor, and shredded coconut for extra texture.",
            "Serve warm and enjoy a nutritious, fiber-rich breakfast!"
        ),
        imageUrl = "https://naturallyella.com/wp-content/uploads/2015/04/Summer-Berry-Overnight-Oats-2.jpg"
    ),
    snack1 = Meal(
        name = "Greek Yogurt with Nuts and Seeds",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/2 cup Greek yogurt",
            "2 tbsp walnuts, chopped",
            "1 tbsp chia seeds",
            "1 tbsp sunflower seeds",
            "1 tbsp honey",
            "A pinch of cinnamon (optional)",
            "1 tbsp almond butter (optional)",
            "1 tbsp flax seeds",
            "1/4 tsp vanilla extract (optional)",
            "1 tbsp granola (optional)",
            "A handful of dried cranberries (optional)"
        ),
        recipe = listOf(
            "In a bowl, add the Greek yogurt.",
            "Top with walnuts, chia seeds, and sunflower seeds.",
            "Drizzle honey on top for sweetness.",
            "Sprinkle a pinch of cinnamon for added flavor and health benefits.",
            "Add flax seeds, almond butter, and granola for texture.",
            "For extra sweetness, add dried cranberries or raisins.",
            "Stir everything together for a protein-packed and nutritious snack."
        ),
        imageUrl = "https://static.wixstatic.com/media/665a63_dafd8c9be79441e3a240152cb5e330b2~mv2.jpg/v1/fill/w_1010,h_1003,al_c,q_85/Greek%20yogurt%20with%20a%20handful%20of%20nuts%20and%20seeds_edited.jpg"
    ),
    lunch = Meal(
        name = "Quinoa Salad with Avocado",
        prepTime = "10 min",
        cookTime = "15 min",
        serving = 2,
        ingredients = listOf(
            "1 cup quinoa",
            "1 avocado, diced",
            "1/4 cup cherry tomatoes, halved",
            "1/4 cup cucumber, diced",
            "1 tbsp olive oil",
            "1 tbsp lemon juice",
            "1/4 cup red onion, diced",
            "Salt and pepper to taste",
            "1 tbsp parsley, chopped",
            "1/4 cup feta cheese (optional)",
            "1 tbsp roasted sunflower seeds (optional)",
            "1 tbsp pumpkin seeds (optional)",
            "1/2 tsp cumin powder (optional)"
        ),
        recipe = listOf(
            "Cook the quinoa as per package instructions.",
            "Allow the quinoa to cool for a few minutes.",
            "In a large bowl, combine the cooked quinoa, diced avocado, cherry tomatoes, cucumber, and red onion.",
            "Drizzle with olive oil and lemon juice.",
            "Toss everything together and season with salt, pepper, and parsley.",
            "Add feta cheese, roasted sunflower seeds, and pumpkin seeds for crunch and extra flavor.",
            "Sprinkle cumin powder for a hint of warmth and serve chilled or at room temperature."
        ),
        imageUrl = "https://betterfoodguru.com/wp-content/uploads/2022/01/Easy-Kale-and-Quinoa-Salad-819x1024.jpg"
    ),
    snack2 = Meal(
        name = "Carrot Sticks with Hummus",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 large carrot, sliced into sticks",
            "3 tbsp hummus (store-bought or homemade)",
            "1 tsp olive oil",
            "A pinch of paprika (optional)",
            "A squeeze of lemon juice (optional)",
            "1 tbsp sesame seeds (optional)",
            "A pinch of cayenne pepper (optional)",
            "1/4 tsp garlic powder (optional)"
        ),
        recipe = listOf(
            "Peel the carrot and slice it into long, thin sticks.",
            "Place the carrot sticks in a bowl or on a plate.",
            "Serve with a side of hummus for dipping.",
            "Drizzle olive oil over the hummus and sprinkle with a pinch of paprika for extra flavor.",
            "For added flavor, squeeze some lemon juice over the carrot sticks and sprinkle sesame seeds on top.",
            "Optional: Add cayenne pepper or garlic powder for a spicy kick."
        ),
        imageUrl = "https://yupitsvegan.com/wp-content/uploads/2019/10/ginger-roasted-carrot-hummus-1.jpg"
    ),
    dinner = Meal(
        name = "Grilled Chicken with Steamed Vegetables",
        prepTime = "10 min",
        cookTime = "20 min",
        serving = 2,
        ingredients = listOf(
            "2 boneless, skinless chicken breasts",
            "1 cup broccoli florets",
            "1 large carrot, sliced",
            "1 tbsp olive oil",
            "1/2 tsp garlic powder",
            "Salt and pepper to taste",
            "1 tbsp lemon juice",
            "1/4 tsp paprika (optional)",
            "1 tbsp fresh parsley, chopped (optional)",
            "1 tbsp sesame seeds (optional)"
        ),
        recipe = listOf(
            "Preheat the grill or grill pan to medium-high heat.",
            "Rub the chicken breasts with olive oil, garlic powder, salt, and pepper.",
            "Grill the chicken for about 6-7 minutes per side, until cooked through.",
            "While the chicken is grilling, steam the broccoli and carrots until tender (about 8-10 minutes).",
            "Once the chicken is cooked, remove it from the grill and let it rest for a few minutes.",
            "Serve the grilled chicken with the steamed vegetables.",
            "Drizzle lemon juice on top and sprinkle with paprika for added flavor.",
            "Garnish with fresh parsley and sesame seeds for a nutritious and tasty dinner."
        ),
        imageUrl = "https://www.lecremedelacrumb.com/wp-content/uploads/2022/11/best-easy-grilled-chicken-3.jpg"
    )
)


val dietPlanDay2 = DietPlan(
    breakfast = Meal(
        name = "Scrambled Eggs with Spinach and Whole Grain Toast",
        prepTime = "5 min",
        cookTime = "7 min",
        serving = 1,
        ingredients = listOf(
            "2 large eggs",
            "1/2 cup fresh spinach, chopped",
            "1 slice whole grain bread",
            "1 tbsp olive oil",
            "1 tbsp milk or cream",
            "Salt and pepper to taste",
            "1/4 tsp garlic powder",
            "1/4 cup cherry tomatoes, halved (optional)",
            "1 tbsp feta cheese, crumbled (optional)",
            "1 tbsp fresh parsley, chopped"
        ),
        recipe = listOf(
            "Heat a non-stick skillet over medium heat and add olive oil.",
            "Crack the eggs into a bowl, add the milk or cream, and whisk together.",
            "Pour the egg mixture into the skillet, and cook for about 2-3 minutes, stirring occasionally.",
            "Once the eggs begin to set, add chopped spinach and cook until wilted.",
            "Season with salt, pepper, and garlic powder.",
            "While the eggs are cooking, toast the whole grain bread in a toaster or on a skillet.",
            "Serve the scrambled eggs with a side of whole grain toast.",
            "Top with halved cherry tomatoes and crumbled feta cheese for extra flavor.",
            "Sprinkle fresh parsley on top for a finishing touch."
        ),
        imageUrl = "https://www.platingsandpairings.com/wp-content/uploads/2023/03/spinach-scrambled-eggs-recipe-8-1365x2048.jpg"
    ),
    snack1 = Meal(
        name = "Apple with Peanut Butter",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 medium apple (such as Fuji or Gala)",
            "2 tbsp peanut butter (or almond butter)",
            "1/4 tsp cinnamon",
            "1 tsp chia seeds (optional)",
            "1 tbsp raisins (optional)",
            "1/4 cup granola (optional)",
            "1 tbsp honey (optional)"
        ),
        recipe = listOf(
            "Wash and slice the apple into wedges or rounds.",
            "Spread peanut butter (or almond butter) on each slice of apple.",
            "Sprinkle with cinnamon for extra flavor.",
            "Optional: Add chia seeds, raisins, and granola for extra texture and nutrients.",
            "Drizzle honey on top for a sweet touch."
        ),
        imageUrl = "https://www.imperialsugar.com/sites/default/files/styles/recipe_image_node_full/public/recipe/apple-peanut-butter-sandwiches-imperial.jpg"
    ),
    lunch = Meal(
        name = "Chickpea and Avocado Wrap",
        prepTime = "10 min",
        cookTime = "None",
        serving = 2,
        ingredients = listOf(
            "1 can chickpeas, drained and rinsed",
            "1 avocado, mashed",
            "1/4 cup red onion, diced",
            "1/4 cup cucumber, diced",
            "1/4 cup bell pepper, diced",
            "2 whole-wheat wraps",
            "1 tbsp tahini",
            "1 tbsp olive oil",
            "1 tbsp lemon juice",
            "1 tsp cumin powder",
            "Salt and pepper to taste",
            "1 tbsp fresh cilantro, chopped"
        ),
        recipe = listOf(
            "In a bowl, mash the avocado and mix it with tahini, olive oil, lemon juice, cumin powder, salt, and pepper.",
            "In another bowl, mash the chickpeas lightly with a fork or potato masher.",
            "Add diced red onion, cucumber, and bell pepper to the mashed chickpeas.",
            "Spread the avocado mixture on the whole-wheat wraps.",
            "Top with the chickpea and vegetable mixture.",
            "Roll up the wraps tightly and slice in half.",
            "Garnish with fresh cilantro for a burst of flavor."
        ),
        imageUrl = "https://lynnecurry.com/wp-content/uploads/2024/09/chickpea-avocado-salad-wraps-recipe-1725882890.jpeg"
    ),
    snack2 = Meal(
        name = "Cucumber and Hummus",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 large cucumber",
            "3 tbsp hummus (store-bought or homemade)",
            "1 tsp olive oil",
            "A pinch of paprika (optional)",
            "1 tbsp sesame seeds (optional)",
            "A squeeze of lemon juice (optional)"
        ),
        recipe = listOf(
            "Peel the cucumber and slice it into rounds or sticks.",
            "Serve the cucumber slices with a side of hummus for dipping.",
            "Drizzle olive oil over the hummus and sprinkle with paprika for flavor.",
            "Optional: Add sesame seeds and a squeeze of lemon juice to enhance the taste."
        ),
        imageUrl = "https://itsavegworldafterall.com/wp-content/uploads/2020/08/Dill-Pickle-Hummus-3.jpg"
    ),
    dinner = Meal(
        name = "Salmon with Quinoa and Steamed Asparagus",
        prepTime = "10 min",
        cookTime = "15 min",
        serving = 2,
        ingredients = listOf(
            "2 salmon fillets",
            "1 cup quinoa",
            "1 bunch asparagus, trimmed",
            "1 tbsp olive oil",
            "1/2 tsp garlic powder",
            "Salt and pepper to taste",
            "1 tbsp lemon juice",
            "1 tbsp fresh dill, chopped",
            "1 tbsp capers (optional)"
        ),
        recipe = listOf(
            "Preheat the oven to 375°F (190°C).",
            "Rub the salmon fillets with olive oil, garlic powder, salt, and pepper.",
            "Place the salmon on a baking sheet lined with parchment paper and bake for 12-15 minutes or until cooked through.",
            "Meanwhile, cook the quinoa according to package instructions.",
            "Trim the asparagus and steam for about 6-8 minutes until tender.",
            "Once the salmon is done, drizzle with lemon juice and sprinkle with fresh dill.",
            "Serve the salmon with a side of quinoa and steamed asparagus.",
            "Optional: Garnish with capers for added flavor."
        ),
        imageUrl = "https://mealpractice.b-cdn.net/80082030821511168/pan-seared-salmon-with-herbed-quinoa-and-grilled-asparagus-MQFYs9KNfA.webp"
    )
)

val dietPlanDay3 = DietPlan(
    breakfast = Meal(
        name = "Chia Pudding with Mixed Fruit",
        prepTime = "5 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "3 tbsp chia seeds",
            "1 cup unsweetened almond milk",
            "1 tbsp honey or maple syrup",
            "1/2 tsp vanilla extract",
            "1/4 cup strawberries, chopped",
            "1/4 cup blueberries",
            "1/4 cup kiwi, diced",
            "1 tbsp coconut flakes (optional)",
            "1 tbsp almond butter (optional)"
        ),
        recipe = listOf(
            "In a bowl, combine chia seeds, almond milk, honey (or maple syrup), and vanilla extract.",
            "Stir well, ensuring the chia seeds are fully mixed into the liquid.",
            "Cover and refrigerate for at least 2 hours or overnight until the mixture thickens.",
            "Before serving, top the chia pudding with fresh mixed fruit like strawberries, blueberries, and kiwi.",
            "Optional: Add coconut flakes and a spoonful of almond butter for extra flavor and healthy fats.",
            "Serve chilled and enjoy!"
        ),
        imageUrl = "https://i2.wp.com/www.downshiftology.com/wp-content/uploads/2020/01/Chia-Pudding.jpg"
    ),
    snack1 = Meal(
        name = "Cottage Cheese with Pineapple",
        prepTime = "2 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/2 cup cottage cheese",
            "1/2 cup fresh pineapple chunks",
            "1 tbsp sunflower seeds",
            "A pinch of cinnamon (optional)"
        ),
        recipe = listOf(
            "Scoop the cottage cheese into a bowl.",
            "Add pineapple chunks on top.",
            "Sprinkle with sunflower seeds for added crunch.",
            "Optional: Dust with a pinch of cinnamon for extra flavor.",
            "Mix gently and enjoy your protein-packed snack."
        ),
        imageUrl = "https://www.tastyaz.com/wp-content/uploads/2022/02/Cottage-Cheese-With-Pineapple-7.jpg"
    ),
    lunch = Meal(
        name = "Sweet Potato and Black Bean Bowl",
        prepTime = "10 min",
        cookTime = "20 min",
        serving = 2,
        ingredients = listOf(
            "1 medium sweet potato, peeled and cubed",
            "1 can black beans, drained and rinsed",
            "1/2 cup corn kernels (fresh or frozen)",
            "1 tbsp olive oil",
            "1 tsp cumin powder",
            "1/2 tsp smoked paprika",
            "Salt and pepper to taste",
            "1 tbsp lime juice",
            "1/4 cup cilantro, chopped",
            "1/4 avocado, sliced"
        ),
        recipe = listOf(
            "Preheat the oven to 400°F (200°C).",
            "Toss the sweet potato cubes with olive oil, cumin powder, smoked paprika, salt, and pepper.",
            "Spread the sweet potatoes on a baking sheet and roast for 20 minutes or until tender.",
            "In a pan, heat the black beans and corn together for 5-7 minutes, stirring occasionally.",
            "Once the sweet potatoes are done, remove them from the oven.",
            "Assemble the bowl by layering sweet potatoes, black beans, and corn.",
            "Top with a drizzle of lime juice, fresh cilantro, and avocado slices.",
            "Serve warm and enjoy!"
        ),
        imageUrl = "https://toshistable.com/wp-content/uploads/2022/04/sweet-potatoes-and-black-bean-bowls-24-683x1024.jpg"
    ),
    snack2 = Meal(
        name = "Carrot and Celery Sticks with Almond Butter",
        prepTime = "5 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 large carrot, sliced into sticks",
            "2 celery stalks, sliced into sticks",
            "2 tbsp almond butter",
            "1 tbsp raisins (optional)",
            "1/2 tsp cinnamon (optional)"
        ),
        recipe = listOf(
            "Peel and slice the carrot into thin sticks.",
            "Slice the celery stalks into sticks as well.",
            "Serve the carrot and celery sticks with a side of almond butter for dipping.",
            "Optional: Add raisins on top of the almond butter and sprinkle with cinnamon for added flavor."
        ),
        imageUrl = "https://i.pinimg.com/736x/53/8b/16/538b1620b9ae7e2a6631cb7daca24b2e.jpg"
    ),
    dinner = Meal(
        name = "Lentil and Vegetable Soup",
        prepTime = "10 min",
        cookTime = "30 min",
        serving = 2,
        ingredients = listOf(
            "1 cup dried lentils, rinsed",
            "1 carrot, diced",
            "1 celery stalk, diced",
            "1 onion, diced",
            "2 garlic cloves, minced",
            "1 can diced tomatoes (14.5 oz)",
            "4 cups vegetable broth",
            "1/2 tsp cumin",
            "1/4 tsp turmeric",
            "1/2 tsp thyme",
            "1/2 tsp black pepper",
            "1 tbsp olive oil",
            "1/4 cup spinach, chopped"
        ),
        recipe = listOf(
            "In a large pot, heat olive oil over medium heat.",
            "Add the onion, carrot, and celery. Cook for about 5-7 minutes until softened.",
            "Add the garlic, cumin, turmeric, thyme, and black pepper. Stir for 1-2 minutes until fragrant.",
            "Add the lentils, diced tomatoes, and vegetable broth to the pot.",
            "Bring to a boil, then reduce the heat and simmer for about 25 minutes until the lentils are tender.",
            "Stir in the spinach during the last 5 minutes of cooking.",
            "Taste and adjust seasoning with salt and pepper if needed.",
            "Serve hot and enjoy!"
        ),
        imageUrl = "https://simple-veganista.com/wp-content/uploads/2019/10/vegan-lentil-soup-recipe-1.jpg"
    )
)

val dietPlanDay4 = DietPlan(
    breakfast = Meal(
        name = "Spinach and Feta Omelette",
        prepTime = "5 min",
        cookTime = "7 min",
        serving = 1,
        ingredients = listOf(
            "2 large eggs",
            "1/4 cup fresh spinach, chopped",
            "2 tbsp feta cheese, crumbled",
            "1 tbsp olive oil",
            "1/4 onion, diced",
            "1/4 cup bell pepper, diced",
            "Salt and pepper to taste"
        ),
        recipe = listOf(
            "In a small bowl, whisk the eggs and season with salt and pepper.",
            "Heat olive oil in a pan over medium heat. Add diced onion and bell pepper, and cook for 2-3 minutes until softened.",
            "Add chopped spinach to the pan and cook for another 1-2 minutes until wilted.",
            "Pour the beaten eggs over the vegetables, spreading them evenly.",
            "Sprinkle crumbled feta cheese on top.",
            "Once the eggs are set, fold the omelette in half and serve."
        ),
        imageUrl = "https://meganvskitchen.com/wp-content/uploads/2022/07/feta-spinach-omelette.-1365x2048.jpg"
    ),
    snack1 = Meal(
        name = "Apple Slices with Peanut Butter",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 medium apple, sliced",
            "2 tbsp peanut butter (or almond butter)",
            "1 tsp cinnamon (optional)"
        ),
        recipe = listOf(
            "Slice the apple into thin rounds or wedges.",
            "Serve with a side of peanut butter for dipping.",
            "Optional: Sprinkle cinnamon on the apple slices for added flavor."
        ),
        imageUrl = "https://www.lottaveg.com/wp-content/uploads/2017/05/Apple-Peanut-Butter-Pin-450x675.png"
    ),
    lunch = Meal(
        name = "Chicken and Avocado Salad",
        prepTime = "10 min",
        cookTime = "10 min",
        serving = 2,
        ingredients = listOf(
            "2 boneless, skinless chicken breasts",
            "1 avocado, diced",
            "1/4 cup red onion, thinly sliced",
            "1/2 cup cherry tomatoes, halved",
            "2 cups mixed greens (spinach, arugula, lettuce)",
            "1 tbsp olive oil",
            "1 tbsp balsamic vinegar",
            "Salt and pepper to taste"
        ),
        recipe = listOf(
            "Heat olive oil in a pan over medium heat.",
            "Season the chicken breasts with salt and pepper, then cook for 5-7 minutes per side until golden and cooked through.",
            "Remove from the heat and let the chicken rest for a few minutes, then slice into strips.",
            "In a large bowl, combine mixed greens, diced avocado, red onion, and cherry tomatoes.",
            "Top with the sliced chicken.",
            "Drizzle with balsamic vinegar, toss, and serve immediately."
        ),
        imageUrl = "https://www.lecremedelacrumb.com/wp-content/uploads/2019/11/chicken-avocado-salad-5sm-2.jpg"
    ),
    snack2 = Meal(
        name = "Trail Mix",
        prepTime = "5 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/4 cup almonds",
            "1/4 cup walnuts",
            "1/4 cup dried cranberries",
            "1/4 cup dark chocolate chips (optional)",
            "1/4 cup sunflower seeds"
        ),
        recipe = listOf(
            "Combine all the ingredients in a small bowl or jar.",
            "Stir well to mix evenly.",
            "Store in an airtight container or eat immediately as a healthy, energizing snack."
        ),
        imageUrl = "https://www.eatingbirdfood.com/wp-content/uploads/2022/11/superfood-trail-mix-hero-633x950.jpg"
    ),
    dinner = Meal(
        name = "Baked Salmon with Asparagus",
        prepTime = "5 min",
        cookTime = "20 min",
        serving = 2,
        ingredients = listOf(
            "2 salmon fillets",
            "1 tbsp olive oil",
            "1 tbsp lemon juice",
            "2 garlic cloves, minced",
            "1 bunch asparagus, trimmed",
            "Salt and pepper to taste",
            "1 tbsp fresh dill, chopped (optional)"
        ),
        recipe = listOf(
            "Preheat the oven to 400°F (200°C).",
            "Place the salmon fillets on a baking sheet lined with parchment paper.",
            "Drizzle the salmon with olive oil and lemon juice, then sprinkle with minced garlic, salt, and pepper.",
            "Arrange the asparagus around the salmon on the baking sheet, drizzle with olive oil, and season with salt and pepper.",
            "Bake in the oven for 18-20 minutes until the salmon is cooked through and flakes easily with a fork.",
            "Garnish with fresh dill if desired, and serve with the roasted asparagus."
        ),
        imageUrl = "https://cdn-uploads.mealime.com/uploads/recipe/thumbnail/307/presentation_0ed152c0-47ef-4536-81b4-02dc6f31f876.jpg"
    )
)

val dietPlanDay5 = DietPlan(
    breakfast = Meal(
        name = "Avocado Toast with Poached Egg",
        prepTime = "5 min",
        cookTime = "5 min",
        serving = 1,
        ingredients = listOf(
            "1 slice whole-grain bread",
            "1/2 ripe avocado, mashed",
            "1 large egg",
            "1 tsp olive oil",
            "Salt and pepper to taste",
            "1/2 tsp red pepper flakes (optional)",
            "1 tsp lemon juice"
        ),
        recipe = listOf(
            "Toast the slice of whole-grain bread to your desired crispiness.",
            "While the bread is toasting, bring a pot of water to a simmer and gently crack the egg into the water to poach for about 3-4 minutes.",
            "Mash the avocado with a fork, then add lemon juice, salt, and pepper to taste.",
            "Spread the mashed avocado on the toasted bread.",
            "Once the egg is poached, place it on top of the avocado toast.",
            "Sprinkle with red pepper flakes if desired, and enjoy your nutritious breakfast!"
        ),
        imageUrl = "https://www.tastingtable.com/img/gallery/sourdough-avocado-toast-recipe/intro-1663771590.webp"
    ),
    snack1 = Meal(
        name = "Cucumber and Hummus",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/2 cucumber, sliced into rounds",
            "3 tbsp hummus (store-bought or homemade)",
            "A pinch of paprika (optional)"
        ),
        recipe = listOf(
            "Slice the cucumber into thin rounds.",
            "Serve with a side of hummus for dipping.",
            "Optional: Sprinkle paprika over the hummus for extra flavor."
        ),
        imageUrl = "https://anothertablespoon.com/wp-content/uploads/2023/07/DSC09313-1170x889.jpeg"
    ),
    lunch = Meal(
        name = "Lentil and Vegetable Soup",
        prepTime = "10 min",
        cookTime = "30 min",
        serving = 2,
        ingredients = listOf(
            "1 cup dry lentils, rinsed",
            "1 onion, diced",
            "2 carrots, diced",
            "1 celery stalk, diced",
            "2 garlic cloves, minced",
            "1 can (14.5 oz) diced tomatoes",
            "4 cups vegetable broth",
            "1 tsp ground cumin",
            "1/2 tsp ground turmeric",
            "Salt and pepper to taste",
            "2 tbsp olive oil",
            "1 tbsp fresh parsley, chopped"
        ),
        recipe = listOf(
            "Heat olive oil in a large pot over medium heat.",
            "Add the diced onion, carrots, and celery and sauté for about 5 minutes until softened.",
            "Add the minced garlic and cook for another minute until fragrant.",
            "Stir in the cumin, turmeric, salt, and pepper, and cook for 1 minute.",
            "Add the lentils, diced tomatoes, and vegetable broth. Bring to a boil.",
            "Reduce heat to low, cover, and simmer for 25-30 minutes, or until the lentils are tender.",
            "Garnish with fresh parsley before serving."
        ),
        imageUrl = "https://cookieandkate.com/images/2019/01/best-lentil-soup-recipe-4.jpg"
    ),
    snack2 = Meal(
        name = "Banana and Almond Butter",
        prepTime = "2 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 medium banana",
            "2 tbsp almond butter (or peanut butter)",
            "1/2 tsp cinnamon (optional)"
        ),
        recipe = listOf(
            "Peel the banana and slice it into rounds.",
            "Serve with a side of almond butter for dipping.",
            "Optional: Sprinkle cinnamon on the banana slices for added flavor."
        ),
        imageUrl = "https://raisingonplants.com/wp-content/uploads/2022/04/Almond-butter-bite-featured-image.jpeg"
    ),
    dinner = Meal(
        name = "Vegetable Stir-Fry with Brown Rice",
        prepTime = "10 min",
        cookTime = "15 min",
        serving = 2,
        ingredients = listOf(
            "1 cup brown rice",
            "1 tbsp sesame oil",
            "1/2 cup bell peppers, sliced",
            "1/2 cup zucchini, sliced",
            "1/2 cup snap peas",
            "1/4 cup onion, sliced",
            "2 tbsp soy sauce",
            "1 tbsp rice vinegar",
            "1 tbsp honey",
            "1 tsp garlic, minced",
            "1 tsp ginger, minced",
            "1 tbsp sesame seeds",
            "2 tbsp chopped cilantro"
        ),
        recipe = listOf(
            "Cook the brown rice according to package instructions.",
            "In a large pan or wok, heat sesame oil over medium-high heat.",
            "Add the sliced bell peppers, zucchini, snap peas, and onion. Stir-fry for 5-7 min until the vegetables are tender-crisp.",
            "Add the minced garlic and ginger, and stir for another minute.",
            "Stir in the soy sauce, rice vinegar, and honey. Cook for an additional 2-3 minutes.",
            "Serve the stir-fried vegetables over the cooked brown rice.",
            "Top with sesame seeds and chopped cilantro before serving."
        ),
        imageUrl = "https://natashaskitchen.com/wp-content/uploads/2020/08/Vegetable-Stir-Fry-2-728x1092.jpg"
    )
)

val dietPlanDay6 = DietPlan(
    breakfast = Meal(
        name = "Greek Yogurt Parfait",
        prepTime = "5 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/2 cup Greek yogurt",
            "1/4 cup granola",
            "1/4 cup mixed berries (blueberries, strawberries, raspberries)",
            "1 tbsp chia seeds",
            "1 tbsp honey",
            "1 tsp vanilla extract"
        ),
        recipe = listOf(
            "In a serving glass or bowl, add a layer of Greek yogurt.",
            "Top with granola and mixed berries.",
            "Drizzle with honey and a touch of vanilla extract.",
            "Sprinkle chia seeds on top for added fiber and omega-3s.",
            "Layer the ingredients to your preference and enjoy your quick and healthy breakfast!"
        ),
        imageUrl = "https://lirp.cdn-website.com/5efdb7b3/dms3rep/multi/opt/greekyogurt-1920w.JPG"
    ),
    snack1 = Meal(
        name = "Apple with Almond Butter",
        prepTime = "2 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 medium apple",
            "2 tbsp almond butter",
            "1/4 tsp cinnamon (optional)"
        ),
        recipe = listOf(
            "Core and slice the apple into wedges.",
            "Serve with almond butter for dipping.",
            "Optional: Sprinkle with cinnamon for added flavor."
        ),
        imageUrl = "https://www.eatingwell.com/thmb/K4jDGSjhcbO0N8yOd1AwKF_rLRg=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/3759286-ca9f503b9884484ebdf15c6edf1b124c.jpg"
    ),
    lunch = Meal(
        name = "Chickpea Salad with Tahini Dressing",
        prepTime = "10 min",
        cookTime = "None",
        serving = 2,
        ingredients = listOf(
            "1 can (15 oz) chickpeas, drained and rinsed",
            "1 cucumber, diced",
            "1/4 red onion, diced",
            "1/4 cup cherry tomatoes, halved",
            "1/4 cup fresh parsley, chopped",
            "1 tbsp olive oil",
            "1 tbsp lemon juice",
            "2 tbsp tahini",
            "Salt and pepper to taste"
        ),
        recipe = listOf(
            "In a large bowl, combine the chickpeas, cucumber, red onion, tomatoes, and parsley.",
            "In a small bowl, whisk together olive oil, lemon juice, tahini, salt, and pepper.",
            "Drizzle the tahini dressing over the salad and toss to combine.",
            "Serve chilled or at room temperature."
        ),
        imageUrl = "https://thedeliciousplate.com/wp-content/uploads/2022/01/Indian-chickpea-salad-16-683x1024.jpg"
    ),
    snack2 = Meal(
        name = "Cottage Cheese with Pineapple",
        prepTime = "2 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/2 cup cottage cheese",
            "1/4 cup pineapple chunks (fresh or canned)",
            "1 tbsp flax seeds"
        ),
        recipe = listOf(
            "Spoon cottage cheese into a bowl.",
            "Top with pineapple chunks and sprinkle with flax seeds for added omega-3s.",
            "Enjoy this refreshing and protein-packed snack!"
        ),
        imageUrl = "https://i.pinimg.com/736x/19/75/6b/19756b9a19e448b4cfe375ea0f21db37.jpg"
    ),
    dinner = Meal(
        name = "Baked Salmon with Asparagus",
        prepTime = "10 min",
        cookTime = "20 min",
        serving = 2,
        ingredients = listOf(
            "2 salmon fillets",
            "1 bunch asparagus, trimmed",
            "1 tbsp olive oil",
            "1 lemon, sliced",
            "1 tsp garlic powder",
            "Salt and pepper to taste",
            "1 tbsp fresh dill, chopped (optional)"
        ),
        recipe = listOf(
            "Preheat the oven to 400°F (200°C).",
            "Place the salmon fillets on a baking sheet lined with parchment paper.",
            "Drizzle olive oil over the salmon and season with garlic powder, salt, and pepper.",
            "Place the lemon slices on top of the salmon fillets.",
            "Arrange the asparagus around the salmon and drizzle with a little olive oil.",
            "Bake for 15-20 minutes, or until the salmon is cooked through and flakes easily.",
            "Garnish with fresh dill if desired and serve."
        ),
        imageUrl = "https://www.cottercrunch.com/wp-content/uploads/2021/04/herb-sauce-baked-salmon-and-asparagus-one-pan-meal-2-1-scaled.jpg.webp"
    )
)

val dietPlanDay7 = DietPlan(
    breakfast = Meal(
        name = "Avocado Toast with Poached Egg",
        prepTime = "5 min",
        cookTime = "10 min",
        serving = 1,
        ingredients = listOf(
            "1 slice whole-grain bread",
            "1/2 avocado, mashed",
            "1 large egg",
            "1 tsp olive oil",
            "Salt and pepper to taste",
            "1/2 tsp red pepper flakes (optional)",
            "1 tsp lemon juice"
        ),
        recipe = listOf(
            "Toast the slice of whole-grain bread until crispy.",
            "While the bread is toasting, poach the egg by gently cracking it into simmering water and cooking for 3-4 minutes.",
            "Spread the mashed avocado on the toasted bread.",
            "Drizzle with olive oil and sprinkle with salt, pepper, and red pepper flakes if desired.",
            "Once the egg is poached, carefully place it on top of the avocado toast.",
            "Drizzle with a little lemon juice and serve immediately."
        ),
        imageUrl = "https://www.rootsandradishes.com/wp-content/uploads/2017/08/avocado-toast-with-everything-bagel-seasoning-4.jpg"
    ),
    snack1 = Meal(
        name = "Mixed Nuts and Dried Fruit",
        prepTime = "2 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/4 cup almonds",
            "1/4 cup cashews",
            "1/4 cup walnuts",
            "1/4 cup dried cranberries",
            "1/4 cup dried apricots, chopped",
            "1 tbsp sunflower seeds"
        ),
        recipe = listOf(
            "In a bowl, combine almonds, cashews, walnuts, dried cranberries, and dried apricots.",
            "Add sunflower seeds and toss everything together.",
            "Enjoy a handful as a nutritious snack full of healthy fats, fiber, and antioxidants."
        ),
        imageUrl = "https://annaandsarah.com/cdn/shop/articles/dried-fruits-and-nuts.jpg"
    ),
    lunch = Meal(
        name = "Lentil Soup with Spinach",
        prepTime = "10 min",
        cookTime = "30 min",
        serving = 2,
        ingredients = listOf(
            "1 cup dried lentils, rinsed",
            "4 cups vegetable broth",
            "1 cup fresh spinach, chopped",
            "1 onion, diced",
            "2 cloves garlic, minced",
            "1 carrot, diced",
            "2 celery stalks, diced",
            "1/2 tsp cumin",
            "1/2 tsp paprika",
            "Salt and pepper to taste",
            "1 tbsp olive oil",
            "1 tsp lemon juice"
        ),
        recipe = listOf(
            "In a large pot, heat olive oil over medium heat.",
            "Add the diced onion, garlic, carrot, and celery. Cook for 5-7 minutes until softened.",
            "Add the cumin and paprika, and cook for an additional minute until fragrant.",
            "Add the rinsed lentils and vegetable broth to the pot. Bring to a boil, then reduce to a simmer.",
            "Cook for 20-25 minutes, or until the lentils are tender.",
            "Stir in the chopped spinach and cook for another 5 minutes.",
            "Season with salt, pepper, and a squeeze of lemon juice to brighten the flavors.",
            "Serve hot and enjoy your hearty and nutritious lentil soup."
        ),
        imageUrl = "https://www.allrecipes.com/thmb/ybwu4sVypnUlYKkNeUmi9IR5XJ4=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/13978-Lentil-Soup-mfs-Steps-016-af33e3e305584caaad74103266ec1aa0.jpg"
    ),
    snack2 = Meal(
        name = "Celery with Peanut Butter",
        prepTime = "2 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "3-4 celery stalks",
            "2 tbsp peanut butter",
            "1 tsp honey (optional)"
        ),
        recipe = listOf(
            "Wash and slice the celery stalks into smaller pieces.",
            "Spread peanut butter on each piece of celery.",
            "Drizzle with honey if desired for a touch of sweetness.",
            "Enjoy this simple, crunchy snack packed with protein and healthy fats."
        ),
        imageUrl = "https://plowingthroughlife.com/wp-content/uploads/2022/01/Celery-and-Peanut-Butter-Recipe-768x1024.jpg"
    ),
    dinner = Meal(
        name = "Stuffed Bell Peppers with Quinoa and Black Beans",
        prepTime = "15 min",
        cookTime = "30 min",
        serving = 2,
        ingredients = listOf(
            "2 bell peppers, tops cut off and seeds removed",
            "1/2 cup quinoa",
            "1 cup black beans, cooked or canned",
            "1/2 cup corn kernels (fresh or frozen)",
            "1/4 cup red onion, diced",
            "1/2 tsp cumin",
            "1/2 tsp chili powder",
            "Salt and pepper to taste",
            "1 tbsp olive oil",
            "1/4 cup shredded cheese (optional)",
            "1 tbsp fresh cilantro, chopped"
        ),
        recipe = listOf(
            "Preheat the oven to 375°F (190°C).",
            "Cook the quinoa according to package instructions.",
            "While the quinoa cooks, heat olive oil in a pan over medium heat.",
            "Add the diced red onion and cook for 3-4 minutes until softened.",
            "Stir in the cooked quinoa, black beans, corn, cumin, chili powder, salt, and pepper. Cook for another 5 minutes, stirring occasionally.",
            "Stuff the bell peppers with the quinoa mixture and place them in a baking dish.",
            "Top with shredded cheese if using and bake in the oven for 20 minutes.",
            "Garnish with fresh cilantro and serve hot."
        ),
        imageUrl = "https://www.cookingclassy.com/wp-content/uploads/2014/09/chicken-fajita-stuffed-peppers-srgb..jpg"
    )
)

val dietPlanDay8 = DietPlan(
    breakfast = Meal(
        name = "Chia Pudding with Mango",
        prepTime = "5 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "3 tbsp chia seeds",
            "1 cup almond milk (or milk of choice)",
            "1 tbsp honey or maple syrup",
            "1/2 mango, diced",
            "1 tsp vanilla extract",
            "1/4 cup unsweetened coconut flakes"
        ),
        recipe = listOf(
            "In a bowl, combine chia seeds, almond milk, honey or maple syrup, and vanilla extract.",
            "Mix well and let it sit in the refrigerator for at least 4 hours or overnight until it thickens.",
            "Once the chia pudding has set, top with diced mango and coconut flakes.",
            "Serve chilled and enjoy a refreshing, nutrient-packed breakfast."
        ),
        imageUrl = "https://www.example.com/chia-pudding"
    ),
    snack1 = Meal(
        name = "Apple Slices with Almond Butter",
        prepTime = "2 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 apple, sliced",
            "2 tbsp almond butter",
            "1 tsp cinnamon (optional)"
        ),
        recipe = listOf(
            "Slice the apple into thin wedges.",
            "Spread almond butter on each slice or serve it as a dip.",
            "Sprinkle with cinnamon for an added touch of flavor (optional).",
            "Enjoy this sweet and satisfying snack."
        ),
        imageUrl = "https://www.example.com/apple-almond-butter"
    ),
    lunch = Meal(
        name = "Vegetable Stir-Fry with Brown Rice",
        prepTime = "10 min",
        cookTime = "15 min",
        serving = 2,
        ingredients = listOf(
            "1 cup brown rice",
            "1 tbsp sesame oil",
            "1 cup bell peppers, sliced",
            "1/2 cup snap peas",
            "1/2 cup broccoli florets",
            "1 carrot, julienned",
            "1 tbsp soy sauce",
            "1 tsp ginger, grated",
            "1 clove garlic, minced",
            "1 tbsp sesame seeds",
            "1 tbsp green onions, chopped"
        ),
        recipe = listOf(
            "Cook the brown rice according to package instructions.",
            "In a large skillet or wok, heat sesame oil over medium heat.",
            "Add bell peppers, snap peas, broccoli, and carrots. Stir-fry for 5-7 minutes until tender.",
            "Add grated ginger and minced garlic, and cook for another minute.",
            "Stir in soy sauce and cooked brown rice, mixing everything together.",
            "Top with sesame seeds and green onions for garnish.",
            "Serve hot and enjoy your delicious, veggie-packed lunch."
        ),
        imageUrl = "https://www.example.com/vegetable-stir-fry"
    ),
    snack2 = Meal(
        name = "Cucumber Slices with Feta Cheese",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 cucumber, sliced",
            "1/4 cup feta cheese, crumbled",
            "1 tbsp olive oil",
            "1 tbsp balsamic vinegar",
            "Salt and pepper to taste",
            "1 tbsp fresh dill, chopped (optional)"
        ),
        recipe = listOf(
            "Slice the cucumber into thin rounds.",
            "Top with crumbled feta cheese.",
            "Drizzle olive oil and balsamic vinegar over the cucumbers.",
            "Season with salt, pepper, and fresh dill for a refreshing snack.",
            "Serve immediately."
        ),
        imageUrl = "https://www.example.com/cucumber-feta"
    ),
    dinner = Meal(
        name = "Salmon with Roasted Asparagus and Sweet Potato",
        prepTime = "10 min",
        cookTime = "25 min",
        serving = 2,
        ingredients = listOf(
            "2 salmon fillets",
            "1 tbsp olive oil",
            "1 tsp garlic powder",
            "1 tsp paprika",
            "1 bunch asparagus, trimmed",
            "1 large sweet potato, peeled and cubed",
            "Salt and pepper to taste",
            "1 tsp lemon juice"
        ),
        recipe = listOf(
            "Preheat the oven to 400°F (200°C).",
            "Toss the sweet potato cubes with olive oil, salt, and pepper, and spread them out on a baking sheet.",
            "Roast the sweet potatoes in the oven for 20-25 minutes, flipping halfway through.",
            "While the sweet potatoes are roasting, prepare the salmon fillets.",
            "Rub the salmon with olive oil, garlic powder, paprika, salt, and pepper.",
            "Place the salmon on a baking sheet and bake for 12-15 minutes, or until cooked through.",
            "For the asparagus, drizzle with olive oil and season with salt and pepper.",
            "Roast the asparagus in the oven for 10-12 minutes, until tender.",
            "Serve the salmon with roasted asparagus and sweet potatoes, and drizzle with lemon juice."
        ),
        imageUrl = "https://www.example.com/salmon-roasted-vegetables"
    )
)

val dietPlanDay9 = DietPlan(
    breakfast = Meal(
        name = "Scrambled Eggs with Spinach and Avocado",
        prepTime = "5 min",
        cookTime = "5 min",
        serving = 1,
        ingredients = listOf(
            "2 large eggs",
            "1/4 cup fresh spinach, chopped",
            "1/2 avocado, diced",
            "1 tbsp olive oil",
            "Salt and pepper to taste",
            "1 tbsp cheddar cheese (optional)",
            "1 tsp fresh parsley, chopped (optional)"
        ),
        recipe = listOf(
            "In a bowl, whisk the eggs with a pinch of salt and pepper.",
            "Heat olive oil in a skillet over medium heat.",
            "Add the spinach to the pan and sauté until wilted, about 1-2 minutes.",
            "Pour the eggs into the skillet and scramble until cooked through, about 3-4 minutes.",
            "Remove from heat and stir in the diced avocado.",
            "Sprinkle with cheddar cheese and fresh parsley, if desired, and serve warm."
        ),
        imageUrl = "https://www.example.com/scrambled-eggs-spinach"
    ),
    snack1 = Meal(
        name = "Trail Mix with Nuts and Dried Fruit",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/4 cup almonds",
            "1/4 cup cashews",
            "1/4 cup dried cranberries",
            "1/4 cup dried apricots, chopped",
            "1/4 cup pumpkin seeds",
            "1/4 cup sunflower seeds",
            "1 tbsp dark chocolate chips (optional)"
        ),
        recipe = listOf(
            "In a small bowl, combine almonds, cashews, dried cranberries, chopped apricots, pumpkin seeds, and sunflower seeds.",
            "Add dark chocolate chips if using, and mix everything together.",
            "Enjoy this healthy, nutrient-packed snack anytime!"
        ),
        imageUrl = "https://www.example.com/trail-mix"
    ),
    lunch = Meal(
        name = "Chickpea and Cucumber Salad",
        prepTime = "10 min",
        cookTime = "None",
        serving = 2,
        ingredients = listOf(
            "1 can chickpeas, drained and rinsed",
            "1 cucumber, diced",
            "1/2 red onion, diced",
            "1/2 cup cherry tomatoes, halved",
            "1/4 cup fresh parsley, chopped",
            "1 tbsp olive oil",
            "1 tbsp lemon juice",
            "Salt and pepper to taste",
            "1/4 tsp cumin (optional)"
        ),
        recipe = listOf(
            "In a large bowl, combine the chickpeas, cucumber, red onion, cherry tomatoes, and parsley.",
            "Drizzle with olive oil and lemon juice.",
            "Season with salt, pepper, and cumin (if using).",
            "Toss everything together and serve immediately or refrigerate for 30 minutes for a colder salad."
        ),
        imageUrl = "https://www.skinnytaste.com/wp-content/uploads/2019/06/Chickpea-Salad-3.jpg"
    ),
    snack2 = Meal(
        name = "Peanut Butter Banana Bites",
        prepTime = "5 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 banana, sliced into rounds",
            "2 tbsp peanut butter",
            "1 tbsp dark chocolate chips (optional)",
            "A pinch of sea salt"
        ),
        recipe = listOf(
            "Slice the banana into round discs.",
            "Spread a small amount of peanut butter on each slice.",
            "Top with a few dark chocolate chips (if using) and sprinkle with sea salt.",
            "Serve immediately for a delicious, sweet snack."
        ),
        imageUrl = "https://www.example.com/peanut-butter-banana"
    ),
    dinner = Meal(
        name = "Stuffed Bell Peppers with Quinoa and Black Beans",
        prepTime = "10 min",
        cookTime = "30 min",
        serving = 2,
        ingredients = listOf(
            "2 large bell peppers, halved and seeds removed",
            "1/2 cup quinoa, cooked",
            "1/2 cup black beans, drained and rinsed",
            "1/4 cup corn kernels (fresh or frozen)",
            "1/4 cup diced tomatoes",
            "1 tsp cumin",
            "1 tsp paprika",
            "1 tbsp olive oil",
            "Salt and pepper to taste",
            "1/4 cup shredded cheese (optional)",
            "Fresh cilantro for garnish"
        ),
        recipe = listOf(
            "Preheat the oven to 375°F (190°C).",
            "Cut the bell peppers in half lengthwise and remove the seeds.",
            "In a large mixing bowl, combine cooked quinoa, black beans, corn, diced tomatoes, cumin, paprika, olive oil, salt, and pepper.",
            "Stuff the bell peppers with the quinoa mixture and place them on a baking sheet.",
            "Cover with foil and bake for 20 minutes.",
            "After 20 minutes, remove the foil and top with shredded cheese (if using).",
            "Bake for an additional 5-10 minutes, until the cheese melts and the peppers are tender.",
            "Garnish with fresh cilantro and serve."
        ),
        imageUrl = "https://www.example.com/stuffed-bell-peppers"
    )
)

val dietPlanDay10 = DietPlan(
    breakfast = Meal(
        name = "Avocado Toast with Poached Egg",
        prepTime = "5 min",
        cookTime = "5 min",
        serving = 1,
        ingredients = listOf(
            "1 slice whole grain bread",
            "1 ripe avocado, mashed",
            "1 large egg",
            "1 tsp olive oil",
            "Salt and pepper to taste",
            "1/2 tsp red pepper flakes (optional)",
            "1 tsp lemon juice",
            "Fresh parsley for garnish"
        ),
        recipe = listOf(
            "Toast the slice of whole grain bread to your liking.",
            "While the bread is toasting, bring a pot of water to a simmer and gently crack the egg into the water to poach for 3-4 minutes.",
            "Meanwhile, mash the avocado in a bowl and season with salt, pepper, and lemon juice.",
            "Spread the mashed avocado evenly onto the toasted bread.",
            "Once the egg is poached, place it on top of the avocado toast.",
            "Sprinkle with red pepper flakes and fresh parsley.",
            "Serve immediately and enjoy!"
        ),
        imageUrl = "https://www.example.com/avocado-toast-poached-egg"
    ),
    snack1 = Meal(
        name = "Carrot and Celery Sticks with Peanut Butter",
        prepTime = "5 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1 large carrot, sliced into sticks",
            "1 celery stalk, sliced into sticks",
            "3 tbsp peanut butter",
            "1 tsp honey (optional)",
            "A pinch of cinnamon (optional)"
        ),
        recipe = listOf(
            "Slice the carrot and celery into sticks.",
            "In a small bowl, mix peanut butter with honey and a pinch of cinnamon (if using).",
            "Dip the vegetable sticks into the peanut butter mixture.",
            "Enjoy a crunchy and satisfying snack."
        ),
        imageUrl = "https://www.example.com/carrot-celery-peanut-butter"
    ),
    lunch = Meal(
        name = "Grilled Veggie Wrap with Hummus",
        prepTime = "10 min",
        cookTime = "15 min",
        serving = 2,
        ingredients = listOf(
            "1 medium zucchini, sliced",
            "1 bell pepper, sliced",
            "1 red onion, sliced",
            "1 tbsp olive oil",
            "2 whole wheat wraps",
            "1/4 cup hummus",
            "1/4 cup feta cheese, crumbled (optional)",
            "A handful of spinach or lettuce",
            "Salt and pepper to taste"
        ),
        recipe = listOf(
            "Heat olive oil in a pan over medium heat.",
            "Add the zucchini, bell pepper, and onion slices and sauté until soft and lightly charred, about 8-10 minutes.",
            "Season with salt and pepper to taste.",
            "Spread hummus on each wrap and top with sautéed vegetables.",
            "Add spinach or lettuce and sprinkle with feta cheese.",
            "Roll up the wrap tightly and slice in half.",
            "Serve immediately or wrap up for a to-go meal."
        ),
        imageUrl = "https://www.example.com/grilled-veggie-wrap"
    ),
    snack2 = Meal(
        name = "Greek Yogurt and Mixed Nuts",
        prepTime = "3 min",
        cookTime = "None",
        serving = 1,
        ingredients = listOf(
            "1/2 cup Greek yogurt",
            "1 tbsp honey",
            "2 tbsp mixed nuts (almonds, walnuts, cashews)",
            "1 tbsp chia seeds",
            "A pinch of cinnamon (optional)"
        ),
        recipe = listOf(
            "In a bowl, add Greek yogurt and drizzle with honey.",
            "Top with mixed nuts, chia seeds, and a pinch of cinnamon.",
            "Stir everything together and enjoy this protein-packed snack."
        ),
        imageUrl = "https://www.example.com/yogurt-nuts"
    ),
    dinner = Meal(
        name = "Chicken Stir-Fry with Broccoli and Bell Peppers",
        prepTime = "10 min",
        cookTime = "15 min",
        serving = 2,
        ingredients = listOf(
            "2 boneless, skinless chicken breasts, sliced",
            "1 cup broccoli florets",
            "1 bell pepper, sliced",
            "1 carrot, julienned",
            "2 tbsp soy sauce",
            "1 tbsp sesame oil",
            "1 garlic clove, minced",
            "1 tbsp ginger, minced",
            "1 tbsp honey",
            "1 tbsp rice vinegar",
            "1 tsp sesame seeds (optional)",
            "Green onions for garnish"
        ),
        recipe = listOf(
            "Heat sesame oil in a large skillet or wok over medium-high heat.",
            "Add the chicken slices and stir-fry for 4-5 minutes until browned and cooked through.",
            "Add the garlic, ginger, and vegetables (broccoli, bell pepper, and carrot) and stir-fry for another 5-6 minutes, until tender-crisp.",
            "In a small bowl, whisk together soy sauce, honey, and rice vinegar.",
            "Pour the sauce over the stir-fry and toss everything together until evenly coated.",
            "Garnish with sesame seeds and green onions before serving."
        ),
        imageUrl = "https://www.example.com/chicken-stir-fry"
    )
)




