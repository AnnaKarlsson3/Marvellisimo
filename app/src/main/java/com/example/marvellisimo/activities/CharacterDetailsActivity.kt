package com.example.marvellisimo.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.CharactersPageActivity
import com.example.marvellisimo.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character_details.*


class CharacterDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)
        supportActionBar?.setDisplayShowTitleEnabled(false)



        val text = intent.getStringExtra(CharactersPageActivity.CHAR_NAME)
        val info = intent.getStringExtra(CharactersPageActivity.CHAR_INFO)
        val imageUrl = intent.getStringExtra(CharactersPageActivity.CHAR_IMAGE)
        val url = intent.getStringExtra(CharactersPageActivity.CHAR_URL)



        character_name.text = text
        character_info.text = info

        Picasso.get().load(imageUrl?.replace("http","https")).fit().into(character_image)


        character_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            startActivity(intent)
        }


    }

}