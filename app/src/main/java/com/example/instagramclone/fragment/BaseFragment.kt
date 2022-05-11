package com.example.instagramclone.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import com.example.instagramclone.R
import com.example.instagramclone.activity.SignInActivity
import com.example.instagramclone.model.Post

/**
 *  BaseFragment is parent for all fragments
 */

open class BaseFragment : Fragment() {
    val TAG: String = this::class.java.simpleName

    private var progressDialog: AppCompatDialog? = null

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

    fun callSignInActivity(context: Context) {
        val intent = Intent(context, SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}