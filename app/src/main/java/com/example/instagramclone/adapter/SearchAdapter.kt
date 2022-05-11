package com.example.instagramclone.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.fragment.HomeFragment
import com.example.instagramclone.fragment.SearchFragment
import com.example.instagramclone.model.Post
import com.example.instagramclone.model.User
import com.google.android.material.imageview.ShapeableImageView

class SearchAdapter(var fragment: SearchFragment, var items: ArrayList<User>) : BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = items[position]
        if (holder is UserViewHolder) {
            holder.tv_fullname.text = user.fullname
            holder.tv_email.text = user.email
            Glide.with(fragment).load(user.userImg).placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person).into(holder.iv_profile)

            val tv_follow = holder.tv_follow

            if (!user.isFollowed) {
                tv_follow.text = fragment.getString(R.string.str_follow)
            } else {
                tv_follow.text = fragment.getString(R.string.str_following)
            }

            tv_follow.setOnClickListener {
                if (!user.isFollowed) {
                    tv_follow.text = fragment.getString(R.string.str_following)
                } else {
                    tv_follow.text = fragment.getString(R.string.str_follow)
                }
                fragment.followOrUnfollow(user)
            }

        }
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_profile: ShapeableImageView = view.findViewById(R.id.iv_profile)
        val tv_fullname: TextView = view.findViewById(R.id.tv_fullname)
        val tv_email: TextView = view.findViewById(R.id.tv_email)
        val tv_follow: TextView = view.findViewById(R.id.tv_follow)
    }
}