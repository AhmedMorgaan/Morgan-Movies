package com.morgan.movies.ui.actorDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.morgan.movies.core.Result
import com.morgan.movies.data.models.ActorDetailsResponse
import com.morgan.movies.data.models.ActorMoviesResponse
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.data.repositories.MoviesRepository
import com.morgan.movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ActorDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
):BaseViewModel() {

     val isArLanguage = MutableLiveData<Boolean>(false)
     val isSortByDate = MutableLiveData<Boolean>(false)
     val isSortByRate = MutableLiveData<Boolean>(false)

    val savedRecyclerViewData = MutableLiveData<MutableList<MovieItem>?>()
    val actorMoviesResponse = MutableLiveData<ActorMoviesResponse>()
    val actorDetailsResponse = MutableLiveData<ActorDetailsResponse>()
    val arabicActorDetailsResponse = MutableLiveData<ActorDetailsResponse>()

//    val ActorsMoviesListPaging = Pager(PagingConfig(10)){
//        ActorMoviesSource(287,moviesRepository)
//    }.flow.cachedIn(viewModelScope)

     fun getArabicActorDetails(actorId: Int?) {
        viewModelScope.launch {
            val result = moviesRepository.getArabicActorDetails(actorId)
            try {
                progressBar.postValue(true)  //value = true
                when (result) {
                    is Result.Success -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(false)
                            arabicActorDetailsResponse.value = result.data
                        }
                    }
                    is Result.Error -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(false)
                            showMessage.postValue(result.exception.message)
                        }
                    }
                    is Result.Loading -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(true)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.postValue(false)
                    showMessage.postValue(e.localizedMessage)
                    Log.e("apiCallError", e.message.toString())
                }
            }
        }
    }

     fun getActorDetails(actorId: Int?) {
        viewModelScope.launch {
            val result = moviesRepository.getActorDetails(actorId)
            try {
                progressBar.postValue(true)  //value = true
                when (result) {
                    is Result.Success -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(false)
                            actorDetailsResponse.value = result.data
                        }
                    }
                    is Result.Error -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(false)
                            showMessage.postValue(result.exception.message)
                        }
                    }
                    is Result.Loading -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(true)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.postValue(false)
                    showMessage.postValue(e.localizedMessage)
                    Log.e("apiCallError", e.message.toString())
                }

            }
        }
    }

     fun getActorMovies(actorId: Int?){
        viewModelScope.launch {
            val result = moviesRepository.getActorMovies(actorId)
            try {
                progressBar.postValue(true)  //value = true
                when (result) {
                    is Result.Success -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(false)
                            actorMoviesResponse.value = result.data
                        }
                    }
                    is Result.Error -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(false)
                            showMessage.postValue(result.exception.message)
                        }
                    }
                    is Result.Loading -> {
                        withContext(Dispatchers.Main){
                            progressBar.postValue(true)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.postValue(false)
                    showMessage.postValue(e.localizedMessage)
                    Log.e("apiCallError", e.message.toString())
                }

            }
        }
    }
}