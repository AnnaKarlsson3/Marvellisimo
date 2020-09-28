package com.example.marvellisimo.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.RealmComicEntity
import com.example.marvellisimo.entity.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_comic_details.*


class ComicDetailsActivity : AppCompatActivity() {
    companion object {
        val realm = Realm.getDefaultInstance()
        val USER_KEY = "user_key"
        val COMIC_KEY = "comic_key"

    }
    var comicId = 1


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        comicId = intent.getIntExtra(ComicsPageActivity.COMIC_ID, 1)
        when (item.itemId) {
            R.id.share -> {

                showPopup(comicId)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPopup(comicId :Int) {
        val popup = androidx.appcompat.widget.PopupMenu(this, share_comic_detailview)
        val users = mutableListOf<User>()


        //add menu items to popup menu and show popup menu
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var index = 0
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        users?.add(user)
                        popup.menu.add(Menu.NONE, index, index, user.username)
                        index++
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        popup.inflate(R.menu.popup_menu)

        val item = popup.menu.getItem(0)
        val spanString = SpannableString(item.title.toString())
        spanString.setSpan(ForegroundColorSpan(Color.YELLOW), 0, spanString.length, 0)
        item.title = spanString
        popup.show()

        //  handle menu item Clicks
        handleMenuItemClicks(popup, users, comicId)

    }

    private fun handleMenuItemClicks(
        popup: PopupMenu,
        users: MutableList<User>,
        comicId: Int
    ) {
        popup.setOnMenuItemClickListener {
            val id = it.itemId
            if (users != null) {
                for (user in users) {
                    if (id == users.indexOf(user)) {
                        val intent = Intent(this, SendMessageActivity::class.java)
                        intent.putExtra(USER_KEY, user)
                        intent.putExtra(COMIC_KEY, comicId)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_details)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        comicId = intent.getIntExtra(ComicsPageActivity.COMIC_ID, 1)
        val text = intent.getStringExtra(ComicsPageActivity.COMIC_TITLE)
        val info = intent.getStringExtra(ComicsPageActivity.COMIC_INFO)
        val imageUrl = intent.getStringExtra(ComicsPageActivity.COMIC_IMAGE)
        val url = intent.getStringExtra(ComicsPageActivity.COMIC_URL)

        comic_name.text = text
        comic_info.text = info

        val comicFromDatabase = ComicDetailsActivity.realm.where(RealmComicEntity::class.java)
            .equalTo("id", comicId)
            .findFirst()

        var favorite = comicFromDatabase!!.favorite

        if (favorite == true) {
            image_Fav_Button_comic.setImageResource(R.drawable.ic_star_solid)
        } else {
            image_Fav_Button_comic.setImageResource(R.drawable.ic_star_regular)
        }


        onClickFavButton(comicFromDatabase)

        Picasso.get().load(imageUrl?.replace("http", "https")).fit().into(comic_image)

        onClickReadmore(url)
        share_comic_detailview.setOnClickListener {
            showPopup(comicId)
        }


    }


    private fun onClickReadmore(url: String?) {
        comic_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun onClickFavButton(comicFromDatabase: RealmComicEntity) {
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
    }


}
















