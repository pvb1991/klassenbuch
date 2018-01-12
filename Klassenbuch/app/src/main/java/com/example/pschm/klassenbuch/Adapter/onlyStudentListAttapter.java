package com.example.pschm.klassenbuch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pschm.klassenbuch.R;
import com.example.pschm.klassenbuch.SqLite.Student;

import java.util.List;

/**
 * Created by pschm on 04.01.2018.
 */

public class onlyStudentListAttapter extends ArrayAdapter<Student> {
    private List<Student> items;
    private int layoutResourceId;
    private Context context;

    public onlyStudentListAttapter(Context context, int layoutResourceId, List<Student> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        onlyStudentListAttapter.StudentHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new onlyStudentListAttapter.StudentHolder();
        holder.student = items.get(position);
        holder.tv = (TextView)row.findViewById(R.id.textView1);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(onlyStudentListAttapter.StudentHolder holder) {
        holder.tv.setText("ID: " + holder.student.getID() + " " + holder.student.getName() + ", " + holder.student.getVorname());
    }

    public static class StudentHolder {
        Student student;
        TextView tv;
    }
}