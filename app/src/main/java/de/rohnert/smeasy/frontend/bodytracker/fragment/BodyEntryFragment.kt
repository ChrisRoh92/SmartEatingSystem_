package de.rohnert.smeasy.frontend.bodytracker.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import com.google.android.material.snackbar.Snackbar
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel
import de.rohnert.smeasy.frontend.bodytracker.adapter.BodyEntryRecyclerViewAdapter
import de.rohnert.smeasy.frontend.bodytracker.animations.BodyEntryStatusViewAnimator
import de.rohnert.smeasy.frontend.bodytracker.dialogs.DialogNewBodyEntry
import de.rohnert.smeasy.frontend.bodytracker.dialogs.DialogShowBodyEntry
import de.rohnert.smeasy.helper.others.CustomDividerItemDecoration

class BodyEntryFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var bodyViewModel: BodyViewModel
    private var helper = Helper()

    // Animation
    private lateinit var animator:BodyEntryStatusViewAnimator
    private var firstStart = false

    // View Elemente:
    private lateinit var recyclerViewCardView:CardView
    private lateinit var statusCardView:CardView
    private lateinit var pbWeight:ProgressBar
    private lateinit var pbKfa:ProgressBar
    private lateinit var pbBMI:ProgressBar
    private var tvStartList:ArrayList<TextView> = ArrayList()
    private var tvAimList:ArrayList<TextView> = ArrayList()
    private var idStartList:ArrayList<Int> = arrayListOf(R.id.bodyentry_tv_weight_start,R.id.bodyentry_tv_kfa_start,R.id.bodyentry_tv_bmi_start)
    private var idAimList:ArrayList<Int> = arrayListOf(R.id.bodyentry_tv_weight_aim,R.id.bodyentry_tv_kfa_aim,R.id.bodyentry_tv_bmi_aim)

    // ScrollView:
    private lateinit var scrollView:NestedScrollView

    private lateinit var tvWeight:TextView
    private lateinit var tvKfa:TextView
    private lateinit var tvBMI:TextView

    // Button:
    private lateinit var btnAdd:Button


    // RecyclerView:
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:BodyEntryRecyclerViewAdapter
    private lateinit var manager:LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bodyViewModel = ViewModelProviders.of(this).get(BodyViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_bodytracker_bodyentries, container, false)


        /*initRecyclerView()*/
        Handler().postDelayed({
            scrollView = rootView.findViewById(R.id.bodyentry_scrollview)
            initStatusView()
            initRecyclerView()
            initObserver()


            /*var snackbar = Snackbar.make(activity!!.findViewById(R.id.nav_host_fragment),"Funktion befindet sich zum Teil noch im Aufbau...",
                Snackbar.LENGTH_LONG)
            var snackView = snackbar.view
            var snackTv:TextView = snackView!!.findViewById(com.google.android.material.R.id.snackbar_text)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                snackTv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            } else {
                snackTv.gravity = Gravity.CENTER_HORIZONTAL
            }
            snackbar.show()*/

        },500)


        return rootView
    }




    private fun initObserver()
    {
        bodyViewModel.getLiveBodyList().observe(viewLifecycleOwner,androidx.lifecycle.Observer{
            adapter.updateContent(it)
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
                scrollView.scrollTo(0,0)
                updateStatusViewAnimation()
                updateStatusViewTextView()

            }

        })
    }

    private fun initRecyclerView()
    {
        recyclerView = rootView.findViewById(R.id.bodyentry_recyclerview)
        adapter = BodyEntryRecyclerViewAdapter(ArrayList(),fragmentManager!!)
        manager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 68))

        adapter.setOnBodyEntryLongClickListener(object:BodyEntryRecyclerViewAdapter.OnBodyEntryLongClickListener{
            override fun setOnBodyEntryLongClickListener(body: Body, pos: Int) {
                bodyViewModel.deleteBody(body)
            }

        })

        adapter.setOnBodyEntryClickListener(object:BodyEntryRecyclerViewAdapter.OnBodyEntryClickListener{
            override fun setOnBodyEntryClickListener(body: Body, pos: Int) {
                var dialog = DialogShowBodyEntry(rootView.context,body)
            }

        })




        //


    }

    private fun initStatusView()
    {
        recyclerViewCardView = rootView.findViewById(R.id.bodyentry_cardview_recyclerview)
        statusCardView = rootView.findViewById(R.id.bodyentry_cardview_statusview)


        btnAdd = rootView.findViewById(R.id.bodyentry_btn_add)
        btnAdd.setOnClickListener {
            if(!bodyViewModel.checkIfBodyExist())
            {
                var dialog = DialogNewBodyEntry(rootView.context,fragmentManager!!,bodyViewModel)

            }
            else
            {
                Toast.makeText(rootView.context,"Du hast heute bereits ein Eintrag erstellt...",Toast.LENGTH_SHORT).show()
            }

        }


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

        tvStartList[0].text = "${helper.getFloatAsFormattedStringWithPattern(startValues[0],"#")} kg"
        tvStartList[1].text = "${helper.getFloatAsFormattedStringWithPattern(startValues[1],"#")}"
        tvStartList[2].text = "${helper.getFloatAsFormattedStringWithPattern(startValues[2],"#")}"

        tvAimList[0].text = "${helper.getFloatAsFormattedStringWithPattern(aimValues[0],"#")} kg"
        tvAimList[1].text = "${helper.getFloatAsFormattedStringWithPattern(aimValues[1],"#")}"
        tvAimList[2].text = "${helper.getFloatAsFormattedStringWithPattern(aimValues[2],"#")}"
    }

    private fun startCardViewAnimation()
    {
        // Animate CardViews
        var alpha = PropertyValuesHolder.ofFloat(View.ALPHA,0f,1f)
        var transY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,200f,0f)
        //var scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,0f,1f)


        var statusCardAnimation = ObjectAnimator.ofPropertyValuesHolder(statusCardView,alpha,transY).apply {
            startDelay = 500
            duration = 250
            interpolator = FastOutSlowInInterpolator()

        }

        var rvCardAnimation = ObjectAnimator.ofPropertyValuesHolder(recyclerViewCardView,alpha,transY).apply {
            startDelay = 0
            duration = 250
            interpolator = FastOutSlowInInterpolator()

        }



        var btnAnimation = ObjectAnimator.ofPropertyValuesHolder(btnAdd,alpha,transY).apply {
            startDelay = 0
            duration = 250
            interpolator = FastOutSlowInInterpolator()

        }

        var cardViewSet = AnimatorSet()
        cardViewSet.playSequentially(statusCardAnimation,rvCardAnimation,btnAnimation)
        cardViewSet.start()
        cardViewSet.addListener(object: Animator.AnimatorListener
        {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                initRecyclerView()
                startStatusViewAnimation()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }

        })
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

        animator = BodyEntryStatusViewAnimator(rootView.context,pbList,tvList,progressList,maxList)
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
        animator.animateNewValues(progressList)
    }

    private fun updateStatusViewTextView()
    {
        var startValues = bodyViewModel.getStartBodyList()
        var aimValues = bodyViewModel.getAimBodyList()

        tvStartList[0].text = "${helper.getFloatAsFormattedStringWithPattern(startValues[0],"#")} kg"
        tvStartList[1].text = "${helper.getFloatAsFormattedStringWithPattern(startValues[1],"#")}"
        tvStartList[2].text = "${helper.getFloatAsFormattedStringWithPattern(startValues[2],"#")}"

        tvAimList[0].text = "${helper.getFloatAsFormattedStringWithPattern(aimValues[0],"#")} kg"
        tvAimList[1].text = "${helper.getFloatAsFormattedStringWithPattern(aimValues[1],"#")}"
        tvAimList[2].text = "${helper.getFloatAsFormattedStringWithPattern(aimValues[2],"#")}"
    }


}