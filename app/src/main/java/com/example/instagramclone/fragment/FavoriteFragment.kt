package com.example.instagramclone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.R
import com.example.instagramclone.adapter.FavoriteAdapter
import com.example.instagramclone.adapter.HomeAdapter
import com.example.instagramclone.managers.AuthManager
import com.example.instagramclone.managers.DatabaseManager
import com.example.instagramclone.managers.handler.DBPostHandler
import com.example.instagramclone.managers.handler.DBPostsHandler
import com.example.instagramclone.model.Post
import com.example.instagramclone.utils.DialogListener
import com.example.instagramclone.utils.Utils
import java.lang.Exception

class FavoriteFragment : BaseFragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)

        loadLikedFeeds()
    }

    private fun loadLikedFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadLikedFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    private fun refreshAdapter(list: ArrayList<Post>) {
        val adapter = FavoriteAdapter(this, list)
        recyclerView.adapter = adapter
    }

    fun likeOrUnLikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)

        loadLikedFeeds()
    }

    fun showDeleteDialog(post: Post) {
        Utils.dialogDouble(
            requireContext(),
            getString(R.string.str_delete_post),
            object : DialogListener {
                override fun onCallback(isChosen: Boolean) {
                    if (isChosen) {
                        deletePost(post)
                    }
                }
            })
    }

    private fun deletePost(post: Post) {
        DatabaseManager.deletePost(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                loadLikedFeeds()
            }

            override fun onError(e: Exception) {

            }
        })
    }
}