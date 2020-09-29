
package com.example.marvellisimo.activities

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ChatFromItem
import com.example.marvellisimo.ChatToItem
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.R.layout.activity_chat_log
import com.example.marvellisimo.entity.ChatMessage
import com.example.marvellisimo.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import java.net.URL


class SendMessageActivity :AppCompatActivity () {

    companion object {
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null
    var Url : String? = null;
    var Id : Int?  = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_chat_log)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        recyclerview_chat_log.adapter = adapter

        Id = intent.getIntExtra(PopUpWindow.ID, 0)


        if (Id!= 0){
            Url = intent.getStringExtra(PopUpWindow.URL)
            toUser = intent.getParcelableExtra<User>(PopUpWindow.USER_KEY)
            performShareLink()
        } else {
            toUser = intent.getParcelableExtra<User>(ComicsPageActivity.USER_KEY)
        }



        listenForMessages()

        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message....")
            performSendMessage()
        }


    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = ComicsPageActivity.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }

    private fun performShareLink(){
        val fromId = FirebaseAuth.getInstance().uid
        /*val user = intent.getParcelableExtra<User>(ComicsPageActivity.USER_KEY)*/
        val toId = toUser?.uid

        Log.d(TAG, "ID: ${Id}")
        Log.d(TAG, "URL ${Url}")
        Log.d(TAG, "${toUser?.username}")


        val text = "Send a Link : ${Url} "
        getIntent().removeExtra("url");
        getIntent().removeExtra("id");


        send(fromId, toId, text)
    }

    private fun send(fromId: String?, toId: String?, text: String) {
        if (fromId == null) return
        Log.d(TAG, "send: ${toId}")
        Log.d(TAG, "send: ${FirebaseAuth.getInstance().uid} && ${fromId}")


        val reference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = toId?.let {
            ChatMessage(
                reference.key!!, text, fromId,
                it, System.currentTimeMillis() / 1000
            )
        }

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                editext_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)

        /*val latestMessageRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)*/
    }

    private fun performSendMessage() {

        val text = editext_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        //val user = intent.getParcelableExtra<User>(ComicsPageActivity.USER_KEY)
        val toId = toUser?.uid

        send(fromId, toId, text)

    }
}








