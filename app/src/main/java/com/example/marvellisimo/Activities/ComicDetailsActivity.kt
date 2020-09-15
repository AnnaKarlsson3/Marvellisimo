package com.example.marvellisimo.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.RealmComicEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comic_details.*

class ComicDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_details)

        val text = intent.getStringExtra(ComicsPageActivity.COMIC_TITLE)
        val info = intent.getStringExtra(ComicsPageActivity.COMIC_INFO)
        val imageUrl = intent.getStringExtra(ComicsPageActivity.COMIC_IMAGE)
        val url = intent.getStringExtra(ComicsPageActivity.COMIC_URL)

        supportActionBar?.title = text

        comic_name.text = text
        comic_info.text = info

        Picasso.get().load(imageUrl?.replace("http","https")).fit().into(comic_image)

        comic_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)

            intent.data = Uri.parse(url)

            startActivity(intent)
        }

    }
}