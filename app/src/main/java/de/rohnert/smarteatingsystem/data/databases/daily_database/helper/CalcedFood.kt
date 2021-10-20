package de.rohnert.smarteatingsystem.data.databases.daily_database.helper

import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood

data class CalcedFood(var id:Int, var f: ExtendedFood, var menge:Float, var values:ArrayList<Float> )