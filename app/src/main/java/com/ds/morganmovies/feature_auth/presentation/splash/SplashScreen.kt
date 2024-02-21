package com.ds.morganmovies.feature_auth.presentation.splash


import android.os.Build.VERSION.SDK_INT
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.ds.morganmovies.core.presentation.theme.Black_op
import com.ds.morganmovies.core.presentation.ui.Screen
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.delay

//@Composable
//fun SplashScreen(
//    navController: NavController
//) {
//
//    val uri = "file:///android_asset/splash_video.mp4"
//    val context = LocalContext.current
//    val player = SimpleExoPlayer.Builder(context).build()
//    val playerView = PlayerView(context)
//    val mediaItem = MediaItem.fromUri(uri)
//    val playWhenReady by rememberSaveable {
//        mutableStateOf(true)
//    }
//    player.setMediaItem(mediaItem)
//    playerView.player = player
//    playerView.useController = false
//
//    DisposableEffect(player){
//        player.prepare()
//        player.playWhenReady = playWhenReady
//        onDispose {
//            player.stop()
//        }
//    }
////    LaunchedEffect(player) {
////        player.prepare()
////        player.playWhenReady = playWhenReady
////    }
//
//    Column(
//        Modifier
//            .background(Color.Black)
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        AndroidView(
//            factory = { playerView },
//        )
//    }
//
//    player.addListener(object : Player.Listener {
//        override fun onPlaybackStateChanged(playbackState: Int) {
//            if (player.playbackState == Player.STATE_ENDED) {
//                player.seekTo(0)
//                navController.navigate(Screen.OnBoarding.route)
//            }
//            super.onPlaybackStateChanged(playbackState)
//        }
//    })
//
//
//
//
//}

@Composable
fun SplashScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Column(
        Modifier
            .background(Black_op)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = com.ds.morganmovies.R.drawable.splash_gif).apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    LaunchedEffect(Unit){
        delay(3000)
        navController.navigate(Screen.OnBoarding.route)
    }

}
