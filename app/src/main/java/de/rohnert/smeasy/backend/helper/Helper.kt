package backend.helper

import android.util.Log
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
    fun getLocalDateFromDate(date:Date):LocalDate
    {
        var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var localDate:LocalDate = LocalDate.parse(getStringFromDate(date),formatter)
        return localDate

    }

    fun getDateFromLocalDate(localDate: LocalDate):Date
    {
        val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        return date
    }

    // Über diese Methode wird die Anzahl der Jahre zwischen zwei Datums zurückgegeben...
    fun getYearsBetweenDates(date1:Date,date2:Date):Int
    {
        var value = abs(date2.time - date1.time) /(3600*24*365)
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

        var mDate: LocalDateTime = LocalDateTime.now()
        return Date.from(mDate.atZone(ZoneId.systemDefault()).toInstant());
    }


}