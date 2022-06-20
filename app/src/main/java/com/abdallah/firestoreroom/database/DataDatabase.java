package com.abdallah.firestoreroom.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabase.Callback;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.abdallah.firestoreroom.dao.DAO;
import com.abdallah.firestoreroom.modal.Data;

@Database(entities = {Data.class},version = 1)
public abstract class DataDatabase extends RoomDatabase {

    private static final String DATABASE_Name="data";
    public abstract DAO DataDao();

    public static volatile DataDatabase instance = null;

    public static DataDatabase getInstance(Context context){
        if(instance==null){
            synchronized (DataDatabase.class){
                if(instance == null){
                    instance=(DataDatabase) Room.databaseBuilder(context, DataDatabase.class, DATABASE_Name)
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return  instance;
    }

    static Callback callback = new Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance);
        }
    };

    static class PopulateAsyncTask extends AsyncTask<Void,Void,Void>{

        private  DAO DataDao;

        public PopulateAsyncTask(DataDatabase dataDatabase) {
            DataDao = dataDatabase.DataDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DataDao.DeleteAll();
            return null;
        }
    }
}
