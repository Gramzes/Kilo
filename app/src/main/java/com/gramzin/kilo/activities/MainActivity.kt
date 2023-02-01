package com.gramzin.kilo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.gramzin.kilo.MessageAdapter
import com.gramzin.kilo.databinding.ActivityMainBinding
import com.gramzin.kilo.model.Message
import com.gramzin.kilo.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val auth = Firebase.auth
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.messageEditText.setText(viewModel.messageText)

        val adapter = viewModel.adapter
        binding.messagesRcView.layoutManager = LinearLayoutManager(this)
        binding.messagesRcView.adapter = adapter

        binding.sendMessageBtn.setOnClickListener{
            val text = binding.messageEditText.text.toString()
            val message = Message(text)
            viewModel.addMessage(message)
        }
    }


}