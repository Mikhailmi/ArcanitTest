package com.arcanit.test.adaptersAndSources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arcanit.test.R
import com.arcanit.test.data.User
import com.arcanit.test.databinding.UserBinding
import com.bumptech.glide.Glide

class UserAdapter(
    private val onClick: (String?) -> Unit
) : PagingDataAdapter<User, UserViewHolder>(DiffUtilCallbackUser()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        val context = holder.itemView.context

        with(holder.binding) {
            Glide.with(context).load(user?.avatar_url)
                .error(R.drawable.cat)
                .placeholder(R.drawable.cat)
                .into(avatarUrl)
        }
        holder.binding.login.text = user?.login
        holder.binding.score.text = user?.score

        holder.binding.root.setOnClickListener {
            onClick(user?.html_url)
        }
    }
}

class UserViewHolder(val binding: UserBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackUser : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.avatar_url == newItem.avatar_url


    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.avatar_url == newItem.avatar_url

}