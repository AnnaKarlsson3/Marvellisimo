
package com.example.marvellisimo.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.items.ChatFromItem
import com.example.marvellisimo.items.ChatToItem
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.R.layout.activity_chat_log
import com.example.marvellisimo.entity.ChatMessage
import com.example.marvellisimo.entity.Inbox
import com.example.marvellisimo.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*


class SendMessageActivity :AppCompatActivity () {

    companion object {
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser: User? = null
    var url : String? = null;
    var id : Int?  = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_chat_log)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerview_chat_log.adapter = adapter

        id = intent.getIntExtra(PopUpWindow.ID, 0)

        if (id!= 0){
            url = intent.getStringExtra(PopUpWindow.URL)
            toUser = intent.getParcelableExtra<User>(PopUpWindow.USER_KEY)
            performShareLink()
        } else {
            toUser = intent.getParcelableExtra<User>(ComicsPageActivity.USER_KEY)
        }

        to_username.text = toUser?.username

        listenForMessages()

        send_button_chat_log.setOnClickListener {
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
        val toId = toUser?.uid

        val text = "Send a Link : ${url} "

        getIntent().removeExtra("url")
        getIntent().removeExtra("id")

        send(fromId, toId, text)
    }

    private fun performSendMessage() {
        val text = editext_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        send(fromId, toId, text)
    }

    private fun send(fromId: String?, toId: String?, text: String) {
        if (fromId == null) return

        val reference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val inboxRefrence = FirebaseDatabase.getInstance().getReference("/inbox/$toId").push()
        val inbox = Inbox(toId!!,inboxRefrence.key!!,fromId, false)

        inboxRefrence.setValue(inbox)
            .addOnSuccessListener {
                Log.d("SignUpActivity", "User is saved to firebase database")
            }
            .addOnFailureListener {
                Log.d("SignUpActivity", "Fail to store user: ${it.message}")
            }

        val chatMessage = toId?.let {
            ChatMessage(
                reference.key!!, text, fromId,
                it, System.currentTimeMillis() / 1000
            )
        }

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                editext_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
            }
        toReference.setValue(chatMessage)
    }
}








