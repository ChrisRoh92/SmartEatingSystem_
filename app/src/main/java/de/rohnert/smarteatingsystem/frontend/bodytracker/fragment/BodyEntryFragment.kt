package de.rohnert.smarteatingsystem.frontend.bodytracker.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.backend.databases.body_database.Body
import com.google.android.material.snackbar.Snackbar
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.frontend.bodytracker.BodyViewModel
import de.rohnert.smarteatingsystem.frontend.bodytracker.adapter.BodyEntryRecyclerViewAdapter
import de.rohnert.smarteatingsystem.frontend.bodytracker.animations.BodyEntryStatusViewAnimator
import de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs.dialog_bodyentry.DialogFragmentNewBodyEntry
import de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs.dialog_bodyentry.DialogShowBodyEntry
import de.rohnert.smarteatingsystem.utils.getDateFromDateLong

class BodyEntryFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var bodyViewModel: BodyViewModel
    private var helper = Helper()
    private lateinit var prefs:SharedAppPreferences

    // Animation
    private lateinit var animator:BodyEntryStatusViewAnimator
    private var firstStart = false

    // View Elemente:
    private lateinit var statusCardView:CardView
    private lateinit var pbWeight:ProgressBar
    private lateinit var pbKfa:ProgressBar
    private lateinit var pbBMI:ProgressBar
    private var tvStartList:ArrayList<TextView> = ArrayList()
    private var tvAimList:ArrayList<TextView> = ArrayList()
    private var idStartList:ArrayList<Int> = arrayListOf(R.id.bodyentry_tv_weight_start,R.id.bodyentry_tv_kfa_start,R.id.bodyentry_tv_bmi_start)
    private var idAimList:ArrayList<Int> = arrayListOf(R.id.bodyentry_tv_weight_aim,R.id.bodyentry_tv_kfa_aim,R.id.bodyentry_tv_bmi_aim)


    private lateinit var tvWeight:TextView
    private lateinit var tvKfa:TextView
    private lateinit var tvBMI:TextView

    // FloatingActionButton - Add New BodyEntry:
    private lateinit var btnAdd:FloatingActionButton


    // RecyclerView:
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:BodyEntryRecyclerViewAdapter
    private lateinit var manager:LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bodyViewModel = ViewModelProvider(requireParentFragment()).get(BodyViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_bodytracker_bodyentries, container, false)
        prefs = SharedAppPreferences(rootView.context)


        initButton()
        initStatusView()
        initRecyclerView()
        initRecyclerView()
        initObserver()


        return rootView
    }



    private fun initButton()
    {
        btnAdd = rootView.findViewById(R.id.bodyentry_btn_add)
        btnAdd.setOnClickListener {
            if(!bodyViewModel.checkIfBodyExist())
            {
                //var dialog = DialogNewBodyEntry(rootView.context,fragmentManager!!,bodyViewModel)
                var dialog = DialogFragmentNewBodyEntry(bodyViewModel)
                dialog.show(childFragmentManager,"NewBodyDialog")

            }
            else
            {
                val lastBodyDate = adapter.content.first().date
                val newDate = Helper().getDateWithOffsetDays(getDateFromDateLong(lastBodyDate),1)
                var dialog = DialogFragmentNewBodyEntry(bodyViewModel,newDate)
                dialog.show(childFragmentManager,"NewBodyDialog")
                //Toast.makeText(rootView.context,"Du hast heute bereits ein Eintrag erstellt...",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initObserver()
    {
        // Aktuelle Bodyliste aus Datenbank für RecyclerView
        bodyViewModel.getLiveBodyList().observe(viewLifecycleOwner,androidx.lifecycle.Observer{ bodylist ->
            adapter.updateContent(bodylist)
        })


        bodyViewModel.getProgressWasSetValue().observe(viewLifecycleOwner,androidx.lifecycle.Observer{
            if(!firstStart)
            {
                startStatusViewAnimation()
                initStatusTextViews()
                firstStart = true
            }
            else
            {

                updateStatusViewAnimation()
                updateStatusViewTextView()

            }

        })
    }

    private fun initRecyclerView()
    {
        recyclerView = rootView.findViewById(R.id.bodyentry_recyclerview)
        adapter = BodyEntryRecyclerViewAdapter(ArrayList(),parentFragmentManager,prefs)
        manager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        //recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context,manager.orientation))
        //recyclerView.addItemDecoration(CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 68))

        adapter.setOnBodyEntryLongClickListener(object:BodyEntryRecyclerViewAdapter.OnBodyEntryLongClickListener{
            override fun setOnBodyEntryLongClickListener(body: Body, pos: Int) {
                undoBodyDelete(body)
                bodyViewModel.deleteBody(body)
            }

        })

        // Normal Klick to show and update BodyData
        adapter.setOnBodyEntryClickListener(object:BodyEntryRecyclerViewAdapter.OnBodyEntryClickListener{
            override fun setOnBodyEntryClickListener(body: Body, pos: Int) {
                var dialog = DialogShowBodyEntry(rootView.context,body,fragmentManager!!)
                dialog.setOnDialogClickListener(object: DialogShowBodyEntry.OnDialogClickListener{
                    override fun setOnDialogClickListener(updatedBody: Body) {
                        bodyViewModel.updateBody(updatedBody)
                    }

                })
            }

        })




    }

    private fun initStatusView()
    {

        statusCardView = rootView.findViewById(R.id.bodyentry_cardview_statusview)


        // ProgressBars...
        pbWeight = rootView.findViewById(R.id.bodyentry_pb_weight)
        pbKfa = rootView.findViewById(R.id.bodyentry_pb_kfa)
        pbBMI = rootView.findViewById(R.id.bodyentry_pb_bmi)

        // Max + Values for PBs


        // TextViews...
        tvWeight = rootView.findViewById(R.id.bodyentry_tv_weight)
        tvKfa = rootView.findViewById(R.id.bodyentry_tv_kfa)
        tvBMI = rootView.findViewById(R.id.bodyentry_tv_bmi)

        //startCardViewAnimation()


    }

    private fun initStatusTextViews()
    {
        for(i in idStartList)
        {
            tvStartList.add(rootView.findViewById(i))
        }

        for(i in idAimList)
        {
            tvAimList.add(rootView.findViewById(i))
        }

        var startValues = bodyViewModel.getStartBodyList()
        var aimValues = bodyViewModel.getAimBodyList()

        // Gewichtsziel
        if(startValues[0] == -1f || aimValues[0] == -1f)
        {
            tvStartList[0].text = ""
            tvAimList[0].text = ""
        }
        else
        {
            tvStartList[0].text = "${helper.getFloatAsFormattedStringWithPattern(startValues[0],"#")} kg"
            tvAimList[0].text = "${helper.getFloatAsFormattedStringWithPattern(aimValues[0],"#")} kg"
        }

        // Kfa Ziel
        if(startValues[1] == -1f || aimValues[1] == -1f)
        {
            tvStartList[1].text = ""
            tvAimList[1].text = ""
        }
        else
        {
            tvStartList[1].text = helper.getFloatAsFormattedStringWithPattern(startValues[1],"#")
            tvAimList[1].text = helper.getFloatAsFormattedStringWithPattern(aimValues[1],"#")
        }

        // BMI Ziel
        if(startValues[2] == -1f || aimValues[2] == -1f)
        {
            tvStartList[2].text = ""
            tvAimList[2].text = ""
        }
        else
        {
            tvStartList[2].text = helper.getFloatAsFormattedStringWithPattern(startValues[2],"#")
            tvAimList[2].text = helper.getFloatAsFormattedStringWithPattern(aimValues[2],"#")
        }

    }

    private fun startStatusViewAnimation()
    {
        var pbList:ArrayList<ProgressBar> = arrayListOf(pbWeight,pbKfa,pbBMI)
        pbList.forEach {
            it.max = 100
            it.progress = 0}
        var tvList:ArrayList<TextView> = arrayListOf(tvWeight,tvKfa,tvBMI)
        var maxList:ArrayList<Float> = arrayListOf(100f,100f,100f)
        var progressList:ArrayList<Float> = bodyViewModel.getProgressList()
        Log.d("Smeasy","BodyEntryFragment - startStatusViewAnimation progressList: $progressList")

        animator = BodyEntryStatusViewAnimator(rootView.context,pbList,tvList,checkIfAimWasSet(),progressList,maxList)
        animator.setOnAnimationStatusViewListener(object: BodyEntryStatusViewAnimator.OnAnimationStatusViewInitListener
        {
            override fun setOnAnimationStatusViewListener() {
                Log.d("Smeasy","BodyEntryFragment - initStatusView BodyEntryStatusAnimator has finished")

            }

        })
    }

    private fun updateStatusViewAnimation()
    {
        var progressList:ArrayList<Float> = bodyViewModel.getProgressList()
        animator.animateNewValues(progressList,checkIfAimWasSet())
    }

    private fun updateStatusViewTextView()
    {
        var startValues = bodyViewModel.getStartBodyList()
        var aimValues = bodyViewModel.getAimBodyList()

        // Gewichtsziel
        if(startValues[0] == -1f || aimValues[0] == -1f)
        {
            tvStartList[0].text = ""
            tvAimList[0].text = ""
        }
        else
        {
            tvStartList[0].text = "${helper.getFloatAsFormattedStringWithPattern(startValues[0],"#")} kg"
            tvAimList[0].text = "${helper.getFloatAsFormattedStringWithPattern(aimValues[0],"#")} kg"
        }

        // Kfa Ziel
        if(startValues[1] == -1f || aimValues[1] == -1f)
        {
            tvStartList[1].text = ""
            tvAimList[1].text = ""
        }
        else
        {
            tvStartList[1].text = helper.getFloatAsFormattedStringWithPattern(startValues[1],"#")
            tvAimList[1].text = helper.getFloatAsFormattedStringWithPattern(aimValues[1],"#")
        }

        // BMI Ziel
        if(startValues[2] == -1f || aimValues[2] == -1f)
        {
            tvStartList[2].text = ""
            tvAimList[2].text = ""
        }
        else
        {
            tvStartList[2].text = helper.getFloatAsFormattedStringWithPattern(startValues[2],"#")
            tvAimList[2].text = helper.getFloatAsFormattedStringWithPattern(aimValues[2],"#")
        }
    }

    private fun undoBodyDelete(body: Body)
    {
        var snackbar = Snackbar.make(activity!!.findViewById(R.id.nav_host_fragment),"Rückgängig machen",Snackbar.LENGTH_LONG)



        snackbar.setAction("Rückgängig", object: View.OnClickListener{
            override fun onClick(v: View?) {
                bodyViewModel.undoBodyDelete(body)
            }

        })
        snackbar.show()
    }

    private fun checkIfAimWasSet():ArrayList<Boolean>
    {
        var export:ArrayList<Boolean> = ArrayList()
        var startValues = bodyViewModel.getStartBodyList()
        var aimValues = bodyViewModel.getAimBodyList()

        // Gewichtsziel
        if(startValues[0] == -1f || aimValues[0] == -1f)
        {
            export.add(false)
        }
        else
        {
            export.add(true)
        }

        // Kfa Ziel
        if(startValues[1] == -1f || aimValues[1] == -1f)
        {
            export.add(false)
        }
        else
        {
            export.add(true)
        }

        // BMI Ziel
        if(startValues[2] == -1f || aimValues[2] == -1f)
        {
            export.add(false)
        }
        else
        {
            export.add(true)
        }

        return export
    }

}