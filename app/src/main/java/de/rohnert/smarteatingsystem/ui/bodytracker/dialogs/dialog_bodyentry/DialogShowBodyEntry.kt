package de.rohnert.smarteatingsystem.ui.bodytracker.dialogs.dialog_bodyentry

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.data.helper.Helper
import de.rohnert.smarteatingsystem.data.databases.body_database.Body
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.ui.bodytracker.adapter.DialogShowBodyEntryRecyclerAdapter
import de.rohnert.smarteatingsystem.ui.premium.dialogs.DialogFragmentPremium
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleLineInput
import de.rohnert.smarteatingsystem.utils.others.CustomDividerItemDecoration

class DialogShowBodyEntry(var context: Context, var entry: Body, var fragmentManager: FragmentManager) : View.OnClickListener {


    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private var prefs = SharedAppPreferences(context)
    private var helper = Helper()

    // Interface:
    private lateinit var mListener: OnDialogClickListener

    // View Elemente
    private lateinit var tvSubTitle:TextView
    private lateinit var btnPhoto:ImageButton
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button

    // RecyclerView
    private lateinit var rv:RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: DialogShowBodyEntryRecyclerAdapter

    // Content.
    private var contentString:ArrayList<String> = ArrayList()
    private var content:ArrayList<Float> = ArrayList()
    private var titles:ArrayList<String> = arrayListOf("Körpergewicht in Kg","Körperfettanteil in %","Bauchumfang in cm","Halsumfang in cm","Brustumfang in cm","Halsumfang in cm")

    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_show_body_entry, null)
        builder.setView(view)



        initViews()
        initRecyclerView()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        tvSubTitle = view.findViewById(R.id.dialog_show_body_tv_subtitle)

        // Button
        btnSave = view.findViewById(R.id.dialog_show_body_btn_save)
        btnAbort = view.findViewById(R.id.dialog_show_body_btn_abort)

        // Listener aktivieren:
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

        // ImageButton
        btnPhoto = view.findViewById(R.id.dialog_show_body_btn_photo)
        btnPhoto.setOnClickListener(this)

        if(!prefs.premiumStatus)
        {
            btnPhoto.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_lock_black))
            btnPhoto.setColorFilter(ContextCompat.getColor(context,R.color.premium_dark))

        }



    }

    private fun initRecyclerView()
    {

        fun createContent()
        {
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.weight,"#.##") + " kg")
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.kfa,"#.##")+ " %")
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.bauch,"#.##")+ " cm")
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.brust,"#.##")+ " cm")
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.hals,"#.##")+ " cm")
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.huefte,"#.##")+ " cm")

            content.add(entry.weight)
            content.add(entry.kfa)
            content.add(entry.bauch)
            content.add(entry.brust)
            content.add(entry.hals)
            content.add(entry.huefte)
        }

        createContent()

        rv = view.findViewById(R.id.dialog_show_body_rv)
        layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        adapter = DialogShowBodyEntryRecyclerAdapter(contentString)
        rv.layoutManager = layoutManager
        rv.adapter = adapter

        rv.addItemDecoration(CustomDividerItemDecoration(RecyclerView.VERTICAL, context, 0))

        // Stuff für den SingleLineInput
        var type = InputType.TYPE_NUMBER_FLAG_DECIMAL

        var units:ArrayList<String> = arrayListOf(" kg"," %"," cm"," cm"," cm"," cm")
        adapter.setOnItemClickListener(object:DialogShowBodyEntryRecyclerAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int)
            {
                var dialog = DialogSingleLineInput("Daten ändern",titles[pos],context,type,helper.getFloatAsFormattedStringWithPattern(content[pos],"#.##"))
                dialog.onDialogClickListener(object:DialogSingleLineInput.OnDialogListener{
                    override fun onDialogClickListener(export: String) {

                    }

                    override fun onDialogClickListener(export: Float) {
                        content[pos] = export
                        contentString[pos] = helper.getFloatAsFormattedStringWithPattern(export,"#.##")+units[pos]
                        adapter.updateContent(contentString)
                    }

                })

            }

        })


    }

    // Implementierte Methoden:
    override fun onClick(v: View?) {
        if(v == btnAbort)
        {
            alertDialog.dismiss()
        }
        else if(v == btnSave)
        {
            // Daten aus dem RecyclerView holen...
            var newBody = Body(entry.date,content[0],content[1],content[2],content[3],content[4],content[5],entry.fotoDir)
            if(mListener!=null)
            {
                mListener.setOnDialogClickListener(newBody)
                alertDialog.dismiss()
            }
        }
        else if(v == btnPhoto)
        {
            if(prefs.premiumStatus)
            {
                if(entry.fotoDir != "")
                {
                    var dialog = DialogShowPicture(context,entry.fotoDir)
                }
                else
                {
                    Toast.makeText(context,"Kein Foto vorhanden...",Toast.LENGTH_SHORT).show()
                }
            }

            else
            {
                var dialog = DialogFragmentPremium()
                dialog.show(fragmentManager,"Premium")
            }


        }
    }


    // Interface
    interface OnDialogClickListener
    {
        fun setOnDialogClickListener(updatedBody: Body)
    }

    fun setOnDialogClickListener(mListener: OnDialogClickListener)
    {
        this.mListener = mListener
    }


}