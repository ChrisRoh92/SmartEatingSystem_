package de.rohnert.smarteatingsystem.utils.testing

import de.rohnert.smarteatingsystem.data.databases.body_database.Body
import de.rohnert.smarteatingsystem.utils.datesAreOnSameDay
import de.rohnert.smarteatingsystem.utils.getDateListFromToDate
import java.util.*
import kotlin.collections.ArrayList


fun getUniqueBodyMap(bodyList:ArrayList<Body>):Map<Int,Body> {
    // Map erstellen für Bodyeinträge mit Indices:
    val bodyMap = mutableMapOf<Int, Body>()

    // Start und End Datum feststellen....
    val fromDate = Date(bodyList[0].date)
    val toDate = Date(bodyList.last().date)

    // Liste von Dates zwischen 1. und letzten Date generieren:
    val dayList = getDateListFromToDate(fromDate, toDate)
    // Durch übergebene Body durchiterien:
    var currentDate = fromDate
    for (body: Body in bodyList) {
        // Prüfen, welcher Index das jeweilige Date von bodyList in dayList hat:
        for (i in dayList.indices) {
            if (datesAreOnSameDay(currentDate, dayList[i])) {
                bodyMap[i] = body
                currentDate = Date(body.date)
                break
            }
        }



    }

    return bodyMap
}


fun main() {
    println("Hallo Christoph")


//    var cal = Calendar.getInstance()
//
//    val fromDate = cal.time
//    cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)+60)
//    val toDate = cal.time
//    println("fromDate = $fromDate")
//    println("toDate = $toDate")
//
//    val dateList = getDateListFromToDate(fromDate,toDate)
//    for(date:Date in dateList)
//        println(date)

    // Liste mit Bodys erstellen
    var bodies = ArrayList<Body>()
    // Kalender Objekt
    var cal = Calendar.getInstance()

    bodies.add(Body(cal.time.time,105f,0f,0f,0f,0f,0f,""))
    cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)+1)
    bodies.add(Body(cal.time.time,104f,0f,0f,0f,0f,0f,""))
    cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)+2)
    bodies.add(Body(cal.time.time,103f,0f,0f,0f,0f,0f,""))
    cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)+3)
    bodies.add(Body(cal.time.time,102f,0f,0f,0f,0f,0f,""))
    cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY)+4)
    bodies.add(Body(cal.time.time,101f,0f,0f,0f,0f,0f,""))
    cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)+1)
    bodies.add(Body(cal.time.time,101f,0f,0f,0f,0f,0f,""))


    val bodyMap = getUniqueBodyMap(bodies)
    for(entry in bodyMap)
    {
        println("Index: ${entry.key} \t Date: ${Date(entry.value.date)} \t Weight: ${entry.value.weight}")
    }









}