package com.gramzin.kilo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gramzin.kilo.R
import com.gramzin.kilo.databinding.DialogItemBinding
import com.gramzin.kilo.model.User

class AddDialogAdapter(): RecyclerView.Adapter<AddDialogAdapter.AddDialogHolder>() {

    private val users = arrayListOf<User>()

    class AddDialogHolder(private val binding: DialogItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        companion object{
            var onClick: ((User) -> Unit)? = null
        }
        fun bind(user: User) {
            turnOnline(false)
            user.isConnected?.apply { turnOnline(this) }
            user.name?.apply {
                binding.dialogName.text = this
            }
            Glide.with(binding.dialogAvatar)
                .load(user.avatarURL)
                .placeholder(R.drawable.img)
                .centerCrop()
                .into(binding.dialogAvatar)
            binding.dialogLayout.setOnClickListener {
                onClick?.let { it1 -> it1(user) }
            }
        }

        private fun turnOnline(isConnected: Boolean){
            if (isConnected)
                binding.connectStatus.visibility = View.VISIBLE
            else
                binding.connectStatus.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddDialogHolder {
        val binding =
            DialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddDialogHolder(binding)
    }


    override fun onBindViewHolder(holder: AddDialogHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size


    fun addUser(user: User){
        users.add(user)
        notifyItemInserted(users.size-1)
    }

    fun changeData(id: String){
        val i = users.indexOfFirst { it.id == id }
        notifyItemChanged(i)
    }
}