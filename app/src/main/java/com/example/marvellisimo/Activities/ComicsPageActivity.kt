package com.example.marvellisimo

import ComicItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.viewModels
import com.example.marvellisimo.Activities.ComicDetailsActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.example.marvellisimo.ViewModel.ViewModelComicCharacter
import com.example.marvellisimo.entity.RealmComicEntity
import com.example.marvellisimo.entity.UrlDb
import com.example.marvellisimo.ViewModel.ViewModelDataPopulator
import com.example.marvellisimo.ViewModel.asLiveData
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_comic_page.*


class ComicsPageActivity : AppCompatActivity() {
    val model: ViewModelDataPopulator by viewModels()
    val modelComic: ViewModelComicCharacter by viewModels()
    var isClicked = true
    val activity = this
    val adapter = GroupAdapter<GroupieViewHolder>()


    companion object {
        val COMIC_KEY = "COMIC_KEY"
        val COMIC_TITLE = "COMIC_TITLE"
        val COMIC_INFO = "COMIC_INFO"
        val COMIC_URL = "COMIC_URL"
        val COMIC_IMAGE = "COMIC_IMAGE"
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
        val realm = Realm.getDefaultInstance()


        dataCashing(realm)
        PrintToRecycleView()
        filterComic()

        navButtons()
        setFavButton()
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
        val favButton: ImageButton = findViewById(R.id.filter_fav_comic_btn)

        favButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(arg0: View?) {
                if (isClicked) {
                    modelComic.getFavoriteComic().observe(activity, {
                        adapter.clear()
                        it.forEach { comic ->
                            adapter.add(ComicItem(comic))
                        }
                    })

                    favButton.setImageResource(R.drawable.ic_star_solid)
                } else {
                    favButton.setImageResource(R.drawable.ic_star_regular)
                    PrintToRecycleView()
                }
                isClicked = !isClicked;
            }

        })
    }

    private fun PrintToRecycleView() {
        //3party adapter https://github.com/lisawray/groupie ..


        modelComic.getComicData().observe(this, {
            adapter.clear()
            it.forEach { comic ->
                adapter.add(ComicItem(comic))
                Log.d("comicResult", "${comic.title}" + "${comic.favorite}")
            }
        })

        /*model.comicDataWrapper.observe(this, {
            it.data.results.forEach { comic -> adapter.add(ComicItem(comic)) }
        })*/

        adapter.setOnItemClickListener { item, view ->
            val comicItem = item as ComicItem
            val intent = Intent(this, ComicDetailsActivity::class.java)
            //intent.putExtra(COMIC_KEY, comicItem.comic)
            intent.putExtra(COMIC_TITLE, comicItem.comic.title)
            intent.putExtra(COMIC_IMAGE, comicItem.comic.thumbnail)
            intent.putExtra(COMIC_INFO, comicItem.comic.description)
            intent.putExtra(COMIC_URL, comicItem.comic.urls?.get(0)?.url)

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
                        favorite = c.favorite;
                        urls?.addAll(c.urls.map {
                            UrlDb().apply {
                                type = it.type
                                url = it.url
                            }
                        })
                    }
                    realm.insertOrUpdate(comic)

                }, fun() {
                    android.util.Log.d("database", "uploaded")
                })
            }//foreach end
        })

    }

    private fun filterComic() {

        search_bar_comic.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                modelComic.getSearchComicData(newText).observe(activity, {
                    adapter.clear()
                    it.forEach { comic ->
                        adapter.add(ComicItem(comic))
                        Log.d("filterdComic", "${comic.title}")
                    }
                })

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })
    }


}


