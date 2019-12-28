package de.rohnert.smeasy.backend.helper

import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class DataHandler(var manager: AssetManager, var mDir:String)
{


    // AppFoodList laden:
    fun getAppFoodList():ArrayList<String>
    {
        var export:ArrayList<String> = ArrayList()

        // Hier noch den Stuff für das Laden aus dem Asset in Android...
        // InputStream input;
        var input:InputStreamReader = InputStreamReader(manager.open("appfoodlist_clean.txt"))
        var bufferedReader:BufferedReader = BufferedReader(input)
        var content:ArrayList<String> = ArrayList()
        var line:String? = ""
        while(line != null)
        {

            line = bufferedReader.readLine()
            if(line == null)
            {
                break
            }
            content.add(line)
        }
        for (line in content) {
            export.add(line)
        }
        return export
    }
}