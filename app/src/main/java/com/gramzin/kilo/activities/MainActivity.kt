package com.gramzin.kilo.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gramzin.kilo.adapter.DialogAdapter
import com.gramzin.kilo.databinding.ActivityMainBinding
import com.gramzin.kilo.model.Dialog
import com.gramzin.kilo.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val key = data?.getStringExtra(AddDialog.ACTIVITY_RES)
            val intent = Intent(this, DialogActivity::class.java)
            intent.putExtra(DialogActivity.DIALOG_KEY, key)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dialogsRcView.layoutManager = LinearLayoutManager(this)
        binding.dialogsRcView.adapter = viewModel.adapter
        DialogAdapter.DialogHolder.onClick = ::onItemClick
        binding.addDialogBtn.setOnClickListener{
            val intent = Intent(this, AddDialog::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun onItemClick(dialog: Dialog){
        val intent = Intent(this, DialogActivity::class.java)
        intent.putExtra(DialogActivity.DIALOG_KEY, dialog.key)
        startActivity(intent)
    }
}