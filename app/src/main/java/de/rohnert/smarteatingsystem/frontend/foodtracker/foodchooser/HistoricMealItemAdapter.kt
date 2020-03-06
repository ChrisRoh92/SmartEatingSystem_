package de.rohnert.smarteatingsystem.frontend.foodtracker.foodchooser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smarteatingsystem.backend.helper.Helper



class HistoricMealItemAdapter(var content:ArrayList<CalcedFood>):
    RecyclerView.Adapter<HistoricMealItemAdapter.ViewHolder>()
{
    private var helper = Helper()
    private var activList:ArrayList<Boolean> = ArrayList()

    init {
        for(i in content)
            activList.add(false)
    }
    // Interface
    //private lateinit var mListener:OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_historicmeal_entries,parent,false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cf = content[position]
        holder.tvName.text = cf.f.name
        holder.tvGruppe.text = cf.f.category
        if(cf.f.unit.contains("g"))
        {
            holder.tvMenge.text = "${helper.getFloatAsFormattedStringWithPattern(cf.menge,"#")} g"
        }
        else
        {
            holder.tvMenge.text = "${helper.getFloatAsFormattedStringWithPattern(cf.menge,"#")} ml"
        }
        holder.tvKcal.text = "${helper.getFloatAsFormattedStringWithPattern(cf.values[0],"#")} kcal"

        // RadioButton
        holder.rbtn.isChecked = activList[position]

        // Item:
        holder.itemView.setOnClickListener{
            // Interface
            changeButtonState(holder.rbtn,holder.adapterPosition)
            /*if(mListener!=null)
            {
                mListener.setOnItemClickListener(content[holder.adapterPosition],holder.rbtn.isChecked)
            }*/
        }


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // texte
        var tvName: TextView = itemView.findViewById(R.id.item_tv_name)
        var tvGruppe: TextView = itemView.findViewById(R.id.item_tv_gruppe)
        var tvMenge: TextView = itemView.findViewById(R.id.item_tv_menge)
        var tvKcal: TextView = itemView.findViewById(R.id.item_tv_kcal)
        // RadioButton:

        var rbtn:RadioButton = itemView.findViewById(R.id.item_rbtn)

    }


    private fun changeButtonState(rbtn:RadioButton,pos:Int)
    {


        rbtn.isChecked = !rbtn.isChecked
        activList[pos] = rbtn.isChecked


    }

    // Alle Einträge auf positiv/negativ setzen (MealcardButton)
    fun changeAllButtons()
    {
        // Abchecken wieviele Buttons aktiv sind
        fun getCheckedButtonNumber():Int
        {
            var value = 0
            for(i in activList)
            {
                if(i == true)
                {
                    value++
                }
            }
            return value
        }

        fun setActivList(status:Boolean)
        {
            activList = ArrayList()
            for(i in content)
            {
                activList.add(status)
            }
        }
        var value = getCheckedButtonNumber()
        if(value == activList.size)
        {
            setActivList(false)
        }
        else if(value == 0)
        {
            setActivList(true)
        }
        else
        {
            setActivList(false)
        }
        notifyDataSetChanged()


    }

    fun changeAllButtonsToChecked()
    {
        activList = ArrayList()
        for(i in content)
        {
            activList.add(true)
        }
        notifyDataSetChanged()
    }

    fun changeAllButtonsToNotChecked()
    {
        activList = ArrayList()
        for(i in content)
        {
            activList.add(false)
        }
        notifyDataSetChanged()
    }


    /*// Interface:
    interface OnItemClickListener
    {
        fun setOnItemClickListener(cf:CalcedFood,buttonState:Boolean)
    }

    fun setOnItemClickListener(mListener:OnItemClickListener)
    {
        this.mListener = mListener
    }*/

    // Gebe die ausgewählten Werte zurück:
    fun getCheckedCalcedFoods():ArrayList<CalcedFood>
    {
        var export:ArrayList<CalcedFood> = ArrayList()
        for((index,i) in content.withIndex())
        {
            if(activList[index])
            {
                export.add(i)
            }
        }
        return export
    }




}