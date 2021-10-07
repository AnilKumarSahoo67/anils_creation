package com.example.anilscreation.RoomDataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ContactPojo.class},version = 1)
public abstract class ContactRoomDataBase extends RoomDatabase {
    public abstract ContactDao contactDao();

    private static volatile ContactRoomDataBase contactRoomInstance;
    static ContactRoomDataBase getDatabase(final Context context) {
        if (contactRoomInstance == null) {
            synchronized (ContactRoomDataBase.class) {
                if (contactRoomInstance == null) {
                    contactRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            ContactRoomDataBase.class, "contact_database")
                            .build();
                }
            }
        }
        return contactRoomInstance;
    }
}
