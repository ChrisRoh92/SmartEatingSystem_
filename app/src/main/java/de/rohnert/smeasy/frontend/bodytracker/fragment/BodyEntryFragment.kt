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
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import com.google.android.material.snackbar.Snackbar
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel
import de.rohnert.smeasy.frontend.bodytracker.adapter.BodyEntryRecyclerViewAdapter
import de.rohnert.smeasy.frontend.bodytracker.animations.BodyEntryStatusViewAnimator
import de.rohnert.smeasy.frontend.bodytracker.dialogs.DialogNewBodyEntry
import de.rohnert.smeasy.helper.others.CustomDividerItemDecoration

class BodyEntryFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var bodyViewModel: BodyViewModel

    // Animation
    private lateinit var animator:BodyEntryStatusViewAnimator

    // View Elemente:
    private lateinit var recyclerViewCardView:CardView
    private lateinit var statusCardView:CardView
    private lateinit var pbWeight:ProgressBar
    private lateinit var pbKfa:ProgressBar
    private lateinit var pbBMI:ProgressBar


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
            initStatusView()
            initRecyclerView()
            initObserver()
            startStatusViewAnimation()

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

        },100)


        return rootView
    }




    private fun initObserver()
    {
        bodyViewModel.getLiveBodyList().observe(viewLifecycleOwner,androidx.lifecycle.Observer{
            adapter.updateContent(it)
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
                bodyViewModel.deleteBody(body)
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
        var progressList:ArrayList<Float> = bodyViewModel.getProgressValues()

        animator = BodyEntryStatusViewAnimator(rootView.context,pbList,tvList,progressList,maxList)
        animator.setOnAnimationStatusViewListener(object: BodyEntryStatusViewAnimator.OnAnimationStatusViewInitListener
        {
            override fun setOnAnimationStatusViewListener() {
                Log.d("Smeasy","BodyEntryFragment - initStatusView BodyEntryStatusAnimator has finished")

            }

        })
    }
}