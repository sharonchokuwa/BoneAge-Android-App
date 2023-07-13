package com.example.sharon.boneage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayResult extends AppCompatActivity {
    DBManipulation myDb;
    Patient patient;
    TextView nameTxt;
    TextView surnameTxt;
    TextView genderTxt;
    TextView accNumTxt;
    TextView ageTxtO;
    TextView ageTxtP;
    ImageView imageViewO;
    ImageView imageViewP;
    //String predictionStr;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);

        Intent intent = getIntent();
        //byte[] byteArray = getIntent().getByteArrayExtra("preprocessedArr");
        //bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        //predictionStr = intent.getStringExtra("predictionResult");
        String accNum = intent.getStringExtra("accNum");

        nameTxt = (TextView)findViewById(R.id.firstNametxt);
        surnameTxt = (TextView)findViewById(R.id.lastNametxt);
        accNumTxt = (TextView) findViewById(R.id.accNum);
        genderTxt = (TextView)findViewById(R.id.gender);
        ageTxtO = (TextView)findViewById(R.id.chroAge);
        ageTxtP = (TextView)findViewById(R.id.prediction);
        imageViewO = (ImageView) findViewById(R.id.original);
        imageViewP = (ImageView) findViewById(R.id.preprocessed);

        patient = displayInfo(accNum);
        //update(accNum, bmp, predictionStr);
    }

    public Patient displayInfo(String accNum)
    {
        myDb = new DBManipulation(this);
        patient = myDb.getPatient(accNum);

        String name = patient.getName();
        String surname = patient.getSurname();
        String gender = patient.getGender();
        String age = patient.getAge();
        Bitmap bitmapO = patient.getImage();
        Bitmap bitmapP = patient.getImageP();
        String pred = patient.getPrediction();

        nameTxt.setText(name);
        surnameTxt.setText(surname);
        genderTxt.setText(gender);
        accNumTxt.setText(accNum);
        String org = age + ".0 months";
        ageTxtO.setText(org);
        String pred2 = pred  + ".0 months";
        ageTxtP.setText(pred2);
        imageViewO.setImageBitmap(bitmapO);
        imageViewP.setImageBitmap(bitmapP);
        return  patient;
    }

}
