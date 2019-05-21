package com.lepanda.studioneopanda.go4lunch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        enableNotifications();
        languageSelection();
        deletionUserAccount();
        returnToCentral();
        loadLocale();
    }

    //NOTIFICATIONS
    private void enableNotifications() {
        Button btnNotifications = findViewById(R.id.btn_settings_notifications);
        btnNotifications.setOnClickListener(v -> {

        });
    }

    //LANGUAGE SELECTION
    private void languageSelection() {
        Button btnLanguage = findViewById(R.id.btn_language);
        btnLanguage.setOnClickListener(v -> showChangeLanguageDialog());
    }

    //DELETE ACCOUNT
    private void deletionUserAccount() {
        Button btnDeleteAccount = findViewById(R.id.btn_settings_delete_account);
        btnDeleteAccount.setOnClickListener(v -> {

            AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(this);
            mAlertBuilder.setTitle("Are you sure ? All your data will be deleted.");
            mAlertBuilder.setPositiveButton("Yes", (dialog, which) -> AuthUI.getInstance()
                    .delete(this)
                    .addOnCompleteListener(task -> {
                        // ...
                        Intent intent = new Intent(this, MainActivity.class);
                        Toast.makeText(this, "Deleting the account..", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(intent);
                    }));
            mAlertBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog mDialog = mAlertBuilder.create();
            mDialog.show();
        });
    }

    //STACKBACK
    private void returnToCentral() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, CentralActivity.class);
            startActivity(intent);
            finish();
        });
    }

    //------------------
    //LANGUAGES SETTINGS
    //------------------

    private void showChangeLanguageDialog() {
        final String[] listItems = {"български", "Español", "Français", "Pусский", "Türk", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        mBuilder.setTitle("Choose a language");
        mBuilder.setSingleChoiceItems(listItems, -1, (dialog, i) -> {
            if (i == 0) {
                setLocale("bg");
                recreate();
            } else if (i == 1) {
                setLocale("es");
                recreate();
            } else if (i == 2) {
                setLocale("fr");
                recreate();
            } else if (i == 3) {
                setLocale("ru");
                recreate();
            } else if (i == 4) {
                setLocale("tr");
                recreate();
            } else if (i == 5) {
                setLocale("en");
                recreate();
            }

            dialog.dismiss();
        });

        AlertDialog mDialog = mBuilder.create();

        mDialog.show();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Language", MODE_PRIVATE).edit();
        editor.putString("ChosenLanguage", language);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Language", Activity.MODE_PRIVATE);
        String language = prefs.getString("ChosenLanguage", "");
        setLocale(language);
    }
}
