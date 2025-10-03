package com.github.joechung2008.diagnostics_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KeyValueAdapter(private val data: Map<String, String>) :
    RecyclerView.Adapter<KeyValueAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val keyText: TextView = view.findViewById(R.id.key_text)
        val valueText: TextView = view.findViewById(R.id.value_text)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_key_value, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val key = data.keys.elementAt(position)
        val value = data[key]
        viewHolder.keyText.text = key
        viewHolder.valueText.text = value
    }

    override fun getItemCount() = data.size
}
