package com.ajmalyousufza.attendanceregister.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.ajmalyousufza.attendanceregister.Adapters.ViewAllAdapter;
import com.ajmalyousufza.attendanceregister.Models.ViewAll_Model;
import com.ajmalyousufza.attendanceregister.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ViewAllAdapter viewAllAdapter;
    List<ViewAll_Model> viewAllModelList;
    Spinner monthspin,classspin;
    RecyclerView recyclerView;
    Button get_details;
    int presentCount=0;
    String selectedMonth,selectedClass;
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
        get_details = findViewById(R.id.get_details);

        monthspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (months[i]){
                    case "January" :
                        selectedMonth = "Jan2022";
                        break;

                    case "February" :
                        selectedMonth = "Feb2022";
                        break;

                    case "March" :
                        selectedMonth = "Mar2022";
                        break;

                    case "April" :
                        selectedMonth = "Apr2022";
                        break;

                    case "May" :
                        selectedMonth = "May2022";
                        break;

                    case "June" :
                        selectedMonth = "Jun2022";
                        break;

                    case "July" :
                        selectedMonth = "Jul2022";
                        break;

                    case "August" :
                        selectedMonth = "Aug2022";
                        break;

                    case "September" :
                        selectedMonth = "Sep2022";
                        break;

                    case "October" :
                        selectedMonth = "Oct2022";
                        break;

                    case "November" :
                        selectedMonth = "Nov2022";
                        break;

                    case "December" :
                        selectedMonth = "Dec2022";
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + months[i]);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        classspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedClass = classes[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        get_details.setOnClickListener(view -> {

            viewAllModelList.clear();
           // list fetching
           // Toast.makeText(ViewAllActivity.this, "Getting details "+selectedClass+"\n"+selectedMonth, Toast.LENGTH_SHORT).show();

            firestore.collection("Admin").document(auth.getCurrentUser().getUid().toString())
                    .collection(selectedClass)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().size()<=0){
                                    recyclerView.setVisibility(View.GONE);
                                    Toast.makeText(ViewAllActivity.this,"First add data in "+selectedClass,Toast.LENGTH_SHORT).show();

                                }
                                else{
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                        recyclerView.setVisibility(View.VISIBLE);
                                    firestore.collection("Admin").document(auth.getCurrentUser().getUid().toString())
                                            .collection(selectedClass).document(document.getId())
                                            .collection(selectedMonth)
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            presentCount=0;
//                                            presentCount = task.getResult().size();
                                            presentCount=0;
                                            Toast.makeText(ViewAllActivity.this, "Before for", Toast.LENGTH_SHORT).show();
                                              // presentCount= task.getResult().size();
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    presentCount+=Integer.parseInt( document.getString("present"));
                                                    Toast.makeText(ViewAllActivity.this,document.getString("present")+" .."+String.valueOf(presentCount),Toast.LENGTH_SHORT).show();
                                                }

                                            Toast.makeText(ViewAllActivity.this, "after for", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    //Toast.makeText(ViewAllActivity.this, "name "+document.getString("name"), Toast.LENGTH_SHORT).show();
                                    ViewAll_Model viewAll_model = new ViewAll_Model();
                                            //document.toObject(ViewAll_Model.class);
                                    viewAll_model.setName(document.getId().toString());
                                    viewAll_model.setAttendance(String.valueOf(presentCount));
                                    viewAllModelList.add(viewAll_model);
                                    viewAllAdapter.notifyDataSetChanged();
                                }
                            } }else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        });


    }


}