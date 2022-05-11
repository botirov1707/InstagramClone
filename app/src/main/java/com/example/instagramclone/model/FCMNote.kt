package com.example.instagramclone.model

data class FCMNote(
    val notification: Notification,
    val registration_ids: ArrayList<String>
)

data class Notification(
    val title: String,
    val body: String
)