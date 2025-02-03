package com.morgan.movies.ui.actorsSearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant
import com.morgan.movies.data.models.ActorItem
import com.morgan.movies.databinding.ItemActorSearchBinding

class ActorsSearchListAdapterPaging() :
    PagingDataAdapter<ActorItem, ActorsSearchListAdapterPaging.MyViewHolder>(
        diffCallBack
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemActorSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
        holder.setIsRecyclable(false)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position, currentItem)
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int, actorItem: ActorItem?)
    }

    inner class MyViewHolder(var binding: ItemActorSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(actorItem: ActorItem?) {
            binding.actorName.text = actorItem?.name
            Glide.with(itemView.context)
                .load("${Constant.BASE_POSTER_IMAGE_URL}${actorItem?.profilePath}")
                .placeholder(R.drawable.image_place_holder)
                .into(binding.actorImage)
        }
    }

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<ActorItem>() {
            override fun areItemsTheSame(oldItem: ActorItem, newItem: ActorItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ActorItem, newItem: ActorItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}