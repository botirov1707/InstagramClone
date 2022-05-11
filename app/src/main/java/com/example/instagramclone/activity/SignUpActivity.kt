package com.example.instagramclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.instagramclone.R
import com.example.instagramclone.managers.AuthManager
import com.example.instagramclone.managers.DatabaseManager
import com.example.instagramclone.managers.PrefsManager
import com.example.instagramclone.managers.handler.AuthHandler
import com.example.instagramclone.managers.handler.DBUserHandler
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.Extensions.toast
import com.example.instagramclone.utils.Utils

/**
 * In SignUpActivity, user can signup using fullName, email, password
 */

class SignUpActivity : BaseActivity() {

    private lateinit var et_fullname: EditText
    private lateinit var et_password: EditText
    private lateinit var et_email: EditText
    private lateinit var et_cpassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    private fun initViews() {
        et_fullname = findViewById(R.id.et_fullname)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_cpassword = findViewById(R.id.et_cpassword)

        val b_signup = findViewById<Button>(R.id.b_signup)
        b_signup.setOnClickListener {
            val fullname = et_fullname.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (fullname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())
                firebaseSignUp(User(fullname, email, password, ""))
        }

        val tv_signin = findViewById<TextView>(R.id.tv_signin)
        tv_signin.setOnClickListener {
            callSignInActivity(context)
        }
    }

    private fun firebaseSignUp(user: User) {
        showLoading(this)
        AuthManager.signUp(user.email, user.password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                user.uid = uid
                storeUserToDB(user)
                toast(getString(R.string.str_signup_success))
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signup_failed))
            }
        })
    }

    private fun storeUserToDB(user: User) {

        user.device_token = PrefsManager(this).loadDeviceToken()!!
        user.device_id = Utils.getDeviceID(this)

        DatabaseManager.storeUser(user, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                dismissLoading()
                callMainActivity(context)
            }

            override fun onError(e: Exception) {

            }
        })
    }
}