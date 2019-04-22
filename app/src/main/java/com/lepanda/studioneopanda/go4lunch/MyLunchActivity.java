package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyLunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lunch);
        returnToCentral();
    }

    //STACKBACK
    private void returnToCentral() {
        Button btnBack = findViewById(R.id.btn_lunch_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLunchActivity.this, CentralActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
