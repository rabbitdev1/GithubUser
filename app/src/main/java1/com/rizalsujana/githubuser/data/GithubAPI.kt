package com.rizalsujana.githubuser.data
import com.rizalsujana.githubuser.ui.home.fragment.UserItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubAPI {
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<GithubUserResponse>

    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<DEtailUser>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<UserItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<UserItem>>
}