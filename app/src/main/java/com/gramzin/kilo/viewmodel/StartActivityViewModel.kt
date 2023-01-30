package com.gramzin.kilo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartActivityViewModel: ViewModel() {
    var email: String = ""
    var password: String = ""
    var name: String = ""
    var repeatPassword = ""
    var signInState = MutableLiveData<Boolean>(true)
}