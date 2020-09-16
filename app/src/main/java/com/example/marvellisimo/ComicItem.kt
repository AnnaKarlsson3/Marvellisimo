import android.view.View
import android.widget.ImageButton
import com.example.marvellisimo.R
import com.example.marvellisimo.ViewModel.Comic
import com.example.marvellisimo.entity.RealmCharacterEntity
import com.example.marvellisimo.entity.RealmComicEntity
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.realm.Realm
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*

//adapterClass:
class ComicItem(val comic: RealmComicEntity) : Item<GroupieViewHolder>() {

    companion object {
        val realm = Realm.getDefaultInstance()
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_comicname_row.text = comic.title
        var favorite = comic.favorite

        val img: String = renamePathHttps("${comic.thumbnail}")
        Picasso.get().load(img).fit().into(viewHolder.itemView.imageView_comic_row)

        var fav_ListButton: ImageButton =
            viewHolder.itemView.findViewById(R.id.image_Fav_Button_comic)

        if (favorite == true) {
            fav_ListButton.setImageResource(R.drawable.ic_star_solid)
        } else {
            fav_ListButton.setImageResource(R.drawable.ic_star_regular)
        }

        fav_ListButton.setOnClickListener(object : View.OnClickListener {
            //favorite = !favorite

            override fun onClick(v: View?) {
                if (favorite == false) {
                    realm.executeTransaction {
                        comic.favorite = true
                        realm.copyToRealmOrUpdate(comic)
                    }
                } else {
                    realm.executeTransaction {
                        comic.favorite = false
                        realm.copyToRealmOrUpdate(comic)
                    }
                }
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

