package com.example.instagramclone.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import com.example.instagramclone.R
import com.example.instagramclone.managers.AuthManager
import com.example.instagramclone.managers.PrefsManager
import com.example.instagramclone.utils.Logger
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging

/**
 * In SplashActivity, user can visit to SignInActivity or MainActivity
 */

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        initViews()
    }

    private fun initViews() {
        countDownTimer()
        loadFCMToken()
    }

    private fun countDownTimer() {
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (AuthManager.isSignedIn()) {
                    callMainActivity(context)
                } else {
                    callSignInActivity(context)
                }
            }
        }.start()
    }

    private fun loadFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Logger.d(TAG, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            // Save it in locally to use later
            val token = task.result
            Logger.d(TAG, token.toString())
            PrefsManager(this).storeDeviceToken(token.toString())
        })
    }

}