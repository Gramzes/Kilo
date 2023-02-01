package com.gramzin.kilo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gramzin.kilo.databinding.ActivityDialogBinding
import com.gramzin.kilo.model.Message
import com.gramzin.kilo.viewmodel.MainViewModel

class DialogActivity : AppCompatActivity() {

    lateinit var binding: ActivityDialogBinding

    val auth = Firebase.auth
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
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