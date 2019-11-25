package de.rohnert.smeasy.helper.customview

import android.content.Context
import android.graphics.Canvas
import android.widget.SeekBar

class CircleSeekBar(var mContext: Context):SeekBar(mContext)
{



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}