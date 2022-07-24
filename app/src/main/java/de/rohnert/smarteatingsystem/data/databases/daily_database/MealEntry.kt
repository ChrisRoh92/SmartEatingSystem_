package de.rohnert.smarteatingsystem.data.databases.daily_database

data class MealEntry(
        var mealID:Int,
        var id:String,
        var menge:Float,
        var kcal:Float,
        var carb:Float,
        var protein:Float,
        var fat:Float
        )
