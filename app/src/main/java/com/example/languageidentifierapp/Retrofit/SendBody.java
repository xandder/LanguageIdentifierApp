package com.example.languageidentifierapp.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendBody{ // Тело запроса для
    @SerializedName("text")
    @Expose
    public String text;
}