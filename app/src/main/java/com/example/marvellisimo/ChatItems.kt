package com.example.marvellisimo


import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_item_from_row.view.*
import kotlinx.android.synthetic.main.chat_item_to_row.view.*


class ChatFromItem(val text: String, val user: String):  Item<GroupieViewHolder>()  {
  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.message_from.text = text
  }

  override fun getLayout(): Int {
    return R.layout.chat_item_from_row
  }
}

class ChatToItem(val text: String, val user: String): Item<GroupieViewHolder>()  {
  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.message_to.text = text
  }

  override fun getLayout(): Int {
    return R.layout.chat_item_to_row
  }
}