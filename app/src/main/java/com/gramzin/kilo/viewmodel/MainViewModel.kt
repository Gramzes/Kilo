package com.gramzin.kilo.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.gramzin.kilo.adapter.MessageAdapter
import com.gramzin.kilo.model.Message
import com.gramzin.kilo.utilities.DatabaseContract

class MainViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database(DatabaseContract.DB_PATH)
    private val messagesRef = database.getReference("messages")

    var messageText = ""
    var adapter: MessageAdapter = MessageAdapter()

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
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
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

    fun addMessage(message: Message){
        messagesRef.push().setValue(message)
    }

}