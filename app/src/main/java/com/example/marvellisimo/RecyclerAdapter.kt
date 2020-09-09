package com.example.marvellisimo

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val characters: ArrayList<Character>): RecyclerView.Adapter<RecyclerAdapter.CharacterHolder>(){
    override fun onBindViewHolder(holder: RecyclerAdapter.CharacterHolder, position: Int) {
        TODO("Not yet implemented")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = characters.size


    inner class CharacterHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
          //TODO
                }

}
