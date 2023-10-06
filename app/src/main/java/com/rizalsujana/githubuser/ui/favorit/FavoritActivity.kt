package com.rizalsujana.githubuser.ui.favorit

import DBHelper
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.rizalsujana.githubuser.R
import com.rizalsujana.githubuser.databinding.ActivityFavoritBinding
import com.rizalsujana.githubuser.ui.home.FollowAdapter
import com.rizalsujana.githubuser.ui.home.fragment.UserItem

class FavoritActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritBinding
    private lateinit var adapter: FollowAdapter
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            // Aksi yang diambil ketika tombol kembali diklik
            onBackPressed() // Ini akan kembali ke tampilan sebelumnya
        }

        adapter = FollowAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        loadingProgressBar = binding.root.findViewById(R.id.loadingProgressBar)



        val dbHelper = DBHelper(this)
//        insertDummyData(dbHelper)
        displayData()
        loadingProgressBar.visibility = View.VISIBLE
    }
    override fun onResume() {
        super.onResume()
        displayData()
    }

    private fun insertDummyData(dbHelper: DBHelper) {
        val users = dbHelper.writableDatabase
         users.delete(DBHelper.TABLE_NAME, null, null)

//         Insert data
         val valuesJohnDoe = ContentValues()
         valuesJohnDoe.put(DBHelper.COLUMN_LOGIN, "jaguar")
         valuesJohnDoe.put(DBHelper.COLUMN_AVATAR_URL, "https://example.com/jaguar.jpg")

         users.insert(DBHelper.TABLE_NAME, null, valuesJohnDoe)
         users.close()
    }

    private fun displayData() {
        val dbHelper = DBHelper(this)
        val users = dbHelper.readableDatabase
        val cursor = users.query(
            DBHelper.TABLE_NAME,
            arrayOf(DBHelper.COLUMN_ID, DBHelper.COLUMN_LOGIN, DBHelper.COLUMN_AVATAR_URL),
            null, null, null, null, null
        )
        val userList = mutableListOf<UserItem>()

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID)).toString()
            val login = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_LOGIN))
            val avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_AVATAR_URL))

            val user = UserItem(id = id, login = login, avatar_url = avatarUrl)
            userList.add(user)
        }
        cursor.close()
        users.close()

        if (userList.isEmpty()) {
            binding.imageNoData.visibility = View.VISIBLE
        } else {
            binding.imageNoData.visibility = View.GONE
        }
        loadingProgressBar.visibility = View.GONE

        adapter.clearData()
        adapter.addData(userList)

        adapter.notifyDataSetChanged()
    }

}