package de.rohnert.smarteatingsystem.frontend.helper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R

class HelperRecyclerAdapter(var content:ArrayList<String>,var subContent:ArrayList<String>) : RecyclerView.Adapter<HelperRecyclerAdapter.ViewHolder>() {


    // Interface:
    private lateinit var mListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_simple_two_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.tvTitle.text = content[position]
        holder.tvSubTitle.text = subContent[position]
        holder.tvSubTitle.textSize = 10f

        holder.itemView.setOnClickListener {
            if(mListener != null)
            {
                mListener.setOnItemClickListener(holder.adapterPosition)
            }
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle:TextView = itemView.findViewById(R.id.simple_title)
        var tvSubTitle:TextView = itemView.findViewById(R.id.simple_subtitle)

    }

    interface OnItemClickListener
    {
        fun setOnItemClickListener(pos:Int)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener)
    {
        this.mListener = mListener
    }


}