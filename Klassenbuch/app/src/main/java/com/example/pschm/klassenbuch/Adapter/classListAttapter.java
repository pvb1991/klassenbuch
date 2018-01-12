package com.example.pschm.klassenbuch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pschm.klassenbuch.R;
import com.example.pschm.klassenbuch.SqLite.Class;
import com.example.pschm.klassenbuch.SqLite.Teacher;

import java.util.List;

/**
 * Created by pschm on 10.01.2018.
 */

public class classListAttapter extends ArrayAdapter<Class> {
    private List<Class> items;
    private int layoutResourceId;
    private Context context;

    public classListAttapter(Context context, int layoutResourceId, List<Class> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        classListAttapter.ClassHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new classListAttapter.ClassHolder();
        holder.claass = items.get(position);
        holder.tv = (TextView) row.findViewById(R.id.textView1);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(classListAttapter.ClassHolder holder) {
        holder.tv.setText(holder.claass.toString());
    }

    public static class ClassHolder {
        Class claass;
        TextView tv;
    }
}
