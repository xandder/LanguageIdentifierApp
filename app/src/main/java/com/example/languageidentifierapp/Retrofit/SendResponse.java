package com.example.languageidentifierapp.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendResponse {
    @SerializedName("languages")
    @Expose
    private ArrayList<String> languages = null;

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }
}
