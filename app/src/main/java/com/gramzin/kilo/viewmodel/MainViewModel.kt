package com.gramzin.kilo.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gramzin.kilo.adapter.DialogAdapter
import com.gramzin.kilo.model.Dialog
import com.gramzin.kilo.model.Message
import com.gramzin.kilo.model.User
import com.gramzin.kilo.utilities.DatabaseContract

class MainViewModel(): ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database(DatabaseContract.DB_PATH)
    private val usersRef = database.getReference("users")
    private val usersStatusRef = usersRef.child(auth.uid!!).child("status")
    private val dialogsRef = database.getReference("user_dialogs").child(auth.uid!!)
    private val userStorageRef = Firebase.storage.reference.child("users")

    var adapter = DialogAdapter()

    init{
        usersStatusRef.setValue(true)
        usersStatusRef.onDisconnect().setValue(false)

        dialogsRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val dialogId = snapshot.getValue<String>()
                if (dialogId!=null) {
                    database.getReference("dialogs").child(dialogId).get().addOnSuccessListener {
                        val dialog = it.getValue<Dialog>()
                        if (dialog != null) {
                            dialog.key = dialogId
                            adapter.addDialog(dialog)
                            val buddyId = if(dialog.id1 != auth.uid) dialog.id1 else dialog.id2
                            database.getReference("users").child(buddyId!!).get().addOnSuccessListener {
                                val user = it.getValue<User>()
                                if (user!=null) {
                                    user.id = it.key
                                    userStorageRef.child(user.id!!).child("avatar.png")
                                        .downloadUrl.addOnSuccessListener {
                                        user.avatarURL = it
                                        adapter.changeData(dialog.key!!)
                                    }
                                    usersRef.child(user.id!!).child("status").addListenerForSingleValueEvent(object:ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            user.isConnected = snapshot.getValue<Boolean>()
                                            adapter.changeData(dialog.key!!)
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }

                                    })
                                    dialog.buddy = user
                                    adapter.changeData(dialog.key!!)
                                }
                            }
                            database.getReference("dialog_messages").child(dialog.key!!).addChildEventListener(object: ChildEventListener{
                                override fun onChildAdded(
                                    snapshot: DataSnapshot,
                                    previousChildName: String?
                                ) {
                                    val message = snapshot.getValue<Message>()
                                    dialog.lastMessage = message
                                    adapter.changeData(dialog.key!!)
                                }

                                override fun onChildChanged(
                                    snapshot: DataSnapshot,
                                    previousChildName: String?
                                ) {
                                    val message = snapshot.getValue<Message>()
                                    dialog.lastMessage = message
                                    adapter.changeData(dialog.key!!)
                                }

                                override fun onChildRemoved(snapshot: DataSnapshot) {
                                    TODO("Not yet implemented")
                                }

                                override fun onChildMoved(
                                    snapshot: DataSnapshot,
                                    previousChildName: String?
                                ) {
                                    TODO("Not yet implemented")
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                        }
                    }
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

    fun signOut(){
        auth.signOut()
    }
}