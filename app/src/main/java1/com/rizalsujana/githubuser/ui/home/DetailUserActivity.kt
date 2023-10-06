package com.rizalsujana.githubuser.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rizalsujana.githubuser.R
import com.rizalsujana.githubuser.data.DEtailUser
import com.rizalsujana.githubuser.data.GithubAPI
import com.rizalsujana.githubuser.data.RetrofitClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailUserActivity : AppCompatActivity() {
    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val textFullname: TextView = findViewById(R.id.fullname)
        val textidunik: TextView = findViewById(R.id.idunik)
        val imageView: ImageView = findViewById(R.id.imageView)
        val textfollower: TextView = findViewById(R.id.follower)
        val textfollowing: TextView = findViewById(R.id.following)
        val loadingBar: ProgressBar = findViewById(R.id.loading_bar)


        setSupportActionBar(toolbar)
        val username = intent.getStringExtra("username")
        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadingBar.visibility = View.VISIBLE

        // Make the API call
        RetrofitClient.instance.getUser(username!!).enqueue(object : Callback<DEtailUser> {
            override fun onResponse(call: Call<DEtailUser>, response: Response<DEtailUser>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    textFullname.text = "${user?.name}(${user?.login})"
                    textidunik.text = "ID: ${user?.id}"
                    textfollower.text = "Follower: ${user?.followers} User"
                    textfollowing.text = "Following: ${user?.following} User"
                    Picasso.get().load(user?.avatar_url).into(imageView)
                }
                loadingBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<DEtailUser>, t: Throwable) {
                loadingBar.visibility = View.GONE
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this,username)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
