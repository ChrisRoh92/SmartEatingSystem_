package de.rohnert.smeasy.frontend.share.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import de.rohnert.smeasy.R
import de.rohnert.smeasy.helper.others.SimpleShare
import kotlinx.android.synthetic.main.app_bar_main.*

class ShareFragment : Fragment() {

    // Allgemeine Variablen
    private lateinit var rootView:View
    private lateinit var shareViewModel: ShareViewModel



    // View Elemente:
    private lateinit var btnRecommend:Button
    private lateinit var btnRate:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shareViewModel =
            ViewModelProviders.of(this).get(ShareViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_share, container, false)

        initToolBar()
        initButton()

        return rootView
    }

    private fun initToolBar()
    {
        var toolbar = activity!!.toolbar
        toolbar.menu.clear()
        toolbar.title = "Teilen"
    }


    private fun initButton()
    {
        btnRecommend = rootView.findViewById(R.id.share_btn_recommend)
        btnRate = rootView.findViewById(R.id.share_btn_rating)

        // Listener:
        btnRecommend.setOnClickListener {
            var share = SimpleShare(rootView.context,activity!!)
        }

        btnRate.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + rootView.context.packageName))
            )
        }
    }
}