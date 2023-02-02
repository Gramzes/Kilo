package com.gramzin.kilo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gramzin.kilo.R
import com.gramzin.kilo.databinding.DialogItemBinding
import com.gramzin.kilo.model.Dialog

class DialogAdapter(): RecyclerView.Adapter<DialogAdapter.DialogHolder>() {

    private val dialogs = arrayListOf<Dialog>()

    class DialogHolder(private val binding: DialogItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        companion object{
            var onClick: ((Dialog) -> Unit)? = null
        }
        fun bind(dialog: Dialog) {
            turnOnline(false)
            binding.dialogMessage.text = ""
            dialog.buddy?.apply {
                isConnected?.apply { turnOnline(this) }
                name?.apply { binding.dialogName.text = this }
                Glide.with(binding.dialogAvatar)
                    .load(this.avatarURL)
                    .placeholder(R.drawable.img)
                    .centerCrop()
                    .into(binding.dialogAvatar)
            }
            dialog.lastMessage?.apply {
                text?.apply { binding.dialogMessage.text = this }
            }


            binding.dialogLayout.setOnClickListener {
                onClick?.let { it1 -> it1(dialog) }
            }
        }

        private fun turnOnline(isConnected: Boolean){
            if (isConnected)
                binding.connectStatus.visibility = View.VISIBLE
            else
                binding.connectStatus.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogHolder {
        val binding =
            DialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DialogHolder(binding)
    }


    override fun onBindViewHolder(holder: DialogHolder, position: Int) {
        holder.bind(dialogs[position])
    }

    override fun getItemCount() = dialogs.size


    fun addDialog(dialog: Dialog){
        dialogs.add(dialog)
        notifyItemInserted(dialogs.size-1)
    }

    fun changeData(key: String){
        val i = dialogs.indexOfFirst { it.key == key }
        notifyItemChanged(i)
    }
}