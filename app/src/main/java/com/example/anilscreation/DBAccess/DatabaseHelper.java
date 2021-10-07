package com.example.anilscreation.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import androidx.annotation.Nullable;


import com.example.anilscreation.Contact.StudentDataPOJO;
import com.example.anilscreation.UserDetails;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myDatabase";    // Database Name
    private static final String TABLE_NAME = "contact_table";   // Table Name
    private static final int DATABASE_Version = 1;    // Database Version
    private static final String UID="_id";     // Column I (Primary Key)
    private static final String NAME = "Name";    //Column II
    private static final String CONTACT= "Contact_num";    // Column III
    private static final String TIMESTAMP= "date_time";    // Column IV

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"+ CONTACT+" VARCHAR(225) , "+ TIMESTAMP +" VARCHAR(255));";
    private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
    private Context context;

   // Table Name    // Database Version
   private static final String TABLE_NAME_USER = "user_details";   // Table Name
    private static final String ID="_id";     // Column I (Primary Key)
    private static final String USER_NAME = "Name";    //Column II
    private static final String USER_CONTACT= "Contact_num";    // Column III
    private static final String EMAIL= "email";    // Column IV
    private static final String ADDRESS= "address";    // Column IV
    private static final String PASSWORD= "password";    // Column V
    private static final String IMAGE= "image";    // Column VI
    private static final String CREATE_TABLE_USER = "CREATE TABLE "+TABLE_NAME_USER+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+USER_NAME+" VARCHAR(255) ,"+ USER_CONTACT+" VARCHAR(225) , "+ EMAIL +" VARCHAR(255) , "+ ADDRESS +" VARCHAR(255), "+ PASSWORD +" VARCHAR(255), "+IMAGE+" TEXT )";
    private static final String DROP_TABLE_USER ="DROP TABLE IF EXISTS "+TABLE_NAME;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE_USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            db.execSQL(DROP_TABLE_USER);
            onCreate(db);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertContact(String name, String number, String formattedDate) {
        SQLiteDatabase dbb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(CONTACT, number);
        contentValues.put(TIMESTAMP, formattedDate);
        long id = dbb.insert(TABLE_NAME, null , contentValues);
        if(id==-1){
            return false;
        }else
            return true;
    }

    public ArrayList<StudentDataPOJO> getContactList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {UID,NAME,CONTACT};

//        Cursor cursor =db.query(TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<StudentDataPOJO> list=new ArrayList<>();

        Cursor cursor = db.rawQuery("select  *  from  "+TABLE_NAME+"" ,null);
        if(cursor!=null){
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(UID));
                    String name = cursor.getString(cursor.getColumnIndex(NAME));
                    String contact = cursor.getString(cursor.getColumnIndex(CONTACT));
                    StudentDataPOJO contactPojo = new StudentDataPOJO(id, name, contact);
                    list.add(contactPojo);
                }
                while (cursor.moveToNext()) ;
            }
            db.close();
        }
        return list;
    }

    public boolean deleteContact(StudentDataPOJO dataPOJO) {
        SQLiteDatabase db=this.getWritableDatabase();
        long id=db.delete(TABLE_NAME, UID+" = "+dataPOJO.getSlNo(),null);
        if(id==-1)
            return false;
        else
            return true;
    }

    public boolean UpdateContact(String name, String number, String formattedDate, StudentDataPOJO studentData) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(CONTACT,number);
        contentValues.put(TIMESTAMP,formattedDate);
        long id=db.update(TABLE_NAME,contentValues,UID+" =" +studentData.getSlNo(),null);
        if(id==-1)
            return false;
        else
            return true;
    }

    public UserDetails getAllUserData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select  *  from  "+TABLE_NAME_USER+" where "+EMAIL+"= ?  " ,new String[]{email});
        UserDetails userDetails = null;
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                String Name = cursor.getString(cursor.getColumnIndex(USER_NAME));
                String Number = cursor.getString(cursor.getColumnIndex(USER_CONTACT));
                String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                String image = cursor.getString(cursor.getColumnIndex(IMAGE));
                userDetails = new UserDetails(id,Name, address, Number, image);
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return userDetails;
    }

    public boolean insertRegData(String name, String email, String phone, String address, String password ,String image) {
        SQLiteDatabase dbb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_CONTACT, phone);
        contentValues.put(EMAIL, email);
        contentValues.put(ADDRESS, address);
        contentValues.put(PASSWORD, password);
        contentValues.put(IMAGE, image);
        long id = dbb.insert(TABLE_NAME_USER, null , contentValues);
        if(id==-1){
            return false;
        }else
            return true;
    }
    public boolean checkPass(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select  "+PASSWORD+"  from  "+TABLE_NAME_USER+" where "+EMAIL+"= ?  " ,new String[]{email});
        cursor.moveToNext();
        String pass=cursor.getString(cursor.getColumnIndex(PASSWORD));

        if(pass.equals(password)){
            return true;
        }else
            return false;
    }

    public boolean UpdateProfilePic(String imageStr,int uid) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(IMAGE,imageStr);
        long id=db.update(TABLE_NAME_USER,contentValues,ID+" ="+uid,null);
        if(id==-1){
            return false;
        }else
            return true;
    }

}
