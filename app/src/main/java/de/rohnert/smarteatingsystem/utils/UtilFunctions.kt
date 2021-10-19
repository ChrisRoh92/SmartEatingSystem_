package de.rohnert.smarteatingsystem.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun getDateStringFromLongDateString(date: Long): String {

    return try {
        getStringFromDate(Date(date))
    } catch (e: Exception) {
        e.printStackTrace()
        "Fehler..."
    }
}

fun getLongDateFromDateString(date:String):Long
{
    return getDateFromString(date).time
}

fun getDateFromDateLong(date:Long):Date = Date(date)



fun getDateFromString(date:String,pattern:String = "dd.MM.yyyy - HH:mm"):Date
{
    return SimpleDateFormat(pattern).parse(date)
}

fun getStringFromDate(date:Date,pattern:String = "dd.MM.yyyy - HH:mm"):String
{
    return SimpleDateFormat(pattern).format(date)
}


fun getTimeStringFromDate(date:Date):String = getStringFromDate(date,"HH:mm")

fun getSimpleDateStringFromDate(date:Date):String = getStringFromDate(date,"dd.MM.yyyy")