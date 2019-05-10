package com.jbrunton.mymovies.ui.moviedetails

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.libs.ui.PicassoHelper
import com.jbrunton.mymovies.libs.ui.controllers.BaseLoadingViewController

class MovieDetailsViewController : BaseLoadingViewController<MovieDetailsViewState>() {
    override val contentView: View get() = view.findViewById(R.id.movie_details)

    val movie_title: TextView get() = view.findViewById(R.id.movie_title)
    val rating: TextView get() = view.findViewById(R.id.rating)
    val overview: TextView get() = view.findViewById(R.id.overview)
    val release_date: TextView get() = view.findViewById(R.id.release_date)
    val favorite: View get() = view.findViewById(R.id.favorite)
    val unfavorite: View get() = view.findViewById(R.id.unfavorite)
    val poster: ImageView get() = view.findViewById(R.id.poster)
    val backdrop: ImageView get() = view.findViewById(R.id.backdrop)

    private val picassoHelper = PicassoHelper()

    override fun updateContentView(viewState: MovieDetailsViewState) {
        movie_title.text = viewState.title
        rating.text = Html.fromHtml(viewState.rating)
        overview.text = viewState.overview
        release_date.text = viewState.yearReleased
        favorite.visibility = viewState.favoriteVisibility
        unfavorite.visibility = viewState.unfavoriteVisibility
        picassoHelper.loadSearchResultImage(context, poster, viewState.posterUrl)
        picassoHelper.loadImage(context, backdrop, viewState.backdropUrl)
    }
}