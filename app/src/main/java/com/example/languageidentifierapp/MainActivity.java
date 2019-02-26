package com.example.languageidentifierapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.languageidentifierapp.Fragments.HelloFragment;
import com.example.languageidentifierapp.Fragments.HistoryFragment;
import com.example.languageidentifierapp.Fragments.NewTextFragment;
import com.example.languageidentifierapp.Retrofit.SendResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.example.languageidentifierapp.helpers.functions.GETAllLanguages;
import static com.example.languageidentifierapp.helpers.functions.loadLanguagesLocal;
import static com.example.languageidentifierapp.helpers.globals.allLanguages;
import static com.example.languageidentifierapp.helpers.globals.fragmentClass;
import static com.example.languageidentifierapp.helpers.globals.title;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(fragmentClass == null)
            fragmentClass = HelloFragment.class; // Устанавливаем приветственный фрагмент
        if(title != null) setTitle(title);
        //Log.d("Test", toolbar.getTitle().toString());
        loadLanguagesLocal(this);
        if(allLanguages.isEmpty() || allLanguages == null) {
            GETAllLanguages(this);
        } else Log.d(TAG, allLanguages.toString());
        setFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_text) { // Выбран пункт меню "Новый текст"
            fragmentClass = NewTextFragment.class;
        } else if (id == R.id.nav_history) { // Выбран пункт меню "История"
            fragmentClass = HistoryFragment.class;
        }

        setFragment();
        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());
        title = item.getTitle().toString();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Установка фрагментов
    public void setFragment(){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Интерфейс запроса всех языков
    public interface API {
        @GET("/language-translator/api/v3/identifiable_languages?version=2018-05-01")//"/language-translator/api/v3/identify")
        Call<SendResponse> send();
    }

    //Сохраняем все языки в БД, чтоб не тянуть их с сервера каждый раз
    //public boolean saveLanguagesLocal(List<Language> languagesList){
    //    LanguagesDbHelper dbHelper = new LanguagesDbHelper(this);
    //    SQLiteDatabase db = dbHelper.getWritableDatabase();
    //    for(Language language : languagesList){
    //        ContentValues cv = new ContentValues();
    //        cv.put(LanguagesContract.LanguagesEntry.COLUMN_LANGUAGE, language.getLanguage());
    //        cv.put(LanguagesContract.LanguagesEntry.COLUMN_NAME, language.getName());
    //        long rowID = db.insert(LanguagesContract.LanguagesEntry.TABLE_NAME, null, cv);
    //        Log.d(TAG, String.valueOf(rowID));
    //    }
    //    return true;
    //}
}
