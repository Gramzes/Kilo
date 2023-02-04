package com.gramzin.kilo.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gramzin.kilo.R
import com.gramzin.kilo.databinding.ActivityStartBinding
import com.gramzin.kilo.model.User
import com.gramzin.kilo.viewmodel.StartActivityViewModel

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private val viewModel: StartActivityViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Вам будет выслано письмо для потверждения почты.", Toast.LENGTH_LONG).show()
            auth.currentUser!!.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful)
                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
            val data: Intent? = result.data
            val imageUri = data?.data
            if(imageUri!=null) {
                val storageRef = Firebase.storage.reference
                val avatarRef = storageRef.child("users").child(auth.uid!!).child("avatar.png")
                avatarRef.putFile(imageUri)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        if(checkIsUserSigned()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding.signUpBtn.setOnClickListener {
            viewModel.signInState.value = !viewModel.signInState.value!!
        }
        binding.signInBtn.setOnClickListener{pressSignInBtn()}

        initUI()
        viewModel.signInState.observe(this, onChangeState)
        setEditTextListeners()


    }

    fun setEditTextListeners() = with(binding){
        passEditText.setOnKeyListener { view, i, keyEvent ->
            viewModel.password = (view as EditText).text.toString()
            true
        }
        nameEditText.setOnKeyListener { view, i, keyEvent ->
            viewModel.name = (view as EditText).text.toString()
            true
        }
        emailEditText.setOnKeyListener { view, i, keyEvent ->
            viewModel.email = (view as EditText).text.toString()
            true
        }
        passRepeatEditText.setOnKeyListener { view, i, keyEvent ->
            viewModel.repeatPassword = (view as EditText).text.toString()
            true
        }
    }

    fun pressSignInBtn(){
        if(viewModel.signInState.value!!){
            signIn()
        }
        else{
            signUp()
        }
    }

    private fun signIn(){
        val email = binding.emailEditText.text.toString()
        val password = binding.passEditText.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if(auth.currentUser!!.isEmailVerified) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Вы не подтвердили email.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    fun signUp(){
        val email = binding.emailEditText.text.toString()
        val password = binding.passEditText.text.toString()
        val repeatPass = binding.passRepeatEditText.text.toString()
        val name = binding.nameEditText.text.toString()
        if (email.isEmpty() || password.isEmpty() || repeatPass.isEmpty() || name.isEmpty()){
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            return
        }
        if(password != repeatPass){
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    viewModel.addUser(User(name))
                    showSettingsUI()
                } else {
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun showSettingsUI() {
        viewModel.signInState.value = true
        resultLauncher.launch(Intent(this, SignUpSettings::class.java))
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

    fun checkIsUserSigned() = auth.currentUser != null

}
