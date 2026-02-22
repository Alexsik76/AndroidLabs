package com.example.lb1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(),
    InputFragment.OnDataPassedListener,
    OutputFragment.OnCancelListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InputFragment())
                .commit()
        }
    }

    override fun onDataPassed(text: String) {
        val outputFragment = OutputFragment()

        val bundle = Bundle()
        bundle.putString("RESULT_TEXT", text)
        outputFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, outputFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCancelClicked() {
        supportFragmentManager.popBackStack()

        supportFragmentManager.executePendingTransactions()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is InputFragment) {
            currentFragment.clearForm()
        }
    }
}