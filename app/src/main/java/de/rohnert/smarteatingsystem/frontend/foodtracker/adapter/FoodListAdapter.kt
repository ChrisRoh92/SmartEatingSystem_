package de.rohnert.smarteatingsystem.frontend.foodtracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.Food
import de.rohnert.smarteatingsystem.R
import kotlin.random.Random

class FoodListAdapter(var meal:String): ListAdapter<Food, FoodListAdapter.FoodListHolder>(DIFF_CALLBACK) {


    private var helper = Helper()

    // Interface:
    lateinit var mLongListener: OnLongClickListener
    lateinit var mListener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_foodlist, parent, false)
        return FoodListHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodListHolder, position: Int) {
        val food = getItem(position)
        holder.tvName.text = food.name
        holder.tvGroup.text = food.category
        holder.tvKcal.text = "${helper.getFloatAsFormattedString(food.kcal,"#.#")} kcal"








        // Click Listener.....
        holder.itemView.setOnLongClickListener {
            if (mLongListener != null) {

                mLongListener.setOnLongClickListener(
                    (getCalcedFoodAt(holder.adapterPosition)),
                    holder.adapterPosition,meal
                )

            }
            true
        }

        holder.itemView.setOnClickListener {
            if (mListener != null) {
                mListener.setOnClickListener(
                    getCalcedFoodAt(holder.adapterPosition),
                    holder.adapterPosition,meal
                )
            }
        }

    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Food>() {
            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
                return false
            }
        }
    }


    fun getCalcedFoodAt(pos: Int): Food {
        return getItem(pos)
    }

    class FoodListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.mealcard_item_name)
        var tvGroup: TextView = itemView.findViewById(R.id.mealcard_item_group)
        var tvKcal: TextView = itemView.findViewById(R.id.mealcard_item_kcal)
        //var icon: ImageView = itemView.findViewById(R.id.mealcard_item_icon)


    }

    interface OnLongClickListener {
        fun setOnLongClickListener(food: Food, position: Int, meal:String)
    }


    fun setOnLongClickListener(mLongListener: OnLongClickListener) {
        this.mLongListener = mLongListener
    }


    interface OnClickListener {
        fun setOnClickListener(food: Food, position: Int, meal:String)
    }

    fun setOnClickListener(mListener: OnClickListener) {
        this.mListener = mListener
    }





}

