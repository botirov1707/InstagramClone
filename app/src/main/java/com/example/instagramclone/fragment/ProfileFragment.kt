package com.example.instagramclone.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.activity.SignInActivity
import com.example.instagramclone.adapter.ProfileAdapter
import com.example.instagramclone.managers.AuthManager
import com.example.instagramclone.managers.DatabaseManager
import com.example.instagramclone.managers.StorageManager
import com.example.instagramclone.managers.handler.DBPostsHandler
import com.example.instagramclone.managers.handler.DBUserHandler
import com.example.instagramclone.managers.handler.DBUsersHandler
import com.example.instagramclone.managers.handler.StorageHandler
import com.example.instagramclone.model.Post
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.DialogListener
import com.example.instagramclone.utils.Logger
import com.example.instagramclone.utils.Utils
import com.google.android.material.imageview.ShapeableImageView
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

class ProfileFragment : BaseFragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var iv_profile: ShapeableImageView
    lateinit var tv_fullname: TextView
    lateinit var tv_email: TextView
    lateinit var tv_posts: TextView
    lateinit var tv_followers: TextView
    lateinit var tv_following: TextView

    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        tv_fullname = view.findViewById(R.id.tv_fullname)
        tv_email = view.findViewById(R.id.tv_email)
        tv_posts = view.findViewById(R.id.tv_posts)
        tv_followers = view.findViewById(R.id.tv_followers)
        tv_following = view.findViewById(R.id.tv_following)


        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        iv_profile = view.findViewById(R.id.iv_profile)
        iv_profile.setOnClickListener {
            pickFishBunPhoto()
        }

        val iv_logout = view.findViewById<ImageView>(R.id.iv_logout)
        iv_logout.setOnClickListener {
            Utils.dialogDouble(
                requireContext(),
                getString(R.string.str_sign_out),
                object : DialogListener {
                    override fun onCallback(isChosen: Boolean) {
                        if (isChosen) {
                            AuthManager.signOut()
                            callSignInActivity(requireContext())
                        }
                    }
                })
        }

        loadUserInfo()
        loadMyPosts()
        loadMyFollowers()
        loadMyFollowing()
    }

    private fun loadMyPosts() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadPosts(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                tv_posts.text = posts.size.toString()
                refreshAdapter(posts)
            }

            override fun onError(e: java.lang.Exception) {
            }
        })
    }

    private fun loadMyFollowers() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFollowers(uid, object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                tv_followers.text = users.size.toString()
            }

            override fun onError(e: java.lang.Exception) {
            }
        })
    }

    private fun loadMyFollowing() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFollowing(uid, object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                tv_following.text = users.size.toString()
            }

            override fun onError(e: java.lang.Exception) {
            }
        })
    }

    private fun loadUserInfo() {
        DatabaseManager.loadUser(AuthManager.currentUser()!!.uid, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                if (user != null)
                    showUserInfo(user)
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun showUserInfo(user: User) {
        tv_fullname.text = user.fullname
        tv_email.text = user.email
        Glide.with(this).load(user.userImg).placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person).into(iv_profile)
    }

    private fun refreshAdapter(items: ArrayList<Post>) {
        val adapter = ProfileAdapter(this, items)
        recyclerView.adapter = adapter
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
                uploadPickPhoto()
            }
        }

    private fun uploadPickPhoto() {
        if (pickedPhoto != null) {
            StorageManager.uploadUserPhoto(pickedPhoto!!, object : StorageHandler {
                override fun onSuccess(imgUrl: String) {
                    DatabaseManager.updateUserImage(imgUrl)
                    iv_profile.setImageURI(pickedPhoto)
                }

                override fun onError(exception: Exception?) {

                }
            })
        }
    }
}