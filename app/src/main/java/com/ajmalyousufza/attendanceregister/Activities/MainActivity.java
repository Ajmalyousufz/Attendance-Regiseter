package com.ajmalyousufza.attendanceregister.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText schoolname,studentClass;
    Button schoolupdate,add_student,getListbtn,submitButton,viewAll;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar,getLitProgressbar;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;
    TextView currentDate;
    String stclasss;
    Boolean isSubmitButtonClickec = false;
    List<StudentListDataModel> listDataModels;
    List<String> listOfsubmittd;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfsubmittd = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.updateprogress);
        getLitProgressbar = findViewById(R.id.getlistprogress);
        getLitProgressbar.setVisibility(View.GONE);
//        submitButton =findViewById(R.id.submit_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        schoolname = findViewById(R.id.schoolName);
        viewAll = findViewById(R.id.view_all);
        studentClass = findViewById(R.id.student_class);
        getListbtn = findViewById(R.id.getlist_btn);
        schoolupdate = findViewById(R.id.schoolNameUpdate_btn);
        add_student = findViewById(R.id.add_student_btn);
        recyclerView = findViewById(R.id.student_recyclerview);
        listDataModels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String stclass = studentClass.getText().toString();
        studentClass.setShowSoftInputOnFocus(true);
        studentAdapter = new StudentAdapter(this,listDataModels,stclass);
        recyclerView.setAdapter(studentAdapter);
        currentDate = findViewById(R.id.datetext);
        //studentAdapter.notifyDataSetChanged();



        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat dat = new SimpleDateFormat("dd/ MMM /yyyy", Locale.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("MMMyyyy", Locale.getDefault());
        SimpleDateFormat dmy = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDmy = dmy.format(c);
        String formattedDate = df.format(c);
        String formaDate = dat.format(c);
        currentDate.setText(formaDate);
        //           DateFormat dateFormat = new SimpleDateFormat("MM");
//            Date date = new Date();
//            Log.d("Month",dateFormat.format(date));
//            currentMonth = dateFormat.format(date);

        progressBar.setVisibility(View.VISIBLE);


        String userid = firebaseAuth.getCurrentUser().getUid();

        getListbtn.setOnClickListener(view -> {

            if(TextUtils.isEmpty(studentClass.getText().toString())){
                Toast.makeText(MainActivity.this, "Plese enter class", Toast.LENGTH_SHORT).show();
            }
            else
            {
            isSubmitButtonClickec=false;
            myEdit.putBoolean("submitclicked",isSubmitButtonClickec);

            getLitProgressbar.setVisibility(View.VISIBLE);
            stclasss = studentClass.getText().toString();

            myEdit.putString("stclass", stclasss);

            myEdit.commit();
            myEdit.apply();

            firebaseFirestore.collection("Admin")
                    .document(userid).collection(studentClass.getText().toString())
                    .document(formattedDate).collection(formattedDmy)
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
                        listDataModels.clear();
                        getLitProgressbar.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()){
                            //recyclerView.clearOnChildAttachStateChangeListeners();
                            StudentListDataModel city = document.toObject(StudentListDataModel.class);
                            listDataModels.add(city);
                            recyclerView.setVisibility(View.VISIBLE);
                             studentAdapter.notifyDataSetChanged();
                        }

                    }}
                }
            });
        }});

        //String useridd = firebaseAuth.getCurrentUser().getUid();
        //String schoolNamee = schoolname.getText().toString();


        firebaseFirestore.collection("Admin")
                .document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    DocumentSnapshot document = task.getResult();
                    schoolname.setText(document.getString("schoolName"));
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
                    //schoolname.setFocusable(false);
                }
            });
        });

        add_student.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,AddStudent.class));
        });

        viewAll.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,ViewAllActivity.class));
        });


    }
}