package com.example.pschm.klassenbuch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pschm.klassenbuch.R;
import com.example.pschm.klassenbuch.SqLite.Teacher;
import com.example.pschm.klassenbuch.SqLite.Unit;

import java.util.List;

/**
 * Created by pschm on 10.01.2018.
 */

public class unitListAttapter extends ArrayAdapter<Unit> {
    private List<Unit> items;
    private int layoutResourceId;
    private Context context;

    public unitListAttapter(Context context, int layoutResourceId, List<Unit> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        unitListAttapter.UnitHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new unitListAttapter.UnitHolder();
        holder.unit = items.get(position);
        holder.tv = (TextView)row.findViewById(R.id.textView1);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(unitListAttapter.UnitHolder holder) {
        holder.tv.setText(holder.unit.toString());
    }

    public static class UnitHolder {
        Unit unit;
        TextView tv;
    }
}