package com.grishko.anecs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.grishko.anecs.anecdotes.AnecController;
import com.grishko.anecs.R;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Активность с картой
        Button mapButton = findViewById(R.id.btnMap);
        mapButton.setOnClickListener(
                buttonView -> {
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                }
        );

        // Активность с закладками
        Button favButton = findViewById(R.id.btnFav);
        favButton.setOnClickListener(
                buttonView -> {
                    Intent intent = new Intent(this, FavActivity.class);
                    startActivity(intent);
                }
        );

        // Активность с анеками
        findViewById(R.id.btnChapaev).setOnClickListener(
                buttonView -> openAnecdote(JokeActivity.JokeCategory.Dark)
        );
        findViewById(R.id.btnCatB).setOnClickListener(
                buttonView -> openAnecdote(JokeActivity.JokeCategory.Miscellaneous)
        );
        findViewById(R.id.btnNations).setOnClickListener(
                buttonView -> openAnecdote(JokeActivity.JokeCategory.Spooky)
        );
    }

    protected void openAnecdote(JokeActivity.JokeCategory category) {
        AnecController.setCategory(category);

        Intent intent = new Intent(this, JokeActivity.class);
        startActivity(intent);
    }
}