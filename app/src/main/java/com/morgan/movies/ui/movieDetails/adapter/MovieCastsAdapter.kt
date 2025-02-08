package com.morgan.movies.ui.movieDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant.Companion.BASE_POSTER_IMAGE_URL
import com.morgan.movies.data.models.Cast
import com.morgan.movies.databinding.ItemCastBinding

class MovieCastsAdapter(private var cast :MutableList<Cast>?)
    :RecyclerView.Adapter<MovieCastsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemCastBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener{
            onItemClickListener?.onItemClick(position,cast?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return cast?.size?:0
    }

    var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(pos: Int, actorItem: Cast?)
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