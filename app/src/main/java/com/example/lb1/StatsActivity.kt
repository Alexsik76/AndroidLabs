package com.example.lb1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.lb1.data.DatabaseManager

class StatsActivity : AppCompatActivity() {

    private lateinit var tvTotalCount: TextView
    private lateinit var tvReadCount: TextView
    private lateinit var tvUnreadCount: TextView
    private lateinit var btnBack: Button
    private lateinit var dbManager: DatabaseManager
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.stats_header)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        dbManager = DatabaseManager(this)

        tvTotalCount = findViewById(R.id.tvTotalCount)
        tvReadCount = findViewById(R.id.tvReadCount)
        tvUnreadCount = findViewById(R.id.tvUnreadCount)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        updateStatistics()
    }

    private fun updateStatistics() {
        val allBooks = dbManager.getAllBooks()
        val total = allBooks.size
        val read = allBooks.count { it.isRead }
        val unread = total - read

        tvTotalCount.text = getString(R.string.stats_value_format, total)
        tvReadCount.text = getString(R.string.stats_value_format, read)
        tvUnreadCount.text = getString(R.string.stats_value_format, unread)
    }
}