package de.rohnert.smarteatingsystem.ui.bodytracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R

class AllowedFoodRecyclerAdapter(var content:ArrayList<String>,var premiumState:Boolean):
    RecyclerView.Adapter<AllowedFoodRecyclerAdapter.ViewHolder>()
{

    private var titles:ArrayList<String> = arrayListOf("Kalorien","Kohlenhydrate","Protein","Fett")

    // Interface:
    private lateinit var mListener:OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_settings_main,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!premiumState)
        {
            holder.tvTitle.setTextColor(ContextCompat.getColor(holder.tvTitle.context,android.R.color.white))
            holder.tvSubTitle.setTextColor(ContextCompat.getColor(holder.tvTitle.context,R.color.backgroundLight2))
        }
        else
        {
            holder.tvTitle.setTextColor(ContextCompat.getColor(holder.tvTitle.context,R.color.textColor1))
            holder.tvSubTitle.setTextColor(ContextCompat.getColor(holder.tvTitle.context,android.R.color.tab_indicator_text))
        }
        holder.tvTitle.text = titles[position]
        holder.tvSubTitle.text = content[position]

        holder.itemView.setOnClickListener {
            if(mListener != null)
            {
                mListener.setOnItemClickListener(holder.adapterPosition)
            }
        }

    }

    fun updateContent(content:ArrayList<String>,premiumState:Boolean = false)
    {
        this.content = content
        this.premiumState = premiumState
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle: TextView = itemView.findViewById(R.id.settings_item_title)
        var tvSubTitle:TextView = itemView.findViewById(R.id.settings_item_subtitle)

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