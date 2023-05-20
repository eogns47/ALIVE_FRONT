package com.example.alive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alive.databinding.MyChatItemBinding
import com.example.alive.databinding.MyVideoChatItemBinding
import com.example.alive.databinding.OtherChatItemBinding
import com.example.alive.databinding.OtherVideoChatItemBinding


class MychatAdapter(
    val items:ArrayList<Message>
): ListAdapter<Message, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return currentList[position].sender_uid
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class MyViewHolder(val binding: MyChatItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.chatTextView.text = message.text
            binding.timeTextView.text = message.time
        }
    }

    inner class OtherViewHolder(val binding: OtherChatItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.chatTextView.text = message.text
            binding.timeTextView.text = message.time
            binding.profileImageView.setImageResource(R.drawable.circuit)
            binding.nameTextView.text = "ALIVE"
        }
    }

    inner class MyVideoViewHolder(val binding: MyVideoChatItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.chatVideoView.setVideoURI(message.uri)
            binding.timeTextView.text = message.time
        }
    }

    inner class OtherVideoViewHolder(val binding: OtherVideoChatItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.chatVideoView.setVideoURI(message.uri)
            binding.timeTextView.text = message.time
            binding.profileImageView.setImageResource(R.drawable.circuit)
            binding.nameTextView.text = "ALIVE"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1->{
                MyViewHolder(MyChatItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
            }
            0->{
                OtherViewHolder(OtherChatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            2->{
                MyVideoViewHolder(MyVideoChatItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
            }
            else->{
                OtherVideoViewHolder(OtherVideoChatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(currentList[position].sender_uid){
            1->{  //사용자
                (holder as MyViewHolder).bind(currentList[position])
                holder.setIsRecyclable(false)
            }0->{   //ai
            (holder as OtherViewHolder).bind(currentList[position])
            holder.setIsRecyclable(false) }
            2->{   //ai
                (holder as MyVideoViewHolder).bind(currentList[position])
                holder.setIsRecyclable(false) }
            else->{   //ai
                (holder as OtherVideoViewHolder).bind(currentList[position])
                holder.setIsRecyclable(false) }
        }
    }

    companion object{
        private val diffUtil = object: DiffUtil.ItemCallback<Message>(){
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

        }
    }
}