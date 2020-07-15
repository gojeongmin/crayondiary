package com.landvibe.photodiary

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MainRecyclerViewAdapter
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        attachButtonEvents()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadDiaries()
    }

    private fun loadDiaries() {
        adapter.items.clear()
        adapter.items.addAll(AppDatabase.instance.diaryDao().getAll())
        adapter.notifyDataSetChanged()
        if (adapter.items.isEmpty()) {
            main_empty_text.visibility = View.VISIBLE
        } else {
            main_empty_text.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        adapter = MainRecyclerViewAdapter(this)
        main_recycler.adapter = adapter
        layoutManager = GridLayoutManager(this, 2)
        main_recycler.layoutManager = layoutManager
    }

    private fun attachButtonEvents() {
        main_new_button.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
    }
}