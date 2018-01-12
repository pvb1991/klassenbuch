package com.example.pschm.klassenbuch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pschm.klassenbuch.ListOfClass;
import com.example.pschm.klassenbuch.R;
import com.example.pschm.klassenbuch.SqLite.Student;
import com.example.pschm.klassenbuch.openDialog;

import java.util.List;

/**
 * Created by pschm on 04.01.2018.
 */

public class studentListAttapter extends ArrayAdapter<Student> {
    private List<Student> items;
    private int layoutResourceId;
    private Context context;
    private StudentHolder holder;
    private String teacherID;
    private String unitID;

    public studentListAttapter(Context context, int layoutResourceId, List<Student> items, String teacherID, String unitID) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
        this.teacherID = teacherID;
        this.unitID = unitID;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new StudentHolder();
        holder.student = items.get(position);
        holder.tv = (TextView)row.findViewById(R.id.textView1);
        holder.btn = (ImageButton)row.findViewById(R.id.button1);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = items.get(position);
                openDialog.DialogStudentDetails(context, student, teacherID, unitID);
                Log.d(ListOfClass.class.getSimpleName(), student.toString());
            }
        });
        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(StudentHolder holder) {
        holder.tv.setText(holder.student.getName() + ", " + holder.student.getVorname());
    }

    public static class StudentHolder {
        Student student;
        TextView tv;
        ImageButton btn;
    }
}
