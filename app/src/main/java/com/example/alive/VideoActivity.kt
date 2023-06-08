package com.example.alive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.core.net.toUri
import com.example.alive.databinding.ActivityMainBinding
import com.example.alive.databinding.ActivityVideoBinding


class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        val uri = intent.getStringExtra("myVideoPath")

        binding.apply{
            videoView.start()
            videoView.setVideoURI(uri!!.toUri())
            videoView.setMediaController(MediaController(this@VideoActivity))     // 없으면 에러
            videoView.requestFocus()    // 준비하는 과정을 미리함

            videoView.setOnPreparedListener {
                videoView.start()       // 동영상 재개
            }

            videoView.setOnCompletionListener {
            }

            btnStart.setOnClickListener {
                videoView.start()       // 동영상 재개
            }

            btnResume.setOnClickListener {
                videoView.resume()      // 동영상 처음부터 재시작
            }

            btnPause.setOnClickListener {
                videoView.pause()       // 동영상 일시정지 (Start 버튼 클릭하면 재개)
            }

            btnStop.setOnClickListener {
                videoView.pause()
                videoView.stopPlayback()    // 동영상 정지 (Resume 버튼 클릭하면 새로 실행)
            }
        }
    }
}