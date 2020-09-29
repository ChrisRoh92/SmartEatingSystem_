package de.rohnert.smarteatingsystem.helper.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Property
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


// Methode zum Animieren eines neuen Inhaltes für ein TextView
fun TextView.setTextWithAnimation(content:String)
{
    val view = this
    val disappearSet = AnimatorSet().apply{
        play(getObjectAnimator(view,View.SCALE_Y,0f)).with(getObjectAnimator(view,View.SCALE_X,0f))
            .with(getObjectAnimator(view,View.ALPHA,0f))
        start()
    }

    val appearSet = AnimatorSet().apply{
        play(getObjectAnimator(view,View.SCALE_Y,1f)).with(getObjectAnimator(view,View.SCALE_X,1f))
            .with(getObjectAnimator(view,View.ALPHA,1f))
    }

    disappearSet.addListener(object :Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            view.text = content
            appearSet.start()

        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }

    })



}

fun ProgressBar.initAnimation(fromValue:Int = 0,toValue:Int)
{
    val view = this
    getValueAnimator(fromValue,toValue).addUpdateListener {
        view.progress = it.animatedValue as Int
    }
}



fun getObjectAnimator(view: View,type:Property<View,Float>,value:Float,duration:Long = 300,delay:Long = 100):ObjectAnimator
{
    val animator = ObjectAnimator.ofFloat(view,type,value).apply {
        startDelay = delay
        this.duration = duration
        interpolator = FastOutSlowInInterpolator()
    }

    return animator
}

fun getValueAnimator(from:Int,to:Int,duration:Long = 400,delay:Long = 200):ValueAnimator
{
    val animator = ValueAnimator.ofInt(from,to).apply {
        startDelay = delay
        this.duration = duration
        interpolator = FastOutLinearInInterpolator()
    }
    return animator
}

