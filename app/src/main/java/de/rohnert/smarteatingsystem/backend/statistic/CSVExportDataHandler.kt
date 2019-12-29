package de.rohnert.smarteatingsystem.backend.statistic

import android.content.Context
import java.io.File

class CSVExportDataHandler(var context: Context)
{
    fun writeArrayToFile(data:ArrayList<String>,dir:String,name:String,file:File)
    {
       /* val csv = File.createTempFile(name,".csv")
        var fileWriter: FileWriter? = null

        try {
            fileWriter = FileWriter(name)
            for (i in data) {
                fileWriter.append(i)
            }

            fileWriter.close()
        }catch (e:Exception)
        {
            e.printStackTrace()
        }*/


        file.printWriter().use {
                out ->
            for(i in data)
            {
                out.println(i)
            }
        }


    }

}