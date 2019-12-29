package de.rohnert.smarteatingsystem.backend.helper

import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFood

class DataStringSplitter {

    // Globale Variablen...
    var helper: Helper = Helper()


    fun getExtendedFoodFromString(input:String): ExtendedFood
    {
        var values = input.split(";")
        var extendedFood = ExtendedFood(
            values[0],
            values[1],
            values[2],
            values[3],
            values[4],
            values[5],
            values[6],
            values[7].toFloat(),
            values[8].toFloat(),
            values[9].toFloat(),
            values[10].toFloat()
        )
        return extendedFood
    }



    // Daily-Objekt erstellen....
    // Aus String ArrayListen, Listen aus MealEntries erstellen....
    /*fun getMealEntries(values: ArrayList<String>): ArrayList<MealEntry> {
        var export: ArrayList<MealEntry> = ArrayList()
        for (i in values) {
            var s = i.split("_")
            export.add(MealEntry(s[1], s[2].toFloat()))
        }

        return export
    }*/

    // Aus einem String ein Daily-Objekt erstellen....
    /*fun createDailyFromString(input: String): Daily {

        var value = input.split(";")
        var mealValues: ArrayList<ArrayList<String>> = ArrayList()
        for(i in 0..4)
        {
            mealValues.add(ArrayList())
        }
        //for(i in value) println(i)
        var mealEntryList: ArrayList<ArrayList<MealEntry>> = ArrayList()
        for (i in value) {
            with(i.toLowerCase()) {
                when {
                    contains("b") -> mealValues[0].add(i)
                    contains("l") -> mealValues[1].add(i)
                    contains("d") -> mealValues[2].add(i)
                    contains("s") -> mealValues[3].add(i)
                    else -> print("")

                }
            }
        }

        for (i in mealValues) {
            mealEntryList.add(getMealEntries(i))
        }

        var daily: Daily = Daily(value[0],mealEntryList[0],mealEntryList[1],mealEntryList[2],mealEntryList[3])

        return daily
    }*/


    // User-Objekt aus String erstellen
    /*fun createUserFromString(input:String):User
    {
        var value = input.split(";")
        var user = User(value[0],value[1],value[2].toInt(),helper.getDateFromString(value[3]))
        return user

    }*/

    // BodyAim-Objekt aus String erstellen
    /*fun createBodyAimFromString(input:String):BodyAim
    {
        var values = input.split(";")
        var export:BodyAim= BodyAim(
            values[0].toFloat(),
            values[1].toFloat(),
            values[2].toFloat(),
            values[3].toFloat(),
            values[4].toFloat(),
            values[5].toFloat(),
            values[6]
        )
        return export
    }*/
    // BodySettings-Objekt aus String erstellen
    /*fun createBodySettingsFromString(input:String):BodySettings
    {
        var values = input.split(";")
        var export:BodySettings= BodySettings(
            values[0].toFloat(),
            values[1].toFloat(),
            values[2].toFloat(),
            values[3].toFloat()

        )
        return export

    }*/


}
