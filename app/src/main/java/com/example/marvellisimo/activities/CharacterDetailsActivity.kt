package com.example.marvellisimo.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.CharactersPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.squareup.picasso.Picasso
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_character_details.*


class CharacterDetailsActivity : AppCompatActivity() {
    companion object {
        val realm = Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(CharactersPageActivity.CHAR_ID, 1)
        val text = intent.getStringExtra(CharactersPageActivity.CHAR_NAME)
        val info = intent.getStringExtra(CharactersPageActivity.CHAR_INFO)
        val imageUrl = intent.getStringExtra(CharactersPageActivity.CHAR_IMAGE)
        val url = intent.getStringExtra(CharactersPageActivity.CHAR_URL)

        val characterFromDatabase = realm.where(RealmCharacterEntity::class.java)
            .equalTo("id", id)
            .findFirst()

        var favorite = characterFromDatabase!!.favorite

        character_name.text = text
        character_info.text = info

        Picasso.get().load(imageUrl?.replace("http", "https")).fit().into(character_image)

        if (favorite) {
            image_Fav_Button_character.setImageResource(R.drawable.ic_star_solid)
        } else {
            image_Fav_Button_character.setImageResource(R.drawable.ic_star_regular)
        }

        share_character_detailview.setOnClickListener {
            PopUpWindow(id, url).show(supportFragmentManager, PopUpWindow.TAG)
        }

        image_Fav_Button_character.setOnClickListener {
            if (!characterFromDatabase.favorite) {
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

        character_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}

