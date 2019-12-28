package de.rohnert.smeasy.frontend.premium.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.fragment.app.DialogFragment
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences

class DialogFragmentPremium :DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View

    // Views
    lateinit var btnShareOne: ImageButton
    lateinit var btnShareTwo: Button
    lateinit var toolbar: Toolbar

    ///////////////////////////////////////////////////////////////////////////////////////////////


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialog
        )


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        rootView = inflater.inflate(R.layout.dialog_premium_alert, container, false)
        initToolBar()
        initButtons()




        return rootView
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun initToolBar() {
        toolbar = rootView.findViewById(R.id.dialog_premium_toolbar)
        //toolbar.collapseIcon = ContextCompat.getDrawable(rootView.context,)
        toolbar.setNavigationOnClickListener {
            // handle back button naviagtion
            dismiss()
        }


    }

    private fun initButtons()
    {
        btnShareOne = rootView.findViewById(R.id.dialog_premium_btn_fb_image)
        btnShareTwo = rootView.findViewById(R.id.dialog_premium_btn_fb)

        btnShareOne.setOnClickListener {
            Toast.makeText(context,"Vielen Dank, dass du für SmartEatingSystem geworben hast",
                Toast.LENGTH_SHORT).show()
            dismiss()
        }

        btnShareTwo.setOnClickListener {
            Toast.makeText(context,"Vielen Dank, dass du für SmartEatingSystem geworben hast",
                Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}