package com.example.marvellisimo.items

import android.util.Log
import android.widget.ImageView
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.navigation_row_layout.view.*

class UserItem(val user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_user_row.text = user.username

        val imgUrl = user.imageUrl

        if (!imgUrl.isNullOrEmpty())
            Picasso.get()
            .load(imgUrl)
            .transform(CropCircleTransformation())
            .into(viewHolder.itemView.imageView_usernav_row)

        var onlineOffline: ImageView =
            viewHolder.itemView.findViewById(R.id.imageview_online_offline)

        if (user.active == true) {
            onlineOffline.setImageResource(R.drawable.presence_online)
        } else {
            onlineOffline.setImageResource(R.drawable.presence_invisible)
        }
    }

    override fun getLayout(): Int {
        return R.layout.navigation_row_layout
    }
}