package com.example.instagramclone.model

class User {
    var uid: String = ""
    var fullname: String = ""
    var email: String = ""
    var password: String = ""
    var userImg: String = ""
    var isFollowed: Boolean = false

    var device_id = ""
    var device_type = "A"
    var device_token = ""

    constructor()

    constructor(fullname: String, email: String) {
        this.fullname = fullname
        this.email = email
    }

    constructor(fullname: String, email: String, image: String) {
        this.fullname = fullname
        this.email = email
        this.userImg = image
    }

    constructor(fullname: String, email: String, password: String, image: String) {
        this.fullname = fullname
        this.email = email
        this.password = password
        this.userImg = image
    }

    override fun toString(): String {
        return "User(uid='$uid', fullname='$fullname', email='$email', password='$password', userImg='$userImg', isFollowed=$isFollowed, device_id='$device_id', device_type='$device_type', device_token='$device_token')"
    }


}