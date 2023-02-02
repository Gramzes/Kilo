package com.gramzin.kilo.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gramzin.kilo.databinding.ActivityAddDialogBinding
import com.gramzin.kilo.model.Dialog
import com.gramzin.kilo.viewmodel.AddDialogViewModel

class AddDialog : AppCompatActivity() {
    companion object{
        const val ACTIVITY_RES = "ACTIVITY_RES"
    }
    private lateinit var binding: ActivityAddDialogBinding
    private val viewModel: AddDialogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.onClick = ::onItemClick
        binding.addDialogRcView.adapter = viewModel.adapter
        binding.addDialogRcView.layoutManager = LinearLayoutManager(this)
    }

    fun onItemClick(dialog: Dialog){
        val intent = Intent()
        intent.putExtra(ACTIVITY_RES, dialog.key)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}