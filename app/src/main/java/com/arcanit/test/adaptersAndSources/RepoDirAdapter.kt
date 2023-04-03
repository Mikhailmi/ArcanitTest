package com.arcanit.test.adaptersAndSources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arcanit.test.data.DirectoryData
import com.arcanit.test.databinding.DirectoryBinding

class RepoDirAdapter(
    private val onClick: (String?) -> Unit
) : ListAdapter<DirectoryData, RepoDirViewHolder>(DiffUtilCallbackRepoDir()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoDirViewHolder {
        return RepoDirViewHolder(
            DirectoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RepoDirViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            name.text = item?.name
        }
        holder.binding.root.setOnClickListener {
            val url = (item?._links as Map<*,*>)[SELF] as String
            onClick(url)
        }
    }

    companion object {
        const val SELF = "self"
    }
}

class RepoDirViewHolder(val binding: DirectoryBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackRepoDir : DiffUtil.ItemCallback<DirectoryData>() {
    override fun areItemsTheSame(
        oldItem: DirectoryData,
        newItem: DirectoryData
    ): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(
        oldItem: DirectoryData,
        newItem: DirectoryData
    ): Boolean = oldItem == newItem
}