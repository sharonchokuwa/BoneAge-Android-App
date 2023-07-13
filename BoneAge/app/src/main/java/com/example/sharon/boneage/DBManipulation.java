package com.example.sharon.boneage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Sharon on 06-Mar-19.
 */

public class DBManipulation extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PatientsBAA.db";
    public static final String TABLE_NAME = "patients_table";
    public static final String COL_1 = "ACC_NUM";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_4 = "GENDER";
    public static final String COL_5 = "AGE";
    public static final String COL_6 = "ORG_IMG";
    public static final String COL_7 = "PRE_IMG";
    public static final String COL_8 = "PREDICTION";

    public DBManipulation(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ACC_NUM TEXT PRIMARY KEY, NAME TEXT," +
                "SURNAME TEXT, GENDER TEXT, AGE TEXT, ORG_IMG BLOB, PRE_IMG BLOB, PREDICTION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id, String name, String surname, String gender, String age, Bitmap image) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, gender);
        contentValues.put(COL_5, age);
        contentValues.put(COL_6, bArray);
        long result = db.insert(TABLE_NAME, null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, String name, String surname, String gender, String age,  Bitmap imageO) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageO.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bArrayO = bos.toByteArray();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, gender);
        contentValues.put(COL_5, age);
        contentValues.put(COL_6, bArrayO);
        db.update(TABLE_NAME, contentValues, "ACC_NUM = ?",new String[] { id });
        return true;
    }


    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ACC_NUM = ?",new String[] {id});
    }

    public Patient getPatient(String id)
    {
        Bitmap bitmapO = null;
        String name;
        String surname;
        String gender;
        String age;
        //Bitmap bitmapP = null;
        //String pred;
        Patient obj = new Patient();

        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from patients_table where ACC_NUM = ?", new String[] {id+""});

        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                 //Get the data fields
                id = cursor.getString(cursor.getColumnIndex("ACC_NUM"));
                name = cursor.getString(cursor.getColumnIndex("NAME"));
                surname = cursor.getString(cursor.getColumnIndex("SURNAME"));
                gender = cursor.getString(cursor.getColumnIndex("GENDER"));
                age = cursor.getString(cursor.getColumnIndex("AGE"));

                byte[] blobO = cursor.getBlob(cursor.getColumnIndex("ORG_IMG"));
                bitmapO = BitmapFactory.decodeByteArray(blobO, 0, blobO.length);

                obj.setAccNum(id);
                obj.setName(name);
                obj.setSurname(surname);
                obj.setGender(gender);
                obj.setAge(age);
                obj.setImage(bitmapO);
            }
        }
        else
        {
            obj = new Patient("nothing", "", "", "","", null, null, "");
        }
        return obj;
    }

}