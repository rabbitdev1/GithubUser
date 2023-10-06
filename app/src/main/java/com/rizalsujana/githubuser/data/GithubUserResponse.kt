package com.rizalsujana.githubuser.data

import com.rizalsujana.githubuser.ui.home.fragment.UserItem

data class GithubUserResponse(
    val total_count: Int,
    val items: List<UserItem>
)

data class DEtailUser(
    val name: String,
    val login:String,
    val id: Int,
    val avatar_url: String,
    val followers: String,
    val following: String,
)
