package com.example.sharon.boneage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class dbFunctions extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int WRITE_PERMISSION = 0x01;
    DBManipulation myDb;
    //Button btnviewAll;
    //Button btnDelete;
    //Button btnviewUpdate;
    EditText editName, editSurname, editGender, editId, editAge;
    Bitmap pickedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_functions);
        requestWritePermission();
        myDb = new DBManipulation(this);

        editName = (EditText)findViewById(R.id.firstName);
        editSurname = (EditText)findViewById(R.id.lastName);
        editId = (EditText)findViewById(R.id.accNum);
        editGender = (EditText) findViewById(R.id.gender);
        editAge = (EditText) findViewById(R.id.age);
        //btnviewAll = (Button)findViewById(R.id.button_viewAll);
        //btnviewUpdate= (Button)findViewById(R.id.button_update);
        //btnDelete= (Button)findViewById(R.id.button_delete);

        Button buttonLoadImage = (Button) findViewById(R.id.chooseImage);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });


        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //(String name, String surname, String gender, Bitmap image)
                boolean isInserted = myDb.insertData(editId.getText().toString(),
                        editName.getText().toString(),
                        editSurname.getText().toString(),
                        editGender.getText().toString(),
                        editAge.getText().toString(), pickedImage);
                if(isInserted == true)
                    Toast.makeText(dbFunctions.this,"Data successfully Inserted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(dbFunctions.this,"Data NOT Inserted", Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get the image from gallery and display on image view
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            //save the image to database
            pickedImage = ((BitmapDrawable)imageView.getDrawable()).getBitmap();


        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.d(LOG_TAG, "Write Permission Failed");
                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestWritePermission(){
        if(ContextCompat.checkSelfPermission(dbFunctions.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }




}
