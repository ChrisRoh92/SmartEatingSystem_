package de.rohnert.smeasy.frontend.bodytracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.bodytracker.adapter.DialogShowBodyEntryRecyclerAdapter
import de.rohnert.smeasy.helper.dialogs.DialogSingleLineInput
import de.rohnert.smeasy.helper.others.CustomDividerItemDecoration
import kotlin.math.roundToInt

class DialogShowBodyEntry(var context: Context,var entry: Body) : View.OnClickListener {


    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var share: SharedAppPreferences
    private var helper = Helper()

    // Interface:
    private lateinit var mListener:OnDialogClickListener

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
        btnPhoto = view.findViewById(R.id.dialog_show_body_btn_photo)
        btnSave = view.findViewById(R.id.dialog_show_body_btn_save)
        btnAbort = view.findViewById(R.id.dialog_show_body_btn_abort)

        // Listener aktivieren:
        btnPhoto.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

    }

    private fun initRecyclerView()
    {

        fun createContent()
        {
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.weight,"#.##"))
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.kfa,"#.##"))
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.bauch,"#.##"))
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.brust,"#.##"))
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.hals,"#.##"))
            contentString.add(helper.getFloatAsFormattedStringWithPattern(entry.huefte,"#.##"))

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


        adapter.setOnItemClickListener(object:DialogShowBodyEntryRecyclerAdapter.OnItemClickListener{
            override fun setOnItemClickListener(pos: Int)
            {
                var dialog = DialogSingleLineInput("Daten ändern",titles[pos],context,type,contentString[pos])
                dialog.onDialogClickListener(object:DialogSingleLineInput.OnDialogListener{
                    override fun onDialogClickListener(export: String) {

                    }

                    override fun onDialogClickListener(export: Float) {
                        content[pos] = export
                        contentString[pos] = helper.getFloatAsFormattedStringWithPattern(export,"#.##")
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
        }
        else if(v == btnPhoto)
        {
            var dialog = DialogShowPicture(context,entry.fotoDir)
        }
    }


    // Interface
    interface OnDialogClickListener
    {
        fun setOnDialogClickListener(updatedBody:Body)
    }

    fun setOnDialogClickListener(mListener:OnDialogClickListener)
    {
        this.mListener = mListener
    }
}