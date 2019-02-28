package com.example.languageidentifierapp.Models.LanguageChecked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.languageidentifierapp.Models.Language.Language;
import com.example.languageidentifierapp.R;

import java.util.ArrayList;

//Адаптер для заполнения списка в истории
public class LanguageCheckedAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater layoutInflater;
    ArrayList<LanguageChecked> spendItems;

    public LanguageCheckedAdapter(Context context, ArrayList<LanguageChecked> spendItems_) {
        super();
        this.ctx = context;
        this.spendItems = spendItems_;
        this.layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return spendItems.size();
    }

    @Override
    public Object getItem(int position) {
        return spendItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        public TextView textTV;
        public TextView languageTV;
        public TextView dateTV;
    }

    @Override
    public View getView(int position, View cView, ViewGroup parent) {
        ViewHolder holder = null;

        if (cView == null) {
            cView = layoutInflater.inflate(R.layout.languagelist_item, null, false);
            holder = new ViewHolder();
            holder.textTV = cView.findViewById(R.id.checked_text);
            holder.languageTV = cView.findViewById(R.id.language);
            holder.dateTV = cView.findViewById(R.id.check_date);
            cView.setTag(holder);
            holder = (ViewHolder) cView.getTag();
        } else {
            holder = (ViewHolder) cView.getTag();
        }
        LanguageChecked lang = getLanguageItem(position);


        holder.textTV.setText(lang.getText());
        holder.languageTV.setText(lang.getLanguage());
        holder.dateTV.setText(lang.getDate());

        return cView;
    }

    LanguageChecked getLanguageItem(int position) {
        return ((LanguageChecked) getItem(position));
    }
}
