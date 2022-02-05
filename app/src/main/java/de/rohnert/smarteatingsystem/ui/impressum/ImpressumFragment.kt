package de.rohnert.smarteatingsystem.ui.impressum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.rohnert.smarteatingsystem.R

class ImpressumFragment:Fragment()
{

    private lateinit var rootView:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_impressum,container,false)
        }

}