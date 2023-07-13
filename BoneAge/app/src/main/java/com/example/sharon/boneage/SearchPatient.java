package com.example.sharon.boneage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchPatient extends AppCompatActivity {

    private Button search;
    private EditText accNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);

        search = (Button) findViewById(R.id.search);
        accNum = (EditText) findViewById(R.id.accNum);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = accNum.getText().toString();
                Intent intent = new Intent(SearchPatient.this, PerformBAA.class);
                intent.putExtra("PATIENT_ACC_NUM", id);
                startActivity(intent);
            }
        });
    }
}
