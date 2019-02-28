package com.example.languageidentifierapp.helpers;

import android.app.AlertDialog;
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

    //Запрос всех языков
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
                .baseUrl("https://gateway.watsonplatform.net")
                .build();
        MainActivity.API api = retrofit.create(MainActivity.API.class);;
        Call<SendResponse> call = api.send();

        call.enqueue(new Callback<SendResponse>() { // Асинхронный вызов
            @Override
            public void onResponse(Call<SendResponse> call, Response<SendResponse> response) {
                if (response.isSuccessful()) {
                    // tasks available
                    try {
                        if (response.body() != null) { //Если все ок
                            Log.d("Response", response.body().getLanguages().toString());
                            saveLanguagesLocal(response.body().getLanguages(), ctx); // Сохраняем все языки в локальную БД
                        }
                    } catch (Exception e) {
                        Log.d("IOException", e.getMessage());
                    }
                } else {
                    // error response, no access to resource?
                    try {
                        Log.d("Response", response.message());
                        if( response.errorBody() != null)
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

    //Сохраняем все языки в локальную БД
    private static void saveLanguagesLocal(List<Language> languagesList, Context ctx){
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
    }

    //Загружаем все языки из локальной БД
    public static void loadLanguagesLocal(Context ctx){
        allLanguages = new HashMap<String, String>();
        LanguagesDbHelper dbHelper = new LanguagesDbHelper(ctx);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(LanguagesContract.LanguagesEntry.TABLE_NAME, null, null, null, null, null, null);
        if(c.moveToFirst()){
            int languageColIndex = c.getColumnIndex(LanguagesContract.LanguagesEntry.COLUMN_LANGUAGE);
            int nameColIndex = c.getColumnIndex(LanguagesContract.LanguagesEntry.COLUMN_NAME);

            do {
                Language lang = new Language(c.getString(languageColIndex),c.getString(nameColIndex));
                allLanguages.put(lang.getLanguage(),lang.getName());
            } while (c.moveToNext());
        }
        //return spendItems;
    }

    //Загружаем из локальной БД проверенные тексты
    public static ArrayList<LanguageChecked> loadCheckedLanguageLocal(Context ctx){
        ArrayList<LanguageChecked> lcArray = new ArrayList<>();
        LanguagesDbHelper dbHelper = new LanguagesDbHelper(ctx);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(LanguageCheckedContract.LanguageCheckedEntry.TABLE_NAME, null, null, null, null, null, null);
        if(c.moveToFirst()){
            int textColIndex = c.getColumnIndex(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_TEXT);
            int languageColIndex = c.getColumnIndex(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_LANGUAGE);
            int dateColIndex = c.getColumnIndex(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_DATE);

            do {
                LanguageChecked languageChecked = new LanguageChecked(c.getString(textColIndex),c.getString(languageColIndex), c.getString(dateColIndex));
                lcArray.add(languageChecked);
            } while (c.moveToNext());
        }
        return lcArray;
    }
}
