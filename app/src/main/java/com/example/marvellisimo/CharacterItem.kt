

import android.view.View
import android.widget.ImageButton
import com.example.marvellisimo.R
import com.example.marvellisimo.ViewModel.Character
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import kotlinx.android.synthetic.main.character_recycle_row_layout.view.*
import kotlinx.android.synthetic.main.comic_recycle_row_layout.view.*

//adapterClass:
class CharacterItem(val character: Character) : Item<GroupieViewHolder>() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_charactername_row.text = character.name


        var isClicked = false
        var fav_ListButton : ImageButton = viewHolder.itemView.findViewById(R.id.image_Fav_Button)


        val imgsize = "/portrait_small"
        val img : String = renamePathHttps("${character.thumbnail.path}$imgsize.${character.thumbnail.extension}")

        Picasso.get().load(img).fit().into(viewHolder.itemView.imageView_character_row)

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
        return R.layout.character_recycle_row_layout
    }
    fun renamePathHttps(path: String): String {
        return path.replace("http", "https")
    }
}