package com.example.sharon.boneage;

import android.graphics.Bitmap;

/**
 * Created by Sharon on 09-Mar-19.
 */

public class Patient {
    private String name;
    private String surname;
    private String gender;
    private String age;
    private String accNum;
    private Bitmap image;
    private Bitmap imageP;
    private String prediction;


    public Patient(){}

    public Patient(String m_id, String m_name, String m_surname, String m_gender, String m_age, Bitmap m_image, Bitmap p_image, String pred) {
        accNum = m_id;
        name = m_name;
        surname = m_surname;
        gender = m_gender;
        image = m_image;
        age = m_age;
        imageP = p_image;
        prediction = pred;
    }

    public Patient(String m_id, String m_name, String m_surname, String m_gender, String m_age, Bitmap m_image) {
        accNum = m_id;
        name = m_name;
        surname = m_surname;
        gender = m_gender;
        image = m_image;
        age = m_age;
    }

    public void setAccNum(String m_id) {
        accNum = m_id;
    }

    public void setName(String m_name) {
        name = m_name;
    }

    public void setSurname(String m_surname) {
        surname= m_surname;
    }

    public void setGender (String m_gender) {
        gender = m_gender;
    }

    public void setAge(String m_age) {
        age = m_age;
    }

    public void setImage(Bitmap m_image) {
        image = m_image;
    }

    public void setPreprocessed(Bitmap p_image) {
        imageP = p_image;
    }
    public void setPrediction(String pred) {
        prediction  = pred;
    }


    public String getAccNum() {
        return accNum;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {return surname;}
    public String getGender () {
        return gender;
    }
    public String getAge () {
        return age;
    }
    public Bitmap getImage () {
        return image;
    }
    public Bitmap getImageP () {
        return imageP;
    }
    public String getPrediction () {
        return prediction;
    }

}


