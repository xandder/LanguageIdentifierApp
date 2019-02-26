package com.example.languageidentifierapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.languageidentifierapp.Models.LanguageChecked.LanguageChecked;
import com.example.languageidentifierapp.Models.LanguageChecked.LanguageCheckedAdapter;
import com.example.languageidentifierapp.R;

import java.util.ArrayList;

import static com.example.languageidentifierapp.helpers.functions.loadCheckedLanguageLocal;

public class HistoryFragment extends Fragment {

    ArrayList<LanguageChecked> languageCheckedArray;
    ListView languageCheckedList;
    LanguageCheckedAdapter adapter;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        languageCheckedArray = loadCheckedLanguageLocal(this.getActivity());
        adapter = new LanguageCheckedAdapter(this.getContext(), languageCheckedArray);
        languageCheckedList = view.findViewById(R.id.checked_list);
        languageCheckedList.setAdapter(adapter);
        return view;
    }
}
