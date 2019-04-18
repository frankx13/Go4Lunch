package com.lepanda.studioneopanda.go4lunch;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
    }

    //LANGUAGE SELECTION
    private void languageSelection() {
        Button btnLanguage = findViewById(R.id.btn_language);
        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }

        });
    }

    //DELETE ACCOUNT
    private void deletionUserAccount() {
        Button btnDeleteAccount = findViewById(R.id.btn_settings_delete_account);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
                mBuilder.setTitle("Account deletion");
                mBuilder.setMessage("Do you really want to delete your account ?");
                mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUI.getInstance()
                                .delete(SettingsActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                        Toast.makeText(SettingsActivity.this, "Deleting account...", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                    }
                });
                mBuilder.setNegativeButton("No", null);
                mBuilder.create();
                mBuilder.show();
            }
        });
    }

    //STACKBACK
    private void returnToCentral() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, CentralActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //------------------
    //LANGUAGES SETTINGS
    //------------------

    private void showChangeLanguageDialog() {
        final String[] listItems = {"български", "Español", "Français", "Pусский", "Türk", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        mBuilder.setTitle("Choose a language");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
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
            }
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
