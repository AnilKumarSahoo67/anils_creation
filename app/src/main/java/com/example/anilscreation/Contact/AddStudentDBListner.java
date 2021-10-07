package com.example.anilscreation.Contact;

public interface AddStudentDBListner {
    void add(String name,String Number);
    void update(String name, String Number, StudentDataPOJO studentData);
    void cancel();
}
