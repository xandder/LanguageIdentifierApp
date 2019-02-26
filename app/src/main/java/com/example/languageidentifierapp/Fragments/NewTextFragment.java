package com.example.languageidentifierapp.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.languageidentifierapp.R;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.example.languageidentifierapp.helpers.functions.checkLanguageRequest;

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
                checkLanguageRequest(editText.getText().toString(), this.getActivity());
            } else Toast.makeText(getActivity(), "Введите текст", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    //Интерфейс запроса на проверку языка
    public interface API {
        @POST("/language-translator/api/v3/identify?version=2018-05-01")//"/language-translator/api/v3/identify")
        Call<ResponseBody> send(@Body RequestBody body);
    }
}
