package com.example.languageidentifierapp.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendBody{
    //@SerializedName("username")
    //@Expose
    //public String username;
//
    //@SerializedName("password")
    //@Expose
    //public String password;

    @SerializedName("text")
    @Expose
    public String text;
}