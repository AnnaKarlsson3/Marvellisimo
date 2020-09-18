package com.example.marvellisimo.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.marvellisimo.CharactersPageActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_character_details.*
import kotlinx.android.synthetic.main.activity_comic_details.*




class ComicDetailsActivity : AppCompatActivity() {
    companion object {
        val realm = Realm.getDefaultInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_details)

        val id = intent.getIntExtra(ComicsPageActivity.COMIC_ID, 1)
        val text = intent.getStringExtra(ComicsPageActivity.COMIC_TITLE)
        val info = intent.getStringExtra(ComicsPageActivity.COMIC_INFO)
        val imageUrl = intent.getStringExtra(ComicsPageActivity.COMIC_IMAGE)
        val url = intent.getStringExtra(ComicsPageActivity.COMIC_URL)

        supportActionBar?.title = text
        comic_name.text = text
        comic_info.text = info

        val comicFromDatabase = ComicDetailsActivity.realm.where(RealmCharacterEntity::class.java)
            .equalTo("id", id)
            .findFirst()

        val favorite = comicFromDatabase!!.favorite

        if (favorite == true) {
                image_Fav_Button_comic.setImageResource(R.drawable.ic_star_solid)
            } else {
                image_Fav_Button_comic.setImageResource(R.drawable.ic_star_regular)
            }

        image_Fav_Button_comic.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (favorite == false) {
                    realm.executeTransaction {
                        comicFromDatabase!!.favorite = true
                        realm.copyToRealmOrUpdate(comicFromDatabase)
                    }
                    finish()
                    startActivity(intent)

                } else {
                    realm.executeTransaction {
                        comicFromDatabase!!.favorite = false
                        realm.copyToRealmOrUpdate(comicFromDatabase)
                    }
                    finish()
                    startActivity(intent)
                }
            }
        });

        Picasso.get().load(imageUrl?.replace("http","https")).fit().into(comic_image)

        comic_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

    }

}


