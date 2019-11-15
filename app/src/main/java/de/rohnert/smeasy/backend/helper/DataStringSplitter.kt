package backend.helper

import com.example.roomdatabaseexample.backend.databases.body_database.Body
import com.example.roomdatabaseexample.backend.databases.daily_database.MealEntry
import com.example.roomdatabaseexample.backend.databases.food_database.Food

class DataStringSplitter {

    // Globale Variablen...
    var helper:Helper =Helper()


    // Food-Objekte erstellen...
    // Über diese Methode wird ein String gesplittet und zurückgegeben
    // Wie genau, muss ich mir noch überlegen....
    fun createFoodFromString(input: String): Food {
        var value = input.split(";")

        var export: Food =
            Food(
                value[0],
                value[1],
                value[2],
                value[3],
                value[4].toFloat(),
                value[5].toFloat(),
                value[6].toFloat(),
                value[7].toFloat(),
                ""
            )
        return export

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



    // Body-Objekte aus String erstellen
    fun createBodyFromString(input:String): Body?
    {
        var value = input.split(";")
        // var weight:Float,var bauch:Float,var brust:Float,var hals:Float, var huefte:Float
        var export: Body? = null
            /*Body(
                helper.getDateFromString(value[0]),
                value[1].toFloat(),
                value[2].toFloat(),
                value[3].toFloat(),
                value[4].toFloat(),
                value[5].toFloat(),
                value[6].toFloat()
            )*/
        return export!!
    }

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