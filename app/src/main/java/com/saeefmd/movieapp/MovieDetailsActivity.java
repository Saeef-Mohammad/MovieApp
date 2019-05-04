package com.saeefmd.movieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.saeefmd.movieapp.NetworkUtilities.NetworkUtilis;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    String movieDetailsUrl;
    int movieId;
    String data;
    List<Genre> movieGenre;
    List<ProductionCompany> movieCompanies;

    MovieDetailsData movieDetailsData;

    ProgressBar pBar;

    ScrollView movieInfoScrollView;

    ImageView movieInfoPoster;
    TextView movieInfoTitle;
    RatingBar movieInfoRatingBar;
    TextView movieInfoAdult;
    TextView movieInfoBudget;
    TextView movieInfoGenre;
    TextView movieInfoOverview;
    TextView movieInfoPopularity;
    TextView movieInfoCompanies;
    TextView movieInfoReleaseDate;
    TextView movieInfoRevenue;
    TextView movieInfoStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieInfoScrollView = findViewById(R.id.movie_info_sv);

        movieInfoTitle = findViewById(R.id.movie_info_title_tv);
        movieInfoPoster = findViewById(R.id.movie_info_poster_iv);
        movieInfoRatingBar = findViewById(R.id.movie_info_rating_bar);
        movieInfoAdult = findViewById(R.id.movie_info_adult_tv);
        movieInfoBudget = findViewById(R.id.movie_info_budget_tv);
        movieInfoGenre = findViewById(R.id.movie_info_genre_tv);
        movieInfoOverview = findViewById(R.id.movie_info_overview_tv);
        movieInfoPopularity = findViewById(R.id.movie_info_popularity_tv);
        movieInfoCompanies = findViewById(R.id.movie_info_companies_tv);
        movieInfoReleaseDate = findViewById(R.id.movie_info_release_date_tv);
        movieInfoRevenue = findViewById(R.id.movie_info_revenue_tv);
        movieInfoStatus = findViewById(R.id.movie_info_status_tv);
        pBar = findViewById(R.id.p_bar);

        movieInfoRatingBar.setMax(5);
        movieInfoRatingBar.setStepSize((float) .25);

        Intent intent = getIntent();

        movieId = intent.getIntExtra("movieId", -1);

        //Toast.makeText(this, "ID : " + movieId, Toast.LENGTH_SHORT).show();

        new MyAsyncTask().execute();

    }

    public String getDataFromApi() {

        movieDetailsUrl = "https://api.themoviedb.org/3/movie/" + movieId +
                "?api_key=f4fb3ff04d69e51db7c6d14a713fb643&language=en-US";

        try {

            NetworkUtilis networkUtilis = new NetworkUtilis(movieDetailsUrl);

            return networkUtilis.getData();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;

    }

    public class MyAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            data = getDataFromApi();

            movieDetailsData = new Gson().fromJson(data, MovieDetailsData.class);
            movieGenre = movieDetailsData.getGenres();
            movieCompanies = movieDetailsData.getProductionCompanies();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pBar.setVisibility(View.VISIBLE);

            //progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Remove the Progress Bar
            pBar.setVisibility(View.GONE);

            // Set movie poster
            Glide.with(MovieDetailsActivity.this)
                    .load("https://image.tmdb.org/t/p/w500" + movieDetailsData.getPosterPath())
                    .into(movieInfoPoster);

            movieInfoTitle.setText(movieDetailsData.getTitle());
            movieInfoRatingBar.setRating(movieDetailsData.getVoteAverage()/2);

            // Set Adult
            if (movieDetailsData.getAdult()) {
                movieInfoAdult.setText("Adult : Yes");
            } else {
                movieInfoAdult.setText("Adult : No");
            }

            movieInfoBudget.setText("Budget : " + movieDetailsData.getBudget()/1000000.0 + "M");

            // Getting genres and set to textview
            String genres = "";
            for (int i=0; i<movieGenre.size(); i++) {
                genres = genres + movieGenre.get(i).getName() + ", ";
            }
            genres = genres.replaceAll(", $", ""); // replacing the ending comma
            movieInfoGenre.setText("Genres : " + genres);

            movieInfoOverview.setText("Overview : " + movieDetailsData.getOverview());
            movieInfoPopularity.setText("Popularity : " + String.valueOf(movieDetailsData.getPopularity()));

            String companies = "";
            for (int i=0; i<movieCompanies.size(); i++) {
                companies = companies + movieCompanies.get(i).getName() + ", ";
            }
            companies = companies.replaceAll(", $", "");
            movieInfoCompanies.setText("Production Companies : " + companies);

            movieInfoReleaseDate.setText("Release Date : " + movieDetailsData.getReleaseDate());
            movieInfoRevenue.setText("Revenue : " + movieDetailsData.getRevenue()/1000000.0 + "M");
            movieInfoStatus.setText("Status : " + movieDetailsData.getStatus());

            movieInfoScrollView.setVisibility(View.VISIBLE);

            //progressDialog.dismiss();
        }
    }
}
