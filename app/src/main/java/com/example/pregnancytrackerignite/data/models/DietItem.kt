package com.example.pregnancytrackerignite.data.models

import androidx.annotation.Keep
import java.util.UUID

@Keep
data class DietItem(val name: String, val calories: Int, val id: Long = 0)

val breakfastItems by lazy {
    listOf(
        DietItem("Boiled Egg", 36),
        DietItem("Oatmeal", 150),
        DietItem("Greek Yogurt", 100),
        DietItem("Avocado Toast", 200),
        DietItem("Banana", 90),
        DietItem("Smoothie (Mixed Berries)", 180),
        DietItem("Pancakes", 220),
        DietItem("Granola Bar", 130),
        DietItem("Whole Grain Cereal", 120),
        DietItem("Scrambled Eggs", 140),
        DietItem("Orange Juice", 110),
        DietItem("Apple", 80),
        DietItem("Toast with Peanut Butter", 180),
        DietItem("Almond Milk", 40),
        DietItem("Chia Seed Pudding", 150),
        DietItem("Blueberry Muffin", 250),
        DietItem("Bagel with Cream Cheese", 300),
        DietItem("Smoothie (Spinach, Banana, Almond Milk)", 180),
        DietItem("Egg White Omelette", 70),
        DietItem("Whole Wheat Toast", 80)
    )
}
val lunchItems by lazy {
    listOf(
        DietItem("Chicken Salad", 150),
        DietItem("Turkey Sandwich", 250),
        DietItem("Quinoa Salad", 180),
        DietItem("Grilled Chicken Breast", 200),
        DietItem("Caesar Salad", 220),
        DietItem("Lentil Soup", 150),
        DietItem("Veggie Wrap", 200),
        DietItem("Tuna Salad", 180),
        DietItem("Beef Tacos", 300),
        DietItem("Sushi Roll", 250),
        DietItem("Tomato Soup", 100),
        DietItem("Chicken Wrap", 300),
        DietItem("Veggie Burger", 270),
        DietItem("Stir-Fried Vegetables", 150),
        DietItem("Pasta Salad", 220),
        DietItem("Chickpea Curry", 250),
        DietItem("Grilled Cheese Sandwich", 320),
        DietItem("Rice with Vegetables", 180),
        DietItem("Shrimp Salad", 170),
        DietItem("Chicken Quesadilla", 350)
    )
}
val dinnerItems by lazy {
    listOf(
        DietItem("Grilled Salmon", 200),
        DietItem("Steak with Vegetables", 350),
        DietItem("Spaghetti Bolognese", 400),
        DietItem("Chicken Stir Fry", 250),
        DietItem("Vegetable Curry", 220),
        DietItem("Roasted Chicken", 300),
        DietItem("Beef Stew", 330),
        DietItem("Grilled Shrimp", 180),
        DietItem("Turkey Meatloaf", 270),
        DietItem("Quinoa with Veggies", 230),
        DietItem("Lamb Chops", 400),
        DietItem("Fish Tacos", 250),
        DietItem("Stuffed Peppers", 280),
        DietItem("Chicken Alfredo Pasta", 350),
        DietItem("Vegetarian Chili", 200),
        DietItem("Baked Cod", 220),
        DietItem("Pork Chops", 350),
        DietItem("Chicken Fajitas", 300),
        DietItem("Mushroom Risotto", 330),
        DietItem("Grilled Tofu", 180)
    )
}