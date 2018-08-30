package com.example.ebadly.opentable;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.ebadly.opentable.dataModels.Movie;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView display_title;
        private TextView byline;
        private TextView headline;
        private TextView summary_short;
        private TextView publication_date;
        private ImageView multiMedia;

        public MyViewHolder(View view) {
            super(view);
            display_title = view.findViewById(R.id.movie_title);
            byline = view.findViewById(R.id.reviewer);
            headline = view.findViewById(R.id.headline);
            summary_short = view.findViewById(R.id.short_summary);
            publication_date = view.findViewById(R.id.pub_date);
            multiMedia = view.findViewById(R.id.multi_media);
        }

        public TextView getDisplayTitle() { return display_title; }
        public TextView getByline() { return byline; }
        public TextView getHeadline() { return headline; }
        public TextView getSummary_short() { return summary_short; }
        public TextView getPublication_date() { return publication_date; }
        public ImageView getMultiMedia() { return multiMedia; }
    }

    public MoviesAdapter(List<Movie> listOfMovies){ moviesList = listOfMovies; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_movie,parent,false);
        return new MyViewHolder(singleItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);

        String displayTitle = movie.getDisplay_title();
        if(!TextUtils.isEmpty(movie.getMpaa_rating())){
            displayTitle += " (" + movie.getMpaa_rating() + ")";
        }
        holder.getDisplayTitle().setText(displayTitle);
        holder.getByline().setText(movie.getByline());
        holder.getHeadline().setText(movie.getHeadline());
        holder.getSummary_short().setText(movie.getSummary_short());
        holder.getPublication_date().setText(movie.getPublication_date());
        ImageView imageView = holder.getMultiMedia();
        Glide.with(imageView).load(movie.getMultimedia().getSrc()).into(imageView);
    }

    @Override
    public int getItemCount() { return moviesList.size(); }

    public void replaceData(List<Movie> newData){
        moviesList = newData;
        notifyDataSetChanged();
    }
}
