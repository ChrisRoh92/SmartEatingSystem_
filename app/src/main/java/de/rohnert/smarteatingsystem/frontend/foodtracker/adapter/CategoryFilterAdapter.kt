package de.rohnert.smarteatingsystem.frontend.foodtracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R

class CategoryFilterAdapter(var content:ArrayList<String>, var activContent:ArrayList<Boolean>): RecyclerView.Adapter<CategoryFilterAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_filter_category, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCategory.text = content[position]
        holder.checkBox.text = ""
        holder.checkBox.isChecked = activContent[position]
        holder.checkBox.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                activContent[holder.adapterPosition] = isChecked
            }

        })
        holder.itemView.setOnClickListener {
            if(holder.checkBox.isChecked)
            {
                holder.checkBox.isChecked = false
                activContent[holder.adapterPosition] = false
            }
            else
            {
                holder.checkBox.isChecked = true
                activContent[holder.adapterPosition] = true
            }
        }
    }

    fun setAllContentToState(state:Boolean)
    {
        var values:ArrayList<Boolean> = ArrayList()
        for(i in activContent)
        {
            values.add(state)
        }
        activContent = values
        notifyDataSetChanged()
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvCategory:TextView = itemView.findViewById(R.id.item_filter_tv_category)
        var checkBox:CheckBox = itemView.findViewById(R.id.item_filter_checkbox)

    }
}