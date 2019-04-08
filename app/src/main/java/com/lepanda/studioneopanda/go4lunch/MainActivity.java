package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lepanda.studioneopanda.go4lunch.api.UserHelper;
import com.lepanda.studioneopanda.go4lunch.models.Users;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //FOR DATA
    private static final int RC_SIGN_IN = 123;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonConnectionClicked();
            }
        });
    }

    private void onButtonConnectionClicked() {
        if (this.isCurrentUserLogged()) {
            this.startMapActivity();
        } else {
            this.startSignInActivity();
        }
    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // --------------------
    // REST REQUEST
    // --------------------

    // 1 - Http request that create user in Firestore
    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();

            //UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(this.onFailureListener()); ??
        }
    }



    private Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.TwitterBuilder().build())) // not working right now
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.icon_app2)
                        .build(),
                RC_SIGN_IN);
    }

    private void startMapActivity() {
        Intent intent = new Intent(this, CentralActivity.class);
        startActivity(intent);
    }

    private void connexionStatus(){
        // if success add user in Firestore
        // if already logged in toast
        // if failure auth toast
    }
}