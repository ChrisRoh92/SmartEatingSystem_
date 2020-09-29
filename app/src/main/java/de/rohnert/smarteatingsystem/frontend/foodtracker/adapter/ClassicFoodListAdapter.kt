package de.rohnert.smarteatingsystem.frontend.foodtracker.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.favourite_foods.FavFood
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences

class ClassicFoodListAdapter(var content:ArrayList<ExtendedFood>, var favContent:ArrayList<FavFood>, var context: Context): RecyclerView.Adapter<ClassicFoodListAdapter.ViewHolder>() {


    private var helper = Helper()
    private var prefs = SharedAppPreferences(context)

    // Interface:
    lateinit var mListener: OnClickListener
    lateinit var mCheckedListener:OnCheckedChangedListener
    lateinit var mSimpleListener: OnSimpleClickListener

    /////////////////////////////////////
    // Override Methods:
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
        holder.tvMarke.text = if(food.marken!="") "von ${food.marken}" else " - "


        // Hier muss geprüft werden, ob das Lebensmittel ok ist oder nicht...
        if(checkIfFoodIsAllowed(food))
        {
            //holder.icon.setImageDrawable(ContextCompat.getDrawable((holder.icon.context),R.drawable.ic_check_dark))
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);


        }
        else
        {
            //holder.icon.setImageDrawable(ContextCompat.getDrawable((holder.icon.context),R.drawable.ic_false_dark))
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_false_dark, 0, 0, 0);
        }

        // ToggleButton
        holder.favButton.isChecked = checkIfFoodIsFavourite(food)
        holder.favButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(mCheckedListener!=null) {
                mCheckedListener.setOnCheckedChangeListener(content[holder.adapterPosition],isChecked)
            }
        }

        // SimpleAddButton:
        holder.btnSimpleAdd.setOnClickListener {
            mSimpleListener?.setOnSimpleClickListener(content[holder.adapterPosition],holder.adapterPosition)
        }





        holder.itemView.setOnClickListener {
            mListener?.setOnClickListener(content[holder.adapterPosition],holder.adapterPosition)
        }
    }


    /////////////////////////////////////
    // Internal Methods:
    private fun checkIfFoodIsFavourite(food:ExtendedFood):Boolean{
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

    private fun checkIfFoodIsAllowed(food:ExtendedFood):Boolean{
        var status = true

        // Prüfen ob im Kcal Bereich:
        if(prefs.maxAllowedKcal == -1f)
        {
            if(food.kcal >= prefs.minAllowedKcal)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        else
        {
            if(food.kcal >= prefs.minAllowedKcal && food.kcal <= prefs.maxAllowedKcal)
            {

            }
            else
            {
                status = false
                return status
            }
        }

        // Prüfen ob im Kcal Bereich:
        if(prefs.maxAllowedCarbs == -1f)
        {
            if(food.carb >= prefs.minAllowedCarbs)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        else
        {
            if(food.carb > prefs.minAllowedCarbs && food.carb < prefs.maxAllowedCarbs)
            {

            }
            else
            {
                status = false
                return status
            }
        }

        // Prüfen ob im Kcal Bereich:
        if(prefs.maxAllowedProtein == -1f)
        {
            if(food.protein >= prefs.minAllowedProtein)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        else
        {
            if(food.protein > prefs.minAllowedProtein && food.protein < prefs.maxAllowedProtein)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        // Prüfen ob im Kcal Bereich:
        if(prefs.maxAllowedFett == -1f)
        {
            if(food.fett >= prefs.minAllowedFett)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        else
        {
            if(food.fett > prefs.minAllowedFett && food.fett < prefs.maxAllowedFett)
            {

            }
            else
            {
                status = false
                return status
            }
        }





        return status
    }


    /////////////////////////////////////
    // External Methods:
    fun updateContent(content:ArrayList<ExtendedFood>,favContent: ArrayList<FavFood>)
    {
        this.content = content
        this.favContent = favContent
        notifyDataSetChanged()
    }



    /////////////////////////////////////
    // ViewHolder:
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.mealcard_item_name)
        var tvGroup: TextView = itemView.findViewById(R.id.mealcard_item_group)
        var tvMarke:TextView = itemView.findViewById(R.id.mealcard_item_marke)
        var tvKcal: TextView = itemView.findViewById(R.id.mealcard_item_kcal)
        var btnSimpleAdd: ImageView = itemView.findViewById(R.id.mealcard_item_btn_simple)
        var favButton:ToggleButton = itemView.findViewById(R.id.button_favorite)


    }


    /////////////////////////////////////
    // Interfaces:
    interface OnClickListener {
        fun setOnClickListener(food: ExtendedFood, position: Int)
    }
    fun setOnClickListener(mListener: OnClickListener) {
        this.mListener = mListener
    }


    interface OnCheckedChangedListener{
        fun setOnCheckedChangeListener(food:ExtendedFood,buttonState:Boolean)
    }
    fun setOnCheckedChangeListener(mCheckedListener:OnCheckedChangedListener){
        this.mCheckedListener = mCheckedListener
    }

    // Interfaces:
    interface OnSimpleClickListener {
        fun setOnSimpleClickListener(food: ExtendedFood, position: Int)
    }
    fun setOnSimpleClickListener(mSimpleListener: OnSimpleClickListener) {
        this.mSimpleListener = mSimpleListener
    }



}