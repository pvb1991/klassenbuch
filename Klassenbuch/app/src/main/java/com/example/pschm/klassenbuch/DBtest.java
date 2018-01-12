package com.example.pschm.klassenbuch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pschm.klassenbuch.SqLite.Student;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;

import java.util.ArrayList;
import java.util.List;

public class DBtest extends AppCompatActivity {
    public Button btn;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    private EditText et7;
    private EditText et8;

    //private CursorAdapter ca;

    private CursorAdapter ca;
    //private static final String LOG_TAG = KlassenbuchOpenHandler.class.getSimpleName();


    boolean b = false;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
        btn = (Button) findViewById(R.id.button);
        et1 =(EditText)findViewById(R.id.editText1);
        et2 =(EditText)findViewById(R.id.editText2);
        et3 =(EditText)findViewById(R.id.editText3);
        et4 =(EditText)findViewById(R.id.editText4);
        et5 =(EditText)findViewById(R.id.editText5);
        et6 =(EditText)findViewById(R.id.editText6);
        et7 =(EditText)findViewById(R.id.editText7);
        et8 =(EditText)findViewById(R.id.editText8);
        final TextView tv = new TextView(this);
        TableLayout table = (TableLayout) findViewById(R.id.dbTestTable);
        TableRow tableRow = new TableRow(this);
        tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableLayout.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        table.addView(tableRow);
        table.addView(tv);

       // SqLiteStudent testMemo = new SqLiteStudent(1, "Peter", "Hans", "Strasse", "1", 10707, "Berlin");
        //Log.d(LOG_TAG, "Inhalt der Testmemo: " + testMemo.toString());
        //dataSource = new DbDataSource(this);

        //
        //



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }



}
