package com.saeefmd.movieapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saeefmd.movieapp.NetworkUtilities.NetworkUtilis;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String data;
    RecyclerView recyclerView;
    MovieData movieData;

    ProgressBar progressBar;

    String movieUrl;

    int pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.movie_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progress_bar);

        pageNumber = 1;

        movieUrl = "https://api.themoviedb.org/3/movie/top_rated?" +
                "api_key=f4fb3ff04d69e51db7c6d14a713fb643&language=en-US&page=";


        new MyAsyncTask(movieUrl + pageNumber).execute();

    }

    public String getDataFromApi(String movieUrl) {

        try {

            NetworkUtilis networkUtilis = new NetworkUtilis(movieUrl);

            return networkUtilis.getData();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;

    }

    public class MyAsyncTask extends AsyncTask {

        String url;

        public MyAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            data = getDataFromApi(url);

            movieData = new Gson().fromJson(data, MovieData.class);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            MovieAdapter movieAdapter = new MovieAdapter(movieData.getResults(), MainActivity.this);
            recyclerView.setAdapter(movieAdapter);

            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_next_page:

                pageNumber += 1;

                if (pageNumber <= movieData.getTotalPages()) {

                    new MyAsyncTask(movieUrl + pageNumber).execute();

                } else {
                    Toast.makeText(this, "No more movies!", Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.menu_previous_page:

                if (pageNumber > 1) {

                    pageNumber -= 1;
                    new MyAsyncTask(movieUrl + pageNumber).execute();

                } else {
                    Toast.makeText(this, "This is the first page!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.menu_first_page:

                pageNumber = 1;
                new MyAsyncTask(movieUrl + pageNumber).execute();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
