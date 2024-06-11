package com.example.ds_movies.ui.videoTrailer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.ds_movies.data.models.VideoItem
import com.example.ds_movies.databinding.ItemViedoTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideosTrailerListAdapter(
    val items: MutableList<VideoItem>?,
    val lifecycle: Lifecycle
    ) : RecyclerView.Adapter<VideosTrailerListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemViedoTrailerBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
       return items?.size?:0
    }

    var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(pos: Int, videoItem: VideoItem?, itemView : YouTubePlayerView,youTubePlayer: YouTubePlayer)
    }

    inner class MyViewHolder(var binding: ItemViedoTrailerBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(position :Int){
            val videoItem = items?.get(position)
            binding.model = videoItem

            val options: IFramePlayerOptions =
                IFramePlayerOptions.Builder().controls(0).fullscreen(1).build()
            binding.itemYoutubePlayerView.enableAutomaticInitialization = false


           val listener : YouTubePlayerListener = object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val defaultPlayerUiController = DefaultPlayerUiController(binding.itemYoutubePlayerView, youTubePlayer)
                    defaultPlayerUiController.setFullscreenButtonClickListener {
                        onItemClickListener?.onItemClick(position,videoItem,binding.itemYoutubePlayerView,youTubePlayer)
                    }
                    defaultPlayerUiController.showYouTubeButton(false)
                    defaultPlayerUiController.showBufferingProgress(false)
                    defaultPlayerUiController.showDuration(false)
                    binding.itemYoutubePlayerView.setCustomPlayerUi(defaultPlayerUiController.rootView)
                    youTubePlayer.cueVideo(videoItem?.key?:"",0f)
                }
            }

            binding.itemYoutubePlayerView.initialize(listener, options)
            lifecycle.addObserver(binding.itemYoutubePlayerView)
        }
    }
}