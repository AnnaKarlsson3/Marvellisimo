package com.example.marvellisimo.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.CharactersPageActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.UserItem
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.example.marvellisimo.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.realm.Realm
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_character_details.*
import kotlinx.android.synthetic.main.activity_characters_page.*


class CharacterDetailsActivity : AppCompatActivity() {
    companion object {
        val realm = Realm.getDefaultInstance()
    }

    val toggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(this, drawerLayout_ch_detail, R.string.open, R.string.close)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val id = intent.getIntExtra(CharactersPageActivity.CHAR_ID, 1)
        val text = intent.getStringExtra(CharactersPageActivity.CHAR_NAME)
        val info = intent.getStringExtra(CharactersPageActivity.CHAR_INFO)
        val imageUrl = intent.getStringExtra(CharactersPageActivity.CHAR_IMAGE)
        val url = intent.getStringExtra(CharactersPageActivity.CHAR_URL)

        val characterFromDatabase = realm.where(RealmCharacterEntity::class.java)
            .equalTo("id", id)
            .findFirst()


        var favorite = characterFromDatabase!!.favorite

        supportActionBar?.title = text
        character_name.text = text
        character_info.text = info

        Picasso.get().load(imageUrl?.replace("http", "https")).fit().into(character_image)


        if (favorite == true) {
            image_Fav_Button_character.setImageResource(R.drawable.ic_star_solid)
        } else {
            image_Fav_Button_character.setImageResource(R.drawable.ic_star_regular)
        }

        image_Fav_Button_character.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (characterFromDatabase.favorite == false) {
                    realm.executeTransaction {
                        characterFromDatabase!!.favorite = true
                        realm.copyToRealmOrUpdate(characterFromDatabase)
                    }
                    image_Fav_Button_character.setImageResource(R.drawable.ic_star_solid)

                } else {

                    realm.executeTransaction {
                        characterFromDatabase!!.favorite = false
                        realm.copyToRealmOrUpdate(characterFromDatabase)
                    }
                    image_Fav_Button_character.setImageResource(R.drawable.ic_star_regular)

                }
            }
        })


        character_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        drawerListener()
        displayCurrentUserInNav()
        fetchUsersAndDisplayInNav()

    }

    private fun drawerListener (){
        drawerLayout_ch_detail.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit_icon ->{
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

        return if (toggle.onOptionsItemSelected(item)){
            return true
        }
        else{
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun displayCurrentUserInNav(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val user = Firebase.auth.currentUser
        val userid = user?.uid

        if (userid != null) {
            ref.child(userid).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("username").value.toString()
                    val image = snapshot.child("imageUrl").value.toString()
                    inlogged_username_ch_detail.text = name

                    ComicsPageActivity.currentUser = snapshot.getValue(User::class.java)

                    Picasso.get()
                        .load(image)
                        .resize(50, 50)
                        .transform(CropCircleTransformation())
                        .into(inlogged_userImg_ch_detail)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }


    //Displays all users
    private fun fetchUsersAndDisplayInNav(){
        val users = mutableListOf<UserItem>()
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val adapterNav = GroupAdapter<GroupieViewHolder>()

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if(user != null){
                    val user = UserItem(user)
                    adapterNav.add(user)
                    users.add(user)
                }
                toolBar_RecyclerView_c_detail.adapter = adapterNav

                for(u in users){
                    Log.d("usersCharacter", "users in list: ${u.user.username}")}

                adapterNav.setOnItemClickListener{item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, SendMessageActivity::class.java)
                    intent.putExtra(ComicsPageActivity.USER_KEY, userItem.user)
                    intent.putExtra(ComicsPageActivity.USER_NAME, userItem.user.username)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val oldUser = users.find { it.user.uid == user.uid } //hitta user i listan som har samma uid som den i db har som ändrats
                    oldUser?.user?.active = user.active

                    adapterNav.notifyDataSetChanged()
                }
                toolBar_RecyclerView_c_detail.adapter = adapterNav
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
    override fun onDestroy() {
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


}

