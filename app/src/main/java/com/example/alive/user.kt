package com.example.alive

import java.io.Serializable

data class User(
        var uid: Int, // primary key, auto increment
        var name: String,
        var image: String? = null, // 프로필 사진
) : Serializable