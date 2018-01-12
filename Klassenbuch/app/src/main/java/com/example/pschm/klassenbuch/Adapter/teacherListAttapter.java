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
import com.example.pschm.klassenbuch.SqLite.Teacher;

import java.util.List;

/**
 * Created by pschm on 10.01.2018.
 */

public class teacherListAttapter extends ArrayAdapter<Teacher> {
    private List<Teacher> items;
    private int layoutResourceId;
    private Context context;

    public teacherListAttapter(Context context, int layoutResourceId, List<Teacher> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        teacherListAttapter.TeacherHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new teacherListAttapter.TeacherHolder();
        holder.teacher = items.get(position);
        holder.tv = (TextView)row.findViewById(R.id.textView1);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(teacherListAttapter.TeacherHolder holder) {
        holder.tv.setText("ID: " + holder.teacher.getID() + " " + holder.teacher.getName() + ", " + holder.teacher.getVorname());
    }

    public static class TeacherHolder {
        Teacher teacher;
        TextView tv;
    }
}
