package com.example.localizapp;

import java.util.List;

public class MultilingualList {
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
