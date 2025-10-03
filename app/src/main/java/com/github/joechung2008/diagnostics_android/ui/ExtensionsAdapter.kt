package com.github.joechung2008.diagnostics_android.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.joechung2008.diagnostics_android.R
import com.github.joechung2008.diagnostics_android.model.Extension
import com.github.joechung2008.diagnostics_android.model.ExtensionError
import com.github.joechung2008.diagnostics_android.model.ExtensionInfo

class ExtensionsAdapter(
    private val onItemClick: (Extension) -> Unit
) : ListAdapter<Extension, ExtensionsAdapter.ExtensionViewHolder>(ExtensionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtensionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_extension, parent, false)
        return ExtensionViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ExtensionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExtensionViewHolder(
        itemView: View,
        private val onItemClick: (Extension) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.extension_name)
        private val errorTextView: TextView = itemView.findViewById(R.id.extension_error)

        fun bind(extension: Extension) {
            itemView.setOnClickListener { onItemClick(extension) }
            when (extension) {
                is ExtensionInfo -> {
                    nameTextView.text = extension.extensionName
                    errorTextView.visibility = View.GONE
                }
                is ExtensionError -> {
                    nameTextView.text = itemView.context.getString(R.string.extension_error_title)
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.text = extension.lastError.errorMessage
                }
            }
        }
    }
}

class ExtensionDiffCallback : DiffUtil.ItemCallback<Extension>() {
    override fun areItemsTheSame(oldItem: Extension, newItem: Extension): Boolean {
        return when {
            oldItem is ExtensionInfo && newItem is ExtensionInfo -> oldItem.extensionName == newItem.extensionName
            oldItem is ExtensionError && newItem is ExtensionError -> oldItem.lastError.time == newItem.lastError.time
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Extension, newItem: Extension): Boolean {
        return oldItem == newItem
    }
}
