package com.example.localizapp;

public class MultilingualString {
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
