package com.example.localizapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameTextView, recipeIngredientsTextView, recipeStepsTextView, textVideo, textAudio;
    private MediaPlayer mediaPlayer;
    private Button btnBack, playButton, pauseButton, stopButton, playVideoButton, pauseVideoButton, stopVideoButton;
    private VideoView videoView;
    private TextView timer1TextView, timer2TextView, timer3TextView;
    private EditText timer2EditText;
    private Button timer1StartButton, timer1PauseButton, timer1ResetButton,
            timer2StartButton, timer2PauseButton, timer2ResetButton, timer2SetButton,
            timer3StartButton, timer3PauseButton, timer3ResetButton;


    private CountDownTimer timer1, timer2, timer3;


    private long timer1MillisInFuture = 60000;
    private long timer2MillisInFuture = 60000;
    private long timer3MillisInFuture = 60000;

    private boolean timer1IsRunning = false;
    private boolean timer2IsRunning = false;
    private boolean timer3IsRunning = false;

    private int timer1SoundId = R.raw.timer1_sound;
    private int timer2SoundId = R.raw.timer2_sound;
    private int timer3SoundId = R.raw.timer3_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String currentLanguage = LocaleHelper.getLanguage(this);
        Context context = LocaleHelper.setLocale(this, currentLanguage);
        Resources resources = context.getResources();
        setContentView(R.layout.activity_recipe_detail);

        videoView = findViewById(R.id.videoView);
        textVideo = findViewById(R.id.textVideo);
        textAudio = findViewById(R.id.textAudio);
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
        textVideo.setText(resources.getString(R.string.textVideo));
        textAudio.setText(resources.getString(R.string.textAudio));

        timer1TextView = findViewById(R.id.timer1TextView);
        timer2TextView = findViewById(R.id.timer2TextView);
        timer3TextView = findViewById(R.id.timer3TextView);

        timer2EditText = findViewById(R.id.timer2EditText);

        timer1StartButton = findViewById(R.id.timer1StartButton);
        timer1PauseButton = findViewById(R.id.timer1PauseButton);
        timer1ResetButton = findViewById(R.id.timer1ResetButton);

        timer2StartButton = findViewById(R.id.timer2StartButton);
        timer2PauseButton = findViewById(R.id.timer2PauseButton);
        timer2ResetButton = findViewById(R.id.timer2ResetButton);
        timer2SetButton = findViewById(R.id.timer2SetButton);

        timer3StartButton = findViewById(R.id.timer3StartButton);
        timer3PauseButton = findViewById(R.id.timer3PauseButton);
        timer3ResetButton = findViewById(R.id.timer3ResetButton);

        timer1StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(1);
            }
        });

        timer1PauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer(1);
            }
        });

        timer1ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer(1);
            }
        });

        timer2StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(2);
            }
        });

        timer2PauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer(2);
            }
        });

        timer2ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer(2);
            }
        });

        timer2SetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer2();
            }
        });

        timer3StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(3);
            }
        });

        timer3PauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer(3);
            }
        });

        timer3ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer(3);
            }
        });

        updateTimerText(1);
        updateTimerText(2);
        updateTimerText(3);

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


    private void startTimer(int timerNumber) {
        if (timerNumber == 1) {
            if (timer1IsRunning) {
                return;
            }
            timer1IsRunning = true;

            timer1 = new CountDownTimer(timer1MillisInFuture, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timer1MillisInFuture = millisUntilFinished;
                    updateTimerText(1);
                    updateTimerColor(1);
                }

                @Override
                public void onFinish() {
                    timer1IsRunning = false;
                    updateTimerText(1);
                    timer1TextView.setTextColor(Color.BLACK);
                    playSound(timer1SoundId);
                }
            }.start();
        } else if (timerNumber == 2) {
            if (timer2IsRunning) {
                return;
            }
            timer2IsRunning = true;

            timer2 = new CountDownTimer(timer2MillisInFuture, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timer2MillisInFuture = millisUntilFinished;
                    updateTimerText(2);
                    updateTimerColor(2);
                }

                @Override
                public void onFinish() {
                    timer2IsRunning = false;
                    updateTimerText(2);
                    timer2TextView.setTextColor(Color.BLACK);
                    playSound(timer2SoundId);
                }
            }.start();
        } else if (timerNumber == 3) {
            if (timer3IsRunning) {
                return;
            }
            timer3IsRunning = true;

            timer3 = new CountDownTimer(timer3MillisInFuture, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timer3MillisInFuture = millisUntilFinished;
                    updateTimerText(3);
                    updateTimerColor(3);
                }

                @Override
                public void onFinish() {
                    timer3IsRunning = false;
                    updateTimerText(3);
                    timer3TextView.setTextColor(Color.BLACK);
                    playSound(timer3SoundId);
                }
            }.start();
        }
    }

    private void pauseTimer(int timerNumber) {
        if (timerNumber == 1) {
            if (timer1IsRunning) {
                timer1.cancel();
                timer1IsRunning = false;
            }
        } else if (timerNumber == 2) {
            if (timer2IsRunning) {
                timer2.cancel();
                timer2IsRunning = false;
            }
        } else if (timerNumber == 3) {
            if (timer3IsRunning) {
                timer3.cancel();
                timer3IsRunning = false;
            }
        }
    }

    private void resetTimer(int timerNumber) {
        if (timerNumber == 1) {
            timer1MillisInFuture = 60000;
            updateTimerText(1);
            timer1TextView.setTextColor(Color.BLACK);
            timer1IsRunning = false;
            if (timer1 != null) {
                timer1.cancel();
            }
        } else if (timerNumber == 2) {
            timer2MillisInFuture = 60000;
            updateTimerText(2);
            timer2TextView.setTextColor(Color.BLACK);
            timer2IsRunning = false;
            if (timer2 != null) {
                timer2.cancel();
            }
        } else if (timerNumber == 3) {
            timer3MillisInFuture = 60000;
            updateTimerText(3);
            timer3TextView.setTextColor(Color.BLACK);
            timer3IsRunning = false;
            if (timer3 != null) {
                timer3.cancel();
            }
        }
    }


    private void setTimer2() {
        try {
            timer2MillisInFuture = Long.parseLong(timer2EditText.getText().toString()) * 60000;
            updateTimerText(2);
        } catch (NumberFormatException e) {
        }
    }
    private void updateTimerText(int timerNumber) {
        long millisInFuture = 0;
        TextView timerTextView = null;

        if (timerNumber == 1) {
            millisInFuture = timer1MillisInFuture;
            timerTextView = timer1TextView;
        } else if (timerNumber == 2) {
            millisInFuture = timer2MillisInFuture;
            timerTextView = timer2TextView;
        } else if (timerNumber == 3) {
            millisInFuture = timer3MillisInFuture;
            timerTextView = timer3TextView;
        }

        int minutes = (int) (millisInFuture / 1000) / 60;
        int seconds = (int) (millisInFuture / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void updateTimerColor(int timerNumber) {
        TextView timerTextView = null;
        long millisInFuture = 0;

        if (timerNumber == 1) {
            millisInFuture = timer1MillisInFuture;
            timerTextView = timer1TextView;
        } else if (timerNumber == 2) {
            millisInFuture = timer2MillisInFuture;
            timerTextView = timer2TextView;
        } else if (timerNumber == 3) {
            millisInFuture = timer3MillisInFuture;
            timerTextView = timer3TextView;
        }

        if (millisInFuture < 60000) {
            timerTextView.setTextColor(Color.RED);
        } else {
            timerTextView.setTextColor(Color.BLACK);
        }
    }


    private void playSound(int soundId) {
        MediaPlayer mp = MediaPlayer.create(this, soundId);
        mp.start();
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