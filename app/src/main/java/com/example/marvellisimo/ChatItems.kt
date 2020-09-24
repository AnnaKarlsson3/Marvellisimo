package com.example.marvellisimo



import android.widget.ImageView
import com.example.marvellisimo.entity.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_item_from_row.view.*
import kotlinx.android.synthetic.main.chat_item_to_row.view.*


class ChatFromItem(val text: String, val user: User): Item<GroupieViewHolder>() {
  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.message_from.text = text

    val targetImageView: ImageView = viewHolder.itemView.findViewById(R.id.image_message_from)

    Picasso.get().load(user.imageUrl).into(targetImageView)

    /*val uri = user.profileImageUrl
    val targetImageView = viewHolder.itemView.imageview_chat_from_row
    Picasso.get().load(uri).into(targetImageView)*/
  }

  override fun getLayout(): Int {
    return R.layout.chat_item_from_row
  }
}

class ChatToItem(val text: String, val user: User): Item<GroupieViewHolder>() {
  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.message_to.text = text

    val targetImageView: ImageView = viewHolder.itemView.findViewById(R.id.image_message_to)

    Picasso.get().load(user.imageUrl).into(targetImageView)

    // load our user image into the star
   /* val uri = user.profileImageUrl
    val targetImageView = viewHolder.itemView.imageview_chat_to_row
    Picasso.get().load(uri).into(targetImageView)*/
  }

  override fun getLayout(): Int {
    return R.layout.chat_item_to_row
  }
}

/*

class ChatFromItem(val text: String, val user: User):  Item<GroupieViewHolder>()  {
  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.message_from.text = text

    Log.d("ValueFrom" , "${viewHolder.itemView.message_from.text}")
  }

  override fun getLayout(): Int {
    return R.layout.chat_item_from_row
  }
}

class ChatToItem(val text: String, val user: User): Item<GroupieViewHolder>()  {
  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.message_to.text = text
    Log.d("ValueTo" , "${viewHolder.itemView.message_to.text}")
  }

  override fun getLayout(): Int {
    return R.layout.chat_item_to_row
  }
}*/