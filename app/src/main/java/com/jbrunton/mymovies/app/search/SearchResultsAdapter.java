package com.jbrunton.mymovies.app.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbrunton.mymovies.app.shared.BaseRecyclerAdapter;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.app.moviedetails.MovieDetailsActivity;
import com.squareup.picasso.Picasso;

public class SearchResultsAdapter extends BaseRecyclerAdapter<MovieViewState, SearchResultsAdapter.ViewHolder> {
    public SearchResultsAdapter(Context context, int layoutId) {
        super(layoutId, new ViewHolderFactory(context));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView releaseDateView;
        private ImageView poster;
        private TextView ratingView;
        private View movieCardView;

        ViewHolder(View itemView) {
            super(itemView);
            movieCardView = itemView.findViewById(R.id.movie_card_view);
            titleView = (TextView) itemView.findViewById(R.id.title);
            releaseDateView = (TextView) itemView.findViewById(R.id.release_date);
            ratingView = (TextView) itemView.findViewById(R.id.rating);
            poster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }

    protected static class ViewHolderFactory implements BaseRecyclerAdapter.ViewHolderFactory<MovieViewState, ViewHolder> {
        private final Context context;

        public ViewHolderFactory(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder createViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public void bindHolder(ViewHolder holder, MovieViewState item) {
            holder.titleView.setText(item.title());
            if (holder.releaseDateView != null) {
                holder.releaseDateView.setText(item.yearReleased());
            }
            if (holder.ratingView != null) {
                holder.ratingView.setText(Html.fromHtml(item.rating()));
            }
            Picasso.with(context)
                    .load(item.posterUrl())
                    .resize(185, 275)
                    .centerCrop()
                    .into(holder.poster);
            holder.movieCardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra("MOVIE_ID", item.movieId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
