package com.example.languageidentifierapp;

import android.util.Log;

import com.example.languageidentifierapp.Fragments.NewTextFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class functions {
    public static void checkLanguageRequest(String text){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    Request request = response.request();
                    if (request.header("Authorization") != null)
                        // Логин и пароль неверны
                        return null;
                    return request.newBuilder()
                            .header("Authorization", Credentials.basic("6987a48d-342e-4a69-8adc-e65b1cc0b9da", "MxYSIi6nQP2Y"))
                            .build();
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                //.addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl("https://gateway.watsonplatform.net")//"http://httpbin.org")//("https://gateway.watsonplatform.net")
                .build();
        NewTextFragment.API api = retrofit.create(NewTextFragment.API.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), text);
        Call<ResponseBody> call = api.send(requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // tasks available
                    try {
                        if (response.body() != null) {
                            JSONObject json = new JSONObject(response.body().string());// = new Gson().fromJson(response.body().string(),)
                            Log.d("Response", json.getJSONArray("languages").getJSONObject(0).toString());
                        }
                    } catch (Exception e) {
                        Log.d("IOException", e.getMessage());
                    }
                } else {
                    // error response, no access to resource?
                    try {
                        Log.d("Response", response.message());
                        Log.d("Response", response.errorBody().string());
                    } catch (IOException e) {
                        Log.d("IOException", e.getMessage());
                    }
//
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
}
