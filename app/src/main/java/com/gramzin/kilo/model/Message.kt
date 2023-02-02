package com.gramzin.kilo.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Message(var text: String? = null, var id: String? = null, var imageURL: String?= null){
    companion object{
        const val MESSAGE_UNKNOWN = 0
        const val MESSAGE_IN = 1
        const val MESSAGE_OUT = 2
    }
    var messageId: String? = null
    var type: Int? = null
}
