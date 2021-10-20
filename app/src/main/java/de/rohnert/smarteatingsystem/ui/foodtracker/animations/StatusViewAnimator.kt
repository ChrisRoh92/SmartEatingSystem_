package de.rohnert.smarteatingsystem.ui.foodtracker.animations

import android.animation.AnimatorSet
import android.content.Context
import android.widget.ProgressBar
import android.widget.TextView

class StatusViewAnimator(var context: Context,
                         var pbList:ArrayList<ProgressBar>,
                         var tvList:ArrayList<TextView>,
                         var progressValues:ArrayList<Float>,
                         var maxValues:ArrayList<Float>)
{





    fun initStatusView()
    {
        val pbSet = AnimatorSet().apply {
            val singleSet = AnimatorSet().apply {

            }
        }
    }
}