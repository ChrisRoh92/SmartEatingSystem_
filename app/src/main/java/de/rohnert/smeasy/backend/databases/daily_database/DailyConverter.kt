package com.example.roomdatabaseexample.backend.databases.daily_database

import androidx.room.TypeConverter
import backend.helper.Helper
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
            var content = value!!.split(";")
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
    fun fromStringToList(value:String?):ArrayList<String>?
    {
        if(value == null) return ArrayList()
        else return ArrayList(value.split(";"))


    }

    @TypeConverter
    fun fromListToString(list:ArrayList<String>?):String?
    {
        if(list.isNullOrEmpty()) return ""

        return list.joinToString(";")
    }

    @TypeConverter
    fun fromDateToString(date: Date?):String?
    {
        return helper.getStringFromDate(date = date!!)
    }

    @TypeConverter
    fun fromStringToDate(value: String?): Date?
    {
        return helper.getDateFromString(value!!)
    }

    @TypeConverter
    fun fromMealEntryToString(entry: MealEntry?):String?
    {
        return "${entry!!.mealID}_${entry!!.id}_${entry.menge}"

    }

    @TypeConverter
    fun fromStringToMealEntry(value: String?): MealEntry?
    {

        var content = value!!.split("_")

        return MealEntry(content[0].toInt(),content[1],content[2].toFloat())
    }






}