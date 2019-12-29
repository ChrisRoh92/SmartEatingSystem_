package de.rohnert.smarteatingsystem.frontend.bodytracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R

class DialogShowBodyEntryRecyclerAdapter(var content:ArrayList<String>):
    RecyclerView.Adapter<DialogShowBodyEntryRecyclerAdapter.ViewHolder>() {

    // Interface
    private lateinit var mListener:OnItemClickListener

    // Titles
    private var titles:ArrayList<String> = arrayListOf("Körpergewicht in Kg","Körperfettanteil in %","Bauchumfang in cm","Halsumfang in cm","Brustumfang in cm","Halsumfang in cm")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_simple_two_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = titles[position]
        holder.tvSubTitle.text = content[position]
        holder.itemView.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnItemClickListener(holder.adapterPosition)
            }
        }
    }


    fun updateContent(content:ArrayList<String>)
    {
        this.content
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle: TextView = itemView.findViewById(R.id.simple_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.simple_subtitle)

    }


    interface OnItemClickListener
    {
        fun setOnItemClickListener(pos:Int)
    }

    fun setOnItemClickListener(mListener:OnItemClickListener)
    {
        this.mListener = mListener
    }


}