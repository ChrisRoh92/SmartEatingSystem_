package de.rohnert.smarteatingsystem.frontend.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R

class MainSettingsAdapter(var content:ArrayList<String>, var subContent:ArrayList<String>):
    RecyclerView.Adapter<MainSettingsAdapter.ViewHolder>() {

    // Interface:
    private lateinit var mListener:OnMainSettingsClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_settings_main, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = content[position]
        holder.tvSubTitle.text = subContent[position]

        holder.itemView.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnMainSettingsClickListener(holder.adapterPosition)
            }
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle: TextView = itemView.findViewById(R.id.settings_item_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.settings_item_subtitle)

    }

    interface OnMainSettingsClickListener
    {
        fun setOnMainSettingsClickListener(pos:Int)
    }

}