
package com.example.marvellisimo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R.layout.*
import com.example.marvellisimo.entity.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_item_from_row.view.*
import kotlinx.android.synthetic.main.chat_item_to_row.view.*


class SendMessageActivity :AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_chat_log)

        // TODO()//  create method to view messages

        val user = intent.getParcelableExtra<User>(ComicsPageActivity.USER_KEY)

        setupDummyData()


    }

    private fun setupDummyData(){
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatItemFrom("text from"))
        adapter.add(ChatItemTo("text to"))

        recyclerview_chat_log.adapter = adapter

    }
}

class ChatItemFrom(val text : String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_from.text = text
    }

    override fun getLayout(): Int {
        return chat_item_from_row
    }

}

class ChatItemTo(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_to.text = text

    }

    override fun getLayout(): Int {
        return chat_item_to_row
    }

}