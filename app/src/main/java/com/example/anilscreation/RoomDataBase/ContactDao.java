package com.example.anilscreation.RoomDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ContactPojo data);

    @Query("SELECT * FROM user_contact_details")
    LiveData<List<ContactPojo>> getAllContact();

    @Delete
    void deleteContact(ContactPojo pojo);

    @Query("UPDATE user_contact_details SET user_name= :userName ,user_number= :userNumber WHERE ID =:id")
    void updateContact(String userName,String userNumber ,int id);
}
