package com.example.marvellisimo.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.UserItem
import com.example.marvellisimo.entity.RealmComicEntity
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