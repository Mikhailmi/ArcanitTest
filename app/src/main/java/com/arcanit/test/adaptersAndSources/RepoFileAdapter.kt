package com.arcanit.test.adaptersAndSources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arcanit.test.data.DirectoryData
import com.arcanit.test.databinding.FileBinding

class RepoFileAdapter(
    private val onClick: (String?) -> Unit
) : ListAdapter<DirectoryData, RepoFileViewHolder>(DiffUtilCallbackRepoFile()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoFileViewHolder {
        return RepoFileViewHolder(
            FileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RepoFileViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            name.text = item?.name
        }
        holder.binding.root.setOnClickListener {
            onClick(item?.html_url)
        }
    }
}

class RepoFileViewHolder(val binding: FileBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackRepoFile : DiffUtil.ItemCallback<DirectoryData>() {
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