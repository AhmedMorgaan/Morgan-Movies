package com.example.ds_movies.ui.categories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant.Companion.BASE_POSTER_IMAGE_URL
import com.example.ds_movies.data.models.MovieItem

class ChildCategoryListAdapter(
    private val moviesList:MutableList<MovieItem>?
    ) : RecyclerView.Adapter<ChildCategoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_child_category,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = moviesList?.get(position)
        holder.movieTitle.text = item?.title
        Glide.with(holder.itemView.context)
            .load(BASE_POSTER_IMAGE_URL+item?.posterPath)
            .into(holder.movieImage)

    }

    override fun getItemCount(): Int {
        return moviesList?.size?:0
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
         val movieTitle :TextView
         val movieImage:ImageView
        init {
            movieTitle = itemView.findViewById(R.id.child_txt_title)
            movieImage = itemView.findViewById(R.id.child_category_image)
        }
    }
}