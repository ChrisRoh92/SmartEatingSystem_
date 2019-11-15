package de.rohnert.smeasy.frontend.foodtracker.animations

import android.animation.*
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

class AnimationStatusView(var context: Context,
                          var pbList:ArrayList<ProgressBar>,
                          var tvList:ArrayList<TextView>,
                          var progressValues:ArrayList<Float>,
                          var maxValues:ArrayList<Float>)
{

    private var valueAnimator = CustomValueAnimator(context)
    private var helper = Helper()
    // Interface:
    private lateinit var mListener:OnAnimationStatusViewInitListener

    init {
        startInitAnimation()
    }

    private fun startInitAnimation()
    {
        /*var set = AnimatorSet()
        //var animatorList:ArrayList<ValueAnimator> = createInitAnimatorList()
        set.playTogether(createInitAnimatorList())
        set.start()*/

        var set = AnimatorSet()
        var pbSet = AnimatorSet()
        var tvSet = AnimatorSet()
        pbSet.playTogether(createInitProgessBarAnimatorList())
        tvSet.playTogether(createInitTextViewAnimatorList())
        set.play(pbSet).before(tvSet)
        //set.playTogether(pbSet,tvSet)
        set.start()
        set.addListener(object: Animator.AnimatorListener
        {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                if(mListener!=null)
                {
                    mListener.setOnAnimationStatusViewListener()
                }
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }

        })





    }

    fun animateNewValues(progressValues:ArrayList<Float>)
    {

            var set = AnimatorSet()
            var pbSet = AnimatorSet()
            var tvSet = AnimatorSet()
            pbSet.playTogether(createProgressBarAnimatorList(progressValues))
            tvSet.playTogether(createTextViewAnimatorList(progressValues))
            set.play(pbSet).before(tvSet)
            set.start()



    }


    // Animationen initial erstellen...
    private fun createInitProgessBarAnimatorList():List<ValueAnimator>
    {
        var export:ArrayList<ValueAnimator> = ArrayList()
        // Hinzufügen vom Kcal ProgressBar:
        var startValue = ((progressValues[0]/maxValues[0])*2700).roundToInt()
        if(startValue <= 2700)
        {
            var value = valueAnimator.animateProgressBarInitial(pbList[0],0,2700,startValue=startValue,interpolator = FastOutSlowInInterpolator(),delay = 1000)
            export.add(value)
        }
        else
        {
            var value = valueAnimator.animateProgressBarInitial(pbList[0],0,2700,startValue=2701,interpolator = FastOutSlowInInterpolator(),delay = 1000)
            export.add(value)
        }

        // Hinzufügen von den anderen ProgressAnimationen...
        for((index,i) in pbList.withIndex())
        {
            if(index == 0) continue
            else
            {
                var startValue = ((progressValues[index]/maxValues[index])*1000).roundToInt()
                var value = valueAnimator.animateProgressBarInitial(i,0,startValue=startValue,interpolator = FastOutSlowInInterpolator(),delay = 1000)
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
            else
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(progressValues[index-1],"#")} g / ${helper.getFloatAsFormattedString(maxValues[index-1],"#")} g",(progressValues[index-1]>maxValues[index-1])))
            }
        }

        return export

    }

    private fun createProgressBarAnimatorList(newProgressValues:ArrayList<Float>):List<ValueAnimator>
    {
        var export:ArrayList<ValueAnimator> = ArrayList()
        // Hinzufügen vom Kcal ProgressBar:
        var startValue = ((newProgressValues[0]/maxValues[0])*2700).roundToInt()
        if(startValue <= 2700)
        {
            var value = valueAnimator.animateProgressBar(pbList[0],pbList[0].progress,startValue,maxValue = 2700,interpolator = FastOutSlowInInterpolator(),delay = 500)
            export.add(value)
        }
        else
        {
            var value = valueAnimator.animateProgressBar(pbList[0],pbList[0].progress,2701,maxValue = 2700,interpolator = FastOutSlowInInterpolator(),delay = 500)
            export.add(value)
        }


        // Hinzufügen von den anderen ProgressAnimationen...
        for((index,i) in pbList.withIndex())
        {
            if(index == 0) continue
            else
            {
                var startValue = ((newProgressValues[index]/maxValues[index])*1000).roundToInt()
                var value = valueAnimator.animateProgressBar(i,i.progress,startValue,interpolator = FastOutSlowInInterpolator(),delay = 500)
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
            else
            {
                export.add(createTextViewAnimation(i,"${helper.getFloatAsFormattedString(newProgressValues[index-1],"#")} g / ${helper.getFloatAsFormattedString(maxValues[index-1],"#")} g",(newProgressValues[index-1]>maxValues[index-1])))
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