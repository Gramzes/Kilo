package com.gramzin.kilo.model

import com.google.firebase.database.IgnoreExtraProperties
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
class Message(var text: String? = null, var id: String? = null, var imageURL: String?= null){
    companion object{
        const val MESSAGE_UNKNOWN = 0
        const val MESSAGE_IN = 1
        const val MESSAGE_OUT = 2
    }
    var messageId: String? = null
    var type: Int? = null
    var timestamp: Long? = null

    fun getTimeDate(): String? {
        return try {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(timestamp)
        } catch (e: Exception) {
            ""
        }
    }
}
