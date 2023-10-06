package com.rizalsujana.githubuser.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rizalsujana.githubuser.PreferenceUtil
import com.rizalsujana.githubuser.R
import com.rizalsujana.githubuser.data.GithubUserResponse
import com.rizalsujana.githubuser.data.RetrofitClient
import com.rizalsujana.githubuser.databinding.FragmentHomeBinding
import com.rizalsujana.githubuser.ui.favorit.FavoritActivity
import com.rizalsujana.githubuser.ui.home.fragment.SearchAdapter
import com.rizalsujana.githubuser.ui.home.fragment.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")

    private lateinit var adapter: SearchAdapter
    private val userList: MutableList<UserItem> = mutableListOf()
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerView)
        val searchView =
            binding.topAppBar.menu.findItem(R.id.menu_search).actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Cari Pengguna Github..."

        if (userList.isEmpty()) {
            binding.imageNoData.visibility = View.VISIBLE
        }
        adapter = SearchAdapter(userList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        loadingProgressBar = binding.root.findViewById(R.id.loadingProgressBar)

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchUsers(it)
                    hideSoftKeyboard(searchView)
                    searchView.setQuery("", false)
                    searchView.clearFocus()
                    loadingProgressBar.visibility = View.VISIBLE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        val isCurrentlyDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val darkModeMenuItem = binding.topAppBar.menu.findItem(R.id.menu_dark_mode)
        darkModeMenuItem.setIcon(if (isCurrentlyDarkMode) R.drawable.baseline_dark_mode_24 else R.drawable.baseline_light_mode_24)
        val isDarkModeEnabled = PreferenceUtil.isDarkModeEnabled(requireContext())

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_favorite -> {
                    val intent = Intent(activity, FavoritActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_dark_mode -> {

                    if (isDarkModeEnabled) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }

                    darkModeMenuItem.setIcon(if (isDarkModeEnabled) R.drawable.baseline_light_mode_24 else R.drawable.baseline_dark_mode_24)
                    PreferenceUtil.toggleDarkMode(requireContext())
                    true
                }
                else -> false
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun fetchUsers(query: String) {
        RetrofitClient.instance.searchUsers(query)
            .enqueue(object : Callback<GithubUserResponse> {
                override fun onResponse(
                    call: Call<GithubUserResponse>,
                    response: Response<GithubUserResponse>
                ) {
                    val users = response.body()?.items
                    adapter.updateUsers(users ?: emptyList())
                    if (users.isNullOrEmpty()) {
                        binding.imageNoData.visibility = View.VISIBLE
                    } else {
                        binding.imageNoData.visibility = View.GONE
                    }
                    loadingProgressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                    loadingProgressBar.visibility = View.GONE
                    val errorMessage = "Terjadi kesalahan: " + t.message
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun hideSoftKeyboard(view: View) {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
