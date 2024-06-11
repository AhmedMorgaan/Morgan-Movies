package com.example.ds_movies.ui.videoTrailer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.d_note.Base.BaseFragment
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE_DETAILS
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.data.models.VideoItem
import com.example.ds_movies.databinding.FragmentVideoTrailerBinding
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

//    //live stream
//    val url = "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
//     var player :ExoPlayer? = null
//    var playWhenReady = true
//    var currentItem = 0
//    var playBackPosition =0L

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
            val filterlist = list?.filter {
                it.type == "Trailer"
            }?.toMutableList()
            val adapter = VideosTrailerListAdapter(filterlist,lifecycle)
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

//    override fun onStart() {
//        Log.e("here", "on start")
//        super.onStart()
//        //  intiPlayer()
//    }

    override fun onResume() {
        super.onResume()
        // activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        if (player==null) {
//            intiPlayer()
//        }
        handelBackPress()
    }

//    override fun onPause() {
//        super.onPause()
//        Log.e("here", "on pause")
//        //  releasePlayer()
//    }

//    override fun onStop() {
//        super.onStop()
//        Log.e("here", "on stop")
//        //  releasePlayer()
//    }

//    @SuppressLint("SuspiciousIndentation")
//    private fun intiPlayer() {
//
//            player = ExoPlayer.Builder(requireContext())
//                .build()
//                .also { exoPlayer ->
//
//                    Log.e("play video","on play video")
//               // binding.playerView.player = exoPlayer
//              //  dataBinding.playerView.useController = false
//
////            val dataSourceFactory = DefaultHttpDataSource.Factory()
////                    val mediaSource = DashMediaSource.Factory(dataSourceFactory)
////                .createMediaSource(MediaItem.fromUri(url))
//
////            val mediaItem = MediaItem.Builder()
////                .setUri(url)
////                .setMimeType(MimeTypes.VIDEO_MPEG)
////                .build()
//
//                var mediaItem = MediaItem.fromUri(url)
//                    exoPlayer.setMediaItem(mediaItem)
//                    exoPlayer.playWhenReady = playWhenReady
//                    exoPlayer.seekTo(currentItem,playBackPosition)
//                    exoPlayer.prepare()
//                    exoPlayer.play()
//            }
//    }
//
//    private fun releasePlayer(){
//        player?.let { exoPlayer ->
//        playWhenReady = exoPlayer.playWhenReady
//        currentItem = exoPlayer.currentMediaItemIndex
//        playBackPosition = exoPlayer.currentPosition
//        exoPlayer.release()
//        }
//        player = null
//    }

    override fun getViewBinding(v: View): FragmentVideoTrailerBinding {
        return FragmentVideoTrailerBinding.bind(v)
    }
}
