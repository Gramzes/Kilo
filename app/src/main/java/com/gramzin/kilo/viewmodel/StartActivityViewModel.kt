package com.gramzin.kilo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gramzin.kilo.model.User
import com.gramzin.kilo.utilities.DatabaseContract

class StartActivityViewModel: ViewModel() {
    var email: String = ""
    var password: String = ""
    var name: String = ""
    var repeatPassword = ""
    var signInState = MutableLiveData(true)

    fun addUser(user: User){
        val auth = Firebase.auth
        val database = Firebase.database(DatabaseContract.DB_PATH)
        val usersRef = database.getReference("users")
        usersRef.child(auth.uid!!).child("name").setValue(user)
    }
}