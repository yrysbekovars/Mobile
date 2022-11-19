package com.grishko.anecs.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grishko.anecs.JokeApp;
import com.grishko.anecs.anecdotes.AnecController;
import com.grishko.anecs.R;
import com.grishko.anecs.database.JokeData;

import org.json.JSONException;

import io.realm.Realm;

public class JokeActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anec);

        textView = findViewById(R.id.textViewAnec);

        Button nextButton = findViewById(R.id.btnNext);
        nextButton.setOnClickListener(view -> nextJoke(AnecController.getCategory()));

        Button saveButton = findViewById(R.id.btnSave);
        saveButton.setOnClickListener(view -> saveJoke());

        nextJoke(AnecController.getCategory());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = super.onCreateView(name, context, attrs);

        setTitle(getString(R.string.activity_main) + " – " + AnecController.getCategory());

        return view;
    }

    public enum JokeCategory {
        Any,
        Dark,
        Spooky,
        Miscellaneous
    }

    public void nextJoke(JokeCategory category) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://v2.jokeapi.dev/joke/" + category.toString(),
                null,
                response -> {
                    try {
                        String joke;
                        if (response.getString("type").equals("twopart"))
                            joke = response.getString("setup") +
                                    "\n" +
                                    response.getString("delivery");
                        else
                            joke = response.getString("joke");
                        showJoke(joke);
                        AnecController.setDisplayedJoke(joke);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(
                        getApplicationContext(),
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                ).show());
        requestQueue.add(jsonObjectRequest);
    }
    protected void showJoke(String text) { textView.setText(text); }

    public void saveJoke() {
        Realm realm = ((JokeApp)getApplication()).getRealmInstance();

        realm.beginTransaction();

        Number id = realm.where(JokeData.class).max("id");
        if (id == null)
            id = 0;
        else
            id = id.intValue() + 1;
        JokeData joke = realm.createObject(
                JokeData.class, id
        );
        joke.setText(AnecController.getDisplayedJoke());
        Log.d("peepee", "Pasting w id " + id + ": " + AnecController.getDisplayedJoke());

        realm.commitTransaction();
    }
}