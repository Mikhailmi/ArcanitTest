package com.arcanit.test.adaptersAndSources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arcanit.test.R
import com.arcanit.test.data.Repo
import com.arcanit.test.databinding.RepoBinding

class RepoAdapter(
    private val onClick: (String?) -> Unit
) : PagingDataAdapter<Repo, RepoViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoBinding.inflate(inflater, parent, false)

        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = getItem(position)
        holder.binding.name.text = repo?.name
        holder.binding.description.text = repo?.description
        val forksCount = repo?.forks_count.toString() + "\n" + R.string.forks
        holder.binding.forksCount.text = forksCount

        holder.binding.root.setOnClickListener {
            onClick(repo?.contents_url)
        }
    }
}

class RepoViewHolder(val binding: RepoBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
        oldItem == newItem
}