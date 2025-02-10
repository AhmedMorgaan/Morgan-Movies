package com.morgan.movies.ui.actorDetails

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant
import com.morgan.movies.core.utils.Constant.Companion.ACTOR
import com.morgan.movies.core.utils.Constant.Companion.MOVIE
import com.morgan.movies.data.models.ActorDetailsResponse
import com.morgan.movies.data.models.ActorItem
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.databinding.FragmentActorDetailsBinding
import com.morgan.movies.ui.actorDetails.adapter.ActorMovieAdapter
import com.morgan.movies.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActorDetailsFragment :
    BaseFragment<FragmentActorDetailsBinding, ActorDetailsViewModel>(R.layout.fragment_actor_details) {

    var adapter: ActorMovieAdapter? = null

    override val viewModel: ActorDetailsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actorItem = arguments?.getParcelable(ACTOR) as ActorItem?
        handelActorDetails(actorItem?.id)
        handelFilterAndSortButtons()
        initActorMoviesRecyclerview()
        viewModel.getActorMovies(actorItem?.id)
        viewModel.getActorDetails(actorItem?.id)
    }


    private fun handelActorDetails(actorId: Int?) {
        viewModel.actorDetailsResponse.observe(viewLifecycleOwner){ actorDetails ->
            binding.apply {
                model = actorDetails
                Glide.with(binding.root)
                    .load(Constant.BASE_POSTER_IMAGE_URL + actorDetails?.profilePath)
                    .placeholder(R.drawable.image_place_holder)
                    .into(actorImage)

                actorBirthday.text =
                    if (actorDetails?.birthday != null) actorDetails.birthday else "N/A"

                actorBorn.text =
                    if (actorDetails?.placeOfBirth.isNullOrEmpty()) "N/A" else actorDetails?.placeOfBirth

                if (actorDetails?.deathday == null) {
                    txtDiedDay.visibility = View.GONE
                    actorDiedDay.visibility = View.GONE
                } else {
                    actorDiedDay.text = actorDetails.deathday
                }
                btnLang.setOnClickListener {
                    viewModel.isArLanguage.value = !viewModel.isArLanguage.value!!
                }
            }
            handelLanguageButton(actorId, actorDetails)
        }
    }

    private fun handelLanguageButton(actorId: Int?, actorDetails: ActorDetailsResponse?) {
        viewModel.arabicActorDetailsResponse.observe(viewLifecycleOwner){ newActorItem ->
            binding.biographyDescription.text =
                if (newActorItem?.biography.isNullOrEmpty()) {
                    getString(R.string.no_translate_to_arabic)
                } else {
                    newActorItem?.biography
                }

            binding.txtLanguage.text = resources.getString(R.string.english)
        }
        viewModel.isArLanguage.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getArabicActorDetails(actorId)
            } else {
                binding.biographyDescription.text =
                    if (actorDetails?.biography.isNullOrEmpty()) "N/A" else actorDetails?.biography
                binding.txtLanguage.text = resources.getString(R.string.arabic)
            }
        }
    }

    private fun handelFilterAndSortButtons() {
        viewModel.isSortByDate.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnSortByDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.Pink
                    )
                )
            } else {
                binding.btnSortByDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.Gray
                    )
                )
            }
        }
        viewModel.isSortByRate.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnSortByRate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.Pink
                    )
                )
            } else {
                binding.btnSortByRate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.Gray
                    )
                )
            }
        }
    }

    private fun initActorMoviesRecyclerview() {
        viewModel.actorMoviesResponse.observe(viewLifecycleOwner){  result ->

            if (adapter !=null){
                if (viewModel.savedRecyclerViewData.value != null ){
                    adapter?.updateMoviesList(viewModel.savedRecyclerViewData.value)
                }
            }else {
               adapter = ActorMovieAdapter(result?.cast)
            }

            binding.actorMoviesRecyclerview.adapter = adapter

            if (result?.cast!!.isEmpty()) {
                binding.actorMoviesRecyclerview.visibility = View.GONE
                binding.actorNoMovies.visibility = View.VISIBLE
            }

            adapter?.onItemClickListener = object : ActorMovieAdapter.OnItemClickListener {
                override fun onItemClick(pos: Int, actorMovies: MovieItem?) {
                    val bundle = Bundle()
                    bundle.putParcelable(MOVIE, actorMovies)
                    findNavController().navigate(
                        R.id.action_actorDetailsFragment_to_movieDetailsFragment,
                        bundle
                    )
                }
            }

            binding.btnSortByDate.setOnClickListener {
                viewModel.isSortByDate.value = !viewModel.isSortByDate.value!!
                if (viewModel.isSortByRate.value!!) {
                    viewModel.isSortByRate.value = false
                }
                if (viewModel.isSortByDate.value!!) {
                    val sortedList = result.cast.sortedByDescending {
                        it.releaseDate
                    }.toMutableList()
                    adapter?.updateMoviesList(sortedList)
                } else {
                    adapter?.updateMoviesList(result.cast)
                }
            }

            binding.btnSortByRate.setOnClickListener {
                viewModel.isSortByRate.value = !viewModel.isSortByRate.value!!
                if (viewModel.isSortByDate.value!!) {
                    viewModel.isSortByDate.value = false
                }
                if (viewModel.isSortByRate.value!!) {
                    val sortedList = result.cast.sortedByDescending {
                        it.voteAverage
                    }.toMutableList()
                    adapter?.updateMoviesList(sortedList)

                } else {
                    adapter?.updateMoviesList(result.cast)
                }
            }
        }
    }

    override fun getViewBinding(v: View): FragmentActorDetailsBinding {
        return FragmentActorDetailsBinding.bind(v)
    }

    override fun onPause() {
        super.onPause()
        viewModel.savedRecyclerViewData.value = adapter?.actorMovies
    }

}