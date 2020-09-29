package com.example.marvellisimo.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

        val comicFromDatabase = realm.where(RealmComicEntity::class.java)
            .equalTo("id", id)
            .findFirst()

        var favorite = comicFromDatabase!!.favorite

        if (favorite) {
            image_Fav_Button_comic.setImageResource(R.drawable.ic_star_solid)
        } else {
            image_Fav_Button_comic.setImageResource(R.drawable.ic_star_regular)
        }

        share_comic_detailview.setOnClickListener {
            PopUpWindow(id, url).show(supportFragmentManager, PopUpWindow.TAG)
        }

        image_Fav_Button_comic.setOnClickListener {
            if (!comicFromDatabase.favorite) {
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

        Picasso.get().load(imageUrl?.replace("http", "https")).fit().into(comic_image)

        comic_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }


    }

}