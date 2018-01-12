package com.example.pschm.klassenbuch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pschm.klassenbuch.R;
import com.example.pschm.klassenbuch.SqLite.Unit;

import java.util.List;

/**
 * Created by pschm on 10.01.2018.
 */

public class stringListAttapter extends ArrayAdapter<String> {
    private List<String> items;
    private int layoutResourceId;
    private Context context;

    public stringListAttapter(Context context, int layoutResourceId, List<String> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        stringListAttapter.StringHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new stringListAttapter.StringHolder();
        holder.str = items.get(position);
        holder.tv = (TextView)row.findViewById(R.id.textView1);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(stringListAttapter.StringHolder holder) {
        holder.tv.setText(holder.str);
    }

    public static class StringHolder {
        String str;
        TextView tv;
    }
}