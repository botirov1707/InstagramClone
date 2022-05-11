package com.example.instagramclone.network.service

import com.example.instagramclone.model.FCMNote
import com.example.instagramclone.model.FCMResp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NoteService {

    companion object {
        private const val SERVER_KEY =
            "AAAAjI-XgSA:APA91bEnF3ag21Zi_apgVw_yTrtrTYN1ACOBLKf9YpKXzgfbi3LDZFf6ILleSr6sq6iG4sqGFocD2JcBSYwDUw-4HhOaiTxowiBSo181msHFe3ryYZFC-F7Sa2MW8_cN7Itid36hPSto"
    }

    @Headers("Authorization:key=$SERVER_KEY")
    @POST("/fcm/send")
    fun sendNote(@Body fcmNote: FCMNote): Call<FCMResp>

}