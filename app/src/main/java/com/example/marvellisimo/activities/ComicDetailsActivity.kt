package com.example.marvellisimo.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.RealmComicEntity
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_comic_details.*


class ComicDetailsActivity : AppCompatActivity() {
    companion object {
        val realm = Realm.getDefaultInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_details)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(ComicsPageActivity.COMIC_ID, 1)
        val text = intent.getStringExtra(ComicsPageActivity.COMIC_TITLE)
        val info = intent.getStringExtra(ComicsPageActivity.COMIC_INFO)
        val imageUrl = intent.getStringExtra(ComicsPageActivity.COMIC_IMAGE)
        val url = intent.getStringExtra(ComicsPageActivity.COMIC_URL)

        comic_name.text = text
        comic_info.text = info

        val comicFromDatabase = ComicDetailsActivity.realm.where(RealmComicEntity::class.java)
            .equalTo("id", id)
            .findFirst()

        var favorite = comicFromDatabase!!.favorite

        if (favorite == true) {
            image_Fav_Button_comic.setImageResource(R.drawable.ic_star_solid)
        } else {
            image_Fav_Button_comic.setImageResource(R.drawable.ic_star_regular)
        }



        share_comic_detailview.setOnClickListener {
            PopUpWindow(id, url).show(supportFragmentManager, PopUpWindow.TAG)
            //PopUpWindow.newInstance("Log out", "Do you").show(supportFragmentManager, PopUpWindow.TAG)
        }


        image_Fav_Button_comic.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (comicFromDatabase.favorite == false) {
                    realm.executeTransaction {
                        comicFromDatabase!!.favorite = true
                        realm.copyToRealmOrUpdate(comicFromDatabase)
                    }
                    image_Fav_Button_comic.setImageResource(R.drawable.ic_star_solid)
                } else {
                    image_Fav_Button_comic.setImageResource(R.drawable.ic_star_regular)
                    realm.executeTransaction {
                        comicFromDatabase!!.favorite = false
                        realm.copyToRealmOrUpdate(comicFromDatabase)
                    }
                }
            }
        })

        Picasso.get().load(imageUrl?.replace("http", "https")).fit().into(comic_image)

        comic_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }


        //drawerListener()
        //displayCurrentUserInNav()
        //fetchUsersAndDisplayInNav()
    }

}


    /*private fun drawerListener (){
        drawerLayout_co_detail.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit_icon -> {
                //set boolean active in db to false when logging out:
                val ref = FirebaseDatabase.getInstance().getReference("/users")
                val user = Firebase.auth.currentUser
                val userid = user?.uid

                if (userid != null) {
                    ref.child(userid).child("active").setValue(false)
                }

                FirebaseAuth.getInstance().signOut()

                //go back to login intent:
                val intent = Intent(this, LoginPageActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        /*return if (toggle.onOptionsItemSelected(item)){
            return true
        }
        else{
            super.onOptionsItemSelected(item)
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun displayCurrentUserInNav(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val user = Firebase.auth.currentUser
        val userid = user?.uid

       /* if (userid != null) {
            ref.child(userid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("username").value.toString()
                    val image = snapshot.child("imageUrl").value.toString()
                    inlogged_username_co_detail.text = name

                    ComicsPageActivity.currentUser = snapshot.getValue(User::class.java)

                    Picasso.get()
                        .load(image)
                        .resize(50, 50)
                        .transform(CropCircleTransformation())
                        .into(inlogged_userImg_co_detail)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })*/
        }
    }






    private fun showDialog() {


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("Choose an user")

        val users = mutableListOf<UserItem>()
        val ref = FirebaseDatabase.getInstance().getReference("/users")


        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val user = UserItem(user)
                    users.add(user)

                    Log.d("UserList" , "${users}")
                   // builder.adapter
                }


                for (u in users) {
                    Log.d("usersCharacter", "users in list: ${u.user.username}")
                }


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val oldUser =
                        users.find { it.user.uid == user.uid } //hitta user i listan som har samma uid som den i db har som ändrats
                    oldUser?.user?.active = user.active


                }
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



        // Create a new AlertDialog using builder object
        val dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()

    }

    //Displays all users
    private fun fetchUsersAndDisplayInNav(){
        val users = mutableListOf<UserItem>()
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val adapterNav = GroupAdapter<GroupieViewHolder>()

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val user = UserItem(user)
                    adapterNav.add(user)
                    users.add(user)
                }
                //toolBar_RecyclerView_co_detail.adapter = adapterNav

                for (u in users) {
                    Log.d("usersCharacter", "users in list: ${u.user.username}")
                }

                adapterNav.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, SendMessageActivity::class.java)
                    intent.putExtra(ComicsPageActivity.USER_KEY, userItem.user)
                    intent.putExtra(ComicsPageActivity.USER_NAME, userItem.user.username)
                    //startActivity(intent)
                   // finish()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val oldUser =
                        users.find { it.user.uid == user.uid } //hitta user i listan som har samma uid som den i db har som ändrats
                    oldUser?.user?.active = user.active

                    adapterNav.notifyDataSetChanged()
                }
               // toolBar_RecyclerView_co_detail.adapter = adapterNav
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



/*override fun onDestroy() {
        //set boolean active in db to false when logging out:
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val user = Firebase.auth.currentUser
        val userid = user?.uid

        if (userid != null) {
            ref.child(userid).child("active").setValue(false)
        }
        FirebaseAuth.getInstance().signOut()
        super.onDestroy()
    }

}*/