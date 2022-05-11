package com.example.instagramclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.fragment.ProfileFragment
import com.example.instagramclone.model.Post
import com.example.instagramclone.utils.Utils
import com.google.android.material.imageview.ShapeableImageView

class ProfileAdapter(var fragment: ProfileFragment, var items: ArrayList<Post>) : BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_profile, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if (holder is PostViewHolder) {
            val iv_post = holder.iv_post
            setViewHeight(iv_post)
            Glide.with(fragment).load(post.postImg).into(iv_post)
        }
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_post: ShapeableImageView = view.findViewById(R.id.iv_post)
    }

    /**
     *  Set view height as screen width
     */
    private fun setViewHeight(view: View) {
        val params: ViewGroup.LayoutParams = view.layoutParams
        params.height = Utils.screenSize(fragment.requireActivity().application).width / 2
        view.layoutParams = params
    }
}