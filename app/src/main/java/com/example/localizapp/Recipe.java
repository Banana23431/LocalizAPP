package com.example.localizapp;

import java.util.List;

public class Recipe {
    private int id;
    private String type;
    private String cuisine;
    private MultilingualString name;
    private MultilingualList ingredients;
    private MultilingualList steps;
    private String image;

    public Recipe(int id, String type, String cuisine, MultilingualString name, MultilingualList ingredients, MultilingualList steps, String image) {
        this.id = id;
        this.type = type;
        this.cuisine = cuisine;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCuisine() {
        return cuisine;
    }

    public MultilingualString getName() {
        return name;
    }

    public List<String> getIngredientsByLanguage(String languageCode) {
        return ingredients.getByLanguage(languageCode);
    }

    public List<String> getStepsByLanguage(String languageCode) {
        return steps.getByLanguage(languageCode);
    }

    public MultilingualList getIngredients() {
        return ingredients;
    }

    public MultilingualList getSteps() {
        return steps;
    }

    public String getImage() {
        return image;
    }
}