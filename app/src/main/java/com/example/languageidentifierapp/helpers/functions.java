package com.example.languageidentifierapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.languageidentifierapp.Fragments.NewTextFragment;
import com.example.languageidentifierapp.MainActivity;
import com.example.languageidentifierapp.Models.Language.Language;
import com.example.languageidentifierapp.Models.LanguageChecked.LanguageChecked;
import com.example.languageidentifierapp.Models.LanguageChecked.LanguageCheckedContract;
import com.example.languageidentifierapp.Models.Language.LanguagesContract;
import com.example.languageidentifierapp.Models.LanguagesDbHelper;
import com.example.languageidentifierapp.Retrofit.SendResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.languageidentifierapp.helpers.globals.allLanguages;

public class functions {
    public static void checkLanguageRequest(String text, Context ctx){
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
                            saveCheckedLanguageLocal(text, json.getJSONArray("languages").getJSONObject(0).getString("language"), ctx);
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

    public static void GETAllLanguages(Context ctx){
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
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl("https://gateway.watsonplatform.net")//"http://httpbin.org")//("https://gateway.watsonplatform.net")
                .build();
        MainActivity.API api = retrofit.create(MainActivity.API.class);

        //RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), text);
        Call<SendResponse> call = api.send();

        call.enqueue(new Callback<SendResponse>() {
            @Override
            public void onResponse(Call<SendResponse> call, Response<SendResponse> response) {
                if (response.isSuccessful()) {
                    // tasks available
                    try {
                        if (response.body() != null) {
                            Log.d("Response", response.body().getLanguages().toString());
                            saveLanguagesLocal(response.body().getLanguages(), ctx);
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
            public void onFailure(Call<SendResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }

    public static boolean saveLanguagesLocal(List<Language> languagesList, Context ctx){
        LanguagesDbHelper dbHelper = new LanguagesDbHelper(ctx);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(Language language : languagesList){
            ContentValues cv = new ContentValues();
            cv.put(LanguagesContract.LanguagesEntry.COLUMN_LANGUAGE, language.getLanguage());
            cv.put(LanguagesContract.LanguagesEntry.COLUMN_NAME, language.getName());
            long rowID = db.insert(LanguagesContract.LanguagesEntry.TABLE_NAME, null, cv);
            allLanguages.put(language.getLanguage(), language.getName());
            Log.d("saveLanguagesLocal", "Язык сохранен под ID: " + String.valueOf(rowID));
        }
        return true;
    }

    public static void loadLanguagesLocal(Context ctx){
        allLanguages = new HashMap<String, String>();
        //ContentValues cv = new ContentValues();
        LanguagesDbHelper dbHelper = new LanguagesDbHelper(ctx);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(LanguagesContract.LanguagesEntry.TABLE_NAME, null, null, null, null, null, null);
        if(c.moveToFirst()){
            //int idColIndex = c.getColumnIndex(SpendItemEntry._ID);
            int languageColIndex = c.getColumnIndex(LanguagesContract.LanguagesEntry.COLUMN_LANGUAGE);
            int nameColIndex = c.getColumnIndex(LanguagesContract.LanguagesEntry.COLUMN_NAME);

            do {
                Language lang = new Language(c.getString(languageColIndex),c.getString(nameColIndex));
                allLanguages.put(lang.getLanguage(),lang.getName());
            } while (c.moveToNext());
        }
        //return spendItems;
    }

    public static boolean saveCheckedLanguageLocal(String text, String language, Context ctx){
        LanguagesDbHelper dbHelper = new LanguagesDbHelper(ctx);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        ContentValues cv = new ContentValues();
        Log.d("saveLanguagesLocal", text+ "   " + allLanguages.get(language) + "   " + language + " " + date);
        cv.put(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_TEXT, text);
        cv.put(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_LANGUAGE, allLanguages.get(language));
        cv.put(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_DATE, sdf.format(date));
        long rowID = db.insert(LanguageCheckedContract.LanguageCheckedEntry.TABLE_NAME, null, cv);
        Log.d("saveLanguagesLocal", "Язык сохранен под ID: " + String.valueOf(rowID));

        return true;
    }

    public static ArrayList<LanguageChecked> loadCheckedLanguageLocal(Context ctx){
        ArrayList<LanguageChecked> lcArray = new ArrayList<>();
        //allLanguages = new HashMap<String, String>();
        //ContentValues cv = new ContentValues();
        LanguagesDbHelper dbHelper = new LanguagesDbHelper(ctx);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(LanguageCheckedContract.LanguageCheckedEntry.TABLE_NAME, null, null, null, null, null, null);
        if(c.moveToFirst()){
            //int idColIndex = c.getColumnIndex(SpendItemEntry._ID);
            int textColIndex = c.getColumnIndex(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_TEXT);
            int languageColIndex = c.getColumnIndex(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_LANGUAGE);
            int dateColIndex = c.getColumnIndex(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_DATE);

            do {
                LanguageChecked languageChecked = new LanguageChecked(c.getString(textColIndex),c.getString(languageColIndex), c.getColumnName(dateColIndex));
                lcArray.add(languageChecked);
            } while (c.moveToNext());
        }
        return lcArray;
    }
}
