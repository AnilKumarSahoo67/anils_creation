package com.example.anilscreation.MovieCall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anilscreation.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {
    private TextView movie_release,movie_lan,movie_description,movie_popularity,movie_name;
    private ImageView movie_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    MaterialToolbar materialToolbar;
    String des,rel,language,popularity,image,name;
    static Result details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setStatusBarTransparent();
        movie_release=findViewById(R.id.movie_release);
        movie_lan=findViewById(R.id.movie_language);
        movie_description=findViewById(R.id.movie_description);
        movie_popularity=findViewById(R.id.movie_popularity);
        movie_image=findViewById(R.id.img_movie);
        materialToolbar=findViewById(R.id.toolbar);
        movie_name=findViewById(R.id.movie_Name);

        collapsingToolbarLayout=findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        setSupportActionBar(materialToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initCollapsingToolbar();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        des=bundle.getString("movieDescription");
        rel=bundle.getString("movieRelease");
        language=bundle.getString("movieLanguage");
        popularity=bundle.getString("moviePopularity");
        image=bundle.getString("movieImage");
        name=bundle.getString("movieName");
        String imageUrl="https://image.tmdb.org/t/p/original"+details.getPosterPath();

        movie_description.setText(details.getOverview());
        movie_lan.setText(details.getOriginalLanguage());
        movie_popularity.setText(""+details.getVoteCount());
        Picasso.get().load(imageUrl).into(movie_image);
        movie_release.setText(details.getReleaseDate());
        movie_name.setText(details.getTitle());

    }

    private void initCollapsingToolbar() {
        collapsingToolbarLayout.setTitle("");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(details.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}