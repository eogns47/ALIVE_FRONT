package com.example.alive

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alive.databinding.ActivityChatRoomBinding
import com.example.alive.retrofit.RetrofitClient
import com.example.alive.retrofit.UploadRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("DEPRECATION")
class chatRoom : AppCompatActivity() {
    lateinit var binding: ActivityChatRoomBinding
    var data: ArrayList<Message> = ArrayList()
    lateinit var adapter: MychatAdapter
    var videoFile:File?=null
    var filename:String?=null
    private var videoUri:Uri?=null
    var getFile:String?=null
    var time:String ="d"
    var REQUEST_VIDEO_CAPTURE = 1;
    private val VIDEO_CAPTURE = 101
    private val CAMERA_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
    private val CAMERA_PERMISSION_FLAG = 100
    private val STORAGE_PERMISSION = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val STORAGE_PERMISSION_FLAG = 200
    private lateinit var videoCaptureLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSend()
        initReceive()
        initRecyclerView()
        initBackBtn()
        if(checkPermission(CAMERA_PERMISSION, CAMERA_PERMISSION_FLAG)){
            checkPermission(STORAGE_PERMISSION, STORAGE_PERMISSION_FLAG)
        }
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
        videoCaptureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 결과 처리
                val videoUri = result.data?.data
                // 동영상 처리 로직 호출 등
            }
        }
    }

    private fun checkPermission(permissions : Array<out String>, flag : Int):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, permissions, flag)
                    return false
                }
            }
        }
        return true
    }

    fun requestPermission() {
        val REQUEST_CAMERA_PERMISSION = 1

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
        val recordVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        filename=newVideoFileName()
        videoFile = File(
            File("${filesDir}/video1").apply {
                if(!this.exists()){
                    Toast.makeText(this@chatRoom,"i'll make file",Toast.LENGTH_SHORT).show()
                    this.mkdirs()
                }
                else{
                    Toast.makeText(this@chatRoom,"file exist",Toast.LENGTH_SHORT).show()
                }
            },
            filename
        )
        //Toast.makeText(this,"h2",Toast.LENGTH_SHORT).show()


        getFile = videoFile!!.path
        //Toast.makeText(this,videoUri.toString(),Toast.LENGTH_SHORT).show()
        //recordVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,videoUri)
        startActivityForResult(recordVideoIntent, REQUEST_VIDEO_CAPTURE)
    }
    private fun newVideoFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        var filename2 = sdf.format(System.currentTimeMillis())
        return "wpqkf${filename2}.mp4"
    }
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            when (requestCode) {
                REQUEST_VIDEO_CAPTURE -> {
                    data?.data?.let { videoUri ->
                        saveVideoFile(videoUri)
                        if (videoFile != null) {
                            val videoUri: Uri = Uri.fromFile(videoFile)
                            initSendVideo(videoUri)

                            postVideo(videoUri)
                            getVideo()
                        } else {
                            // 동영상 파일 저장 실패
                            // 에러 처리
                        }
                    }

                    val videoPath = getFile

                }
            }
        }
        else{
            Toast.makeText(this,"sibal",Toast.LENGTH_SHORT).show()
        }
    }

    private fun postVideo(uri: Uri) {
        val path = uri.path
        val file = File(path) // 임시 파일을 생성합니다.

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val videoPart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val userService = RetrofitClient.userService
        val call = userService.uploadFile(videoPart)

        call.enqueue(object : Callback<UploadRes> {
            override fun onResponse(call: Call<UploadRes>, response: Response<UploadRes>) {
                // 파일 업로드 성공
                Toast.makeText(this@chatRoom, "success - " + response.body()?.result, Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<UploadRes>, t: Throwable) {
                // 파일 업로드 실패
                Toast.makeText(this@chatRoom, "fail - " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getVideo() {
        val userService = RetrofitClient.userService
        val call = userService.downloadFile()

            call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // 파일 업로드 성공

                val responseBody = response.body()
                var myUri: Uri? = null
                if (responseBody != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        myUri = saveVideoToFile(responseBody, "test1")
                        withContext(Dispatchers.Main) {
                            initReceiveVideo(myUri)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 파일 업로드 실패
                Toast.makeText(this@chatRoom, "fail - " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveVideoToFile(responseBody: ResponseBody, videoName: String):Uri {
        val file = File(this.filesDir, videoName) // 저장할 파일 경로와 이름
        val inputStream: InputStream = responseBody.byteStream()
        val outputStream: OutputStream = FileOutputStream(file)

        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()

        return Uri.fromFile(file)
    }


    private fun saveVideoFile(videoUri: Uri): File? {
        val videoFile: File?
        val videoFilePath = "${filesDir}/video1/${filename}"
        try {
            val inputStream = contentResolver.openInputStream(videoUri)
            val outputStream = FileOutputStream(videoFilePath)
            inputStream?.use { input ->
                outputStream.use { output ->
                    val buffer = ByteArray(4 * 1024) // 4KB buffer
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                    }
                    output.flush()
                }
            }
            videoFile = File(videoFilePath)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return videoFile
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
        setTime()
        data.add(Message(2,2,2, "d", time,videouri))
        adapter.submitList(data)
        adapter.notifyDataSetChanged()
        binding.recyclerView.scrollToPosition(data.size-1)
    }
    fun initReceiveVideo(videouri:Uri?) {

        data.add(Message(3, 3, 3, "receive", time,videouri))
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
