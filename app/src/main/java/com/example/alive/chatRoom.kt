package com.example.alive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.alive.databinding.ActivityChatRoomBinding
import com.example.alive.databinding.ActivityMainBinding

class chatRoom : AppCompatActivity() {
    lateinit var binding: ActivityChatRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }




}