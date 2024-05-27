package com.example.ds_movies.ui.movieDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant.Companion.BASE_POSTER_IMAGE_URL
import com.example.ds_movies.data.models.Cast
import com.example.ds_movies.databinding.ItemCastBinding

class MovieCastsAdapter(private var cast :MutableList<Cast>?)
    :RecyclerView.Adapter<MovieCastsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemCastBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return cast?.size?:0
    }

   inner class MyViewHolder(var binding:ItemCastBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(position :Int){
            val actor = cast?.get(position)
            Glide.with(binding.root)
                .load(BASE_POSTER_IMAGE_URL+actor?.profilePath)
                .placeholder(R.drawable.profile_placeholder)
                .into(binding.castImage)

            binding.castName.text = actor?.name
        }

    }

}