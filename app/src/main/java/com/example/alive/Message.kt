package com.example.alive

import android.net.Uri


data class Message(
    var room_id: Int,
    var sender_uid: Int,
    var receiver_uid: Int,
    var text: String,
    var time: String,
    var uri: Uri?
)