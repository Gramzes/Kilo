package com.gramzin.kilo.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gramzin.kilo.adapter.MessageAdapter
import com.gramzin.kilo.model.Message
import com.gramzin.kilo.utilities.DatabaseContract

class DialogModel(dialogId: String): ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database(DatabaseContract.DB_PATH)
    private val messagesRef = database.getReference("dialog_messages").child(dialogId)
    private val messageStorageRef = Firebase.storage.reference.child("dialogs").child(dialogId)

    var messageText = ""
    var adapter: MessageAdapter = MessageAdapter()
    var onMessageAdded: ((Int)->Unit)? = null
    init {

        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue<Message>()
                if (message != null) {
                    if (message.id == auth.uid)
                        message.type = Message.MESSAGE_OUT
                    else
                        message.type = Message.MESSAGE_IN
                    adapter.addMessage(message)
                    onMessageAdded?.let { it(adapter.itemCount - 1) }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue<Message>()
                if (message != null) {
                    if (message.id == auth.uid)
                        message.type = Message.MESSAGE_OUT
                    else
                        message.type = Message.MESSAGE_IN
                    adapter.changeData(message)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun addMessage(message: Message, imageUri: Uri?){
        message.id = auth.uid
        val messageRef = messagesRef.push()
        message.messageId = messageRef.key
        if (imageUri!=null) {
            val imageRef = messageStorageRef.child("${messageRef.key!!}.png")
            imageRef.putFile(imageUri).addOnSuccessListener{
                imageRef.downloadUrl.addOnSuccessListener {
                    message.imageURL = it.toString()
                    messageRef.setValue(message)
                    messageRef.child("timestamp").setValue(ServerValue.TIMESTAMP)
                }
            }
        }
        else{
            messageRef.setValue(message)
            messageRef.child("timestamp").setValue(ServerValue.TIMESTAMP)
        }

    }

}