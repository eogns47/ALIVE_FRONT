package com.example.alive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alive.databinding.ActivityChatRoomBinding
import com.example.alive.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class chatRoom : AppCompatActivity() {
    lateinit var binding: ActivityChatRoomBinding
    var data: ArrayList<Message> = ArrayList()
    lateinit var adapter: MychatAdapter
    var time:String ="d"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSend()
        initReceive()
        initRecyclerView()
        initBackBtn()
        val layoutParams =
            binding.mainLayout.getChildAt(3).layoutParams as LinearLayout.LayoutParams
        val recyclerView =
            binding.mainLayout.getChildAt(1).layoutParams as LinearLayout.LayoutParams

        binding.plus.setOnClickListener() {
            if (layoutParams.weight == 0f) {
                recyclerView.weight = 5f
                layoutParams.weight = 5f
            }
            else {
                layoutParams.weight = 0f
                recyclerView.weight = 10f
            }
            binding.mainLayout.getChildAt(3).layoutParams = layoutParams
            binding.mainLayout.getChildAt(1).layoutParams = recyclerView
        }
    }

    fun setTime(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        time = current.format(formatter)
    }
    fun initBackBtn(){
        binding.backBtn.setOnClickListener{
            val intent = Intent(this@chatRoom, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun initSend() {
        binding.sendBtn.setOnClickListener {
            setTime()
            data.add(Message(1, 1, 1, "send", time))
            adapter.submitList(data)
            adapter.notifyDataSetChanged()
            binding.recyclerView.scrollToPosition(data.size-1)
        }
    }

    fun initReceive() {
        binding.menu.setOnClickListener {
            data.add(Message(0, 0, 0, "receive", time))
            adapter.submitList(data)
            adapter.notifyDataSetChanged()
            binding.recyclerView.scrollToPosition(data.size-1)
        }
    }

    fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        adapter = MychatAdapter(data)


        binding.recyclerView.adapter = adapter
    }
}

