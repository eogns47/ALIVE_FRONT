package com.example.alive

enum class ReadMessage{
    READ, NOTREAD
}
data class Message(
    var room_id: Int,
    var sender_uid: Int,
    var receiver_uid: Int,
    var text: String,
    var time: String
)