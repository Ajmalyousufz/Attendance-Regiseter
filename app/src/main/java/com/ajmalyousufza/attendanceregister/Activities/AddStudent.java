package com.ajmalyousufza.attendanceregister.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajmalyousufza.attendanceregister.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class AddStudent extends AppCompatActivity {



    EditText studentClass;
    EditText studentName;
    Button addStudent;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        studentClass = findViewById(R.id.student_class_added);
        studentName = findViewById(R.id.studentName_added);
        addStudent = findViewById(R.id.add_student_button);
        progressBar = findViewById(R.id.prgressbarr);
        progressBar.setVisibility(View.GONE);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMMyyyy", Locale.getDefault());
        SimpleDateFormat dmy = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedDmy = dmy.format(c);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        addStudent.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String stdName = studentName.getText().toString();
            String stdClass = studentClass.getText().toString();
            //String currentMonth;

//            DateFormat dateFormat = new SimpleDateFormat("MM");
//            Date date = new Date();
//            Log.d("Month",dateFormat.format(date));
//            currentMonth = dateFormat.format(date);

            String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            HashMap<String,Object> stdatalist = new HashMap<>();
            stdatalist.put("name",stdName);
            stdatalist.put("class",stdClass);

            firestore.collection("Admin").document(uid)
                    .collection(stdClass).document(stdName).collection(formattedDate)
                    .document(formattedDmy)
                    .set(stdatalist).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddStudent.this, "Student "+stdName+" added", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}