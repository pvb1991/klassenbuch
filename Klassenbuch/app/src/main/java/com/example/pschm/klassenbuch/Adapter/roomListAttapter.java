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
import com.example.pschm.klassenbuch.SqLite.Room;

import java.util.List;

/**
 * Created by pschm on 10.01.2018.
 */

public class roomListAttapter extends ArrayAdapter<Room> {
        private List<Room> items;
        private int layoutResourceId;
        private Context context;

        public roomListAttapter(Context context, int layoutResourceId, List<Room> items) {
                super(context, layoutResourceId, items);
          this.layoutResourceId = layoutResourceId;
          this.context = context;
          this.items = items;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          View row = convertView;
          roomListAttapter.RoomHolder holder = null;
          LayoutInflater inflater = ((Activity) context).getLayoutInflater();
          row = inflater.inflate(layoutResourceId, parent, false);

          holder = new roomListAttapter.RoomHolder();
          holder.room = items.get(position);
          holder.tv = (TextView) row.findViewById(R.id.textView1);

          row.setTag(holder);

          setupItem(holder);
          return row;
        }

        private void setupItem(roomListAttapter.RoomHolder holder) {
        holder.tv.setText(holder.room.toString());
        }

public static class RoomHolder {
    Room room;
    TextView tv;
}
}
