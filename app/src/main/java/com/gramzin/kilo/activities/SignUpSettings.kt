package com.gramzin.kilo.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.gramzin.kilo.databinding.ActivitySignUpSettingsBinding

class SignUpSettings : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpSettingsBinding

    private var selectedImage: Uri? = null
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImage = uri
            binding.avatarImage.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.avatarImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.saveSettingBtn.setOnClickListener {
            val intent = Intent()
            intent.data = selectedImage
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}