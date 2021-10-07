package com.example.anilscreation.RoomDataBase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactDao contactDao;
    ContactRoomDataBase roomDataBase;
    private LiveData<List<ContactPojo>> allData;
    public ContactViewModel(@NonNull Application application) {
        super(application);
        roomDataBase=ContactRoomDataBase.getDatabase(application);
        contactDao=roomDataBase.contactDao();
        allData=contactDao.getAllContact();
    }
    public void insert(ContactPojo data){
        new AsyncInsertData(contactDao).execute(data);
    }
    public void deleteContact(ContactPojo data){
        new AsyncDeleteData(contactDao).execute(data);
    }

    public LiveData<List<ContactPojo>> getAllContacts(){
        return allData;
    }

    public void updateCotact(String name, String number, int id) {
        new AsyncUpdateContact(contactDao,name,number,id).execute();
    }

    private class AsyncInsertData extends AsyncTask<ContactPojo,Void,Void> {
        ContactDao contactDao;
        public AsyncInsertData(ContactDao contactDao) {
            this.contactDao=contactDao;
        }

        @Override
        protected Void doInBackground(ContactPojo... contactPojos) {
            contactDao.insert(contactPojos[0]);
            return null;
        }
    }

    private class AsyncDeleteData extends AsyncTask<ContactPojo ,Void, Void> {
        ContactDao contactDao;
        public AsyncDeleteData(ContactDao contactDao) {
            this.contactDao=contactDao;
        }

        @Override
        protected Void doInBackground(ContactPojo... contactPojos) {
            contactDao.deleteContact(contactPojos[0]);
            return null;
        }
    }

    private class AsyncUpdateContact extends AsyncTask<Void,Void,Void> {
        ContactDao contactDao;
        String name,number;
        int id;
        public AsyncUpdateContact(ContactDao contactDao, String name, String number, int id) {
            this.contactDao=contactDao;
            this.name=name;
            this.number=number;
            this.id=id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.updateContact(name,number,id);
            return null;
        }
    }
}
