package de.rohnert.smeasy.frontend.helper.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.rohnert.smeasy.R

class HelpFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_help, container, false)

        return rootView
    }
}