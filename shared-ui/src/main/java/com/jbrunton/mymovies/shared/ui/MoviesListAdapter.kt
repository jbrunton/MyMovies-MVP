package com.jbrunton.mymovies.shared.ui

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.kotterknife.bindOptionalView
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.BaseRecyclerAdapter
import com.jbrunton.mymovies.libs.ui.PicassoHelper

class MoviesListAdapter(
        context: Context,
        layoutId: Int,
        onMovieSelected: (Movie) -> Unit
) : BaseRecyclerAdapter<MovieSearchResultViewState, MoviesListAdapter.ViewHolder>(
        layoutId,
        ViewHolderFactory(context, onMovieSelected)
) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView by bindView(R.id.movie_title)
        val releaseDateView: TextView? by bindOptionalView(R.id.release_date)
        val poster: ImageView by bindView(R.id.poster)
        val ratingView: TextView? by bindOptionalView(R.id.rating)
        val movieCardView: View by bindView(R.id.movie_card_view)
        val divider: View? by bindOptionalView(R.id.divider)
    }

    class ViewHolderFactory(
            private val context: Context,
            private val onMovieSelected: (Movie) -> Unit
    ) : BaseRecyclerAdapter.ViewHolderFactory<MovieSearchResultViewState, ViewHolder> {

        private val picassoHelper = PicassoHelper()

        override fun createViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun bindHolder(holder: ViewHolder, item: MovieSearchResultViewState, items: List<com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState>, position: Int) {
            holder.titleView.text = item.title
            holder.releaseDateView?.let { it.text = item.yearReleased }
            holder.ratingView?.let { it.text = Html.fromHtml(item.rating) }
            picassoHelper.loadSearchResultImage(context, holder.poster, item.posterUrl)
            holder.movieCardView.setOnClickListener {
                onMovieSelected(item.movie)
            }
            holder.divider?.let {
                it.visibility = if (position < items.size - 1) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
}
