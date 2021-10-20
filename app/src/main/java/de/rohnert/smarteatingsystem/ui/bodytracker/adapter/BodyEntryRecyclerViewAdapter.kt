package de.rohnert.smarteatingsystem.ui.bodytracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.data.helper.Helper
import de.rohnert.smarteatingsystem.data.databases.body_database.Body
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.utils.getDateStringFromLongDateString
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.ui.bodytracker.dialogs.dialog_bodyentry.DialogShowPicture
import de.rohnert.smarteatingsystem.ui.premium.dialogs.DialogFragmentPremium


class BodyEntryRecyclerViewAdapter(var content: ArrayList<Body>, var fragmentManager: FragmentManager, var prefs:SharedAppPreferences):
    RecyclerView.Adapter<BodyEntryRecyclerViewAdapter.ViewHolder>() {

    private var helper = Helper()
    // Interface:
    private lateinit var mLongListener:OnBodyEntryLongClickListener
    private lateinit var mListener:OnBodyEntryClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_bodyentry, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val body = content[position]
        holder.tvDate.text = "vom ${getDateStringFromLongDateString(body.date)}"
        holder.tvWeight.text = "${helper.getFloatAsFormattedString(body.weight,"#.#")} kg"

        if(body.fotoDir.isEmpty() || body.fotoDir.isBlank())
        {
            holder.imageBtn.setImageResource(R.drawable.ic_baseline_no_photography_24)
            holder.imageBtn.isEnabled = false
        }

        else
        {
            holder.imageBtn.setOnClickListener {
                if(prefs.premiumStatus)
                {
                    /*Log.d("Smeasy","BodyEntryRecyclerViewAdapter imageButton Click Listener")
                    var captureDialog = DialogCapturePhoto(content[position].fotoDir)
                    captureDialog.show(fragmentManager,"capture")*/
                    if(content[position].fotoDir != "")
                    {
                        var dialog = DialogShowPicture(holder.imageBtn.context,content[position].fotoDir)
                    }
                    else
                    {
                        Toast.makeText(holder.imageBtn.context,"Kein Foto vorhanden...", Toast.LENGTH_SHORT).show()
                    }

                }
                else
                {
                    var dialog = DialogFragmentPremium()
                    dialog.show(fragmentManager,"Premium")
                }

            }
        }


        if(!prefs.premiumStatus)
        {
            holder.imageBtn.setImageDrawable(ContextCompat.getDrawable(holder.imageBtn.context,R.drawable.ic_lock_black))
            holder.imageBtn.setColorFilter(ContextCompat.getColor(holder.imageBtn.context,R.color.premium_dark))

        }



        holder.itemView.setOnLongClickListener {
            if(mLongListener!=null)
            {
                mLongListener.setOnBodyEntryLongClickListener(content[holder.adapterPosition],holder.adapterPosition)
            }
            true
        }

        holder.itemView.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnBodyEntryClickListener(content[holder.adapterPosition],holder.adapterPosition)
            }

        }


    }


    fun updateContent(content: ArrayList<Body>)
    {
//        var local = content.asReversed()
        this.content = content
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var imageBtn:ImageButton = itemView.findViewById(R.id.recycleritem_bodyentry_btn)
        var tvDate:TextView = itemView.findViewById(R.id.recycleritem_bodyentry_tvDate)
        var tvWeight:TextView = itemView.findViewById(R.id.recycleritem_bodyentry_tvWeight)

    }

    // Interface:
    interface OnBodyEntryLongClickListener
    {
        fun setOnBodyEntryLongClickListener(body: Body, pos:Int)
    }

    fun setOnBodyEntryLongClickListener(mLongListener:OnBodyEntryLongClickListener)
    {
        this.mLongListener = mLongListener
    }

    interface OnBodyEntryClickListener
    {
        fun setOnBodyEntryClickListener(body: Body, pos:Int)
    }

    fun setOnBodyEntryClickListener(mListener:OnBodyEntryClickListener)
    {
        this.mListener = mListener
    }
}