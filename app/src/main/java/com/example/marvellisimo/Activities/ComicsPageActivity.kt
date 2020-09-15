package com.example.marvellisimo

import ComicItem
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import com.example.marvellisimo.ViewModel.Character
import com.example.marvellisimo.ViewModel.Comic
import com.example.marvellisimo.Activities.CharacterDetailsActivity
import com.example.marvellisimo.Activities.ComicDetailsActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ViewModel.ViewModelComicCharacterPage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_comic_page.*
import kotlinx.android.synthetic.main.character_recycle_row_layout.*
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*
import okhttp3.Call
import okhttp3.Response
import java.net.CacheResponse

class ComicsPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_page)

        PrintToRecycleView()
        setFavButton();
        navButtons();

    }



    private fun navButtons(){
        val dis_button = findViewById<Button>(R.id.comic_btn)
        dis_button.setEnabled(false);
        dis_button.paintFlags = dis_button.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val button = findViewById<Button>(R.id.character_btn)

        button.setOnClickListener{
            val intent = Intent(this, CharactersPageActivity::class.java)
            startActivity(intent)
        }

    }

    var isClicked = false
    private fun setFavButton(){
        val favButton: ImageButton = findViewById(R.id.filter_fav_image_btn)

        favButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(arg0: View?) {
                if(isClicked){
                    favButton.setImageResource(R.drawable.ic_star_solid)
                }else{
                    favButton.setImageResource(R.drawable.ic_star_regular)
                }
                isClicked = !isClicked;
            }
        })
    }
    companion object{
        val COMIC_KEY = "COMIC_KEY"
    }


    private fun PrintToRecycleView(){
        //3party adapter https://github.com/lisawray/groupie ..
        val adapter = GroupAdapter<GroupieViewHolder>()
        val model: ViewModelComicCharacterPage by viewModels()


        model.comicDataWrapper.observe(this, {
            it.data.results.forEach { comic -> adapter.add(ComicItem(comic)) }
        })

        adapter.setOnItemClickListener { item, view ->
            val comicItem = item as ComicItem
            val intent = Intent(this, ComicDetailsActivity::class.java)
            intent.putExtra(COMIC_KEY, comicItem.comic)
            startActivity(intent)
        }

        recycle_view_comic.adapter = adapter


    }


}


