package com.example.instagramclone.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.example.instagramclone.R

/**
 * BaseActivity is parent for all Activities
 */

open class BaseActivity : AppCompatActivity() {

    val TAG: String = this::class.java.simpleName

    lateinit var context: Context
    private var progressDialog: AppCompatDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
    }

    fun showLoading(activity: Activity?) {
        if (activity == null) return

        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        } else {
            progressDialog = AppCompatDialog(activity, R.style.CustomDialog)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.setContentView(R.layout.custom_progress_dialog)
            val iv_progress = progressDialog!!.findViewById<ImageView>(R.id.iv_progress)
            val animationDrawable = iv_progress!!.drawable as AnimationDrawable
            animationDrawable.start()
            if (!activity.isFinishing) progressDialog!!.show()
        }

    }

    protected fun dismissLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    fun callMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun callSignInActivity(context: Context) {
        val intent = Intent(context, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}