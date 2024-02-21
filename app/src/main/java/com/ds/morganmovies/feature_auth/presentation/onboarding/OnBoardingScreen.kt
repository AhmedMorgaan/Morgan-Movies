package com.ds.morganmovies.feature_auth.presentation.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ds.morganmovies.R
import com.ds.morganmovies.core.presentation.theme.Black_op
import com.ds.morganmovies.core.presentation.theme.BottomCardShape
import com.ds.morganmovies.core.presentation.theme.ComicSansFontFamily
import com.ds.morganmovies.core.presentation.theme.Gray
import com.ds.morganmovies.core.presentation.theme.Gray_op
import com.ds.morganmovies.core.presentation.theme.Red
import com.ds.morganmovies.core.presentation.theme.SemiGray
import com.ds.morganmovies.feature_auth.domian.model.OnBoardingData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavController
) {
    val items = ArrayList<OnBoardingData>()
    items.add(
        OnBoardingData(
            R.drawable.onboarding_1,
            "Easily browse and discover movies in various categories, from action and drama to comedy and thriller"
        )
    )
    items.add(OnBoardingData(R.drawable.onboarding_2, "Rate and review your favorite films With our app, you'll never miss out on finding the perfect movie for any occasion"))

    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )
    OnBoardingPager(items = items, pageState = pagerState, modifier = Modifier.fillMaxSize())
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingPager(
    items: ArrayList<OnBoardingData>, pageState: PagerState, modifier: Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier
    ) {
        Column(
            Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(state = pageState) { page ->
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Image(
                        painter = painterResource(id = items[page].image),
                        contentDescription = "",
                        Modifier.size(
                            height = screenHeight - (screenHeight / 3.3f), width = screenWidth
                        )
                    )

                }
            }

        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2.6f),
                elevation = 0.dp,
                backgroundColor = Gray_op,
                shape = BottomCardShape.large
            ) {
                Box(
                    Modifier.padding(horizontal = 30.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PagerIndicator(items = items, currentPage = pageState.currentPage)
                        Text(
                            text = items[pageState.currentPage].content,
                            textAlign = TextAlign.Center,
                            fontFamily = ComicSansFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = SemiGray
                        )

                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 30.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            Arrangement.SpaceBetween
                        ) {
                            if (pageState.currentPage != 1) {

                                TextButton(onClick = {

                                }) {
                                    Text(
                                        text = "Skip Now",
                                        fontFamily = ComicSansFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 18.sp,
                                        color = Red
                                    )
                                }
                                OutlinedButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            pageState.scrollToPage(page = 1, pageOffset = 1f)
                                        }
                                    },
                                    border = BorderStroke(8.dp, Red),
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Red
                                    ),
                                    modifier = Modifier.size(50.dp)
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.ic_right_arrow), contentDescription = "",modifier= Modifier.size(20.dp))
                                }
                            }
                            else{
                                Button(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 20.dp),
                                    shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 20.dp),
                                    colors = ButtonDefaults.buttonColors(Red)
                                ) {
                                    Text(
                                        text = "Get Started",
                                        fontFamily = ComicSansFontFamily,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Black_op
                                    )

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PagerIndicator(items: ArrayList<OnBoardingData>, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
    ) {
        repeat(items.size) {
            Indicator(isSelected = it == currentPage)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(targetValue = if (isSelected) 40.dp else 15.dp)

    Box(
        Modifier
            .padding(4.dp)
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSelected) Red else Gray
            )
    )
}