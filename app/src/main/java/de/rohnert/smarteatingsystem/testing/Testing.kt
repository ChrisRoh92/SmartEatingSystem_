package de.rohnert.smarteatingsystem.testing

import java.util.*

fun main()
{
    println("Hallo Welt!")

    val date = Date()
    println("date.time: ${date.time}")

    val newDate = Date(date.time)
    println("newDate: $newDate")

    val strDate = date.time.toString()
    println("strDate: $strDate")

    val newDateFromString = Date(strDate.toLong())
    println("newDateFromString: $newDateFromString")

}