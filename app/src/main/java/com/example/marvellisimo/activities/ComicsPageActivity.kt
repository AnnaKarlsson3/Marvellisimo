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
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvellisimo.activities.ComicDetailsActivity
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
import io.realm.Realm
import io.realm.RealmConfiguration
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_comic_page.*


class ComicsPageActivity : AppCompatActivity() {

    val modelComic: ViewModelComicCharacterPage by viewModels()
    var isClicked = true
    val activity = this
    val adapter = GroupAdapter<GroupieViewHolder>()
    val manager = LinearLayoutManager(this)
    var isScrolling = false
    var current = 0
    var totalOnRecycleView = 0
    var scrolledOut = 0
    var offset = 0
    var totalComicFromApi = 0


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
        var currentUser: User? = null
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
        onScrolling()

        val user = Firebase.auth.currentUser

        if (user==null) {
            startActivity(Intent(this, LoginPageActivity::class.java))
        }
    }

    private fun drawerListener() {
        drawerLayout.addDrawerListener(toggle)
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

        return if (toggle.onOptionsItemSelected(item)) {
            return true
        } else {
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
        modelComic.getComicData(offset).observe(this, {
            adapter.clear()
            it.forEach { comic ->
                adapter.add(ComicItem(comic))
                Log.d("comicResult", "${comic.title}")
            }
        })
        recycle_view_comic.adapter = adapter


    }

    private fun onScrolling() {
        recycle_view_comic.layoutManager = manager

        recycle_view_comic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                totalComicFromApi = modelComic.getTotalComicCount()
                val limit = modelComic.getComicLimit()
                Log.d("C", "comic limit:${limit}")
                Log.d("C", "comic offset: ${offset}")
                Log.d("C", "comic total in Api: ${totalComicFromApi}")

                println("comic--- total in recycleview: ${totalOnRecycleView}, current:${current}, scrolled${scrolledOut}")



                if (isScrolling && (current + scrolledOut == offset + limit) && offset < totalComicFromApi) {
                    offset += limit

                    modelComic.getComicData(offset)
                    adapter.notifyDataSetChanged()

                }
            }
        })
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

    private fun filterComic() {
        search_bar_comic.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                modelComic.getSearchComicData(newText).observe(activity, {
                    adapter.clear()
                    it.forEach { comic ->
                        adapter.add(ComicItem(comic))
                        Log.d("filterdComic", "${comic.title}")
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

    private fun displayCurrentUserInNav() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val user = Firebase.auth.currentUser
        val userid = user?.uid

        if (userid != null) {
            ref.child(userid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("username").value.toString()
                    val image = snapshot.child("imageUrl").value.toString()
                    inlogged_username.text = name

                    currentUser = snapshot.getValue(User::class.java)

                    Picasso.get()
                        .load(image)
                        /*.resize(100, 100)*/
                        .transform(CropCircleTransformation())
                        .into(inlogged_userImg)
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

        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                    if(user != null){
                        val user=UserItem(user)
                        adapterNav.add(user)
                         users.add(user)
                    }
                toolBar_RecyclerView.adapter = adapterNav

                for(u in users){
                    Log.d("users", "users in list: ${u.user.username}")}

                adapterNav.setOnItemClickListener{item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, SendMessageActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    intent.putExtra(USER_NAME, userItem.user.username)
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
                toolBar_RecyclerView.adapter = adapterNav
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




