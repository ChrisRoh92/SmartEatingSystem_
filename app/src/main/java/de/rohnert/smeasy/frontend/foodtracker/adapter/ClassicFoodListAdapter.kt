package de.rohnert.smeasy.frontend.foodtracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.databases.food_database.favourite_foods.FavFood
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import kotlin.random.Random

class ClassicFoodListAdapter(var content:ArrayList<Food>,var favContent:ArrayList<FavFood>, var context: Context): RecyclerView.Adapter<ClassicFoodListAdapter.ViewHolder>() {


    private var helper = Helper()
    private var sharePrefs = SharedAppPreferences(context)

    // Interface:
    lateinit var mLongListener: ClassicFoodListAdapter.OnLongClickListener
    lateinit var mListener: ClassicFoodListAdapter.OnClickListener
    lateinit var mCheckedListener:OnCheckedChangedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_foodlist, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = content[position]
        holder.tvName.text = food.name
        holder.tvGroup.text = food.category
        holder.tvKcal.text = "${helper.getFloatAsFormattedString(food.kcal,"#.#")} kcal"

        // Hüer muss geprüft werden, ob das Lebensmittel ok ist oder nicht...
        if(food.kcal < sharePrefs.maxAllowedKcal)
        {
            holder.icon.setImageDrawable(ContextCompat.getDrawable((holder.icon.context),R.drawable.ic_check_dark))


        }
        else
        {
            holder.icon.setImageDrawable(ContextCompat.getDrawable((holder.icon.context),R.drawable.ic_false_dark))

        }

        // ToggleButton
        holder.favButton.isChecked = checkIfFoodIsFavourite(food)
        holder.favButton.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener
        {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(mCheckedListener!=null)
                {
                    mCheckedListener.setOnCheckedChangeListener(content[holder.adapterPosition],isChecked)
                }

            }

        })



        holder.itemView.setOnClickListener {
            if (mListener != null) {
                mListener.setOnClickListener(content[holder.adapterPosition],holder.adapterPosition)
            }
        }
    }

    fun checkIfFoodIsFavourite(food:Food):Boolean
    {
        var check = false
        for(i in favContent)
        {
            if(i.id == food.id)
            {
                check = true
                break
            }
        }
        return check
    }

    fun updateContent(content:ArrayList<Food>)
    {
        this.content = content
        notifyDataSetChanged()
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.mealcard_item_name)
        var tvGroup: TextView = itemView.findViewById(R.id.mealcard_item_group)
        var tvKcal: TextView = itemView.findViewById(R.id.mealcard_item_kcal)
        var icon: ImageView = itemView.findViewById(R.id.mealcard_item_icon)
        var favButton:ToggleButton = itemView.findViewById(R.id.button_favorite)
    }


    interface OnLongClickListener {
        fun setOnLongClickListener(food: Food, position: Int)
    }


    fun setOnLongClickListener(mLongListener: OnLongClickListener) {
        this.mLongListener = mLongListener
    }


    interface OnClickListener {
        fun setOnClickListener(food: Food, position: Int)
    }

    fun setOnClickListener(mListener: OnClickListener) {
        this.mListener = mListener
    }

    interface OnCheckedChangedListener
    {
        fun setOnCheckedChangeListener(food:Food,buttonState:Boolean)
    }

    fun setOnCheckedChangeListener(mCheckedListener:OnCheckedChangedListener)
    {
        this.mCheckedListener = mCheckedListener
    }
}