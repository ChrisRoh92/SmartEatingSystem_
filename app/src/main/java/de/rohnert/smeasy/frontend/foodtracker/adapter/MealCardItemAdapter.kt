package de.rohnert.smeasy.frontend.foodtracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smeasy.R


class MealCardItemAdapter : ListAdapter<CalcedFood, MealCardItemAdapter.MealCardHolder>(DIFF_CALLBACK) {


    private var helper = Helper()

    // Interface:
    lateinit var mLongListener:OnLongClickListener
    lateinit var mListener:OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCardHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_mealcard_foodtracker, parent, false)
        return MealCardHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealCardHolder, position: Int) {
        val currentCalcedFood = getItem(position)
        holder.tvName.text = currentCalcedFood.f.name
        holder.tvGroup.text = currentCalcedFood.f.category
        holder.tvMenge.text = "${helper.getFloatAsFormattedString(currentCalcedFood.menge,"#")} g/ml"
        holder.tvKcal.text = "${helper.getFloatAsFormattedString(currentCalcedFood.values[0],"#")} kcal"

        holder.itemView.setOnLongClickListener {
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
        }

    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CalcedFood>() {
            override fun areItemsTheSame(oldItem: CalcedFood, newItem: CalcedFood): Boolean {
                return newItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: CalcedFood, newItem: CalcedFood): Boolean {
                return false
            }
        }
    }


    fun getCalcedFoodAt(pos:Int):CalcedFood
    {
        return getItem(pos)
    }

    class MealCardHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var tvName: TextView = itemView.findViewById(R.id.mealcard_item_name)
        var tvGroup: TextView = itemView.findViewById(R.id.mealcard_item_group)
        var tvMenge: TextView = itemView.findViewById(R.id.mealcard_item_menge)
        var tvKcal: TextView = itemView.findViewById(R.id.mealcard_item_kcal)


    }

    interface OnLongClickListener
    {
        fun setOnLongClickListener(calcedFood:CalcedFood,position:Int)
    }


    fun setOnLongClickListener(mLongListener: OnLongClickListener)
    {
        this.mLongListener = mLongListener
    }





    interface OnClickListener
    {
        fun setOnClickListener(calcedFood:CalcedFood,position:Int)
    }

    fun setOnClickListener(mListener:OnClickListener)
    {
        this.mListener = mListener
    }

}