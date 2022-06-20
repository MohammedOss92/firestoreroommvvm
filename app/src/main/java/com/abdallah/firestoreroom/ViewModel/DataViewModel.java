package com.abdallah.firestoreroom.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abdallah.firestoreroom.modal.Data;
import com.abdallah.firestoreroom.repository.DataRepository;

import java.util.List;

public class DataViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private LiveData<List<Data>> getAllList;

    public DataViewModel(@NonNull Application application) {
        super(application);
        dataRepository=new DataRepository(application);
        getAllList=dataRepository.getAllData();
    }

    public LiveData<List<Data>> getAllData(){

        return getAllList;
    }
}
