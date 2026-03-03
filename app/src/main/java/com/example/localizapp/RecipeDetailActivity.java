package com.example.localizapp;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameTextView;
    private TextView recipeIngredientsTextView;
    private TextView recipeStepsTextView;
    private MediaPlayer mediaPlayer;
    private Button btnBack, playButton, pauseButton, stopButton,playVideoButton, pauseVideoButton, stopVideoButton;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String currentLanguage = LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(this, currentLanguage);
        Resources resources = context.getResources();
        setContentView(R.layout.activity_recipe_detail);

        videoView = findViewById(R.id.videoView);
        btnBack = findViewById(R.id.btnBack);
        recipeNameTextView = findViewById(R.id.recipeNameTextView);
        recipeIngredientsTextView = findViewById(R.id.recipeIngredientsTextView);
        recipeStepsTextView = findViewById(R.id.recipeStepsTextView);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);

        playVideoButton = findViewById(R.id.playVideoButton);
        pauseVideoButton = findViewById(R.id.pauseVideoButton);
        stopVideoButton = findViewById(R.id.stopVideoButton);

        btnBack.setText(resources.getString(R.string.btn_back));
        playButton.setText(resources.getString(R.string.btn_play_audio));
        pauseButton.setText(resources.getString(R.string.btn_pause_audio));
        stopButton.setText(resources.getString(R.string.btn_stop_audio));
        playVideoButton.setText(resources.getString(R.string.btn_play_video));
        pauseVideoButton.setText(resources.getString(R.string.btn_pause_video));
        stopVideoButton.setText(resources.getString(R.string.btn_stop_video));


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
                finish();
            }
        });

        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        pauseVideoButton.setEnabled(false);
        stopVideoButton.setEnabled(false);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        if (recipeId != -1) {
            Recipe recipe = loadRecipeById(recipeId);
            if (recipe != null) {
                displayRecipeDetails(recipe);
            } else {
                Log.e("RecipeDetailActivity", "Recipe not found with id: " + recipeId);
            }
        } else {
            Log.e("RecipeDetailActivity", "No recipe ID passed to activity");
        }
    }


    private Recipe loadRecipeById(int recipeId) {
        List<Recipe> recipeList = loadRecipes();
        for (Recipe recipe : recipeList) {
            if (recipe.getId() == recipeId) {
                return recipe;
            }
        }
        return null;
    }

    private List<Recipe> loadRecipes() {
        Gson gson = new Gson();
        try {
            InputStream is = getAssets().open("recipes.json");
            Reader reader = new InputStreamReader(is);
            Type recipeListType = new TypeToken<List<Recipe>>(){}.getType();
            List<Recipe> recipes = gson.fromJson(reader, recipeListType);

            return recipes;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void displayRecipeDetails(Recipe recipe) {
        String languageCode = LocaleHelper.getLanguage(this);
        recipeNameTextView.setText(recipe.getName().getByLanguage(languageCode));

        List<String> ingredients = recipe.getIngredientsByLanguage(languageCode);
        StringBuilder ingredientsText = new StringBuilder();
        for (String ingredient : ingredients) {
            ingredientsText.append(ingredient).append("\n");
        }
        recipeIngredientsTextView.setText(ingredientsText.toString());

        List<String> steps = recipe.getStepsByLanguage(languageCode);
        StringBuilder stepsText = new StringBuilder();
        for (String step : steps) {
            stepsText.append(step).append("\n");
        }
        recipeStepsTextView.setText(stepsText.toString());
    }

    private void startAudio() {
        if (mediaPlayer == null) {
            int audioResId = getAudioResourceIdForLanguage(LocaleHelper.getLanguage(this));
            mediaPlayer = MediaPlayer.create(this, audioResId);
            mediaPlayer.setOnCompletionListener(mp -> stopPlay());
        }
        mediaPlayer.start();
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
    }

    private void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    private int getAudioResourceIdForLanguage(String languageCode) {
        switch (languageCode) {
            case "ru": return R.raw.recipe_ru;
            case "en": return R.raw.recipe_en;
            case "de": return R.raw.recipe_de;
            default: return R.raw.recipe_en;
        }
    }

    public void onPlayClick(View view) {
        startAudio();
    }

    public void onPauseClick(View view) {
        pauseAudio();
    }

    public void onStopClick(View view) {
        stopPlay();
    }

    public void onPlayVideoClick(View view) {
        if (videoView != null) {
            if (!videoView.isPlaying() && videoView.getCurrentPosition() == 0) {
                String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
                videoView.setVideoURI(Uri.parse(videoPath));
                videoView.start();
            } else {
                videoView.start();
            }
            playVideoButton.setEnabled(false);
            pauseVideoButton.setEnabled(true);
            stopVideoButton.setEnabled(true);
        }
    }

    public void onPauseVideoClick(View view) {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
        playVideoButton.setEnabled(true);
        pauseVideoButton.setEnabled(false);
        stopVideoButton.setEnabled(true);
    }

    public void onStopVideoClick(View view) {
        if (videoView != null) {
            videoView.stopPlayback();
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
            videoView.setVideoURI(Uri.parse(videoPath));
        }
        playVideoButton.setEnabled(true);
        pauseVideoButton.setEnabled(true);
        stopVideoButton.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}