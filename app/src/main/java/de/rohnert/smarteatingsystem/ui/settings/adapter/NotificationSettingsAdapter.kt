package de.rohnert.smarteatingsystem.ui.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R

class NotificationSettingsAdapter(var content:ArrayList<String>, var subContent:ArrayList<String>, var checkContent:ArrayList<Boolean>):
    RecyclerView.Adapter<NotificationSettingsAdapter.ViewHolder>() {

    // Interface:
    private lateinit var mListener:OnNotificationSettingsClickListener
    private lateinit var mCheckBoxListener:OnNotificationCheckBoxListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_settings_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = content[position]
        holder.tvSubTitle.text = subContent[position]
        holder.checkBox.isChecked = checkContent[position]

        holder.itemView.setOnClickListener {
            if(mListener!=null)
            {
                mListener.setOnNotificationSettingsClickListener(holder.adapterPosition)
            }
        }

        holder.checkBox.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(mCheckBoxListener!=null)
                {
                    mCheckBoxListener.setOnNotificationCheckBoxListener(holder.adapterPosition)
                }
            }

        })
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle: TextView = itemView.findViewById(R.id.settings_item_notification_title)
        var tvSubTitle: TextView = itemView.findViewById(R.id.settings_item_notification_subtitle)
        var checkBox:CheckBox = itemView.findViewById(R.id.settings_item_notification_checkbox)

    }


    interface OnNotificationSettingsClickListener
    {
        fun setOnNotificationSettingsClickListener(pos:Int)
    }

    fun setOnNotificationSettingsClickListener(mListener:OnNotificationSettingsClickListener)
    {
        this.mListener = mListener
    }

    interface OnNotificationCheckBoxListener
    {
        fun setOnNotificationCheckBoxListener(pos:Int)
    }

    fun setOnNotificationCheckBoxListener(mCheckBoxListener:OnNotificationCheckBoxListener)
    {
        this.mCheckBoxListener = mCheckBoxListener
    }
}