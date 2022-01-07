package com.ajmalyousufza.attendanceregister.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.ajmalyousufza.attendanceregister.R;

public class MainActivity extends AppCompatActivity {

    EditText schoolname;
    Button schoolupdate,add_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        schoolname = findViewById(R.id.schoolName);
        schoolupdate = findViewById(R.id.schoolNameUpdate_btn);
        add_student = findViewById(R.id.add_student_btn);

        add_student.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,AddStudent.class));
        });

    }
}