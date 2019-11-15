package de.rohnert.smeasy.frontend.foodtracker.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import backend.helper.Helper
import de.rohnert.smeasy.R
import de.rohnert.smeasy.helper.animation.CustomValueAnimator

import kotlin.math.roundToInt

class AnimationFoodPicker (var context: Context, var pbList:ArrayList<ProgressBar>,var tvList:ArrayList<TextView>,var maxValues:ArrayList<Float>,var progressValues:ArrayList<Float>)
{
    private var valueAnimator = CustomValueAnimator(context)
    private var helper = Helper()

    init {
        startAnimation()
    }


    private fun startAnimation()
    {
        var pbSet = AnimatorSet()
        var tvSet = AnimatorSet()
        pbSet.playTogether(getProgressBarList())
        tvSet.playTogether(getTextViewList())
        var set = AnimatorSet()
        set.play(pbSet).before(tvSet)
        set.startDelay = 500
        set.start()
    }

    fun updateAnimation(values:ArrayList<Float>)
    {
        var pbSet = AnimatorSet()
        var tvSet = AnimatorSet()
        pbSet.playTogether(getUpdatetProgressBarList(values))
        tvSet.playTogether(getTextViewList(values))
        var set = AnimatorSet()
        set.play(pbSet).before(tvSet)
        set.start()
        this.progressValues = values
    }

    private fun getProgressBarList(values:ArrayList<Float> = progressValues, max:ArrayList<Float> = maxValues):List<ValueAnimator>
    {
        var export:ArrayList<ValueAnimator> = ArrayList()
        for((index,i) in pbList.withIndex())
        {
            export.add(valueAnimator.animateProgressBar(i,0,values[index].roundToInt(),max[index].roundToInt()))
        }

        return export
    }

    private fun getUpdatetProgressBarList(values:ArrayList<Float> = progressValues, max:ArrayList<Float> = maxValues):List<ValueAnimator>
    {
        var export:ArrayList<ValueAnimator> = ArrayList()
        for((index,i) in pbList.withIndex())
        {
            export.add(valueAnimator.animateProgressBar(i,progressValues[index].roundToInt(),values[index].roundToInt(),max[index].roundToInt()))
        }

        return export
    }

    private fun getTextViewList(values:ArrayList<Float> = progressValues, max:ArrayList<Float> = maxValues):List<ObjectAnimator>
    {
        var export:ArrayList<ObjectAnimator> = ArrayList()
        export.add(createTextViewAnimation(tvList[0],"${helper.getFloatAsFormattedString(values[0],"#")} kcal / ${max[0]} kcal",(values[0] > max[0])))
        for((index,i) in tvList.withIndex())
        {
            if(index != 0)
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(values[index],"#")} g / ${max[index]} g",(values[index] > max[index])))
            }

        }

        return export
    }

    private fun createTextViewAnimation(view: TextView,content:String,color:Boolean):ObjectAnimator
    {
        view.text = content
        view.alpha = 0f
        if(color)
        {
            view.setTextColor(ContextCompat.getColor(context, R.color.design_error))
        }
        else
        {
            view.setTextColor(ContextCompat.getColor(context, R.color.textColor1))
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
}