package com.example.instagramclone.model

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class Post {
    var id: String = ""
    var caption: String = ""
    var postImg: String = ""
    var currentDate: String = currentTime()

    var uid: String = ""
    var fullname: String = ""
    var userImg: String = ""
    var device_token: String = ""

    var isLiked: Boolean = false

    constructor(caption: String, postImg: String) {
        this.caption = caption
        this.postImg = postImg
    }

    constructor(id: String, caption: String, postImg: String) {
        this.id = id
        this.caption = caption
        this.postImg = postImg
    }

    constructor(postImg: String) {
        this.postImg = postImg
    }


    @SuppressLint("SimpleDateFormat")
    private fun currentTime(): String {
        val sdf = SimpleDateFormat("dd/M//yyyy hh:mm")
        return sdf.format(Date())
    }
}