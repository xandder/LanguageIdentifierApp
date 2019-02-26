package com.example.languageidentifierapp.Models.Language;

import android.provider.BaseColumns;

public final class LanguagesContract {
    private LanguagesContract(){}

    public static final class LanguagesEntry implements BaseColumns {
        public final static String TABLE_NAME = "languages";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_LANGUAGE = "language"; //короткое название
        public final static String COLUMN_NAME = "name"; //полное название
    }
}
