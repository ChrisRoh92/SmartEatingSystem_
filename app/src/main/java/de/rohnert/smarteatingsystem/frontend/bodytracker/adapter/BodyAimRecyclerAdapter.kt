package de.rohnert.smarteatingsystem.frontend.bodytracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R

class BodyAimRecyclerAdapter(var content:ArrayList<String>): RecyclerView.Adapter<BodyAimRecyclerAdapter.ViewHolder>()
{

    // Interface:
    private lateinit var mListener:OnItemClickListener

    // Content
    private var titles:ArrayList<String> = arrayListOf(
        "Gewicht",
        "Körperfettanteil (KFA)",
        "BodyMassIndex (BMI)",
        "Bauchumfang",
        "Brustumfang",
        "Halsumfang",
        "Hüftumfang")




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_settings_main,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.tvTitle.text = titles[position]
        holder.tvSubTitle.text = content[position]
        holder.itemView.setOnClickListener {
            if(mListener != null)
            {
                mListener.setOnItemClickListener(holder.adapterPosition)
            }
        }

    }

    // Sachen Updaten...
    fun updateData(content:ArrayList<String>)
    {
        this.content
        notifyDataSetChanged()

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle: TextView = itemView.findViewById(R.id.settings_item_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.settings_item_subtitle)
    }


    // Interface:
    interface OnItemClickListener
    {
        fun setOnItemClickListener(pos:Int)
    }

    fun setOnItemClickListener(mListener:OnItemClickListener)
    {
        this.mListener = mListener
    }


}