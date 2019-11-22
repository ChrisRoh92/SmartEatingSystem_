package backend.helper

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs


class Helper
{


    // Über diese Methode wird aus einem String ein Date-Objekt
    fun getDateFromString(input:String):Date
    {
        var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var localDate:LocalDate = LocalDate.parse(input,formatter)
        val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        return date
    }
    // Über diese Methode wird aus einem Date-Objekt wieder ein String
    fun getStringFromDate(date:Date):String
    {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var localDate:LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        val formatted = localDate.format(formatter)

        return formatted
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