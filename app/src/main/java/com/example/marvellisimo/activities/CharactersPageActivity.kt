package com.example.marvellisimo

import CharacterItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Button


import android.widget.ImageButton
import android.widget.SearchView

import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.marvellisimo.activities.CharacterDetailsActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.marvellisimo.activities.LoginPageActivity
import com.example.marvellisimo.activities.SendMessageActivity
import com.example.marvellisimo.entity.User

import com.example.marvellisimo.viewModel.ViewModelComicCharacterPage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_characters_page.*


class CharactersPageActivity : AppCompatActivity() {

    val modelCharacter: ViewModelComicCharacterPage by viewModels()
    var isClicked = false
    val activity = this
    val adapter = GroupAdapter<GroupieViewHolder>()
    val manager = LinearLayoutManager(this)
    var isScrolling = false
    var current = 0
    var totalOnRecycleView = 0
    var scrolledOut = 0
    var offset = 0
    var totalCharacterFromApi = 0

    val toggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(this, drawerLayout_character, R.string.open, R.string.close)
    }

    companion object {
        val CHAR_ID = "CHAR_ID"
        val CHAR_KEY = "CHAR_KEY"
        val CHAR_NAME = "CHAR_NAME"
        val CHAR_INFO = "CHAR_INFO"
        val CHAR_URL = "CHAR_URL"
        val CHAR_IMAGE = "CHAR_IMAGE"
        val CHAR_FAVORITE = "CHAR_FAVORITE"
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_page)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        drawerListener()
        displayCurrentUserInNav()
        fetchUsersAndDisplayInNav()
        filterCharacter()
        PrintToRecycleView()
        navButtons()
        setFavButton();
        clickOnRecycleView()
        onScrolling()
    }

    private fun drawerListener (){
        drawerLayout_character.addDrawerListener(toggle)
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

    private fun navButtons() {
        val dis_button = findViewById<Button>(R.id.character_btn)
        dis_button.setEnabled(false);

        val button = findViewById<Button>(R.id.comic_btn)
        button.setOnClickListener {
            val intent = Intent(this, ComicsPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFavButton() {
        val favButton: ImageButton = findViewById(R.id.filter_fav_character_btn)

        favButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(arg0: View?) {
                isClicked = !isClicked;
                if (isClicked) {
                    modelCharacter.getFavoriteCharacter().observe(activity, {
                        adapter.clear()
                        it.forEach { character ->
                            adapter.add(CharacterItem(character))
                        }
                    })

                    favButton.setImageResource(R.drawable.ic_star_solid)
                } else {
                    favButton.setImageResource(R.drawable.ic_star_regular)
                    modelCharacter.characterResults.observe(activity,{
                        adapter.clear()
                        it.forEach { character ->
                            adapter.add(CharacterItem(character))
                        }
                    })
                    //PrintToRecycleView()
                }

            }
        })
    }

    private fun PrintToRecycleView() {

        //3party adapter https://github.com/lisawray/groupie ..
        modelCharacter.getCharacterData(offset).observe(this, {
            adapter.clear()
            it.forEach { character ->
                adapter.add(CharacterItem(character))
                Log.d("characterResult", "${character.name}")
            }
        })

        recycle_view_character.adapter = adapter


    }

    private fun onScrolling() {
        recycle_view_character.layoutManager = manager

        recycle_view_character.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                current = manager.childCount
                totalOnRecycleView = manager.itemCount
                scrolledOut = manager.findFirstVisibleItemPosition()

                totalCharacterFromApi = modelCharacter.getTotalCharacterCount()
                val limit = modelCharacter.getCharacterLimit()
                Log.d("C", "character limit:${limit}")
                Log.d("C", "character offset: ${offset}")
                Log.d("C", "character total in Api: ${totalCharacterFromApi}")

                println("character--- total in recycleview: ${totalOnRecycleView}, current:${current}, scrolled${scrolledOut}")

                if (isScrolling && (scrolledOut + current) >= (offset + limit) && offset < totalCharacterFromApi) {
                    offset += limit
                    modelCharacter.getCharacterData(offset)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun clickOnRecycleView() {
        adapter.setOnItemClickListener { item, view ->
            val characterItem = item as CharacterItem
            val intent = Intent(this, CharacterDetailsActivity::class.java)

            //intent.putExtra(CHAR_KEY, characterItem.character)
            intent.putExtra(CHAR_ID, characterItem.character.id)
            intent.putExtra(CHAR_NAME, characterItem.character.name)
            intent.putExtra(CHAR_IMAGE, characterItem.character.thumbnail)
            intent.putExtra(CHAR_INFO, characterItem.character.description)
            intent.putExtra(CHAR_FAVORITE, characterItem.character.favorite)
            intent.putExtra(CHAR_URL, characterItem.character.urls?.get(0)?.url)
            startActivity(intent)
        }
    }

    private fun filterCharacter() {
        search_bar_character.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                modelCharacter.getSearchCharacterData(newText).observe(activity, {
                    adapter.clear()
                    it.forEach { character ->
                        adapter.add(CharacterItem(character))
                        Log.d("filterdCharacter", "${character.name}")
                    }
                })
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })
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
                    inlogged_username_character.text = name

                    ComicsPageActivity.currentUser = snapshot.getValue(User::class.java)

                    Picasso.get()
                        .load(image)
                        .resize(50, 50)
                        .transform(CropCircleTransformation())
                        .into(inlogged_userImg_character)
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
                    val useritem = UserItem(user)
                    adapterNav.add(useritem)
                    users.add(useritem)
                }
                toolBar_RecyclerView_character.adapter = adapterNav

                for(u in users){
                    Log.d("usersCharacter", "users in list: ${u.user.username}")}

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
                toolBar_RecyclerView_character.adapter = adapterNav
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java) ?: return
                val userItem=users.find { it.user.uid==user.uid }!!

                users.remove(userItem)
                adapterNav.remove(userItem)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}
