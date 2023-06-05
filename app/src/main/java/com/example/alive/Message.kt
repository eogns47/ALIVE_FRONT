<<<<<<< HEAD
package com.example.alive

import android.net.Uri


data class Message(
    var room_id: Int,
    var sender_uid: Int,
    var receiver_uid: Int,
    var text: String,
    var time: String,
    var videopath:Uri?
=======
package com.example.alive

import android.net.Uri


data class Message(
    var room_id: Int,
    var sender_uid: Int,
    var receiver_uid: Int,
    var text: String,
    var time: String,
    var videopath:Uri?
>>>>>>> 0659d87e46da6669d7bc7f40662185df217d1bc7
)