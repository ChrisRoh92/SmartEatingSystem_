package de.rohnert.smarteatingsystem.data.databases.daily_database

import android.util.Log
import androidx.room.TypeConverter
import de.rohnert.smarteatingsystem.data.helper.Helper
import de.rohnert.smarteatingsystem.utils.Constants
import de.rohnert.smarteatingsystem.utils.Constants.LOGGING_TAG
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
        if(entry != null)
        {
            return "" +
                    "${entry!!.mealID}_" +
                    "${entry.id}_" +
                    "${entry.menge}_" +
                    "${entry.kcal}_" +
                    "${entry.carb}_" +
                    "${entry.protein}_" +
                    "${entry.fat}"

        }

        return ""

    }

    @TypeConverter
    fun fromStringToMealEntry(value: String?): MealEntry?
    {

        Log.d(LOGGING_TAG,value)

        val content = value!!.split("_")


        return MealEntry(
                    content[0].toInt(),
                    content[1],
                    content[2].toFloat(),
                    content[3].toFloat(),
                    content[4].toFloat(),
                    content[5].toFloat(),
                    content[6].toFloat()
        )
    }






}