package de.rohnert.smarteatingsystem.frontend.premium.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.rohnert.smarteatingsystem.R

class PremiumFragment:Fragment()
{

    private lateinit var rootView:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_premium, container, false)

        return rootView
    }
}