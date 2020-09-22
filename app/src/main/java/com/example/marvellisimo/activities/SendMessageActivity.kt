
package com.example.marvellisimo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.R.layout.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*


class SendMessageActivity :AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_chat_log)

        // TODO()//  create method to view messages

        val adapter = GroupAdapter<GroupieViewHolder>()

        recyclerview_chat_log.adapter = adapter

        //adapter.add(ChatItemFrom)
        //adapter.add(ChatItemTo)

    }
}

class ChatItemFrom: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return chat_item_from_row
    }

}

class ChatItemTo: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return chat_item_to_row
    }


}