package com.gramzin.kilo.model

data class Message(val name: String, val text: String?, val imageURL: String?){
    companion object{
        const val MESSAGE_UNKNOWN = 0
        const val MESSAGE_IN = 1
        const val MESSAGE_OUT = 2
    }
    var type = MESSAGE_UNKNOWN
}
