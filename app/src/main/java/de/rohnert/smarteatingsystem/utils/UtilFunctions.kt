package de.rohnert.smarteatingsystem.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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



fun stringIsNumericOnline(str:String):Boolean
{
    return str.matches("-?\\d+(\\.\\d+)?".toRegex())
}


fun datesAreOnSameDay(date1:Date,date2:Date):Boolean
{
    // Calendar Instanz von date1
    val cal1 = Calendar.getInstance()
    cal1.time = date1

    // Calendar Instanz von date2
    val cal2 = Calendar.getInstance()
    cal2.time = date2

    return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
}

fun getDateListFromToDate(fromDate:Date,toDate:Date):ArrayList<Date>
{
    var dates:ArrayList<Date> = ArrayList()
    var cal = Calendar.getInstance()
    var dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
    cal.time = fromDate


    // fromDate eintragen:


    // Fall, dass fromDate == toDate ist:
    if(datesAreOnSameDay(fromDate,toDate))
    {
        dates.add(fromDate)
        return dates
    }

    else
    {
        do {
            dates.add(cal.time)
            dayOfYear++
            cal.set(Calendar.DAY_OF_YEAR,dayOfYear)
        } while(!datesAreOnSameDay(toDate,cal.time))

        // Letzten Eintrag:
        dates.add(cal.time)


    }




    return dates
}