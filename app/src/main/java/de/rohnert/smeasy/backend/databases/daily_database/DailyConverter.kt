package de.rohnert.smeasy.backend.databases.daily_database

import androidx.room.TypeConverter
import de.rohnert.smeasy.backend.helper.Helper
import java.util.*
import kotlin.collections.ArrayList

class DailyConverter
{
    var helper = Helper()


    @TypeConverter
    fun fromMealListToString(list: ArrayList<MealEntry>):String?
    {
        var export:ArrayList<String> = ArrayList()
        if(list.isNotEmpty())
        {
            for(i in list) export.add(fromMealEntryToString(i)!!)
        }



        return export.joinToString(";")
    }

    @TypeConverter
    fun fromStringToMealList(value: String?): ArrayList<MealEntry>
    {
        if(!value.isNullOrEmpty())
        {
            var content = value.split(";")
            var mealList:ArrayList<MealEntry> = ArrayList()
            for(i in content)
            {
                mealList.add(fromStringToMealEntry(i)!!)
            }

            return mealList
        }
        else
        {
            return ArrayList()
        }

    }

    @TypeConverter
    fun fromMealEntryToString(entry: MealEntry?):String?
    {
        return "${entry!!.mealID}_${entry.id}_${entry.menge}"

    }

    @TypeConverter
    fun fromStringToMealEntry(value: String?): MealEntry?
    {

        var content = value!!.split("_")

        return MealEntry(content[0].toInt(),content[1],content[2].toFloat())
    }






}