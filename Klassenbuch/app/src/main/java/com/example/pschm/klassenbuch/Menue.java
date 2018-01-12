package com.example.pschm.klassenbuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class Menue extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static Button btnStartUnit;
    private static Button btnListofStudents;
    private static Button btnNewClass;

    private static Button btnListOfClass;
    private static Button btnCreateRoom;
    private static Button btnAdmin;
    private static Button btnLogout;
    private static Button btnDbTest;

    private String teacherID;
    private int admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_menue);
        this.setTheme(R.style.MyTextViewStyle);
        sharedPreferences = getSharedPreferences("spFile", 0);
        teacherID = sharedPreferences.getString("teacherID", null);
        admin = sharedPreferences.getInt("admin", 0);

        btnStartUnit        = (Button) findViewById(R.id.btnStartUnit);
        btnListofStudents   = (Button) findViewById(R.id.btnListOfClass);
        btnNewClass         = (Button) findViewById(R.id.btnCreateNewClass);
        btnListOfClass      = (Button) findViewById(R.id.btnListofStudents);
        btnCreateRoom       = (Button) findViewById(R.id.btnCreateRoom);
        btnAdmin            = (Button) findViewById(R.id.btnAdmin);
        btnLogout           = (Button) findViewById(R.id.btnLogout);

        btnStartUnit.setText(R.string.startUnit);
        btnStartUnit.setTextSize(20);
        btnListofStudents.setText(R.string.listOfStudents);
        btnListofStudents.setTextSize(20);
        btnNewClass.setText(R.string.newClass);
        btnNewClass.setTextSize(20);
        btnListOfClass.setText(R.string.listOfClass);
        btnListOfClass.setTextSize(20);
        btnCreateRoom.setText(R.string.createRoom);
        btnCreateRoom.setTextSize(20);
        btnAdmin.setText(R.string.admin);
        btnAdmin.setTextSize(20);
        btnLogout.setText(R.string.logout);
        btnLogout.setTextSize(20);


        btnStartUnit.setBackgroundResource(R.drawable.ic_orange_button);
        btnListofStudents.setBackgroundResource(R.drawable.ic_orange_button);
        btnNewClass.setBackgroundResource(R.drawable.ic_orange_button);
        btnListOfClass.setBackgroundResource(R.drawable.ic_orange_button);
        btnCreateRoom.setBackgroundResource(R.drawable.ic_orange_button);
        btnAdmin.setBackgroundResource(R.drawable.ic_orange_button);
        btnLogout.setBackgroundResource(R.drawable.ic_orange_button);

        if(admin == 0){
            btnAdmin.setVisibility(View.INVISIBLE);
        }

        //--------------------------------------------------------------
        //Test-Button für Datenbanktest
        /*btnDbTest = (Button) findViewById(R.id.btnDbTest);
        btnDbTest.setText("DB TEST");
        btnDbTest.setBackgroundResource(R.drawable.ic_orange_button);
        btnDbTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menue.this, DBtest.class);
                startActivity(intent);
            }
        });*/


        //------------------------------------------------------------
        btnStartUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menue.this, StartUnit.class);
                startActivity(intent);
            }
        });

        btnListofStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menue.this, ListOfStudents.class);
                startActivity(intent);
            }
        });

        btnNewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menue.this, NewClass.class);
                startActivity(intent);
            }
        });

        btnListOfClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menue.this, ListOfClass.class);
                startActivity(intent);

            }
        });

        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menue.this, CreateRoom.class);
                startActivity(intent);
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menue.this, admin.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menue.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}


