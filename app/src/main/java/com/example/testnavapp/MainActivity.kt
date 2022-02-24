package com.example.testnavapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.example.testnavapp.databinding.ActivityMainBinding
import com.example.testnavapp.ui.DataStoreActivity
import com.google.android.material.badge.BadgeDrawable

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var alarmsBadge: BadgeDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.navigationRail.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.alarms -> {
                    alarmsBadge = binding.navigationRail.getOrCreateBadge(R.id.alarms)
                    alarmsBadge.isVisible = true
                    alarmsBadge.number = alarmsBadge.number + 1
                    true
                }
                R.id.schedule -> true
                else -> false
            }
        }

        binding.btDataStore.setOnClickListener {
            val intent = Intent(this, DataStoreActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "onCreateOptionsMenu")
        return super.onCreateOptionsMenu(menu)
    }
}