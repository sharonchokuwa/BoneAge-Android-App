package com.example.sharon.boneage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button performBAA;
    private Button accessDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        performBAA = (Button) findViewById(R.id.performBAA);
        accessDatabase = (Button) findViewById(R.id.imageDatabase);
        accessDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, dbFunctions.class);
                startActivity(intent);
            }
        });

        performBAA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchPatient.class);
                startActivity(intent);
            }
        });
    }
}
