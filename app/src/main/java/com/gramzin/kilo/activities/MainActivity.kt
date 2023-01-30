package com.gramzin.kilo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gramzin.kilo.MessageAdapter
import com.gramzin.kilo.R
import com.gramzin.kilo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = MessageAdapter()
        binding.messagesRcView.layoutManager = LinearLayoutManager(this)
        binding.messagesRcView.adapter = adapter
    }
}