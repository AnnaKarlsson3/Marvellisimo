

import android.R.attr.name
import android.R.attr.onClick
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.marvellisimo.R
import com.example.marvellisimo.ViewModel.Comic
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*


//adapterClass:
class ComicItem(val comic: Comic) : Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_comicname_row.text = comic.title
        //put image in: (use picasso lib, 3e party: https://github.com/square/picasso)
        var fav_ListButton : ImageButton = viewHolder.itemView.findViewById(R.id.image_Fav_Button)

        val imgsize = "/portrait_small"
        val img : String = renamePathHttps("${comic.thumbnail.path}.${comic.thumbnail.extension}")

        var isClicked = false
        Picasso.get().load(img).fit().into(viewHolder.itemView.imageView_comic_row)

        fav_ListButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(isClicked){
                    fav_ListButton.setImageResource(R.drawable.ic_star_solid)
                }else{
                    fav_ListButton.setImageResource(R.drawable.ic_star_regular)
                }
                isClicked = !isClicked;
            }

        });


    }

    //renders out the rows in view:
    override fun getLayout(): Int {
        return R.layout.comic_recycle_row_layout
    }

    fun renamePathHttps(path: String): String {
        return path.replace("http", "https")
    }
}

