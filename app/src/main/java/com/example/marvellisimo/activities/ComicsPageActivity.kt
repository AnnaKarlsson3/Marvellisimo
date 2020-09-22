package com.example.marvellisimo

import ComicItem
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
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
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat.setBackground
import androidx.lifecycle.observe
import com.example.marvellisimo.activities.ComicDetailsActivity
import com.example.marvellisimo.activities.LoginPageActivity
import com.example.marvellisimo.viewModel.ViewModelComicCharacterPage
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_comic_page.*
import java.io.PrintWriter


class ComicsPageActivity : AppCompatActivity() {

    val modelComic: ViewModelComicCharacterPage by viewModels()
    var isClicked = true
    val activity = this
    val adapter = GroupAdapter<GroupieViewHolder>()

    val toggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
    }

    companion object {
        val COMIC_ID = "COMIC_ID"
        val COMIC_KEY = "COMIC_KEY"
        val COMIC_TITLE = "COMIC_TITLE"
        val COMIC_INFO = "COMIC_INFO"
        val COMIC_URL = "COMIC_URL"
        val COMIC_IMAGE = "COMIC_IMAGE"
        val COMIC_FAVORITE = "COMIC_FAVORITE"
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(
                ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false
            )
            if (notConnected) {
                disconnected()
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
        //val realm = Realm.getDefaultInstance()

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerListener()
        friendNavRecycle()
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
            //intent.putExtra(COMIC_KEY, comicItem.comic)
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



        private fun friendNavRecycle(){
            val adapterTest = GroupAdapter<GroupieViewHolder>()
            adapterTest.add(TestNavRecItem())
            adapterTest.add(TestNavRecItem())
            adapterTest.add(TestNavRecItem())
            toolBar_RecyclerView.adapter = adapterTest
        }



        private fun disconnected() {
            Toast.makeText(applicationContext,"Du är offline ", Toast.LENGTH_SHORT).show()
        }

}


