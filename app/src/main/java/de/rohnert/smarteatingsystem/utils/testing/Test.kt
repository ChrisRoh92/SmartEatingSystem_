package de.rohnert.smarteatingsystem.utils.testing

import de.rohnert.smarteatingsystem.utils.getDateListFromToDate
import java.util.*


fun main() {
    println("Hallo Christoph")


    var cal = Calendar.getInstance()

    val fromDate = cal.time
    cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)+60)
    val toDate = cal.time
    println("fromDate = $fromDate")
    println("toDate = $toDate")

    val dateList = getDateListFromToDate(fromDate,toDate)
    for(date:Date in dateList)
        println(date)



}