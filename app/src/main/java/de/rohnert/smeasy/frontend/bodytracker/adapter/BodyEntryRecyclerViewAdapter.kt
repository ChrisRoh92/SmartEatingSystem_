package de.rohnert.smeasy.frontend.bodytracker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel


class BodyEntryRecyclerViewAdapter(var content: ArrayList<Body>):
    RecyclerView.Adapter<BodyEntryRecyclerViewAdapter.ViewHolder>() {

    private var helper = Helper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_bodyentry, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDate.text = "vom ${content[position].date}"
        holder.tvWeight.text = "${helper.getFloatAsFormattedString(content[position].weight,"#.#")} kg"
        holder.imageBtn.setOnClickListener {
            Log.d("Smeasy","BodyEntryRecyclerViewAdapter imageButton Click Listener")
        }
    }


    fun updateContent(content: ArrayList<Body>)
    {
        this.content = content
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var imageBtn:ImageButton = itemView.findViewById(R.id.recycleritem_bodyentry_btn)
        var tvDate:TextView = itemView.findViewById(R.id.recycleritem_bodyentry_tvDate)
        var tvWeight:TextView = itemView.findViewById(R.id.recycleritem_bodyentry_tvWeight)

    }
}