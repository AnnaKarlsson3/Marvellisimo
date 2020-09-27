package com.example.marvellisimo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.UserItem
import com.example.marvellisimo.entity.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_comic_page.*
import kotlinx.android.synthetic.main.activity_comic_page.toolBar_RecyclerView
import kotlinx.android.synthetic.main.fragment_simple_dialog.*
import kotlinx.android.synthetic.main.fragment_simple_dialog.view.*

class PopUpWindow : DialogFragment(){

    companion object {

        const val TAG = "PopUpWindow"



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        /*setupClickListeners(view)*/
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {

        val users = mutableListOf<UserItem>()
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val adapterNav = GroupAdapter<GroupieViewHolder>()

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if(user != null){
                    val user= UserItem(user)
                    adapterNav.add(user)
                    users.add(user)
                }
                recyclerView_popup_view.adapter = adapterNav

                for(u in users){
                    Log.d("users", "users in list: ${u.user.username}")}

                adapterNav.setOnItemClickListener{item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, SendMessageActivity::class.java)
                    intent.putExtra(ComicsPageActivity.USER_KEY, userItem.user)
                    intent.putExtra(ComicsPageActivity.USER_NAME, userItem.user.username)
                    startActivity(intent)

                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val oldUser = users.find { it.user.uid == user.uid } //hitta user i listan som har samma uid som den i db har som ändrats
                    oldUser?.user?.active = user.active

                    adapterNav.notifyDataSetChanged()
                }
                recyclerView_popup_view.adapter = adapterNav
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {


                TODO()

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    /*private fun setupClickListeners(view: View) {
        view.btnPositive.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }
        view.btnNegative.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }
    }*/
}