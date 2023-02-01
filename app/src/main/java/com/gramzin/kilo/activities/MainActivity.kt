package com.gramzin.kilo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.gramzin.kilo.MessageAdapter
import com.gramzin.kilo.databinding.ActivityMainBinding
import com.gramzin.kilo.model.Message

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database("https://kilo-chat-e7976-default-rtdb.europe-west1.firebasedatabase.app")
        val messagesRef = database.getReference("messages")
        val key = messagesRef.key

        val adapter = MessageAdapter()
        binding.messagesRcView.layoutManager = LinearLayoutManager(this)
        binding.messagesRcView.adapter = adapter
        binding.sendMessageBtn.setOnClickListener{
            val text = binding.messageEditText.text.toString()
            val id = auth.uid
            val message = Message()
            message.id = id
            message.text = text
            messagesRef.push().setValue(message)
        }

        messagesRef.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue<Message>()
                if (message!=null) {
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
}