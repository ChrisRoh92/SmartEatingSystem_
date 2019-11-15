package de.rohnert.smeasy.helper.others

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class CustomDividerItemDecoration(var mOrientation:Int, var context: Context,var margin:Int): RecyclerView.ItemDecoration()
{

    private val ATR:IntArray = intArrayOf(android.R.attr.listDivider)
    private lateinit var myDivider:Drawable

    // Variablen:
    val horizontal_List = LinearLayoutManager.HORIZONTAL
    val vertical_List = LinearLayoutManager.VERTICAL

    init {
        val a:TypedArray = context.obtainStyledAttributes(ATR)
        myDivider = a.getDrawable(0)!!
        a.recycle()

        setOrientation(mOrientation)

    }

    private fun setOrientation(orientation: Int)
    {
        if(orientation != horizontal_List && orientation != vertical_List)
        {
            throw IllegalArgumentException("invalid")
        }
        mOrientation = orientation
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if(mOrientation == vertical_List)
        {
            drawVerticalRecycler(c,parent)
        }
        else
        {
            drawHorizontalRecycler(c,parent)
        }
    }

    private fun drawVerticalRecycler(c:Canvas,parent:RecyclerView)
    {
        var left = parent.paddingLeft
        var right = parent.width - parent.paddingRight
        var count = parent.childCount
        for(i in 0 until (count-1))
        {
            var child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + myDivider.intrinsicHeight
            myDivider.setBounds(left+dpToPx(margin),top, right,bottom)
            myDivider.draw(c)
        }
    }

    private fun drawHorizontalRecycler(c:Canvas,parent:RecyclerView) {
        // Hier bitte noch nachtragen...
    }

    // Methode um DP in reale BildschirmPixel umzurechnen...
    fun dpToPx(value:Int):Int
    {
        var r = context.resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value.toFloat(),r.displayMetrics))
    }


    override fun getItemOffsets(outRect: Rect,view: View,parent: RecyclerView,state: RecyclerView.State)
    {
        if(mOrientation == vertical_List)
        {
            outRect.set(0,0,0,myDivider.intrinsicHeight)
        }
        else
        {
            outRect.set(0,0,myDivider.intrinsicWidth,0)
        }
    }
}