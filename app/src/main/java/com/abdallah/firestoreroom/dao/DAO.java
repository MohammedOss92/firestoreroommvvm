package com.abdallah.firestoreroom.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.abdallah.firestoreroom.modal.Data;

import java.util.List;

@Dao
public interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insert (List<Data>list);

    @Query("Delete from data")
    void DeleteAll();

    @Query("select * from data")
    LiveData<List<Data>> getAll();
}
