package de.rohnert.smarteatingsystem.helper.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.view.View
import android.widget.TextView



fun TextView.show(content:String,callback:()->Unit,duration:Long = 500L,delay:Long = 100)
{
    this@show.text = content
    val set = AnimatorSet().apply {
        play(getObjectAnimator(this@show, View.ALPHA,1f,duration,delay))
            .with(getObjectAnimator(this@show, View.TRANSLATION_X,0f,duration,delay)

        )
    }
    set.start()
    set.addListener(object:Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            callback()
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }

    })
}

fun TextView.hide(callback:()->Unit,duration:Long = 500L,delay:Long = 100)
{
    val set = AnimatorSet().apply {
        play(getObjectAnimator(this@hide, View.ALPHA,0f,duration,delay))
            .with(getObjectAnimator(this@hide, View.TRANSLATION_X,-50f,duration,delay)
            )
    }
    set.start()
    set.addListener(object:Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            callback()
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }

    })
}


fun TextView.setTextWithSimpleAnimation(content:String,callback:()->Unit,duration:Long = 500L,delay:Long = 100)
{
    val set = AnimatorSet().apply {
        play(getObjectAnimator(this@setTextWithSimpleAnimation, View.ALPHA,0f,duration,0))
        this@setTextWithSimpleAnimation.text = content
        play(getObjectAnimator(this@setTextWithSimpleAnimation, View.ALPHA,1f,duration,delay))
        start()
    }
    set.addListener(object:Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
           callback()
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }

    })

}

