package com.ajmalyousufza.attendanceregister.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajmalyousufza.attendanceregister.Activities.MainActivity;
import com.ajmalyousufza.attendanceregister.Models.StudentListDataModel;
import com.ajmalyousufza.attendanceregister.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Context context;
    String studentclass;
    List<StudentListDataModel> studentListDataModelList;
    // Fetching the stored data
    // from the SharedPreference
    SharedPreferences sh;

    public StudentAdapter(Context context, List<StudentListDataModel> studentListDataModelList,String studentclass) {
        this.context = context;
        this.studentListDataModelList = studentListDataModelList;
        this.studentclass = studentclass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMMyyyy", Locale.getDefault());
        SimpleDateFormat dmy = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedDmy = dmy.format(c);
        holder.present=0;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String userid = auth.getCurrentUser().getUid();

        sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String s1 = sh.getString("stclass", "");
        Boolean issubmitclicled = sh.getBoolean("submitclicked",false);

        holder.st_name.setText(studentListDataModelList.get(position).getName());
        if(holder.checkBox.isChecked()){
            holder.present=1;
            //Toast.makeText(context, "present "+holder.present, Toast.LENGTH_SHORT).show();
        }
        else {
            holder.present=0;
            //Toast.makeText(context, "present "+holder.present, Toast.LENGTH_SHORT).show();

        }

        //holder.checkBox.setChecked(true);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int present;
                if(compoundButton.isChecked()){
                    holder.present=1;
                    holder.checkBoxabsence.setChecked(false);

                    HashMap<String ,Object> map = new HashMap<>();
                    map.put("present",holder.present);



                    String presst = String.valueOf(holder.present);

                    HashMap<String ,Object> lmap = new HashMap<>();
                    lmap.put("present",presst);
                    firestore.collection("Admin")
                            .document(userid).collection(s1).document(holder.st_name.getText().toString())
                            .collection(formattedDate).document(formattedDmy).set(lmap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Present", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    holder.present=0;
                    holder.checkBoxabsence.setChecked(true);
                    String presst = String.valueOf(holder.present);

                    HashMap<String ,Object> emap = new HashMap<>();
                    emap.put("present",presst);
                    firestore.collection("Admin")
                            .document(userid).collection(s1).document(holder.st_name.getText().toString())
                            .collection(formattedDate).document(formattedDmy).set(emap);
                }

            }
        });

        holder.checkBoxabsence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    holder.present=0;
                    holder.checkBox.setChecked(false);
                    String presst = String.valueOf(holder.present);

                    HashMap<String ,Object> amap = new HashMap<>();
                    amap.put("present",presst);
                    firestore.collection("Admin")
                            .document(userid).collection(s1).document(holder.st_name.getText().toString())
                            .collection(formattedDate).document(formattedDmy).set(amap);

                    Toast.makeText(context, "Absent", Toast.LENGTH_SHORT).show();
                }
                else {
                    holder.present=1;
                    holder.checkBox.setChecked(true);

                    String presst = String.valueOf(holder.present);

                    HashMap<String ,Object> map = new HashMap<>();
                    map.put("present",presst);
                    firestore.collection("Admin")
                            .document(userid).collection(s1).document(holder.st_name.getText().toString())
                            .collection(formattedDate).document(formattedDmy).set(map);
                }
            }
        });

//        List<Integer> att_list = new ArrayList<>();
//        att_list.add(holder.present);
//
//        HashMap<String,Object> maphash = new HashMap<>();
//        maphash.put("monthattence",holder.present);

        if(issubmitclicled){

        }


       // Toast.makeText(context, "present "+holder.present+" /"+s1, Toast.LENGTH_SHORT).show();


    }

    @Override
    public int getItemCount() {
        return studentListDataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView st_name;
        CheckBox checkBox,checkBoxabsence;
        int present;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            st_name = itemView.findViewById(R.id.student_name);
            checkBox = itemView.findViewById(R.id.student_attentance_cb);
            checkBoxabsence = itemView.findViewById(R.id.student_attentance_absent);

        }
    }
}
