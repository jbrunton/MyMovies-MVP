package com.jbrunton.mymovies.features.discover

import android.view.View
import com.jbrunton.mymovies.features.discover.views.GenresView
import com.jbrunton.mymovies.features.discover.views.MoviesGridView
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.controllers.BaseLoadingViewController

class DiscoverViewController(
        val viewModel: DiscoverViewModel
) : BaseLoadingViewController<DiscoverViewState>() {
    override val contentView: View get() = view.findViewById(R.id.discover_content)

    val now_playing: MoviesGridView by bindView(R.id.now_playing)
    val popular: MoviesGridView by bindView(R.id.popular)
    val genres_view: GenresView by bindView(R.id.genres_view)

    override fun updateContentView(viewState: DiscoverViewState) {
        now_playing.updateView(viewState.nowPlayingViewState)
        popular.updateView(viewState.popularViewState)

        genres_view.updateView(viewState.genresViewState)

        if (viewState.scrollToGenreResults) {
            contentView.scrollTo(0, genres_view.bottom)
        }
    }

    fun setListener(listener: DiscoverListener) {
        genres_view.setListener(viewModel)
    }
}
