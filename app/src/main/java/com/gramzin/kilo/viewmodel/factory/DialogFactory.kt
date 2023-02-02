package com.gramzin.kilo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gramzin.kilo.viewmodel.DialogModel

class DialogFactory(private val dialogId: String):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass==DialogModel::class.java){
            return DialogModel(dialogId) as T
        }
        return super.create(modelClass)
    }
}

