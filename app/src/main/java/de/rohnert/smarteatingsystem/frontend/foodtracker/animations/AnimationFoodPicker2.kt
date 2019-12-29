package de.rohnert.smarteatingsystem.frontend.foodtracker.animations

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
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.helper.animation.CustomValueAnimator
import kotlin.math.roundToInt

class AnimationFoodPicker2(var context: Context,var circlePb:ProgressBar, var pbList:ArrayList<ProgressBar>, var tvList:ArrayList<TextView>, var maxValues:ArrayList<Float>, var progressValues:ArrayList<Float>)
{
    private var valueAnimator = CustomValueAnimator(context)
    private var helper = Helper()

    init {
        startAnimation()
    }


    fun startAnimation()
    {

        var pbSet = AnimatorSet()
        var tvSet = AnimatorSet()
        pbSet.playTogether(getInitProgressBarList())
        tvSet.playTogether(getInitTextViewList())
        var set = AnimatorSet()
        set.play(pbSet).before(tvSet)
        set.startDelay = 500
        set.start()
    }

    fun updateAnimation(values:ArrayList<Float>)
    {
        this.progressValues = values
        var pbSet = AnimatorSet()
        var tvSet = AnimatorSet()
        pbSet.playTogether(getUpdatetProgressBarList())
        tvSet.playTogether(getUpdatedTextViews())
        var set = AnimatorSet()
        set.play(pbSet).before(tvSet)
        set.start()


    }

    // Initial Animations
    private fun getInitCircleProgressBarAnimation():ValueAnimator
    {
        var export: ValueAnimator
        circlePb.max = 3600
        var startValue = ((progressValues[0]/maxValues[0])*2700).roundToInt()
        if(startValue <= 2700)
        {
            export = valueAnimator.animateProgressBarInitial(circlePb,0,2700,startValue=startValue,interpolator = FastOutSlowInInterpolator(),delay = 0)

        }
        else
        {
            export =  valueAnimator.animateProgressBarInitial(circlePb,0,2700,startValue=2701,interpolator = FastOutSlowInInterpolator(),delay = 0)

        }


        return export
    }
    private fun getInitProgressBarList(values:ArrayList<Float> = progressValues, max:ArrayList<Float> = maxValues):List<ValueAnimator>
    {
        var export:ArrayList<ValueAnimator> = ArrayList()
        export.add(getInitCircleProgressBarAnimation())
        for((index,i) in pbList.withIndex())
        {

            export.add(valueAnimator.animateProgressBarInitial(i,0,i.max,values[index+1].roundToInt(),delay = 0))
        }

        return export
    }
    private fun getInitTextViewList(values:ArrayList<Float> = progressValues, max:ArrayList<Float> = maxValues):List<ObjectAnimator>
    {
        var export:ArrayList<ObjectAnimator> = ArrayList()
        export.add(createTextViewAnimation(tvList[0],"${helper.getFloatAsFormattedString(values[0],"#")} kcal",(values[0] > max[0])))
        export.add(createTextViewAnimation(tvList[1],"${helper.getFloatAsFormattedString(max[0],"#")} kcal",(values[0] > max[0])))

        // Nährwerte...
        export.add(createTextViewAnimation(tvList[2],"${helper.getFloatAsFormattedString(values[1],"#")} g / ${max[1]} g",(values[1] > max[1])))
        export.add(createTextViewAnimation(tvList[3],"${helper.getFloatAsFormattedString(values[2],"#")} g / ${max[2]} g",(values[2] > max[2])))
        export.add(createTextViewAnimation(tvList[4],"${helper.getFloatAsFormattedString(values[3],"#")} g / ${max[3]} g",(values[3] > max[3])))

        return export
    }

    // Update Animations...
    private fun getUpdatedCircleProgressBarAnimation():ValueAnimator
    {
        // Hinzufügen vom Kcal ProgressBar:
        var export: ValueAnimator
        var startValue = ((progressValues[0]/maxValues[0])*2700).roundToInt()
        if(startValue <= 2700)
        {
            export = valueAnimator.animateProgressBar(circlePb,circlePb.progress,startValue,maxValue = 2700,interpolator = FastOutSlowInInterpolator(),delay = 0)

        }
        else
        {
            export = valueAnimator.animateProgressBar(circlePb,circlePb.progress,2701,maxValue = 2700,interpolator = FastOutSlowInInterpolator(),delay = 0)

        }


        return export

    }
    private fun getUpdatetProgressBarList(values:ArrayList<Float> = progressValues, max:ArrayList<Float> = maxValues):List<ValueAnimator>
    {
        var export:ArrayList<ValueAnimator> = ArrayList()
        export.add(getUpdatedCircleProgressBarAnimation())
        for((index,i) in pbList.withIndex())
        {
            export.add(valueAnimator.animateProgressBar(i,progressValues[index+1].roundToInt(),values[index+1].roundToInt(),max[index+1].roundToInt()))
        }

        return export
    }

    private fun getUpdatedTextViews(values:ArrayList<Float> = progressValues, max:ArrayList<Float> = maxValues):List<ObjectAnimator>
    {
        var export:ArrayList<ObjectAnimator> = ArrayList()
        export.add(createTextViewAnimation2(tvList[0],"${helper.getFloatAsFormattedString(values[0],"#")} kcal",(values[0] > max[0])))
        export.add(createTextViewAnimation2(tvList[1],"${helper.getFloatAsFormattedString(max[0],"#")} kcal",(values[0] > max[0])))

        // Nährwerte...
        export.add(createTextViewAnimation2(tvList[2],"${helper.getFloatAsFormattedString(values[1],"#")} g / ${max[1]} g",(values[1] > max[1])))
        export.add(createTextViewAnimation2(tvList[3],"${helper.getFloatAsFormattedString(values[2],"#")} g / ${max[2]} g",(values[2] > max[2])))
        export.add(createTextViewAnimation2(tvList[4],"${helper.getFloatAsFormattedString(values[3],"#")} g / ${max[3]} g",(values[3] > max[3])))

        return export
    }






    private fun createTextViewAnimation2(view: TextView,content:String,color:Boolean):ObjectAnimator
    {
        view.text = content
        //view.alpha = 0f
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

        var export: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view).apply {
            startDelay = 0
            duration = 200
            interpolator = FastOutSlowInInterpolator()

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