package com.saeefmd.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();

        int movieId = intent.getIntExtra("movieId", -1);

        Toast.makeText(this, "ID : " + movieId, Toast.LENGTH_SHORT).show();

    }
}
