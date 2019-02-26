package com.example.languageidentifierapp.Models.LanguageChecked;

import android.provider.BaseColumns;

public class LanguageCheckedContract {
    private LanguageCheckedContract(){}

    public static final class LanguageCheckedEntry implements BaseColumns {
        public final static String TABLE_NAME = "languages_checked";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TEXT = "text"; //тест
        public final static String COLUMN_LANGUAGE = "language"; //язык
        public final static String COLUMN_DATE = "date"; //дата
    }
}
