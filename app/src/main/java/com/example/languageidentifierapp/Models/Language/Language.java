package com.example.languageidentifierapp.Models.Language;

//Язык
public class Language {
    private String language; // короткое название
    private String name; // полное название

    public  Language(String language_, String name_){
        this.language = language_;
        this.name = name_;
    }

    public String getLanguage(){
        return this.language;
    }

    public String getName(){
        return this.name;
    }
}
