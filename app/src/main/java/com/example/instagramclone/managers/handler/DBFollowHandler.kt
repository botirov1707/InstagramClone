package com.example.instagramclone.managers.handler


interface DBFollowHandler {
    fun onSuccess(isFollowed: Boolean)
    fun onError(e: Exception)
}