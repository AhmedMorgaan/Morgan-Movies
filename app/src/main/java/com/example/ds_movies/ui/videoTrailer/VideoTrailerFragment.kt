package com.example.ds_movies.ui.videoTrailer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE_DETAILS
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.data.models.VideoItem
import com.example.ds_movies.databinding.FragmentVideoTrailerBinding
import com.example.ds_movies.ui.base.BaseFragment
import com.example.ds_movies.ui.videoTrailer.adapter.VideosTrailerListAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // initYouTubePlayer()
        getVideosId()
       // initYouTubePlayerWithDefaultPlayerUiController()
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
//    private fun initYouTubePlayer() {
//        val options: IFramePlayerOptions =
//            IFramePlayerOptions.Builder().controls(1).fullscreen(1).build()
//        binding.youtubePlayerView.enableAutomaticInitialization = false
//
//        binding.youtubePlayerView.addFullscreenListener(object : FullscreenListener {
//            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
//                Log.e("onEnterFullscreen", "1233: ")
//                isFullscreen = true
//
//                // the video will continue playing in fullscreenView
//                binding.youtubePlayerView.visibility = View.GONE
//                binding.fullScreenViewContainer.visibility = View.VISIBLE
//                binding.fullScreenViewContainer.addView(fullscreenView)
//
//                // optionally request landscape orientation
//                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//            }
//
//            override fun onExitFullscreen() {
//                isFullscreen = false
//                Log.e("onExitFullscreen", "213: ")
//                // the video will continue playing in the player
//                binding.youtubePlayerView.visibility = View.VISIBLE
//                binding.fullScreenViewContainer.visibility = View.GONE
//                binding.fullScreenViewContainer.removeAllViews()
//                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            }
//        })
//
//        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                this@VideoTrailerFragment.youTubePlayer = youTubePlayer
//                youTubePlayer.loadVideo("PLl99DlL6b4", 0f)
//            }
//        }
//        binding.youtubePlayerView.initialize(listener, options)
//        lifecycle.addObserver(binding.youtubePlayerView)
//    }
//    private fun initYouTubePlayerWithDefaultPlayerUiController() {
//        val options: IFramePlayerOptions =
//            IFramePlayerOptions.Builder().controls(0).fullscreen(1).build()
//        binding.youtubePlayerView.enableAutomaticInitialization = false
//
//        binding.youtubePlayerView.addFullscreenListener(object : FullscreenListener {
//            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
//                Log.e("onEnterFullscreen", "1233: ")
//                isFullscreen = true
//
//                // the video will continue playing in fullscreenView
//                binding.youtubePlayerView.visibility = View.GONE
//                binding.fullScreenViewContainer.visibility = View.VISIBLE
//                binding.fullScreenViewContainer.addView(fullscreenView)
//
//                // optionally request landscape orientation
//                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//            }
//
//            override fun onExitFullscreen() {
//                isFullscreen = false
//                Log.e("onExitFullscreen", "213: ")
//                // the video will continue playing in the player
//                binding.youtubePlayerView.visibility = View.VISIBLE
//                binding.fullScreenViewContainer.visibility = View.GONE
//                binding.fullScreenViewContainer.removeAllViews()
//                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            }
//        })
//
//        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                this@VideoTrailerFragment.youTubePlayer = youTubePlayer
//                // using pre-made custom ui
//                val defaultPlayerUiController = DefaultPlayerUiController(binding.youtubePlayerView, youTubePlayer)
//                defaultPlayerUiController.setFullscreenButtonClickListener {
//                    youTubePlayer.toggleFullscreen()
//                }
//                defaultPlayerUiController.showYouTubeButton(false)
//                defaultPlayerUiController.showBufferingProgress(false)
//                defaultPlayerUiController.showDuration(false)
//                binding.youtubePlayerView.setCustomPlayerUi(defaultPlayerUiController.rootView)
//                youTubePlayer.cueVideo("PLl99DlL6b4", 0f)
//            }
//        }
//        binding.youtubePlayerView.initialize(listener, options)
//        lifecycle.addObserver(binding.youtubePlayerView)
//
//    }

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

    override fun onResume() {
        super.onResume()
        handelBackPress()
    }

    override fun getViewBinding(v: View): FragmentVideoTrailerBinding {
        return FragmentVideoTrailerBinding.bind(v)
    }
}
