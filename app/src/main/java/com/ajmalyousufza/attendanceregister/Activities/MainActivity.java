package com.ajmalyousufza.attendanceregister.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ajmalyousufza.attendanceregister.Adapters.StudentAdapter;
import com.ajmalyousufza.attendanceregister.Models.StudentListDataModel;
import com.ajmalyousufza.attendanceregister.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText schoolname,studentClass;
    Button schoolupdate,add_student,getListbtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar,getLitProgressbar;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;
    List<StudentListDataModel> listDataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.updateprogress);
        getLitProgressbar = findViewById(R.id.getlistprogress);
        getLitProgressbar.setVisibility(View.GONE);
        firebaseFirestore = FirebaseFirestore.getInstance();
        schoolname = findViewById(R.id.schoolName);
        studentClass = findViewById(R.id.student_class);
        getListbtn = findViewById(R.id.getlist_btn);
        schoolupdate = findViewById(R.id.schoolNameUpdate_btn);
        add_student = findViewById(R.id.add_student_btn);
        recyclerView = findViewById(R.id.student_recyclerview);
        listDataModels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(this,listDataModels);
        recyclerView.setAdapter(studentAdapter);
        //studentAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.VISIBLE);


        String userid = firebaseAuth.getCurrentUser().getUid();

        getListbtn.setOnClickListener(view -> {
            getLitProgressbar.setVisibility(View.VISIBLE);
            String stclass = studentClass.getText().toString();
            firebaseFirestore.collection("Admin")
                    .document(userid).collection(stclass)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                    if(task.getResult().size()<=0){
                        getLitProgressbar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Class "+stclass+" and there students Not Added", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    if(task.isSuccessful()){
                        recyclerView.setVisibility(View.VISIBLE);
                        getLitProgressbar.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()){
                            StudentListDataModel city = document.toObject(StudentListDataModel.class);
                            listDataModels.add(city);
                            studentAdapter.notifyDataSetChanged();
                        }

                    }}}
                }
            });
        });



        firebaseFirestore.collection("Admin")
                .document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    DocumentSnapshot document = task.getResult();
                    schoolname.setText(document.getData().values().toString());
                }
            }
        });


        schoolupdate.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String useridd = firebaseAuth.getCurrentUser().getUid();
            String schoolName = schoolname.getText().toString();
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("schoolName",schoolName);

            firebaseFirestore.collection("Admin")
                    .document(userid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "School name udated", Toast.LENGTH_SHORT).show();
                    schoolname.setFocusable(false);
                }
            });
        });

        add_student.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,AddStudent.class));
        });

    }
}