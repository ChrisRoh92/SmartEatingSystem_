package de.rohnert.smarteatingsystem.frontend.foodtracker._archiv.foodchooser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.databases.daily_database.helper.CalcedFood

class HistoricFoodChooserAdapter(var content:ArrayList<ArrayList<ArrayList<CalcedFood>>>, var dates:ArrayList<String>):
    RecyclerView.Adapter<HistoricFoodChooserAdapter.ViewHolder>() {


    private var adapter:ArrayList<ArrayList<HistoricMealItemAdapter>> = ArrayList()
    private var status:Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_historicmeal_item,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int
    {
        for(i in content)
        {
            adapter.add(ArrayList())
        }
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.tvTitle.text = dates[position]

        // Buttons mit Listener:
        /*holder.btnDaily.setOnClickListener{
            for(i in adapter[holder.adapterPosition])
            {
                if(status)
                {
                    i.changeAllButtonsToNotChecked()
                    status = false
                }
                else
                {
                    i.changeAllButtonsToChecked()
                    status = true
                }

            }
        }*/
        holder.btnBreakfast.setOnClickListener{
            adapter[holder.adapterPosition][0].changeAllButtons()
        }
        holder.btnLunch.setOnClickListener{
            adapter[holder.adapterPosition][1].changeAllButtons()
        }
        holder.btnDinner.setOnClickListener{
            adapter[holder.adapterPosition][2].changeAllButtons()
        }
        holder.btnSnack.setOnClickListener{
            adapter[holder.adapterPosition][3].changeAllButtons()
        }

        // recyclerviews initialisieren:

        createRecyclerView(holder.rvBreakfast,holder.adapterPosition,0,holder.adapterPosition)
        createRecyclerView(holder.rvLunch,holder.adapterPosition,1,holder.adapterPosition)
        createRecyclerView(holder.rvDinner,holder.adapterPosition,2,holder.adapterPosition)
        createRecyclerView(holder.rvSnack,holder.adapterPosition,3,holder.adapterPosition)
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        // Title
        var tvTitle:TextView = itemView.findViewById(R.id.item_tv_title)
        // Buttons:
        //var btnDaily: Button = itemView.findViewById(R.id.item_btn_daily)
        var btnBreakfast: Button = itemView.findViewById(R.id.item_btn_breakfast)
        var btnLunch: Button = itemView.findViewById(R.id.item_btn_lunch)
        var btnDinner: Button = itemView.findViewById(R.id.item_btn_dinner)
        var btnSnack: Button = itemView.findViewById(R.id.item_btn_snack)
        // RecyclerViews:
        var rvBreakfast:RecyclerView = itemView.findViewById(R.id.item_breakfast_rv)
        var rvLunch:RecyclerView = itemView.findViewById(R.id.item_lunch_rv)
        var rvDinner:RecyclerView = itemView.findViewById(R.id.item_dinner_rv)
        var rvSnack:RecyclerView = itemView.findViewById(R.id.item_snack_rv)

    }



    private fun createRecyclerView(rv:RecyclerView,pos:Int,mealpos:Int,adapterpos:Int)
    {
        var manager = LinearLayoutManager(rv.context,RecyclerView.VERTICAL,false)
        adapter[pos].add(HistoricMealItemAdapter(content[pos][mealpos]))
        rv.layoutManager = manager
        rv.adapter = adapter[pos][mealpos]

        // Listener hinzufügen...

    }











    // Getter Stuff:
    fun getCalcedFoods():ArrayList<CalcedFood>
    {
        var export:ArrayList<CalcedFood> = ArrayList()
        for(i in adapter)
        {
            for(j in i)
            {
                export.addAll(j.getCheckedCalcedFoods())
            }

        }

        return export
    }





}