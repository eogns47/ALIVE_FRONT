package com.example.alive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alive.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.profilephoto.clipToOutline = true
        init()
        initchaTView()
    }


    private fun init(){

    }

    fun initchaTView(){
        binding.chatting.setOnClickListener {
            val intent = Intent(this@MainActivity, chatRoom::class.java)
            startActivity(intent)
        }
    }



}
