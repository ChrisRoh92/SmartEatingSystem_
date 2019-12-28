package de.rohnert.smeasy.frontend.helper.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.helper.adapter.HelperRecyclerAdapter
import de.rohnert.smeasy.helper.others.CustomDividerItemDecoration
import kotlinx.android.synthetic.main.app_bar_main.*

class HelpFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var prefs:SharedAppPreferences

    // View Elemente:

    // RecyclerView
    private lateinit var rv:RecyclerView
    private lateinit var layoutManager:LinearLayoutManager
    private lateinit var adapter: HelperRecyclerAdapter

    // Content:
    private var content:ArrayList<String> = ArrayList()
    private var subContent:ArrayList<String> = ArrayList()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_help, container, false)

        initToolBar()
        createContent()
        initRecyclerView()

        return rootView
    }


    private fun initToolBar()
    {
        var toolbar = activity!!.toolbar
        toolbar.menu.clear()
        toolbar.title = "Hilfe"
    }

    private fun initRecyclerView()
    {
        rv = rootView.findViewById(R.id.fragment_help_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapter =
            HelperRecyclerAdapter(content, subContent)
        rv.layoutManager = layoutManager
        rv.adapter = adapter

        rv.addItemDecoration(
            CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 0)
        )

        adapter.setOnItemClickListener(object:
            HelperRecyclerAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int) {
                // Hier werden dann verschiedene Dialoge aufgerufen, um dem Nutzer weitere Infos zu geben...
            }

        })

    }

    private fun createContent()
    {
        content.add("FAQ")
        content.add("Über uns")
        content.add("Fehlerbehebung")
        content.add("Verbesserungen")
        content.add("Versions-Info")
        content.add("Lizenzen")

        subContent.add("Erhalte Antworten auf die regelmäßig aufkommenden Fragen")
        subContent.add("Erfahre wer SmartEatingSystem entwickelt hat")
        subContent.add("Teile uns hier Fehler, die dir aufgefallen sind, mit.")
        subContent.add("Helfe uns diese App zu verbessern")
        subContent.add("Erhalte hier Informationen zu aller bisherigen Versionen")
        subContent.add("Hier siehst du alle genutzten Lizenzen")


    }
}