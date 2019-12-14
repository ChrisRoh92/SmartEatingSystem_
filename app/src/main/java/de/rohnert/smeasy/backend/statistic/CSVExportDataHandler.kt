package de.rohnert.smeasy.backend.statistic

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class CSVExportDataHandler(var context: Context)
{
    fun writeArrayToFile(data:ArrayList<String>,dir:String)
    {
        val myFile = File(dir)
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
        var file: File = File(dir)
        return file.exists()
    }
}