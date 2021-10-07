package com.example.anilscreation.MovieCall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.anilscreation.R;
import com.example.anilscreation.gitHubUser.ProjectClickCallBack;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private List<Result> results;
    private Activity context;

    public RecyclerViewAdapter(Activity context, List<Result> results)
    {
        this.results = results;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_item_layout,parent,false);

        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Result movies=results.get(position);
        holder.movieTitle.setText(movies.getTitle());
        holder.movieDescription.setText(movies.getOverview());
        String imageUrl="https://image.tmdb.org/t/p/original"+movies.getPosterPath();
        Picasso.get().load(imageUrl).into(holder.movieImage);
        holder.movieLanguage.setText("Movie Lanuage: "+movies.getOriginalLanguage());
        holder.movieReleaseDate.setText("Release Date:"+movies.getReleaseDate());
        holder.moviePopularity.setText("Popularity: "+String.valueOf(movies.getVoteCount()));

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView movieImage;
        TextView movieTitle,movieDescription,movieReleaseDate,movieLanguage,moviePopularity;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardView);
            movieImage = itemView.findViewById(R.id.movieImage);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDescription = itemView.findViewById(R.id.movieDescription);
            movieReleaseDate = itemView.findViewById(R.id.movieRelease);
            movieLanguage = itemView.findViewById(R.id.movieOriginalLanguage);
            moviePopularity = itemView.findViewById(R.id.moviePopularity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Result movies = results.get(getAdapterPosition());
                    MovieDetails.details=results.get(getAdapterPosition());
                    Intent intent=new Intent(context,MovieDetails.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("movieName",movies.getTitle());
                    bundle.putString("movieRelease",movies.getReleaseDate());
                    bundle.putString("movieImage",movies.getPosterPath());
                    bundle.putString("movieDescription",movies.getOverview());
                    bundle.putString("movieLanguage",movies.getOriginalLanguage());
                    bundle.putString("moviePopularity",String.valueOf(movies.getVoteCount()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                   // context.startActivity(new Intent(context,MovieDetails.class));
                }
            });

        }
}
}
