package com.example.localizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.example.localizapp.Recipe;
import com.example.localizapp.MultilingualString;
import com.example.localizapp.MultilingualList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView messageView;
    Button btnRus, btnEng, btnGer;
    Context context;
    Resources resources;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private RadioGroup filterGroup;
    private RadioButton filterAll, filterBreakfast, filterLunch, filterDinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));
        resources = context.getResources();
        setContentView(R.layout.activity_main);

        messageView = (TextView) findViewById(R.id.textView);
        btnRus = findViewById(R.id.btnRus);
        btnEng = findViewById(R.id.btnEng);
        btnGer = findViewById(R.id.btnGer);

        btnEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = LocaleHelper.setLocale(MainActivity.this, "en");
                resources = context.getResources();
                messageView.setText(resources.getString(R.string.welcome_message));
                updateLocale("en");
            }
        });

        btnRus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = LocaleHelper.setLocale(MainActivity.this, "ru");
                resources = context.getResources();
                messageView.setText(resources.getString(R.string.welcome_message));
                updateLocale("ru");
            }
        });

        btnGer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = LocaleHelper.setLocale(MainActivity.this, "de");
                resources = context.getResources();
                messageView.setText(resources.getString(R.string.welcome_message));
                updateLocale("de");
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeList = loadRecipes();

        recipeAdapter = new RecipeAdapter(this, recipeList);
        recyclerView.setAdapter(recipeAdapter);

        filterGroup = findViewById(R.id.filter_group);
        filterAll = findViewById(R.id.filter_all);
        filterBreakfast = findViewById(R.id.filter_breakfast);
        filterLunch = findViewById(R.id.filter_lunch);
        filterDinner = findViewById(R.id.filter_dinner);

        filterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                filterRecipes(checkedId);
            }
        });

        // Изначально показываем все рецепты
        filterAll.setChecked(true);
    }

    private void filterRecipes(int checkedId) {
        List<Recipe> filteredList = new ArrayList<>();

        if (checkedId == R.id.filter_all) {
            filteredList.addAll(recipeList);
        } else {
            String filterType = null;
            if (checkedId == R.id.filter_breakfast) {
                filterType = "breakfast";
            } else if (checkedId == R.id.filter_lunch) {
                filterType = "lunch";
            } else if (checkedId == R.id.filter_dinner) {
                filterType = "dinner";
            }

            for (Recipe recipe : recipeList) {
                if (recipe.getType().equals(filterType)) {
                    filteredList.add(recipe);
                }
            }
        }

        recipeAdapter = new RecipeAdapter(this, filteredList);
        recyclerView.setAdapter(recipeAdapter);
    }
    private void updateLocale(String language) {
        context = LocaleHelper.setLocale(this, language);
        resources = context.getResources();

        // Обновление текста для welcome_message
        messageView.setText(resources.getString(R.string.welcome_message));

        // Обновление текста для фильтров
        filterAll.setText(resources.getString(R.string.filter_all));
        filterBreakfast.setText(resources.getString(R.string.filter_breakfast));
        filterLunch.setText(resources.getString(R.string.filter_lunch));
        filterDinner.setText(resources.getString(R.string.filter_dinner));

        // Обновление рецептов
        recipeList = loadRecipes();
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recyclerView.setAdapter(recipeAdapter);
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
}
