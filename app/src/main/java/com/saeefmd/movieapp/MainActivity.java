package com.saeefmd.movieapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String data;
    RecyclerView recyclerView;
    MovieData movieData;

    //ProgressDialog progressDialog;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.movie_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progress_bar);

        //progressDialog = new ProgressDialog(this);
        //progressDialog.setCancelable(false);
        //progressDialog.setTitle("Loading...");
        //progressDialog.setMessage("Please wait!");

        new MyAsyncTask().execute();

    }

    public String getDataFromApi(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                    .url(url)
                    .build();

        try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }

    }

    public class MyAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                data = getDataFromApi("https://api.themoviedb.org/3/movie/top_rated?api_key=f4fb3ff04d69e51db7c6d14a713fb643&language=en-US&page=1");

                movieData = new Gson().fromJson(data, MovieData.class);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

            //progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            MovieAdapter movieAdapter = new MovieAdapter(movieData.getResults(), MainActivity.this);
            recyclerView.setAdapter(movieAdapter);

            progressBar.setVisibility(View.GONE);

            //progressDialog.dismiss();
        }
    }
}
