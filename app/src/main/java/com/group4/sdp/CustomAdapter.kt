package com.group4.sdp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val srno: ArrayList<Any>, private val bmi: ArrayList<Any>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_one, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView1.text = srno[position].toString()
        holder.textview4.text = bmi[position].toString()

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return bmi.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView1: TextView = itemView.findViewById(R.id.serial)
        val textview4: TextView = itemView.findViewById(R.id.bmi)
    }
}
