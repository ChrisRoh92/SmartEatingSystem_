package de.rohnert.smeasy.frontend.bodytracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.roomdatabaseexample.backend.databases.body_database.Body

class BodyViewModel(application: Application) : AndroidViewModel(application)
{






    fun getBodyList():ArrayList<Body>
    {
        var export:ArrayList<Body> = ArrayList()
        for(i in 1..10)
        {
            var body = Body("${i*2}.11.2019",100f*((100-2*i)/100f),25f,90f,90f,90f,90f,"")
            export.add(body)
        }
        return ArrayList(export.asReversed())
    }

    fun getProgressValues():ArrayList<Float>
    {
        return arrayListOf(0f,0f,0f,0f)
    }
}