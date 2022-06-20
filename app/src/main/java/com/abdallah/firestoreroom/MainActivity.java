package com.abdallah.firestoreroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.abdallah.Adapter.DataAdapter;
import com.abdallah.firestoreroom.ViewModel.DataViewModel;
import com.abdallah.firestoreroom.modal.Data;
import com.abdallah.firestoreroom.repository.DataRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private DataViewModel dataViewModel;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private EditText age,name;
    private NumberPicker experience;
    private Button save;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private String get_name,get_age;
    private int get_experience;
    private FirebaseFirestore db;
    Data data;
    private int id = 0;
    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    List<Data> dataList;
    Set<String> keys;
    DataRepository dataRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingactionbutton);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList=new ArrayList<>();
        dataAdapter = new DataAdapter(this,dataList);
        dataRepository= new DataRepository(getApplication());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Room Database with Firestore");

        db=FirebaseFirestore.getInstance();
        keys = new LinkedHashSet<>();
//        getSharedpreferences();
        setSharedPreferences();
        getDataFromeDatabase();
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getAllData().observe(this, new Observer<List<Data>>() {
            @Override
            public void onChanged(List<Data> dataList) {
//                Toast.makeText(MainActivity.this, "working", Toast.LENGTH_SHORT).show();
                dataAdapter.getAllData(dataList);
                recyclerView.setAdapter(dataAdapter);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void getDataFromeDatabase() {
        db.collection("UsersData")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                            keys.add(snapshot.getId());
                            dataList.add(new Data(Integer.parseInt(String.valueOf(snapshot.get("id"))),
                                    snapshot.getString("name"),Integer.parseInt(String.valueOf(snapshot.get(("age")))),
                                    Integer.parseInt(String.valueOf(snapshot.get("experience")))));
                        }
                        dataRepository.Insert(dataList);
                    }
                });
    }

    private void setSharedPreferences() {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id",id);
        editor.commit();

    }

    private void openDialog() {
        builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.each_items,null);
        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        experience = view.findViewById(R.id.experience);
        save = view.findViewById(R.id.save);
        dialog=builder.create();
        dialog.setView(view);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataIntoDatabase();
//                getSharedpreferences();
                setSharedPreferences();
            }
        });
        dialog.show();


    }

    private void getSharedpreferences() {

        SharedPreferences preferences=this.getPreferences(Context.MODE_PRIVATE);
        int get_data = preferences.getInt("id",0);
        id = get_data;
    }

    private void saveDataIntoDatabase() {
        get_age=age.getText().toString().trim();
        get_name=name.getText().toString().trim();
        get_experience=experience.getValue();

        if (!TextUtils.isEmpty(get_name) &&  !TextUtils.isEmpty(get_age)){
            data = new Data(++id,get_name,Integer.parseInt(get_age),get_experience);
            db.collection("UserData")
                    .document()
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(this, "Please fill all the Fields", Toast.LENGTH_SHORT).show();
        }

        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.deleteAllDataFromRoom:

                break;

            case R.id.deleteAllDataFromFirestore:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}