package com.example.alive

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alive.databinding.ActivityChatRoomBinding
import com.example.alive.databinding.ActivityMainBinding
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class chatRoom : AppCompatActivity() {
    lateinit var binding: ActivityChatRoomBinding
    var data: ArrayList<Message> = ArrayList()
    lateinit var adapter: MychatAdapter
    var time:String ="d"
    val REQUEST_VIDEO_CAPTURE = 1
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

        binding.camera.setOnClickListener {
            requestPermission()
        }
    }

    fun requestPermission() {
        val REQUEST_CAMERA_PERMISSION = 2

        // 카메라 권한이 있는지 확인
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // 권한이 이미 있는 경우
            // 카메라 Intent 실행 코드 작성
            runCamera()
        } else {
            // 권한이 없는 경우, 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

    }

    fun runCamera() {
        val camIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivity(camIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val videoUri: Uri? = data?.data

        // 동영상을 사용하는 코드 작성
        initSendVideo(videoUri)

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
            data.add(Message(1, 1, 1, "send", time,null))
            adapter.submitList(data)
            adapter.notifyDataSetChanged()
            binding.recyclerView.scrollToPosition(data.size-1)
        }
    }


    fun initReceive() {
        binding.menu.setOnClickListener {
            data.add(Message(0, 0, 0, "receive", time,null))
            adapter.submitList(data)
            adapter.notifyDataSetChanged()
            binding.recyclerView.scrollToPosition(data.size-1)
        }
    }

    fun initSendVideo(videouri:Uri?) {
        if(videouri==null){
            Toast.makeText(applicationContext, "동영상 재생 준비 완료", Toast.LENGTH_SHORT).show()
        }
        data.add(Message(2,2,2, "d", time,videouri))
        adapter.submitList(data)
        adapter.notifyDataSetChanged()
        binding.recyclerView.scrollToPosition(data.size-1)

    }
    fun initReceiveVideo(videouri:Uri?) {

        data.add(Message(0, 0, 0, "receive", time,videouri))
        adapter.submitList(data)
        adapter.notifyDataSetChanged()
        binding.recyclerView.scrollToPosition(data.size-1)

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

