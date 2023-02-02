package com.gramzin.kilo.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gramzin.kilo.adapter.AddDialogAdapter
import com.gramzin.kilo.model.Dialog
import com.gramzin.kilo.model.User
import com.gramzin.kilo.utilities.DatabaseContract

class AddDialogViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val database = Firebase.database(DatabaseContract.DB_PATH)
    private val usersRef = database.getReference("users")
    private val userStorageRef = Firebase.storage.reference.child("users")

    var adapter = AddDialogAdapter()
    var onClick: ((Dialog) -> Unit)? = null

    init {
        usersRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val user = snap.getValue<User>()
                    if (user!=null) {
                        user.id = snap.key
                        userStorageRef.child(user.id!!).child("avatar.png")
                            .downloadUrl.addOnSuccessListener {
                                user.avatarURL = it
                                adapter.changeData(user.id!!)
                            }
                        adapter.addUser(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        AddDialogAdapter.AddDialogHolder.onClick = ::onItemClick
    }

    fun onItemClick(user: User){
        var childs = arrayListOf<Dialog>()
        var counter = 0L
        val usersDialogsRef = database.getReference("user_dialogs").child(auth.uid!!).get().addOnSuccessListener {
            val count = it.childrenCount
            for (snap in it.children){
                database.getReference("dialogs").child(snap.getValue<String>()!!).get().addOnSuccessListener { it ->
                    val dialog = it.getValue<Dialog>()
                    if (dialog!=null) {
                        dialog.key = it.key
                        childs.add(dialog)
                    }
                    counter++
                    if (counter == count){
                        findDialogOrCreateNew(childs, user)
                    }
                }
            }
            if (count == 0L){
                findDialogOrCreateNew(childs, user)
            }
        }
    }

    fun findDialogOrCreateNew(childs: List<Dialog>, user: User){
        val i =childs.indexOfFirst { dialog ->
            (dialog.id1 == user.id || dialog.id2 == user.id) &&  user.id != auth.uid ||
                    (dialog.id1==dialog.id2 && dialog.id2==auth.uid && auth.uid==user.id) }
        if (i>=0){
            onClick?.let { it(childs[i]) }
        }
        else{
            val userDialogsRef1 = database.getReference("user_dialogs").child(auth.uid!!)
            val dialogsRef = database.getReference("dialogs")
            val newDialogRef = dialogsRef.push()
            val dialog = Dialog(auth.uid!!, user.id!!)
            newDialogRef.setValue(Dialog(auth.uid!!, user.id!!))
            dialog.key = newDialogRef.key
            userDialogsRef1.push().setValue(newDialogRef.key)
            if (auth.uid != user.id) {
                val userDialogsRef2 = database.getReference("user_dialogs").child(user.id!!)
                userDialogsRef2.push().setValue(newDialogRef.key)
            }
            onClick?.let { it(dialog) }
        }
    }
}