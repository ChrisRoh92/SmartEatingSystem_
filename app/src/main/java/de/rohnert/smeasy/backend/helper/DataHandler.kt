package backend.helper

import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

class DataHandler(var manager: AssetManager, var mDir:String)
{




    fun getFileAsArrayList(dir:String):ArrayList<String>
    {
        var export:ArrayList<String> = ArrayList()
        val value:List<String> = File(mDir+dir).readLines()
        export.addAll(value)


        return export
    }

    fun writeArrayToFile(data:ArrayList<String>,dir:String)
    {
        val myFile = File(mDir+dir)
        myFile.printWriter().use {
            out ->
            for(i in data)
            {
                out.println(i)
            }
        }
    }

    // Prüfen ob File existiert
    fun checkIfFileExist(dir:String):Boolean
    {
        var file: File = File(mDir+dir)
        return file.exists()
    }

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