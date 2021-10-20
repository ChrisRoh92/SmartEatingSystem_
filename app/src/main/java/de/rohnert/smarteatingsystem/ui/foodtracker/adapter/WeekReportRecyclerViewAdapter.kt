package de.rohnert.smarteatingsystem.ui.foodtracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.data.helper.Helper
import de.rohnert.smarteatingsystem.data.databases.daily_database.helper.CalcedFood
import de.rohnert.smarteatingsystem.R

class WeekReportRecyclerViewAdapter(var content:ArrayList<CalcedFood>, var context: Context): RecyclerView.Adapter<WeekReportRecyclerViewAdapter.ViewHolder>()
{

    // Allgemeine Variablen:
    private var helper = Helper()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_weekreport_standard, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCalcedFood = content[position]
        holder.tvName.text = currentCalcedFood.f.name
        holder.tvGroup.text = currentCalcedFood.f.category
        if(currentCalcedFood.f.unit.contains("g"))
        {
            holder.tvMenge.text = "${helper.getFloatAsFormattedString(currentCalcedFood.menge,"#")} g"
        }
        else
        {
            holder.tvMenge.text = "${helper.getFloatAsFormattedString(currentCalcedFood.menge,"#")} ml"
        }

        holder.tvKcal.text = "${helper.getFloatAsFormattedString(currentCalcedFood.values[0],"#")} kcal"

        /*holder.itemView.setOnLongClickListener {
            if(mLongListener!=null)
            {

                mLongListener.setOnLongClickListener((getCalcedFoodAt(holder.adapterPosition)),holder.adapterPosition)

            }
            true
        }

        holder.itemView.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnClickListener(getCalcedFoodAt(holder.adapterPosition),holder.adapterPosition)
            }
        }*/


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvName: TextView = itemView.findViewById(R.id.weekreport_standard_item_name)
        var tvGroup: TextView = itemView.findViewById(R.id.weekreport_standard_item_group)
        var tvMenge: TextView = itemView.findViewById(R.id.weekreport_standard_item_menge)
        var tvKcal: TextView = itemView.findViewById(R.id.weekreport_standard_item_kcal)
    }





}