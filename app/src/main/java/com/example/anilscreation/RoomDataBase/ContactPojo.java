package com.example.anilscreation.RoomDataBase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_contact_details")
public class ContactPojo {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int ID;
    @ColumnInfo(name = "user_name")
    public String NAME;
    @ColumnInfo(name = "user_number")
    public String NUMBER;
    @ColumnInfo(name = "user_profileImg")
    public String IMAGE;

    public ContactPojo(String NAME, String NUMBER, String IMAGE) {
        this.NAME = NAME;
        this.NUMBER = NUMBER;
        this.IMAGE = IMAGE;
    }

    public int getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public String getIMAGE() {
        return IMAGE;
    }
}
