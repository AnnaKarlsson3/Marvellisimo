package com.example.marvellisimo.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ApiService.apiKey
import com.example.marvellisimo.ApiService.getMD5
import com.example.marvellisimo.CharactersPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.ViewModel.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character_details.*


class CharacterDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        val character = intent.getParcelableExtra<Character>(CharactersPageActivity.CHAR_KEY)
        supportActionBar?.title = character?.name

        character_name.text = character?.name
        character_info.text = character?.description


        val img : String = "${character?.thumbnail?.path}.${character?.thumbnail?.extension}".replace(
            "http",
            "https"
        )

        Picasso.get().load(img).fit().into(character_image)

        character_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            if (character != null) {
                intent.data = Uri.parse(character.urls[0].url)
            }
            startActivity(intent)
        }


    }

}