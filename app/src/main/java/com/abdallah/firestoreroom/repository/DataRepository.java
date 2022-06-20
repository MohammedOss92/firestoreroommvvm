package com.abdallah.firestoreroom.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.abdallah.firestoreroom.dao.DAO;
import com.abdallah.firestoreroom.database.DataDatabase;
import com.abdallah.firestoreroom.modal.Data;

import java.util.List;

public class DataRepository {

    private DataDatabase dataDatabase;
    private LiveData<List<Data>> getAllData;

    public DataRepository(Application application) {
        dataDatabase=DataDatabase.getInstance(application);
        getAllData=dataDatabase.DataDao().getAll();
    }

    public void Insert(List<Data> dataList){
        new InsertAsyncTask(dataDatabase).execute(dataList);
    }

    public LiveData<List<Data>> getAllData(){

        return getAllData;
    }

    static class InsertAsyncTask extends AsyncTask<List<Data>,Void,Void>{

        private DAO DataDao;

        public InsertAsyncTask(DataDatabase dataDatabase) {
            DataDao=dataDatabase.DataDao();
        }

        @Override
        protected Void doInBackground(List<Data>... lists) {
            DataDao.Insert(lists[0]);
            return null;
        }
    }
}
