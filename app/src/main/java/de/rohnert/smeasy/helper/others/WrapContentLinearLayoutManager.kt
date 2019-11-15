package de.rohnert.smeasy.helper.others

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WrapContentLinearLayoutManager(var context: Context,var mOrientation:Int, var mReverse:Boolean): LinearLayoutManager(context,mOrientation,mReverse)
{
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            //Log.e("TAG", "meet a IOOBE in RecyclerView")
        }

    }
}