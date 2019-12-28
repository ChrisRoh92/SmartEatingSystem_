package de.rohnert.smeasy.frontend.statistics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smeasy.backend.helper.Helper
import de.rohnert.smeasy.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smeasy.R

class StatisticFoodTrackedFoodRecyclerAdapter(var content:ArrayList<CalcedFood>):
    RecyclerView.Adapter<StatisticFoodTrackedFoodRecyclerAdapter.ViewHolder>()
{
    // Allgemeines Stuff:
    private var helper = Helper()

    // Interface:
    private lateinit var mListener:OnRecyclerViewClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_statistic_food_tracked_food, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val calcedFood = content[position]
        holder.tvIndex.text = (position+1).toString()
        holder.tvTitle.text = calcedFood.f.name
        holder.tvSubTitle.text = calcedFood.f.category
        var unitBig = "kg"
        var unitSmall = "g"
        if(calcedFood.f.unit.contains("ml"))
        {
            unitBig = "l"
            unitSmall = "ml"
        }

        if(calcedFood.menge > 1000f)
        {
            holder.tvValue.text = "${helper.getFloatAsFormattedStringWithPattern(calcedFood.menge/1000f,"#.##")} $unitBig"
        }
        else
        {
            holder.tvValue.text = "${helper.getFloatAsFormattedStringWithPattern(calcedFood.menge,"#")} $unitSmall"
        }

        holder.itemView.setOnClickListener{
            if(mListener!=null)
            {
                mListener.setOnRecyclerViewClickListener(holder.adapterPosition)
            }
        }


    }

    fun updateContent(content:ArrayList<CalcedFood>)
    {
        this.content = content
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle:TextView = itemView.findViewById(R.id.item_statistic_food_title)
        var tvSubTitle:TextView = itemView.findViewById(R.id.item_statistic_food_subtitle)
        var tvValue:TextView = itemView.findViewById(R.id.item_statistic_food_value)
        var tvIndex:TextView = itemView.findViewById(R.id.item_statistic_food_index)

    }

    // Interface:
    interface OnRecyclerViewClickListener
    {
        fun setOnRecyclerViewClickListener(pos:Int)
    }

    fun setOnRecyclerViewClickListener(mListener:OnRecyclerViewClickListener)
    {
        this.mListener = mListener
    }
}