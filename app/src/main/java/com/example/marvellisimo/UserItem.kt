package com.example.marvellisimo

import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.util.Log
import android.widget.ImageView
import com.example.marvellisimo.entity.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.navigation_row_layout.view.*

class UserItem(val user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_user_row.text = user.username
//        Picasso.get().load(user.imageUrl).into(viewHolder.itemView.imageView_usernav_row)

        Log.d("userImg", "${user.imageUrl}")

        val imgUrl = user.imageUrl

        Picasso.get()
            .load(imgUrl)
            /*.resize(100, 100)*/
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