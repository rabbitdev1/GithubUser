package com.rizalsujana.githubuser.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.rizalsujana.githubuser.R
import androidx.recyclerview.widget.RecyclerView
import com.rizalsujana.githubuser.ui.home.fragment.UserItem
import com.squareup.picasso.Picasso

class FollowAdapter(private val users: MutableList<UserItem>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.username)
        val avatar: ImageView = view.findViewById(R.id.avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
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

    override fun getItemCount() = users.size

    fun updateUsers(newUsers: List<UserItem>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
}
