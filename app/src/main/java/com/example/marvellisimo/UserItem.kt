package com.example.marvellisimo

import com.example.marvellisimo.entity.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*
import kotlinx.android.synthetic.main.navigation_row_layout.view.*

class UserItem(val user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_user_row.text = user.username
//        Picasso.get().load(user.imageUrl).into(viewHolder.itemView.imageView_usernav_row)
    }

    override fun getLayout(): Int {
        return R.layout.navigation_row_layout
    }
}