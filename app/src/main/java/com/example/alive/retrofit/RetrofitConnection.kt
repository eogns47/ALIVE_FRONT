package com.example.alive.retrofit

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Streaming

interface UserService {
    @Streaming
    @GET("downloadFile")
    fun downloadFile(): Call<ResponseBody>

    @Multipart
    @POST("uploadFile")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Call<UploadRes>
}
