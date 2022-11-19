package com.grishko.anecs.anecdotes;

import com.grishko.anecs.activities.JokeActivity;

public class AnecController {
    private static JokeActivity.JokeCategory category;
    private static String displayedJoke;

    public static JokeActivity.JokeCategory getCategory() {
        return category;
    }

    public static void setCategory(JokeActivity.JokeCategory category) {
        AnecController.category = category;
    }

    public static String getDisplayedJoke() {
        return displayedJoke;
    }

    public static void setDisplayedJoke(String displayedJoke) {
        AnecController.displayedJoke = displayedJoke;
    }
}
