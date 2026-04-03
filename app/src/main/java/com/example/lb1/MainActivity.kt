package com.example.lb1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity(), InputFragment.OnDataPassedListener {

    private lateinit var btnShowStats: Button
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.app_name_display)
        setSupportActionBar(toolbar)

        btnShowStats = findViewById(R.id.btnShowStats)
        btnShowStats.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDataPassed(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}