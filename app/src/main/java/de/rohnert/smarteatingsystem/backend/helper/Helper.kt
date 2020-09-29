package de.rohnert.smarteatingsystem.backend.helper

import android.util.Log
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.roundToInt


class Helper
{


    // Über diese Methode wird aus einem String ein Date-Objekt
    fun getDateFromString(input:String):Date
    {
        //var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        //var localDate:LocalDate = LocalDate.parse(input,formatter)
        //val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val formatter2 = SimpleDateFormat("dd.MM.yyyy")
        var date2 = formatter2.parse(input)

        return date2
    }
    // Über diese Methode wird aus einem Date-Objekt wieder ein String
    fun getStringFromDate(date:Date):String
    {
        val formatter2 = SimpleDateFormat("dd.MM.yyyy")
        //val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        //var localDate:LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        var formatted2 = formatter2.format(date)
        //val formatted = localDate.format(formatter)

        return formatted2
    }

    fun getWeekNumberFromDate(date:Date):Int
    {
        val cal = Calendar.getInstance()
        cal.time = date


        return cal.get(Calendar.WEEK_OF_YEAR)
    }

    fun getDayOfWeekFromDate(date:Date):Int
    {
        var cal = Calendar.getInstance(Locale.GERMANY)
        cal.time = date
        var dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-2
        if(dayOfWeek<0)
            dayOfWeek += 7

        return dayOfWeek
    }

    fun getDateWithOffsetDays(date:Date,offset:Int):Date
    {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE,offset)

        return cal.time
    }



    fun getStringFromDateWithPattern(date:Date,pattern: String):String
    {
        val formatter2 = SimpleDateFormat(pattern)
       //val formatter = DateTimeFormatter.ofPattern(pattern)
       //var localDate:LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
       //val formatted = localDate.format(formatter)
        var formatted2 = formatter2.format(date)

        return formatted2
    }

    // LocalDate aus Date
    /*fun getLocalDateFromDate(date:Date):LocalDate
    {
        var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var localDate:LocalDate = LocalDate.parse(getStringFromDate(date),formatter)
        return localDate

    }

    fun getDateFromLocalDate(localDate: LocalDate):Date
    {
        val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        return date
    }*/

    // Über diese Methode wird die Anzahl der Jahre zwischen zwei Datums zurückgegeben...
    fun getYearsBetweenDates(date1:Date,date2:Date):Int
    {
        var value = abs(date2.time - date1.time) /(3600*24*365)
        value /= 1000


        return value.toInt()
    }

    // Über diese Methode wird die Anzahl der Jahre zwischen zwei Datums zurückgegeben...
    fun getDaysBetweenDates(higherDate:Date,lowerDate:Date):Int
    {
        var value = abs(higherDate.time - lowerDate.time) /(3600*24)
        value /= 1000


        return value.toInt()
    }

    // Über dise Funktion wird eine DecimalZahl als String zurückgegeben:
    fun getFloatAsFormattedString(input:Float, pattern:String ="#.##"):String
    {
        val df = DecimalFormat(pattern)
        return df.format(input)
    }

    fun isDateInFuture(date:Date):Boolean
    {
        var value = ((date.time)/(1000*60*60*24f)).roundToInt() - (getCurrentDate().time/(1000*60*60*24f)).roundToInt()

        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date
        cal2.time = getCurrentDate()
        val sameDay = cal1.get(Calendar.DAY_OF_YEAR) >= cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) === cal2.get(Calendar.YEAR)


        Log.d("Smeasy","Helper - isDateInFuture - sameDay = $sameDay")

        return sameDay
    }
    
    fun getWeekListFromDate(date:Date):ArrayList<Date>
    {
        var export:ArrayList<Date> = ArrayList()
        var cal = Calendar.getInstance()
        cal.time = date
        //cal.set(mDate.year,mDate.month,mDate.day)
        cal.firstDayOfWeek = Calendar.MONDAY
        var items = arrayListOf(Calendar.MONDAY,Calendar.TUESDAY,Calendar.WEDNESDAY,Calendar.THURSDAY,Calendar.FRIDAY,Calendar.SATURDAY,Calendar.SUNDAY)

        for(i in items)
        {
            cal.set(Calendar.DAY_OF_WEEK, i)
            export.add(cal.time)
            //println(simpleDateFormat.format(cal.getTime()))
        }


        return export
    }

    fun getWeekListAsString(mDate:Date):ArrayList<String>
    {
        var values = getWeekListFromDate(mDate)
        var export:ArrayList<String> = ArrayList()
        for(i in values)
        {
            export.add(getStringFromDate(i))
        }

        return export


    }

    fun getDateListWithValue(date: Date,value:Long):ArrayList<Date>
    {

        var cal = Calendar.getInstance()
        cal.time = date
        var dates:ArrayList<Date> = ArrayList()
        dates.add(cal.time)
        for(i in 0 until value-1 step 1)
        {
            cal.add(Calendar.DATE,-1)
            dates.add(cal.time)
        }

        return dates
    }

    fun convertDateListToStringList(dateList:ArrayList<Date>):ArrayList<String>
    {
        var export:ArrayList<String> = ArrayList()
        for(i in dateList)
        {
            export.add(getStringFromDate(i))
        }
        return export
    }





    // Über dise Funktion wird eine DecimalZahl als String zurückgegeben:
    fun getFloatAsFormattedStringWithPattern(input:Float,pattern:String):String
    {
        val df = DecimalFormat(pattern)
        var export:String = df.format(input)



        return export
    }

    // Heutiges Datum abholen...
    fun getCurrentDate():Date
    {
        return Date()
    }


}