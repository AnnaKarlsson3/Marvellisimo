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
import kotlinx.android.synthetic.main.activity_comic_details.*


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

        if (favorite == true) {
            image_Fav_Button_character.setImageResource(R.drawable.ic_star_solid)
        } else {
            image_Fav_Button_character.setImageResource(R.drawable.ic_star_regular)
        }

        share_character_detailview.setOnClickListener {
            PopUpWindow(id, url).show(supportFragmentManager, PopUpWindow.TAG)
            //PopUpWindow.newInstance("Log out", "Do you").show(supportFragmentManager, PopUpWindow.TAG)
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
    }

}

