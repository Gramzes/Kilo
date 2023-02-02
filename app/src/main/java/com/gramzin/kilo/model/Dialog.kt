package com.gramzin.kilo.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Dialog(var id1: String? = null, var id2: String? = null){
    var key: String? = null
    var buddy: User? = null
    var lastMessage: Message? = null
}
