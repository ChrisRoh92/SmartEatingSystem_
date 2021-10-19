package de.rohnert.smarteatingsystem.frontend.foodtracker.animations

import android.animation.*
import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.utils.animation.CustomValueAnimator
import kotlin.math.roundToInt

class AnimationStatusView(var context: Context,
                          var pbList:ArrayList<ProgressBar>,
                          var tvList:ArrayList<TextView>,
                          var progressValues:ArrayList<Float>,
                          var maxValues:ArrayList<Float>)
{

    private var valueAnimator = CustomValueAnimator(context)
    private var helper = Helper()
    private var prefs = SharedAppPreferences(context)
    // Interface:
    private lateinit var mListener:OnAnimationStatusViewInitListener



    fun startInitAnimation(callback:()->Unit)
    {


        var set = AnimatorSet()
        var pbSet = AnimatorSet()
        var tvSet = AnimatorSet()
        pbSet.playTogether(createInitProgessBarAnimatorList())
        tvSet.playTogether(createInitTextViewAnimatorList())
        set.play(pbSet)
        //set.playTogether(pbSet,tvSet)
        set.startDelay = 500
        set.start()
        set.addListener(object: Animator.AnimatorListener
        {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                tvSet.start()
                callback()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }

        })





    }

    fun animateNewValues(progressValues:ArrayList<Float>)
    {

            var pbSet = AnimatorSet().apply {
                playTogether(createProgressBarAnimatorList(progressValues))
            }
            var tvSet = AnimatorSet().apply {
                playTogether(createTextViewAnimatorList(progressValues))
            }
            var set = AnimatorSet().apply {
                play(pbSet).before(tvSet)
                start()
            }

    }

    fun setNewMaxValues(values:ArrayList<Float>)
    {
        this.maxValues = values
    }


    // Animationen initial erstellen...
    private fun createInitProgessBarAnimatorList():List<ValueAnimator>
    {
        var export:ArrayList<ValueAnimator> = ArrayList()
        // Hinzufügen vom Kcal ProgressBar:
        var startValue = 0
        var start = ((progressValues[0]/maxValues[0])*2700)
//        Log.d("Smeasy","AnimationStatusView - createInitProgressBarAnimatorList progressValues[0] = ${progressValues[0]}")
//        Log.d("Smeasy","AnimationStatusView - createInitProgressBarAnimatorList maxValues[0] = ${maxValues[0]}")
//        Log.d("Smeasy","AnimationStatusView - createInitProgressBarAnimatorList start = $start")
        if(start == 0f)
        {

        }
        else
        {
            startValue = start.roundToInt()
        }
       // var startValue = ((progressValues[0]/maxValues[0])*2700).roundToInt()
        if(startValue <= 2700)
        {
            var value = valueAnimator.animateProgressBarInitial(pbList[0],0,2700,startValue=startValue,interpolator = FastOutSlowInInterpolator(),delay = 0)
            export.add(value)
        }
        else
        {
            var value = valueAnimator.animateProgressBarInitial(pbList[0],0,2700,startValue=2701,interpolator = FastOutSlowInInterpolator(),delay = 0)
            export.add(value)
        }

        // Hinzufügen von den anderen ProgressAnimationen...
        for((index,i) in pbList.withIndex())
        {
            if(index == 0) continue
            else
            {
                var startValue = ((progressValues[index]/maxValues[index])*1000).roundToInt()
                var value = valueAnimator.animateProgressBarInitial(i,0,startValue=startValue,interpolator = FastOutSlowInInterpolator(),delay = 0)
                export.add(value)
            }
        }




        return export
    }

    private fun createInitTextViewAnimatorList():List<ObjectAnimator>
    {

        var export:ArrayList<ObjectAnimator> = ArrayList()
        for((index,i) in tvList.withIndex())
        {
            if(index == 0)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(progressValues[0],"#")} Kcal",(progressValues[0] > maxValues[0])))
            }
            else if(index == 1)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(maxValues[0] - progressValues[0],"#")} Kcal",((maxValues[0] - progressValues[0])<0)))
            }
            else if( index in 2..4)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(progressValues[index-1],"#")} g / ${helper.getFloatAsFormattedString(maxValues[index-1],"#")} g",(progressValues[index-1]>maxValues[index-1])))
            }
            else if (index in 5..6)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(progressValues[index-1],"#")} % ",(progressValues[index-1]>maxValues[index-1])))
            }

            else
            {
                export.add(createTextViewAnimation(i,prefs.aim,false))

            }


        }

        return export

    }


    // Update...
    private fun createProgressBarAnimatorList(newProgressValues:ArrayList<Float>):List<ValueAnimator>
    {
        var export:ArrayList<ValueAnimator> = ArrayList()
        // Hinzufügen vom Kcal ProgressBar:
        var startValue = ((newProgressValues[0]/maxValues[0])*2700).roundToInt()
        if(startValue <= 2700)
        {
            var value = valueAnimator.animateProgressBar(pbList[0],pbList[0].progress,startValue,maxValue = 2700,interpolator = FastOutSlowInInterpolator(),delay = 0)
            export.add(value)
        }
        else
        {
            var value = valueAnimator.animateProgressBar(pbList[0],pbList[0].progress,2701,maxValue = 2700,interpolator = FastOutSlowInInterpolator(),delay = 0)
            export.add(value)
        }


        // Hinzufügen von den anderen ProgressAnimationen...
        for((index,i) in pbList.withIndex())
        {
            if(index == 0) continue
            else
            {
                var startValue = ((newProgressValues[index]/maxValues[index])*1000).roundToInt()
                var value = valueAnimator.animateProgressBar(i,i.progress,startValue,interpolator = FastOutSlowInInterpolator(),delay = 0)
                export.add(value)
            }
        }




        return export
    }

    private fun createTextViewAnimatorList(newProgressValues:ArrayList<Float>):List<ObjectAnimator>
    {
        var export:ArrayList<ObjectAnimator> = ArrayList()
        for((index,i) in tvList.withIndex())
        {
            if(index == 0)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(newProgressValues[0],"#")} Kcal",(newProgressValues[0] > maxValues[0])))
            }
            else if(index == 1)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(maxValues[0] - newProgressValues[0],"#")} Kcal",((maxValues[0] - newProgressValues[0])<0)))
            }
            else if(index in 2..4)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(newProgressValues[index-1],"#")} g / ${helper.getFloatAsFormattedString(maxValues[index-1],"#")} g",(newProgressValues[index-1]>maxValues[index-1])))
            }
            /*else if (index in 5..6)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(newProgressValues[index],"#")} %",(newProgressValues[index-1]>maxValues[index])))
            }
            else
            {
                export.add(createTextViewAnimation(i,"Halten",false))

            }*/

        }

        return export
    }





    private fun createTextViewAnimation(view: TextView,content:String,color:Boolean):ObjectAnimator
    {
        view.text = content
        view.alpha = 0f
        if(color)
        {
            view.setTextColor(ContextCompat.getColor(context,R.color.design_error))
        }
        else
        {
            view.setTextColor(ContextCompat.getColor(context,R.color.textColor1))
        }

        var alpha = PropertyValuesHolder.ofFloat(View.ALPHA,0f,1f)
        var scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,0f,1f)
        var scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,0f,1f)

        var export: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view,alpha,scaleX,scaleY).apply {
            startDelay = 0
            duration = 200
            interpolator = FastOutSlowInInterpolator()

        }

        return export

    }

    // Interface für initListener()
    interface OnAnimationStatusViewInitListener
    {
        fun setOnAnimationStatusViewListener()
    }

    fun setOnAnimationStatusViewListener(mListener:OnAnimationStatusViewInitListener)
    {this.mListener = mListener}


}