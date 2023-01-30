package com.gramzin.kilo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gramzin.kilo.R
import com.gramzin.kilo.databinding.ActivityStartBinding
import com.gramzin.kilo.viewmodel.StartActivityViewModel

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private val viewModel: StartActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            viewModel.signInState.value = !viewModel.signInState.value!!
        }

        initUI()
        viewModel.signInState.observe(this, onChangeState)

        binding.passEditText.setOnKeyListener { view, i, keyEvent ->
            viewModel.password = (view as EditText).text.toString()
            true
        }
        binding.nameEditText.setOnKeyListener { view, i, keyEvent ->
            viewModel.name = (view as EditText).text.toString()
            true
        }
        binding.emailEditText.setOnKeyListener { view, i, keyEvent ->
            viewModel.email = (view as EditText).text.toString()
            true
        }
        binding.passRepeatEditText.setOnKeyListener { view, i, keyEvent ->
            viewModel.repeatPassword = (view as EditText).text.toString()
            true
        }
    }

    private val onChangeState = Observer<Boolean> {
        with(binding) {
            if (it) {
                nameEditText.visibility = View.GONE
                passRepeatEditText.visibility = View.GONE
                signInBtn.text = resources.getString(R.string.sign_in)
                signUpBtn.text = resources.getString(R.string.sign_up)
            } else {
                nameEditText.visibility = View.VISIBLE
                passRepeatEditText.visibility = View.VISIBLE
                signInBtn.text = resources.getString(R.string.sign_up)
                signUpBtn.text = resources.getString(R.string.sign_in)
            }
        }
    }

    fun initUI() = with(binding){
        emailEditText.setText(viewModel.email)
        nameEditText.setText(viewModel.name)
        passEditText.setText(viewModel.password)
        passRepeatEditText.setText(viewModel.repeatPassword)
    }
}
