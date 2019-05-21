package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lepanda.studioneopanda.go4lunch.api.WorkerHelper;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //FOR DATA
    private static final int RC_SIGN_IN = 123;
    List<Workmate> workmates;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workmates = new ArrayList();
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> onButtonConnectionClicked());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateUIWhenResuming();
    }

    private void updateUIWhenResuming() {
        this.btnLogin.setText(this.isCurrentUserLogged() ? getString(R.string.button_login_text_logged) : getString(R.string.button_login_text_not_logged));
    }

    private void onButtonConnectionClicked() {
        if (this.isCurrentUserLogged()) {
            this.startMapActivity();
        } else {
            this.startSignInActivity();
        }
    }

    protected OnFailureListener onFailureListener() {
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // --------------------
    // REST REQUEST
    // --------------------

    // 1 - Http request that create user in Firestore
    private void createUserInFirestore() {

        if (this.getCurrentUser() != null) {

            String uid = this.getCurrentUser().getUid();
            String username = this.getCurrentUser().getDisplayName();
            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;

            WorkerHelper.createUser(uid, username, urlPicture).addOnFailureListener(this.onFailureListener());

            for (Workmate w : workmates) {
                w = new Workmate();
                w.setUid(uid);
                w.setUsername(username);
                w.setUrlPicture(urlPicture);
                workmates.add(w);

                Log.i("llesttest", "createUserInFirestore: " + username + "---" + uid + "---" + urlPicture + "---" + w.getUsername());
            }
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
        finish();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                Toast.makeText(this, "Authentication success", Toast.LENGTH_SHORT).show();
                this.createUserInFirestore();
            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(this, "The data for this request returned empty", Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "The app has encountered an unknown error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
