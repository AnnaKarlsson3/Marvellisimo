package com.example.marvellisimo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.*
import com.example.marvellisimo.R
import com.example.marvellisimo.R.layout
import com.example.marvellisimo.R.layout.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*


class SendMessageActivity :AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_chat_log)

       // TODO()//  val username = get the user object so we can use the data in this view

        val adapter = GroupAdapter<GroupieViewHolder>()

        recyclerview_chat_log.adapter = adapter

        adapter.add(ChatItemFrom)
        adapter.add(ChatItemTo)

        // TODO: 2020-09-18 add useritem



    }
}

class ChatItemFrom: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return chat_item_from_row_layout
    }

}

class ChatItemTo: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return chat_item_to_row
    }


}