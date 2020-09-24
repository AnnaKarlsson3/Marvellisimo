package com.example.marvellisimo.activities

import CharacterItem
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.CharactersPageActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.Character
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

        supportActionBar?.title = text
        character_name.text = text
        character_info.text = info

        Picasso.get().load(imageUrl?.replace("http", "https")).fit().into(character_image)


        if (favorite == true) {
            image_Fav_Button_character.setImageResource(R.drawable.ic_star_solid)
        } else {
            image_Fav_Button_character.setImageResource(R.drawable.ic_star_regular)
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

        //only passes data to sendmessage view without specifying which user data is supposed to be sent to
      share_character_detailview.setOnClickListener{
          val intent = Intent(this, SendMessageActivity::class.java)
          intent.putExtra("share_character_text", text )
          startActivity(intent)

      }








    }






}

