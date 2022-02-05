package de.rohnert.smarteatingsystem.ui.functions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.rohnert.smarteatingsystem.R

class FunctionFragment:Fragment()
{
    private lateinit var rootView:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_function,container,false)
    }


}