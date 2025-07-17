package com.example.pregnancytrackerignite.data.utils

import android.content.Context
import com.example.pregnancytrackerignite.R

object SizeHelper {
     var sizeList : ArrayList<Int> =  ArrayList()
     var fruitNameList : ArrayList<String> =  ArrayList()

    suspend fun addSizeInList(context: Context) {
        sizeList.clear()
        fruitNameList.clear()
        sizeList.apply {
            for (i in 4..40) {
                val resourceId = context.resources.getIdentifier("week$i", "drawable", context.packageName)
                add(resourceId)
            }
        }
        fruitNameList.apply {
            add("Poppy Seed")
            add("Sesame Seed")
            add("Lentil")
            add("Blueberry")
            add("Kidney Bean")
            add("Grape")
            add("Kumquat")
            add("Fig")
            add("Lime")
            add("Peeped")
            add("Lemon")
            add("Apple")
            add("Avocado")
            add("Turnip")
            add("Bell Pepper")
            add("Heirloom Tomato")
            add("Banana")
            add("Carrot")
            add("Spaghetti Squash")
            add("Large Mango")
            add("Ear of Corn")
            add("Rutabaga")
            add("Scallion")
            add("Cauliflower")
            add("Large Eggplant")
            add("Butternut Squash")
            add("Large Cabbage")
            add("Coconut")
            add("Jicama")
            add("Pineapple")
            add("Cantaloupe")
            add("Honeydew Melon")
            add("Head of Romaine Lettuce")
            add("Bunch of Swiss Chard")
            add("Leek")
            add("Mini Watermelon")
            add("Small Pumpkin")
        }
    }
}
