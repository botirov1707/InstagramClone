package com.example.instagramclone.fragment

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramclone.R
import com.example.instagramclone.managers.AuthManager
import com.example.instagramclone.managers.DatabaseManager
import com.example.instagramclone.managers.PrefsManager
import com.example.instagramclone.managers.StorageManager
import com.example.instagramclone.managers.handler.DBPostHandler
import com.example.instagramclone.managers.handler.DBUserHandler
import com.example.instagramclone.managers.handler.StorageHandler
import com.example.instagramclone.model.Post
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.Logger
import com.example.instagramclone.utils.Utils
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.RuntimeException
import kotlin.Exception

class UploadFragment : BaseFragment() {

    private var listener: UploadListener? = null

    lateinit var fl_photo: FrameLayout
    lateinit var iv_photo: ImageView
    lateinit var et_caption: EditText

    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)
        initViews(view)
        return view
    }

    /**
     * onAttach is for communication of Fragments
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is UploadListener) {
            context
        } else {
            throw RuntimeException("$context must implement UploadListener")
        }
    }

    /**
     * onDetach is for communication of Fragments
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun initViews(view: View) {
        val fl_view = view.findViewById<FrameLayout>(R.id.fl_view)
        setViewHeight(fl_view)
        et_caption = view.findViewById(R.id.et_caption)
        fl_photo = view.findViewById(R.id.fl_photo)
        iv_photo = view.findViewById(R.id.iv_photo)
        val iv_close = view.findViewById<ImageView>(R.id.iv_close)
        val iv_pick = view.findViewById<ImageView>(R.id.iv_pick)
        val iv_upload = view.findViewById<ImageView>(R.id.iv_upload)

        iv_pick.setOnClickListener {
            resetAll()
            pickFishBunPhoto()
        }

        iv_close.setOnClickListener {
            hidePickedPhoto()
        }

        iv_upload.setOnClickListener {
            uploadNewPost()
        }
    }

    private fun uploadNewPost() {
        val caption = et_caption.text.toString().trim()
        if (caption.isNotEmpty() && pickedPhoto != null) {
            uploadPostPhoto(caption, pickedPhoto!!)
        }
    }

    private fun uploadPostPhoto(caption: String, uri: Uri) {
        showLoading(requireActivity())
        StorageManager.uploadPostPhoto(uri, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                val post = Post(caption, imgUrl)
                val uid = AuthManager.currentUser()!!.uid

                DatabaseManager.loadUser(uid, object : DBUserHandler {
                    override fun onSuccess(user: User?) {
                        post.uid = uid
                        post.fullname = user!!.fullname
                        post.userImg = user.userImg
                        storePostToDB(post)
                    }

                    override fun onError(e: Exception) {
                        dismissLoading()
                    }

                })
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
            }
        })
    }

    private fun storePostToDB(post: Post) {
        post.device_token = PrefsManager(requireContext()).loadDeviceToken()!!
        DatabaseManager.storePosts(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                storeFeedToDB(post)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }

        })
    }

    private fun storeFeedToDB(post: Post) {
        DatabaseManager.storeFeeds(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                dismissLoading()
                resetAll()
                listener!!.scrollToHome()
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    private fun resetAll() {
        et_caption.text.clear()
        allPhotos.clear()
        pickedPhoto = null
        fl_photo.visibility = GONE
    }

    private fun showPickedPhoto() {
        fl_photo.visibility = VISIBLE
        iv_photo.setImageURI(pickedPhoto)
    }

    private fun hidePickedPhoto() {
        pickedPhoto = null
        fl_photo.visibility = GONE
    }

    /**
     * Pick photo using FishBun library
     */
    private fun pickFishBunPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .hasCameraInPickerPage(true)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos[0]
                showPickedPhoto()
            }
        }

    /**
     *  Set view height as screen width
     */
    private fun setViewHeight(flView: FrameLayout) {
        val params: ViewGroup.LayoutParams = flView.layoutParams
        params.height = Utils.screenSize(requireActivity().application).width
        flView.layoutParams = params
    }

    /**
     * This interface is created for communication with HomeFragment
     */
    interface UploadListener {
        fun scrollToHome()
    }
}