package de.rohnert.smarteatingsystem.backend.repository.subrepositories.body

class BodyProcessor
{


    fun calcBMI(height:Float,weight:Float):Float
    {
        var mHeight = height/100f
        var bmi = weight / (mHeight*mHeight)
        return bmi
    }
}