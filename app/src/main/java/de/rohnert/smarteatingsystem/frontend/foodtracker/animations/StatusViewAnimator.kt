package de.rohnert.smarteatingsystem.frontend.foodtracker.animations

import android.animation.AnimatorSet
import android.content.Context
import android.widget.ProgressBar
import android.widget.TextView
import de.rohnert.smarteatingsystem.helper.animation.getValueAnimator

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