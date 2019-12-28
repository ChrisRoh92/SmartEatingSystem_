package de.rohnert.smeasy.frontend.bodytracker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel
import de.rohnert.smeasy.frontend.bodytracker.dialogs.DialogCapturePhoto
import de.rohnert.smeasy.frontend.bodytracker.dialogs.DialogShowPicture
import de.rohnert.smeasy.frontend.premium.dialogs.DialogFragmentPremium


class BodyEntryRecyclerViewAdapter(var content: ArrayList<Body>,var fragmentManager: FragmentManager, var prefs:SharedAppPreferences):
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
        holder.tvDate.text = "vom ${content[position].date}"
        holder.tvWeight.text = "${helper.getFloatAsFormattedString(content[position].weight,"#.#")} kg"


        if(!prefs.premiumStatus)
        {
            holder.imageBtn.setImageDrawable(ContextCompat.getDrawable(holder.imageBtn.context,R.drawable.ic_lock_black))
            holder.imageBtn.setColorFilter(ContextCompat.getColor(holder.imageBtn.context,R.color.premium_dark))

        }

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
        var local = content.asReversed()
        this.content = ArrayList(local)
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
        fun setOnBodyEntryLongClickListener(body:Body,pos:Int)
    }

    fun setOnBodyEntryLongClickListener(mLongListener:OnBodyEntryLongClickListener)
    {
        this.mLongListener = mLongListener
    }

    interface OnBodyEntryClickListener
    {
        fun setOnBodyEntryClickListener(body:Body,pos:Int)
    }

    fun setOnBodyEntryClickListener(mListener:OnBodyEntryClickListener)
    {
        this.mListener = mListener
    }
}