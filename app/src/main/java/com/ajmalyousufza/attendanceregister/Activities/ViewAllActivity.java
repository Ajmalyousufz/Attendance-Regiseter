package com.ajmalyousufza.attendanceregister.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ajmalyousufza.attendanceregister.Adapters.ViewAllAdapter;
import com.ajmalyousufza.attendanceregister.Models.ViewAll_Model;
import com.ajmalyousufza.attendanceregister.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ViewAllAdapter viewAllAdapter;
    List<ViewAll_Model> viewAllModelList;
    Spinner monthspin,classspin;
    RecyclerView recyclerView;
    String[] months = {"January","February","March","April","May","June","July","August"
            ,"September","October","November","December"};
    String[] classes = {
            "1","2","3","4","5","6","7","8","9","10","11","12"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        monthspin = findViewById(R.id.month_spinner);
        classspin = findViewById(R.id.class_spinner);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.viewall_recyclerveiw);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this,viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);

        monthspin.setOnItemSelectedListener(this);
        classspin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,months);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        monthspin.setAdapter(aa);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_spinner_item,classes);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        classspin.setAdapter(ab);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}