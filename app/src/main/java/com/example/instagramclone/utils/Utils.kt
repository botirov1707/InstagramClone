package com.example.instagramclone.utils

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.example.instagramclone.R
import com.example.instagramclone.managers.AuthManager
import com.example.instagramclone.model.*
import com.example.instagramclone.network.RetrofitHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Utils {
    fun screenSize(context: Application): ScreenSize {
        val displayMetrics = DisplayMetrics()
        val windowsManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceWidth = displayMetrics.widthPixels
        val deviceHeight = displayMetrics.heightPixels
        return ScreenSize(deviceWidth, deviceHeight)
    }

    fun dialogDouble(context: Context?, title: String?, callback: DialogListener) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_dialog_double)
        dialog.setCanceledOnTouchOutside(true)
        val d_title = dialog.findViewById<TextView>(R.id.d_title)
        val d_confirm = dialog.findViewById<TextView>(R.id.d_confirm)
        val d_cancel = dialog.findViewById<TextView>(R.id.d_cancel)
        d_title.text = title
        d_confirm.setOnClickListener {
            dialog.dismiss()
            callback.onCallback(true)
        }
        d_cancel.setOnClickListener {
            dialog.dismiss()
            callback.onCallback(false)
        }
        dialog.show()
    }

    fun dialogSingle(context: Context?, title: String?, callback: DialogListener) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_dialog_single)
        dialog.setCanceledOnTouchOutside(true)
        val d_title = dialog.findViewById<TextView>(R.id.d_title)
        val d_confirm = dialog.findViewById<TextView>(R.id.d_confirm)
        d_title.text = title
        d_confirm.setOnClickListener {
            dialog.dismiss()
            callback.onCallback(true)
        }
        dialog.show()
    }

    @SuppressLint("HardwareIds")
    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getUid(): String {
        return AuthManager.currentUser()!!.uid
    }

    fun sendNote(context: Context, me: User, device_token: String) {
        val notification = Notification(
            context.getString(R.string.app_name),
            context.getString(R.string.str_followed_note).replace("$", me.fullname)
        )
        val deviceList = ArrayList<String>()
        deviceList.add(device_token)
        val fcmNote = FCMNote(notification, deviceList)

        RetrofitHttp.noteService.sendNote(fcmNote).enqueue(object : Callback<FCMResp> {
            override fun onResponse(call: Call<FCMResp>, response: Response<FCMResp>) {

            }

            override fun onFailure(call: Call<FCMResp>, t: Throwable) {

            }
        })
    }
}