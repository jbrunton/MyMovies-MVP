package com.jbrunton.mymovies.search

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.moviedetails.MovieDetailsActivity
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.BaseRecyclerAdapter
import com.squareup.picasso.Picasso

class SearchResultsAdapter(
        context: Context,
        layoutId: Int
) : BaseRecyclerAdapter<MovieSearchResultViewState, SearchResultsAdapter.ViewHolder>(
        layoutId,
        ViewHolderFactory(context)
) {

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val titleView: TextView
        val releaseDateView: TextView?
        val poster: ImageView
        val ratingView: TextView?
        val movieCardView: View

        init {
            movieCardView = itemView.findViewById(R.id.movie_card_view)
            titleView = itemView.findViewById<View>(R.id.title) as TextView
            releaseDateView = itemView.findViewById<View>(R.id.release_date) as TextView?
            ratingView = itemView.findViewById<View>(R.id.rating) as TextView?
            poster = itemView.findViewById<View>(R.id.poster) as ImageView
        }
    }

    class ViewHolderFactory(private val context: Context) : BaseRecyclerAdapter.ViewHolderFactory<MovieSearchResultViewState, ViewHolder> {

        override fun createViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun bindHolder(holder: ViewHolder, item: MovieSearchResultViewState) {
            holder.titleView.text = item.title
            if (holder.releaseDateView != null) {
                holder.releaseDateView.text = item.yearReleased
            }
            if (holder.ratingView != null) {
                holder.ratingView.text = Html.fromHtml(item.rating)
            }
            Picasso.with(context)
                    .load(item.posterUrl)
                    .resize(185, 275)
                    .centerCrop()
                    .into(holder.poster)
            holder.movieCardView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("MOVIE_ID", item.movieId)
                context.startActivity(intent)
            }
        }
    }
}
