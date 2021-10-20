package de.rohnert.smarteatingsystem.ui.privacy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import de.rohnert.smarteatingsystem.R

class PrivacyFragment : Fragment()
{

    // Allgemeine Variablen
    private lateinit var rootView:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_privacy, container, false)
        initToolBar()
        return rootView
    }

    private fun initToolBar()
    {

        // Access to Toolbar.
        var toolbar = activity!!.findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.clear()
        toolbar.title = "Datenschutz"

    }
}