package de.rohnert.smarteatingsystem.data.databases.daily_database.helper

import de.rohnert.smarteatingsystem.data.databases.daily_database.Daily
import de.rohnert.smarteatingsystem.data.databases.daily_database.MealEntry


fun getMealEntryFromCalcedFood(f: CalcedFood):MealEntry
{
    return MealEntry(
        f.id,
        f.f.id,
        f.menge,
        f.values[0],
        f.values[1],
        f.values[2],
        f.values[3]
    )
}

//fun getNutritionDateFromDailyObject(daily: Daily):HashMap<String, Float>
//{
//    var data:HashMap<String, Float> = hashMapOf(
//        Pair("kcal", 0.0F),
//        Pair("carb", 0.0F),
//        Pair("protein", 0.0F),
//        Pair("fat", 0.0F)
//    )
//
//    // Get from Breakfast:
//    if(!daily.breakfastEntry.isNullOrEmpty())
//    {
//        for(entry in daily.breakfastEntry!!)
//        {
////            val food:Food =
////            data["kcal"] += entry.
//        }
//    }
//
//
//
//}