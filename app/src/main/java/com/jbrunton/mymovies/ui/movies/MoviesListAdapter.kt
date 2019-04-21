package com.jbrunton.mymovies.ui.movies

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.entities.models.Movie
import com.jbrunton.libs.ui.BaseRecyclerAdapter
import com.jbrunton.libs.ui.PicassoHelper
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState

class MoviesListAdapter(
        context: Context,
        layoutId: Int,
        onMovieSelected: (Movie) -> Unit
) : BaseRecyclerAdapter<MovieSearchResultViewState, MoviesListAdapter.ViewHolder>(
        layoutId,
        ViewHolderFactory(context, onMovieSelected)
) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView
        val releaseDateView: TextView?
        val poster: ImageView
        val ratingView: TextView?
        val movieCardView: View
        val divider: View?

        init {
            movieCardView = itemView.findViewById(R.id.movie_card_view)
            titleView = itemView.findViewById(R.id.movie_title)
            releaseDateView = itemView.findViewById(R.id.release_date)
            ratingView = itemView.findViewById(R.id.rating)
            poster = itemView.findViewById(R.id.poster)
            divider = itemView.findViewById<View?>(R.id.divider)
        }
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
            if (holder.releaseDateView != null) {
                holder.releaseDateView.text = item.yearReleased
            }
            if (holder.ratingView != null) {
                holder.ratingView.text = Html.fromHtml(item.rating)
            }
            picassoHelper.loadSearchResultImage(context, holder.poster, item.posterUrl)
            holder.movieCardView.setOnClickListener {
                onMovieSelected(item.movie)
            }
            if (holder.divider != null) {
                holder.divider.visibility = if (position < items.size - 1) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
}
