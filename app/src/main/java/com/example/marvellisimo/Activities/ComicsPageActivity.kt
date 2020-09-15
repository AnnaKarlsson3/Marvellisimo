package com.example.marvellisimo

import ComicItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import com.example.marvellisimo.Activities.ComicDetailsActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ViewModel.ViewModelComicCharacter
import com.example.marvellisimo.entity.RealmComicEntity
import com.example.marvellisimo.entity.UrlDb
import com.example.marvellisimo.ViewModel.ViewModelDataPopulator
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_comic_page.*


class ComicsPageActivity : AppCompatActivity() {
    val model: ViewModelDataPopulator by viewModels()
    val modelComic : ViewModelComicCharacter by viewModels()
    var isClicked = false


    companion object {
        val COMIC_KEY = "COMIC_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_page)
        Realm.init(this)

        val configuration = RealmConfiguration.Builder()
            .name("comicDb")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(configuration)
        val realm = Realm.getInstance(configuration)

        val modelComic: ViewModelComicCharacter by viewModels ()

       modelComic.getComics().observe(this,{
           
       })



        dataCashing(realm)
        PrintToRecycleView()
        setFavButton();
        navButtons();
    }

    private fun navButtons() {
        val dis_button = findViewById<Button>(R.id.comic_btn)
        dis_button.setEnabled(false);

        val button = findViewById<Button>(R.id.character_btn)
        button.setOnClickListener {
            val intent = Intent(this, CharactersPageActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setFavButton() {
        val favButton: ImageButton = findViewById(R.id.filter_fav_image_btn)

        favButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(arg0: View?) {
                if (isClicked) {
                    favButton.setImageResource(R.drawable.ic_star_solid)
                } else {
                    favButton.setImageResource(R.drawable.ic_star_regular)
                }
                isClicked = !isClicked;
            }
        })
    }

    private fun PrintToRecycleView() {
        //3party adapter https://github.com/lisawray/groupie ..
        val adapter = GroupAdapter<GroupieViewHolder>()

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

    private fun dataCashing(realm: Realm) {

        model.comicDataWrapper.observe(this, {
            it.data.results.forEach { c ->
                realm.executeTransactionAsync(fun(realm: Realm) {
                    val comic = RealmComicEntity().apply {
                        id = c.id
                        title = c.title
                        description = c.description
                        thumbnail = "${c.thumbnail.path}.${c.thumbnail.extension}"
                        urls?.addAll(c.urls.map {
                            UrlDb().apply {
                                type = "string"
                                url = it.toString()
                            }
                        })
                    }
                    realm.copyToRealmOrUpdate(comic)

                }, fun() {
                    android.util.Log.d("database", "uploaded")
                })
            }//foreach end
        })

    }


}


