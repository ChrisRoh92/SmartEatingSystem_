package de.rohnert.smarteatingsystem.helper.animation

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import de.rohnert.smarteatingsystem.R


fun ProgressBar.animateInit(fromValue:Int,toValue:Int,callback:()->Unit,duration:Long = 500,delay:Long = 0)
{
    this.animateToValue(fromValue,this.max,{fromValue->
        this@animateInit.animateToValue(fromValue,toValue,{
            callback()
        },delay = 100L)
    },duration = duration)
}

// Von aktuellen Wert zu Max, und zurück zu neuem Wert!
fun ProgressBar.animateToValueWithMax(fromValue:Int,toValue:Int,callback: () -> Unit,duration:Long = 200,delay:Long = 0)
{

    this.animateToValue(fromValue,this.max,{fromValue ->
        this@animateToValueWithMax.animateToValue(fromValue,toValue,{
            callback()
        },delay = 100L)
    },duration = duration)
}


// Animate ProgressBar From To Value:
fun ProgressBar.animateToValue(fromValue:Int=this.max,toValue:Int,callback:(toValue:Int)->Unit,duration:Long = 250,delay:Long = 0)
{

    val animator = ValueAnimator.ofInt(fromValue,toValue).apply {
        this.duration = duration
        startDelay = delay
        interpolator = FastOutSlowInInterpolator()
        addUpdateListener {
            val progress = it.animatedValue as Int
            this@animateToValue.progress = progress
            this@animateToValue.changeColorValueOverMax(progress>this@animateToValue.max)
        }
    }
    animator.start()
    animator.addListener(object:Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            callback(toValue)
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }

    })
}

fun ProgressBar.changeColorValueOverMax(status:Boolean)
{
    if(status)
    {
        this@changeColorValueOverMax.progressTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context,
                R.color.design_error))
    }
    else
    {
        this@changeColorValueOverMax.progressTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context,
                R.color.colorAccent))

    }
}