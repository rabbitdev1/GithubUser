package com.rizalsujana.githubuser.ui.home
import DBHelper
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.rizalsujana.githubuser.R
import com.rizalsujana.githubuser.data.DEtailUser
import com.rizalsujana.githubuser.data.RetrofitClient
import com.rizalsujana.githubuser.databinding.ActivityDetailUserBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private var isFavorite = false
    private lateinit var username: String
    private var user: DEtailUser? = null

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        username = intent.getStringExtra("username")!!
        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.loadingBar.visibility = View.VISIBLE

        // Make the API call
        RetrofitClient.instance.getUser(username).enqueue(object : Callback<DEtailUser> {
            override fun onResponse(call: Call<DEtailUser>, response: Response<DEtailUser>) {
                if (response.isSuccessful) {
                    user = response.body()
                    binding.fullname.text = "${user?.name}(${user?.login})"
                    binding.idunik.text = "ID: ${user?.id}"
                    binding.follower.text = "Follower: ${user?.followers} User"
                    binding.following.text = "Following: ${user?.following} User"
                    Picasso.get().load(user?.avatar_url).into(binding.imageView)

                    // Set status favorit berdasarkan data yang ada di database
                    isFavorite = isUserFavorite(username)
                    updateFavoriteButtonState()
                }
                binding.loadingBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<DEtailUser>, t: Throwable) {
                binding.loadingBar.visibility = View.GONE
            }
        })

        // Menghubungkan tombol favorit dengan fungsi klik
        binding.favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteButtonState()

            val dbHelper = DBHelper(this)

            if (isFavorite) {
                Log.d("DetailUserActivity", "Tombol Favorit Diklik")
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()

                val db = dbHelper.writableDatabase
                val values = ContentValues()
                values.put(DBHelper.COLUMN_LOGIN, username)
                values.put(DBHelper.COLUMN_AVATAR_URL, user?.avatar_url)
                db.insert(DBHelper.TABLE_NAME, null, values)
                db.close()
            } else {
                Log.d("DetailUserActivity", "Tombol Non Favorit Diklik")
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()

                val db = dbHelper.writableDatabase
                db.delete(DBHelper.TABLE_NAME, "${DBHelper.COLUMN_LOGIN}=?", arrayOf(username))
                db.close()
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun isUserFavorite(username: String): Boolean {
        val dbHelper = DBHelper(this)
        val db = dbHelper.readableDatabase

        val selection = "${DBHelper.COLUMN_LOGIN} = ?"
        val selectionArgs = arrayOf(username)

        val cursor = db.query(
            DBHelper.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val isFavorite = cursor.count > 0
        cursor.close()
        db.close()

        return isFavorite
    }

    private fun updateFavoriteButtonState() {
        if (isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
