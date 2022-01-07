package com.ajmalyousufza.attendanceregister.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajmalyousufza.attendanceregister.Models.StudentListDataModel;
import com.ajmalyousufza.attendanceregister.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    Context context;
    List<StudentListDataModel> studentListDataModelList;

    public StudentAdapter(Context context, List<StudentListDataModel> studentListDataModelList) {
        this.context = context;
        this.studentListDataModelList = studentListDataModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.st_name.setText(studentListDataModelList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return studentListDataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView st_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            st_name = itemView.findViewById(R.id.student_name);
        }
    }
}
