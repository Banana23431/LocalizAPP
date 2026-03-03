package com.example.localizapp;

import java.util.List;

public class Recipe {
    private int id;
    private String type;
    private String cuisine;
    private MultilingualString name;
    public MultilingualList ingredients;
    public MultilingualList steps;

    public Recipe(int id, String type, String cuisine, MultilingualString name, MultilingualList ingredients, MultilingualList steps) {
        this.id = id;
        this.type = type;
        this.cuisine = cuisine;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
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

    public static class MultilingualString {
        private String ru;
        private String en;
        private String de;

        public MultilingualString(String ru, String en, String de) {
            this.ru = ru;
            this.en = en;
            this.de = de;
        }

        public String getRu() {
            return ru;
        }

        public String getEn() {
            return en;
        }

        public String getDe() {
            return de;
        }

        public String getByLanguage(String languageCode) {
            switch (languageCode) {
                case "ru":
                    return ru;
                case "en":
                    return en;
                case "de":
                    return de;
                default:
                    return en; // Default to English
            }
        }
    }

    public static class MultilingualList {
        private List<String> ru;
        private List<String> en;
        private List<String> de;

        public MultilingualList(List<String> ru, List<String> en, List<String> de) {
            this.ru = ru;
            this.en = en;
            this.de = de;
        }

        public List<String> getRu() {
            return ru;
        }

        public List<String> getEn() {
            return en;
        }

        public List<String> getDe() {
            return de;
        }

        public List<String> getByLanguage(String languageCode) {
            switch (languageCode) {
                case "ru":
                    return ru;
                case "en":
                    return en;
                case "de":
                    return de;
                default:
                    return en; // Default to English
            }
        }
    }
}