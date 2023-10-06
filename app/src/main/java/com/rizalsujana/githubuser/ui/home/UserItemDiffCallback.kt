package com.rizalsujana.githubuser.ui.home
import androidx.recyclerview.widget.DiffUtil
import com.rizalsujana.githubuser.ui.home.fragment.UserItem

class UserItemDiffCallback(private val oldList: List<UserItem>, private val newList: List<UserItem>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int) =
        oldList[oldPosition].id == newList[newPosition].id

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) =
        oldList[oldPosition] == newList[newPosition]
}
