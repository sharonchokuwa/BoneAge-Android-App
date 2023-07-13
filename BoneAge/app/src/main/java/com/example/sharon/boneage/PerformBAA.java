package com.example.sharon.boneage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.example.sharon.boneage.R.id.age;

public class PerformBAA extends AppCompatActivity  {

    Handler handler = new Handler();
    DBManipulation myDb;
    Patient patient;
    TextView nameTxt;
    TextView surnameTxt;
    TextView genderTxt;
    TextView accNumTxt;
    TextView heading;
    TextView Orig_label2;
    TextView Pre_label3;
    TextView ageTxt;
    TextView ageTxt2;
    TextView ageTxt2_Label;
    ImageView imageView;
    ImageView imageView2;
    Button performBAA;
    long startTime, endTime;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    String accNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_ba);

        accNum = getIntent().getStringExtra("PATIENT_ACC_NUM");
        nameTxt = (TextView)findViewById(R.id.name);
        heading = (TextView)findViewById(R.id.heading);
        surnameTxt = (TextView)findViewById(R.id.lastname);
        accNumTxt = (TextView) findViewById(R.id.accNum);
        genderTxt = (TextView)findViewById(R.id.gender);
        ageTxt = (TextView)findViewById(age);
        ageTxt2 = (TextView)findViewById(R.id.agetxt2);
        ageTxt2_Label = (TextView)findViewById(R.id.age2);
        imageView = (ImageView) findViewById(R.id.imgView);
        imageView2 = (ImageView) findViewById(R.id.imgView2);
        Orig_label2 = (TextView)findViewById(R.id.label2);
        Pre_label3 = (TextView)findViewById(R.id.label3);
        performBAA = (Button) findViewById(R.id.performBAA);
        patient = displayInfo(accNum);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        performBAA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                preprocess();

            }
        });

    }

    public Patient displayInfo(String accNum)
    {
        myDb = new DBManipulation(this);
        patient = myDb.getPatient(accNum);

        String name = patient.getName();
        String surname = patient.getSurname();
        String gender = patient.getGender();
        String age = patient.getAge();
        Bitmap bitmap = patient.getImage();

        nameTxt.setText(name);
        surnameTxt.setText(surname);
        genderTxt.setText(gender);
        accNumTxt.setText(accNum);
        String ageStr = age + " months";
        ageTxt.setText(ageStr);
        imageView.setImageBitmap(bitmap);

        return  patient;
    }

    public void showFinalResult(Wrapper w)
    {
        Bitmap mybitmap = BitmapFactory.decodeByteArray(w.preprocessedArr, 0, w.preprocessedArr.length);
        imageView2.setImageBitmap(mybitmap);

        int index = w.predictionResult.indexOf(".");
        String predStr = w.predictionResult.substring(0, index) + " months";

        ageTxt2_Label.setText(predStr);

        String newStr = "Bone Age Result";
        heading.setText(newStr);
        ageTxt2_Label.setVisibility(View.VISIBLE);
        ageTxt2.setVisibility(View.VISIBLE);
        Orig_label2.setVisibility(View.VISIBLE);
        Pre_label3.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.VISIBLE);
        performBAA.setVisibility(View.GONE);
    }



    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        final String response2 = hasImage + "";
        return hasImage;
    }

    public void preprocess()
    {
        Bitmap bitmap = patient.getImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] array = bos.toByteArray();

        String patientG = genderTxt.getText().toString();
        SendImageParams params = new SendImageParams(array, patientG);
        SendImageClient sic = new SendImageClient();
        sic.execute(params);
    }

    private static class SendImageParams {
        byte[] imageArray;
        String patientGender;

        SendImageParams(byte[] imageArray, String patientGender) {
            this.imageArray = imageArray;
            this.patientGender = patientGender;

        }
    }


    public class SendImageClient extends AsyncTask<SendImageParams, Void, Wrapper>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //performBAA.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            startTime = System.currentTimeMillis();
        }

        @Override
        protected Wrapper doInBackground(SendImageParams... voids) {

            byte[] data = null;
            String response = "";

            Wrapper w = new Wrapper();

            //byte[] originalImage = voids[0].imageArray;
            //String resultString = subjectString.replaceAll("[^\\x00-\\x7F]", "");
            //String patientGender = voids[0].patientGender.replaceAll("[^\\\\x00-\\\\x7F]", "");
            //String patientGender = voids[0].patientGender.replaceAll("(\\n)", "");
            String patientGender = voids[0].patientGender;


            try{
                //Socket socket = new Socket("192.168.8.103", 9999); //50620
                Socket socket = new Socket("172.20.10.2", 9999); //172.20.10.2
                //Socket socket = new Socket("192.168.8.101", 9999);
                //Socket socket = new Socket("192.168.8.100", 9999);

                OutputStream out = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);

                //dos.writeInt(patientGender.getBytes("utf-8").length);
                //dos.write(patientGender.getBytes("utf-8"));
                //dos.flush();

                dos.writeInt(voids[0].patientGender.length());
                dos.write(patientGender.getBytes("utf-8"));
                dos.flush();


                //dos.writeInt(voids[0].imageArray.length);
                String image_size = voids[0].imageArray.length + "";
                dos.write(image_size.getBytes("utf-8"));
                Log.d("MYINT", "voids[0].imageArray.length: " + voids[0].imageArray.length);
                //dos.writeInt(voids[0].imageArray.length);
                dos.write(voids[0].imageArray, 0, voids[0].imageArray.length);

                dos.flush();
                socket.shutdownOutput();

                InputStream in = socket.getInputStream();
                DataInputStream dis = new DataInputStream(in);

                byte[] prediction_r = new byte[4];
                in.read(prediction_r, 0, prediction_r.length);
                response = new String(prediction_r);

                byte[] imageSize_r = new byte[4];
                in.read(imageSize_r, 0, imageSize_r.length);
                final String imageS = new String(imageSize_r);

                StringBuffer strBuff = new StringBuffer();
                char c;
                for (int i = 0; i < imageS.length() ; i++) {
                    c = imageS.charAt(i);
                    if (Character.isDigit(c)) {
                        strBuff.append(c);
                    }
                }

                int len = Integer.parseInt(strBuff.toString()) + 1;
                data = new byte[len];

                try
                {
                    int count;
                    while ((count = dis.read(data)) > 0)
                    {
                        in.read(data, 0, count);
                    }
                }catch (EOFException e) {
                    e.printStackTrace();
                    Log.e("", "Exception: "+ Log.getStackTraceString(e));
                }

                w.predictionResult = response;
                w.preprocessedArr = data;
                dos.close();
                out.close();
                dis.close();
                in.close();
                socket.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.e("", "Exception: "+ Log.getStackTraceString(e));
                handler.post( new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Sorry, I/O exception", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return w;
        }
        @Override
        protected void onProgressUpdate(Void... num) {
           // progressUptxt.setText("BAA in progress...  " + values[0] + "s");
            //progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Wrapper w) {
            super.onPostExecute(w);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            //performBAA.setEnabled(true);
            endTime= System.currentTimeMillis();
            long duration = (endTime-startTime)/1000;
            final String durationStr = duration + "";

            handler.post( new Runnable(){
                @Override
                public void run(){
                    Toast.makeText(getApplicationContext(), "Time taken= " +  durationStr + "s", Toast.LENGTH_LONG).show();
                }
            });
            //saveImage(w);
            showFinalResult(w);
        }
    }

    public class Wrapper
    {
        public byte[] preprocessedArr;
        public String predictionResult;
    }

    public void saveImage(Wrapper w)
    {
        Bitmap pre_bitmap;

        if(w.preprocessedArr != null)
        {
            pre_bitmap = BitmapFactory.decodeByteArray(w.preprocessedArr, 0, w.preprocessedArr.length);
            boolean response = myDb.updateData(accNum, patient.getName(), patient.getSurname(), patient.getGender(),
                    patient.getAge(), patient.getImage());

            if(response) {
                Toast.makeText(PerformBAA.this, "Data successfully updated", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(PerformBAA.this, DisplayResult.class);
                intent.putExtra("accNum", accNum);
                startActivity(intent);
            }
            else {
                Toast.makeText(PerformBAA.this, "Data NOT updated", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(PerformBAA.this, "Preprocessed image is null", Toast.LENGTH_LONG).show();
            return;

        }

        //Intent intent = new Intent(PerformBAA.this, DisplayResult.class);
        //intent.putExtra("preprocessedArr", w.preprocessedArr);
        //intent.putExtra("predictionResult", w.predictionResult);
        //intent.putExtra("students_list", (Serializable) students_list);
        //intent.putExtra("accNum", accNum);
        //startActivity(intent);
    }

}
