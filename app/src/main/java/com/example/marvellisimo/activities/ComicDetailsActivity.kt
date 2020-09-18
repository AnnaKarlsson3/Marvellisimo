package com.example.marvellisimo.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
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
        val favorite = intent.getBooleanExtra(ComicsPageActivity.COMIC_FAVORITE , false)

        supportActionBar?.title = text
        comic_name.text = text
        comic_info.text = info



            if (favorite == true) {
                image_Fav_Button_comic.setImageResource(R.drawable.ic_star_solid)
            } else {
                image_Fav_Button_comic.setImageResource(R.drawable.ic_star_regular)
            }


        Picasso.get().load(imageUrl?.replace("http","https")).fit().into(comic_image)

        comic_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)

            intent.data = Uri.parse(url)

            startActivity(intent)
        }

    }

}


