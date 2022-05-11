package com.example.instagramclone.network

import com.example.instagramclone.network.service.NoteService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHttp {

    private const val BASE_URL = "https://fcm.googleapis.com"

    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    val noteService: NoteService = retrofit.create(NoteService::class.java)
}