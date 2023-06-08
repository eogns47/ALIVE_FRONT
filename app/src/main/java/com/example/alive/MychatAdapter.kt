package com.example.alive

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.MediaController
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alive.databinding.MyChatItemBinding
import com.example.alive.databinding.MyVideoChatItemBinding
import com.example.alive.databinding.OtherChatItemBinding
import com.example.alive.databinding.OtherVideoChatItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent


class MychatAdapter(
    val items:ArrayList<Message>,
    val chatRoom: Activity
): ListAdapter<Message, RecyclerView.ViewHolder>(diffUtil) {

    interface OnItemClickListener{
        fun OnItemClick(data:Message,position: Int)
    }

    var itemClickListener: OnItemClickListener?=null


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
            binding.chatVideoView.stopPlayback()

            binding.chatVideoView.setVideoURI(message.videopath)
//            binding.chatVideoView.start()
            binding.timeTextView.text = message.time
//            binding.chatVideoView.seekTo(message.videoPosition)
//
//            if (!message.isVideoPlaying) {
//                binding.chatVideoView.start()
//            } else {
//                binding.chatVideoView.pause()
//            }


        }
        init {
            binding.chatVideoView.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }

        }

    }
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is MyVideoViewHolder) {
            val position = holder.position
            if (position != RecyclerView.NO_POSITION) {
                val message = currentList[position]
                message.isVideoPlaying = holder.binding.chatVideoView.isPlaying
                message.videoPosition = holder.binding.chatVideoView.currentPosition
            }
        }
        super.onViewRecycled(holder)
    }

    inner class OtherVideoViewHolder(val binding: OtherVideoChatItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.chatVideoView.setVideoURI(message.videopath)
            binding.timeTextView.text = message.time
            binding.profileImageView.setImageResource(R.drawable.circuit)
        }
        init {
            binding.chatVideoView.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }

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