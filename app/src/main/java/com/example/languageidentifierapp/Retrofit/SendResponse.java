package com.example.languageidentifierapp.Retrofit;

import com.example.languageidentifierapp.Models.Language.Language;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//Для получения списка языков из запроса
public class SendResponse {
    @SerializedName("languages")
    @Expose
    private List<Language> languages = null;

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
}
