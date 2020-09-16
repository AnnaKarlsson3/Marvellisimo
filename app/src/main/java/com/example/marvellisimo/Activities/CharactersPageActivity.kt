package com.example.marvellisimo

import CharacterItem
import com.example.marvellisimo.R
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Button



import android.widget.ImageButton

import androidx.activity.viewModels
import com.example.marvellisimo.Activities.CharacterDetailsActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ViewModel.ViewModelComicCharacterPage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_characters_page.*
import kotlinx.android.synthetic.main.character_recycle_row_layout.view.*




class CharactersPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_page)

        PrintToRecycleView()
        navButtons()
        setFavButton();


    }

    private fun navButtons(){

        val dis_button = findViewById<Button>(R.id.character_btn)
        dis_button.paintFlags = dis_button.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        dis_button.setEnabled(false);

        val button = findViewById<Button>(R.id.comic_btn)
        button.setOnClickListener{
            val intent = Intent(this, ComicsPageActivity::class.java)
            startActivity(intent)
        }

    }

    var isClicked = true
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
        val CHAR_KEY = "CHAR_KEY"
    }


    private fun PrintToRecycleView(){
        //3party adapter https://github.com/lisawray/groupie ..
        val adapter = GroupAdapter<GroupieViewHolder>()
        val model: ViewModelComicCharacterPage by viewModels()

        model.characterDataWrapper.observe(this, {
            it.data.results.forEach { character -> adapter.add(CharacterItem(character)) }
        })


        adapter.setOnItemClickListener { item, view ->
            val characterItem = item as CharacterItem
            val intent = Intent(this, CharacterDetailsActivity::class.java)
            intent.putExtra(CHAR_KEY, characterItem.character)
            startActivity(intent)
        }
        recycle_view_character.adapter = adapter
    }




}
