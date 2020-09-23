package com.example.marvellisimo

import ComicItem
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.example.marvellisimo.activities.ComicDetailsActivity
import com.example.marvellisimo.activities.LoginPageActivity
import com.example.marvellisimo.activities.SendMessageActivity
import com.example.marvellisimo.entity.User
import com.example.marvellisimo.viewModel.ViewModelComicCharacterPage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_comic_page.*


class ComicsPageActivity : AppCompatActivity() {

    val modelComic: ViewModelComicCharacterPage by viewModels()
    var isClicked = true
    val activity = this
    val adapter = GroupAdapter<GroupieViewHolder>()

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(
                ConnectivityManager
                .EXTRA_NO_CONNECTIVITY, false)
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }



    val toggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
    }

    companion object {
        val COMIC_ID = "COMIC_ID"
        val COMIC_TITLE = "COMIC_TITLE"
        val COMIC_INFO = "COMIC_INFO"
        val COMIC_URL = "COMIC_URL"
        val COMIC_IMAGE = "COMIC_IMAGE"
        val COMIC_FAVORITE = "COMIC_FAVORITE"
        val USER_KEY = "USER_KEY"
        val USER_NAME = "USER_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_page)
        Realm.init(this)

        val configuration = RealmConfiguration.Builder()
            .name("comicDb")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(configuration)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerListener()
        displayCurrentUserInNav()
        fetchUsersAndDisplayInNav()
        filterComic()
        PrintToRecycleView()
        setFavButton();
        navButtons();
        clickToRecycleView()
    }

    private fun drawerListener (){
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit_icon ->{
                FirebaseAuth.getInstance().signOut()
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
        val dis_button = findViewById<Button>(R.id.comic_btn)
        dis_button.setEnabled(false);

        val button = findViewById<Button>(R.id.character_btn)
        button.setOnClickListener {
            val intent = Intent(this, CharactersPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFavButton() {
        val favButton: ImageButton = findViewById(R.id.filter_fav_comic_btn)

        favButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(arg0: View?) {
                if (isClicked) {
                    modelComic.getFavoriteComic().observe(activity, {
                        adapter.clear()
                        it.forEach { comic ->
                            adapter.add(ComicItem(comic))
                        }
                    })
                    favButton.setImageResource(R.drawable.ic_star_solid)
                } else {
                    favButton.setImageResource(R.drawable.ic_star_regular)
                    PrintToRecycleView()
                }
                isClicked = !isClicked;
            }
        })
    }

    private fun PrintToRecycleView() {
        //3party adapter https://github.com/lisawray/groupie ..
        modelComic.getComicData().observe(this, {
            adapter.clear()
            it.forEach { comic ->
                adapter.add(ComicItem(comic))
                Log.d("comicResult", "${comic.title}")
            }
        })
        recycle_view_comic.adapter = adapter
    }

    private fun clickToRecycleView() {
        adapter.setOnItemClickListener { item, view ->
            val comicItem = item as ComicItem
            val intent = Intent(this, ComicDetailsActivity::class.java)
            intent.putExtra(COMIC_ID, comicItem.comic.id)
            intent.putExtra(COMIC_TITLE, comicItem.comic.title)
            intent.putExtra(COMIC_IMAGE, comicItem.comic.thumbnail)
            intent.putExtra(COMIC_INFO, comicItem.comic.description)
            intent.putExtra(COMIC_FAVORITE, comicItem.comic.favorite)
            intent.putExtra(COMIC_URL, comicItem.comic.urls?.get(0)?.url)
            startActivity(intent)
        }
    }

    private fun filterComic(){
        search_bar_comic.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                modelComic.getSearchComicData(newText).observe(activity, {
                    adapter.clear()
                    it.forEach{ comic -> adapter.add(ComicItem(comic))
                        Log.d("filterdComic", "${comic.title}")}
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
                    inlogged_username.text = name
                    Picasso.get().load(image).into(inlogged_userImg)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    //Displays all users
    private fun fetchUsersAndDisplayInNav(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapterNav = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach{
                    Log.d("nav", "new massage")
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapterNav.add(UserItem(user))
                    }
                }
                toolBar_RecyclerView.adapter = adapterNav
                adapterNav.setOnItemClickListener{item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, SendMessageActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user.uid)
                    intent.putExtra(USER_NAME, userItem.user.username)
                    startActivity(intent)
                    /*Log.d("ToUser", "${userItem.user.username}")*/
                    finish()
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })


    }

    private fun disconnected() {
        Log.d("networkaccess", "disconnected")
        Toast.makeText(applicationContext,"Du är offline ", Toast.LENGTH_SHORT).show()

    }

    private fun connected() {
        Log.d("networkaccess", "connected")
    }













}



