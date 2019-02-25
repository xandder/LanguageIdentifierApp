package com.example.languageidentifierapp.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.languageidentifierapp.R;
import com.example.languageidentifierapp.Retrofit.SendBody;
import com.example.languageidentifierapp.Retrofit.SendResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
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
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.example.languageidentifierapp.functions.checkLanguageRequest;

public class NewTextFragment extends Fragment {

    FloatingActionButton checkButton;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_text, container, false);
        checkButton = view.findViewById(R.id.check_btn);
        EditText editText = view.findViewById(R.id.text_field);

        checkButton.setOnClickListener(v -> {
            if (!editText.getText().toString().equals("")) {
                checkLanguageRequest(editText.getText().toString());
            } else Toast.makeText(getActivity(), "Введите текст", Toast.LENGTH_SHORT).show();
        });

        return view;

        //checkButton =
    }

    public interface API {
        @POST("/language-translator/api/v3/identify?version=2018-05-01")//"/language-translator/api/v3/identify")
        Call<ResponseBody> send(@Body RequestBody body);
    }
}
