package com.example.languageidentifierapp.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.languageidentifierapp.Models.Language.LanguagesContract;
import com.example.languageidentifierapp.Models.LanguageChecked.LanguageCheckedContract;

//Хелпер для работы с бд
public class LanguagesDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "SpendItemDbHelper";

    private static final String DATABASE_NAME = "languages.db";
    private static final int DATABASE_VERSION = 1;

    public LanguagesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Создаем таблицу со всеми языками
        String SQL_CREATE_LANGUAGES_TABLE = "CREATE TABLE " + LanguagesContract.LanguagesEntry.TABLE_NAME + " ("
                + LanguagesContract.LanguagesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LanguagesContract.LanguagesEntry.COLUMN_LANGUAGE + " TEXT NOT NULL, "
                + LanguagesContract.LanguagesEntry.COLUMN_NAME + " TEXT NOT NULL);";

        //Создаем таблицу с проверенными текстами для истории
        String SQL_CREATE_CHECKED_LANGUAGES_TABLE = "CREATE TABLE " + LanguageCheckedContract.LanguageCheckedEntry.TABLE_NAME + " ("
                + LanguageCheckedContract.LanguageCheckedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LanguageCheckedContract.LanguageCheckedEntry.COLUMN_TEXT + " TEXT NOT NULL, "
                + LanguageCheckedContract.LanguageCheckedEntry.COLUMN_LANGUAGE + " TEXT NOT NULL, "
                + LanguageCheckedContract.LanguageCheckedEntry.COLUMN_DATE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_LANGUAGES_TABLE);
        db.execSQL(SQL_CREATE_CHECKED_LANGUAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        //db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        // Создаём новую таблицу
        //onCreate(db);
    }
}
