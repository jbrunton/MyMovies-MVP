package com.jbrunton.mymovies.features.discover

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.controllers.BaseLoadingViewController

class DiscoverViewController(
        val viewModel: DiscoverViewModel
) : BaseLoadingViewController<DiscoverViewState>() {
    override val contentView: View get() = view.findViewById(R.id.discover_content)

    val nowPlayingViewController by lazy { createGridViewController(R.id.now_playing) }
    val popularViewController by lazy { createGridViewController(R.id.popular) }

    val genres_view: GenresView by bindView(R.id.genres_view)

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        nowPlayingViewController.onViewCreated(view)
        popularViewController.onViewCreated(view)
    }

    override fun updateContentView(viewState: DiscoverViewState) {
        nowPlayingViewController.updateView(viewState.nowPlayingViewState)
        popularViewController.updateView(viewState.popularViewState)

        genres_view.updateView(viewState.genresViewState)

        if (viewState.scrollToGenreResults) {
            contentView.scrollTo(0, genres_view.bottom)
        }
    }

    fun setListener(listener: DiscoverListener) {
        genres_view.listener = viewModel
    }

    fun createGridViewController(listId: Int) = object : MoviesGridViewController() {
        override val recyclerView: RecyclerView by bindView(listId)
        override fun onMovieSelected(movie: Movie) = viewModel.onMovieSelected(movie)
    }
}
