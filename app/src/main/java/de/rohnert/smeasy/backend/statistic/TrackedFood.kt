package de.rohnert.smeasy.backend.statistic

import com.example.roomdatabaseexample.backend.databases.food_database.Food

data class TrackedFood(var food: Food,var menge:Float,var values:ArrayList<Float> )