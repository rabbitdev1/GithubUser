package com.rizalsujana.githubuser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizalsujana.githubuser.data.RetrofitClient
import com.rizalsujana.githubuser.databinding.FragmentFollowBinding
import com.rizalsujana.githubuser.ui.home.fragment.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {

    private var username: String? = null
    private var position: Int? = null
    private var binding: FragmentFollowBinding? = null
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        loadingProgressBar = binding?.loadingProgressBar2!!
        loadingProgressBar.visibility = View.VISIBLE

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        fetchData()
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_POSITION = "arg_position"
    }

    private fun fetchData() {
        if (position == 1) {
            RetrofitClient.instance.getFollowers(username!!)
                .enqueue(object : Callback<List<UserItem>> {
                    override fun onResponse(
                        call: Call<List<UserItem>>,
                        response: Response<List<UserItem>>
                    ) {
                        if (response.isSuccessful) {
                            val followers = response.body()
                            if (followers != null) {
                                displayData(followers as MutableList<UserItem>)
                            } else {
                                println(response)
                            }
                            loadingProgressBar.visibility = View.GONE
                        } else {
                            println(response)
                        }
                    }

                    override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        } else {
            RetrofitClient.instance.getFollowing(username!!)
                .enqueue(object : Callback<List<UserItem>> {
                    override fun onResponse(
                        call: Call<List<UserItem>>,
                        response: Response<List<UserItem>>
                    ) {
                        if (response.isSuccessful) {
                            val following = response.body()
                            if (following != null) {
                                displayData(following as MutableList<UserItem>)
                            } else {
                                println(following)
                            }
                            loadingProgressBar.visibility = View.GONE
                        } else {
                            println(response)
                        }
                    }

                    override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
    }
    private fun displayData(users: MutableList<UserItem> = mutableListOf()) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        val adapter = FollowAdapter(users)
        binding?.recyclerView?.adapter = adapter
    }


}
