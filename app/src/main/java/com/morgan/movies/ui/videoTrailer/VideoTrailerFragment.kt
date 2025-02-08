package com.morgan.movies.ui.videoTrailer

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant.Companion.MOVIE_DETAILS
import com.morgan.movies.core.utils.Utils
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.data.models.VideoItem
import com.morgan.movies.databinding.FragmentVideoTrailerBinding
import com.morgan.movies.ui.base.BaseFragment
import com.morgan.movies.ui.videoTrailer.adapter.VideosTrailerListAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class VideoTrailerFragment :
    BaseFragment<FragmentVideoTrailerBinding, VideoTrailerViewModel>(R.layout.fragment_video_trailer) {

    override val viewModel: VideoTrailerViewModel by viewModels()
    private var isFullscreen = false
    private lateinit var youTubePlayer: YouTubePlayer

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Utils().hideSystemUI(requireActivity().window,R.id.main_view)
        getVideosId()
        handelBackPress()
    }
    private fun getVideosId() {
        val movieDetails = arguments?.getParcelable(MOVIE_DETAILS) as MovieItem?
        lifecycleScope.launch {
           val list =  viewModel.getMovieVideo(movieId = movieDetails?.id)
            val filterList = list?.filter {
                it.type == "Trailer"
            }?.toMutableList()
            if (filterList.isNullOrEmpty()){
                binding.trailerVideosRecyclerview.visibility = View.GONE
                binding.errorMessageNoVideos.visibility = View.VISIBLE
            }
            val adapter = VideosTrailerListAdapter(filterList,lifecycle)
            binding.trailerVideosRecyclerview.adapter = adapter
            enterFullScreenMode(adapter)
        }

    }

    private fun enterFullScreenMode(adapter: VideosTrailerListAdapter){
        adapter.onItemClickListener = object : VideosTrailerListAdapter.OnItemClickListener{
            override fun onItemClick(
                pos: Int,
                videoItem: VideoItem?,
                itemView: YouTubePlayerView,
                youTubePlayer: YouTubePlayer
            ) {
                this@VideoTrailerFragment.youTubePlayer = youTubePlayer
                youTubePlayer.toggleFullscreen()
                itemView.addFullscreenListener(object : FullscreenListener {
                    override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                        isFullscreen = true

                        // the video will continue playing in fullscreenView
                        binding.trailerVideosRecyclerview.visibility = View.GONE
                        binding.fullScreenViewContainer.visibility = View.VISIBLE
                        if(binding.fullScreenViewContainer.childCount !=0){
                            binding.fullScreenViewContainer.removeAllViews()
                        }
                        binding.fullScreenViewContainer.addView(fullscreenView)

                        // optionally request landscape orientation
                        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }

                    override fun onExitFullscreen() {
                        isFullscreen = false
                        // the video will continue playing in the player
                        binding.trailerVideosRecyclerview.visibility = View.VISIBLE
                        binding.fullScreenViewContainer.visibility = View.GONE
                        binding.fullScreenViewContainer.removeAllViews()
                        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                })
            }
        }
    }
    private fun handelBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        handelBackPress()
        Utils().hideSystemUI(requireActivity().window,R.id.main_view)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onDestroy() {
        super.onDestroy()
        Utils().showSystemUI(requireActivity().window,R.id.main_view)
    }

    override fun getViewBinding(v: View): FragmentVideoTrailerBinding {
        return FragmentVideoTrailerBinding.bind(v)
    }
}
