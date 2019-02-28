package com.example.languageidentifierapp.Fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.languageidentifierapp.Models.LanguageChecked.LanguageCheckedContract;
import com.example.languageidentifierapp.Models.LanguagesDbHelper;
import com.example.languageidentifierapp.R;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.example.languageidentifierapp.helpers.globals.allLanguages;

public class NewTextFragment extends Fragment {

    View view;
    FloatingActionButton checkButton;
    ProgressBar progressBar;

    //Фрагмент с проверкой языка текста
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_new_text, container, false);
        checkButton = view.findViewById(R.id.check_btn);
        EditText editText = view.findViewById(R.id.text_field);
        progressBar = view.findViewById(R.id.progress_bar);

        checkButton.setOnClickListener(v -> {
            if (!editText.getText().toString().equals("")) {
                progressBar.setVisibility(View.VISIBLE); // Стартуем анимацию загрузки
                checkLanguageRequest(editText.getText().toString()); // Вызываем функцию с запросом к апи на проверку языка
            } else Toast.makeText(getActivity(), "Введите текст", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    //Интерфейс запроса на проверку языка
    public interface API {
        @POST("/language-translator/api/v3/identify?version=2018-05-01")
        Call<ResponseBody> send(@Body RequestBody body);
    }

    //Запрос проверяющий язык текста
    public void checkLanguageRequest(String text){

        //Добавляем авторизацию в запрос
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

        //Подготовка запроса
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://gateway.watsonplatform.net")
                .build();
        NewTextFragment.API api = retrofit.create(NewTextFragment.API.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), text);

        Call<ResponseBody> call = api.send(requestBody);
        call.enqueue(new Callback<ResponseBody>() { //асинхронный вызов
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // tasks available
                    try {
                        if (response.body() != null) { //Если все удачно и без ошибок
                            JSONObject json = new JSONObject(response.body().string());
                            Log.d("Response", json.getJSONArray("languages").getJSONObject(0).toString());
                            saveCheckedLanguageLocal(text, json.getJSONArray("languages").getJSONObject(0).getString("language")); //Берем самый первый элемент с языком, который больше всего подходит
                            onLoaded(json.getJSONArray("languages").getJSONObject(0).getString("language"), true); //Вызываем функцию окончания запроса
                        }
                    } catch (Exception e) {
                        Log.d("IOException", e.getMessage());
                        onLoaded(e.getMessage(), false);
                    }
                } else {
                    // error response, no access to resource?
                    try {
                        if(response.errorBody() != null) {
                            Log.d("Response", response.errorBody().string());
                            onLoaded(response.errorBody().string(), false);
                        }
                        else {
                            Log.d("Response", response.message());
                            onLoaded(response.message(), false);
                        }

                    } catch (IOException e) {
                        Log.d("IOException", e.getMessage());
                        onLoaded(e.getMessage(), false);
                    }
//
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                onLoaded(t.getMessage(), false);
            }
        });
    }

    //Когда получен ответ
    private void onLoaded(String shortLanguage, boolean isSuccesfull){
        progressBar.setVisibility(View.INVISIBLE); // гасим анимацию загрузки
        if(isSuccesfull) {
            AlertDialog dialog = new AlertDialog.Builder(this.getContext()) // Диалог, если определили язык
                    .setPositiveButton("Ок", null)
                    .setTitle("Язык определен")
                    .setMessage("Язык введенного текста: " + allLanguages.get(shortLanguage))
                    .create();
            dialog.show();
        }
        else {
            String errorMessage = shortLanguage;
            AlertDialog dialog = new AlertDialog.Builder(this.getContext()) // Диалог, если ошибка
                    .setPositiveButton("Ок", null)
                    .setTitle("Ошибка")
                    .setMessage("Текст ошибки: " + errorMessage)
                    .create();
            dialog.show();
        }
    }

    //Сохраняем в локальную БД проверенный текст
    public void saveCheckedLanguageLocal(String text, String language){
        LanguagesDbHelper dbHelper = new LanguagesDbHelper(this.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        ContentValues cv = new ContentValues();
        Log.d("saveCheckdLanguageLocal", text+ "   " + allLanguages.get(language) + "   " + language + " " + date);
        cv.put(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_TEXT, text);
        cv.put(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_LANGUAGE, allLanguages.get(language));
        cv.put(LanguageCheckedContract.LanguageCheckedEntry.COLUMN_DATE, sdf.format(date));
        long rowID = db.insert(LanguageCheckedContract.LanguageCheckedEntry.TABLE_NAME, null, cv);
        Log.d("saveCheckdLanguageLocal", "Язык сохранен под ID: " + String.valueOf(rowID));
    }
}
