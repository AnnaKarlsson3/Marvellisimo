package com.example.marvellisimo.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marvellisimo.CharactersPageActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.ViewModel.Character
import com.example.marvellisimo.ViewModel.Comic
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character_details.*
import kotlinx.android.synthetic.main.activity_comic_details.*

class ComicDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_details)

        val comic = intent.getParcelableExtra<Comic>(ComicsPageActivity.COMIC_KEY)

            supportActionBar?.title = comic?.title

            comic_name.text = comic?.title
            comic_info.text = comic?.description



        if (comic != null) {
            var img = "${comic.thumbnail?.path}.${comic.thumbnail.extension}".replace("http","https")
            Picasso.get().load(img).fit().into(comic_image)
        }else{
            comic_name.text =""
        }

        comic_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            if (comic != null) {
                intent.data = Uri.parse(comic.urls[0].url)
            }
            startActivity(intent)
        }

    }
}