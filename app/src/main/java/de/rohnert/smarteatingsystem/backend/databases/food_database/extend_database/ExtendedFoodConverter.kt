package de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database

import androidx.room.TypeConverter
import de.rohnert.smarteatingsystem.utils.stringIsNumericOnline


class ExtendedFoodConverter
{


    @TypeConverter
    fun fromPortionNameToString(value:ArrayList<String>):String
    {

        return value.joinToString(";")

    }

    @TypeConverter
    fun fromPortionValueToString(value:ArrayList<Double>):String
    {

        return value.joinToString(";")

    }



    @TypeConverter
    fun fromStringToPortionName(str:String):ArrayList<String>
    {
        return if(str.isBlank() || str.isEmpty())
            ArrayList()
        else
            ArrayList(str.split(";"))


    }

    @TypeConverter
    fun fromStringToPortionSize(str:String):ArrayList<Double>
    {

        if(str.isBlank() || str.isEmpty())
            return ArrayList()
        else
        {
            var temp = str.split(";")

            var values = ArrayList<Double>()
            for(i in temp)
            {
                if(stringIsNumericOnline(i))
                {
                    values.add(i.toDouble())
                }
            }
            return values
        }


    }
}