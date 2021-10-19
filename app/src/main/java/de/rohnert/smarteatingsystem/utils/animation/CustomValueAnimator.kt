package de.rohnert.smarteatingsystem.utils.animation

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.content.ContextCompat
import de.rohnert.smarteatingsystem.R


class CustomValueAnimator(var context: Context)
{


    // InitMethoden:
// Soll ProgressBars ähnlich wie bei einem Tacho komplett hochfahren, und dann auf den eigentlichen Wert zurückfahren...
    fun animateProgressBarInitial(bar:ProgressBar,start:Int = 0,end:Int = bar.max,startValue:Int,duration:Int = 750,delay:Int = 500, interpolator: TimeInterpolator = FastOutSlowInInterpolator()):ValueAnimator
    {
        fun startSecondAnimation()
        {
            var animator = ValueAnimator.ofInt(end,startValue)
            animator.duration = duration.toLong()
            animator.startDelay = delay.toLong()/5
            animator.addUpdateListener {
                bar.progress = it.animatedValue as Int

                if(startValue > end)
                {
                    bar.progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context,
                            R.color.design_error))
                }
                else
                {
                    bar.progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context,
                            R.color.colorAccent))
                }

            }
            animator.interpolator = interpolator
            animator.start()
        }

        var animator = ValueAnimator.ofInt(start,end)
        animator.duration = duration.toLong()
        animator.startDelay = delay.toLong()
        animator.addUpdateListener {
            bar.progress = it.animatedValue as Int


        }
        animator.interpolator = interpolator
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                startSecondAnimation()

            }
        })
        return animator
    }
    fun animateTextInitial(tv:TextView,start:Int = 0,end:Int,subString:String,startValue:Int,duration:Int = 750,delay:Int = 500, interpolator: TimeInterpolator = FastOutSlowInInterpolator()):ValueAnimator
    {
        fun startSecondAnimation()
        {
            var animator = ValueAnimator.ofInt(end,startValue)
            animator.duration = duration.toLong()
            animator.startDelay = delay.toLong()/5
            animator.addUpdateListener {
                tv.text = "${it.animatedValue as Int} $subString"
                var progress = it.animatedValue as Int
                if(progress > end)
                {
                    tv.setTextColor(ContextCompat.getColor(context,R.color.design_error))
                }
                else
                {
                    tv.setTextColor(ContextCompat.getColor(context,R.color.textColor1))
                }

            }
            animator.interpolator = interpolator
            animator.start()
        }

        var animator = ValueAnimator.ofInt(start,end)
        animator.duration = duration.toLong()
        animator.startDelay = delay.toLong()
        animator.addUpdateListener {
            tv.text = "${it.animatedValue as Int} $subString"


        }
        animator.interpolator = interpolator
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                startSecondAnimation()

            }
        })

        return animator

    }



    // Update Animationen...
    // Über diese Methode kann ein ProgressBar zu einem Neuen Wert gezogen werden
    fun animateProgressBar(bar:ProgressBar,oldValue:Int,newValue:Int,maxValue: Int=bar.max,duration:Int = 500,delay:Int = 0, interpolator: TimeInterpolator = FastOutSlowInInterpolator()):ValueAnimator
    {
        // Das ganze nur Starten, wenn der neue Wert nicht

            var animator = ValueAnimator.ofInt(oldValue,newValue)
            animator.duration = duration.toLong()
            animator.startDelay = delay.toLong()
            animator.addUpdateListener {
                bar.progress = it.animatedValue as Int
                if(bar.progress >= maxValue)
                {
                    if(newValue > maxValue)
                    {
                        bar.progressTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(context,
                                R.color.design_error))
                    }

                }

                else
                {
                    bar.progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context,
                            R.color.colorAccent))
                }

            }
            animator.interpolator = interpolator


        return animator




    }

    fun animateText(tv:TextView,oldValue:Int,newValue:Int,maxValue:Int,subString:String,duration:Int = 500,delay:Int = 0, interpolator: TimeInterpolator = FastOutSlowInInterpolator()):ValueAnimator
    {
        var animator = ValueAnimator.ofInt(oldValue,newValue)
        animator.duration = duration.toLong()
        animator.startDelay = delay.toLong()
        animator.addUpdateListener {
            tv.text = "${it.animatedValue as Int} $subString"
            var progress = it.animatedValue as Int
            if(progress > maxValue)
            {
                tv.setTextColor(ContextCompat.getColor(context,R.color.design_error))
            }
            else
            {
                tv.setTextColor(ContextCompat.getColor(context,R.color.colorAccent))
            }

        }
        animator.interpolator = interpolator
        return animator
    }

    // Über diese Methode kann ein ProgressBar zu einem Neuen Wert gezogen werden
    fun animateSeekBar(bar:SeekBar,oldValue:Int,newValue:Int,duration:Int = 500,delay:Int = 0, interpolator: TimeInterpolator = FastOutSlowInInterpolator())
    {
        // Das ganze nur Starten, wenn der neue Wert nicht
        if(newValue < bar.max)
        {
            var animator = ValueAnimator.ofInt(oldValue,newValue)
            animator.duration = duration.toLong()
            animator.startDelay = delay.toLong()
            animator.addUpdateListener {
                bar.progress = it.animatedValue as Int

            }
            animator.interpolator = interpolator
            animator.start()
        }

    }



}