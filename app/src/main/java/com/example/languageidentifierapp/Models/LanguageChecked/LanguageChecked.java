package com.example.languageidentifierapp.Models.LanguageChecked;

public class LanguageChecked {
    private String text; // Проверенный текст
    private String language; // Язык текста
    private String date; // Дата проверки

    public LanguageChecked(String text_, String language_, String date_){
        this.text = text_;
        this.language = language_;
        this.date = date_;
    }

    public String getText(){
        return this.text;
    }

    public String getLanguage(){
        return this.language;
    }

    public String getDate(){
        return this.date;
    }
}
