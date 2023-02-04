package com.gramzin.kilo.activities

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gramzin.kilo.databinding.ActivityDialogBinding
import com.gramzin.kilo.model.Message
import com.gramzin.kilo.viewmodel.DialogModel
import com.gramzin.kilo.viewmodel.factory.DialogFactory


class DialogActivity : AppCompatActivity() {
    companion object{
        const val DIALOG_KEY = "DIALOG_KEY"
    }
    lateinit var binding: ActivityDialogBinding
    private lateinit var viewModel: DialogModel
    private var selectedImage: Uri? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val text = binding.messageEditText.text.toString()
            val message = Message(text)
            viewModel.addMessage(message, uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialogKey = intent.getStringExtra(DIALOG_KEY)
        viewModel = ViewModelProvider(this, DialogFactory(dialogKey!!))[DialogModel::class.java]

        binding.messageEditText.setText(viewModel.messageText)

        val adapter = viewModel.adapter
        binding.messagesRcView.layoutManager = LinearLayoutManager(this)
        binding.messagesRcView.adapter = adapter

        binding.sendMessageBtn.setOnClickListener{
            val text = binding.messageEditText.text.toString()
            val message = Message(text)
            viewModel.addMessage(message, null)
            binding.messageEditText.setText("")
        }

        binding.attachFileBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        viewModel.onMessageAdded = ::messageAdded
    }

    fun messageAdded(position: Int){
        binding.messagesRcView.smoothScrollToPosition(position)
    }
}