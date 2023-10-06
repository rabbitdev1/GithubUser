package com.rizalsujana.githubuser.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rizalsujana.githubuser.databinding.UserItemBinding
import com.rizalsujana.githubuser.ui.home.fragment.UserItem
import com.squareup.picasso.Picasso

class FollowAdapter(private val users: MutableList<UserItem>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    class ViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.username
        val avatar = binding.avatar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.name.text = user.login

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailUserActivity::class.java)
            intent.putExtra("username", user.login)
            it.context.startActivity(intent)
        }

        Picasso.get().load(user.avatar_url).into(holder.avatar)
    }
    fun clearData() {
        users.clear()
        notifyDataSetChanged()
    }

    fun addData(data: List<UserItem>) {
        users.addAll(data)
        notifyDataSetChanged()
    }
    override fun getItemCount() = users.size
//    fun updateUsers(newUsers: List<UserItem>) {
//        users.clear()
//        users.addAll(newUsers)
//        notifyDataSetChanged()
//    }
}
