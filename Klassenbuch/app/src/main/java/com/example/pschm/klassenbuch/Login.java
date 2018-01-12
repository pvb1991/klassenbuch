package com.example.pschm.klassenbuch;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pschm.klassenbuch.SqLite.Teacher;
import com.example.pschm.klassenbuch.SqLite.TeacherDataSource;

import java.util.ArrayList;
import java.util.List;

import static com.example.pschm.klassenbuch.R.layout.activity_menue;



public class Login extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private Button btn_anmelden;
    private Button btn_options;
    private ImageView img_logo;
    private EditText tf_email;
    private EditText tf_passwort;
    private String teacherID = "1";
    public static final String LOG_TAG = Login.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.setTheme(R.style.MyTextViewStyle);

        sharedPreferences = getSharedPreferences("spFile", 0);
        createDatabase(this);

        //Widges erstellen
        btn_anmelden = (Button) findViewById(R.id.buttonAnmelden);
        btn_options = (Button) findViewById(R.id.buttonOptions);
        img_logo = (ImageView) findViewById(R.id.logo);
        tf_email = (EditText) findViewById(R.id.EingabeEmail);
        tf_passwort = (EditText) findViewById(R.id.EingabePasswort);

        btn_anmelden.setText(R.string.login);
        //btn_options.setText(R.string.settings);
        btn_options.setText("BAAR BUTTON");

        //Bildschirmmaße bestimmen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        //Logo größe anpassen
        ViewGroup.LayoutParams params = img_logo.getLayoutParams();
        params.width = screenWidth / 2;
        params.height = screenHeight / 3;
        img_logo.requestLayout();

        //Textfelder größe anpassen
        params = tf_email.getLayoutParams();
        params.width = screenWidth / 3;
        tf_email.requestLayout();

        params = tf_passwort.getLayoutParams();
        params.width = screenWidth / 3;
        tf_passwort.requestLayout();

        //Buttonsgröße anpassen
        params = btn_anmelden.getLayoutParams();
        params.width = screenWidth / 5;
        btn_anmelden.requestLayout();

        params = btn_options.getLayoutParams();
        params.width = screenWidth / 5;
        btn_options.requestLayout();

        // Button clicked
        btn_anmelden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher teacher = new Teacher();
                boolean matchEmail = false;
                boolean matchPassword = false;

                List<Teacher> listTeacher = new ArrayList<>();
                listTeacher = TeacherDataSource.getStaticAllTeacherList(v.getContext());

                String strEmail = String.valueOf(tf_email.getText());
                String strPassword = String.valueOf(tf_passwort.getText());

                for(int i=0; i!=listTeacher.size(); i++){
                    teacher = listTeacher.get(i);
                    if(teacher.getEmail().equals(strEmail)){
                        matchEmail = true;
                        break;
                    }
                }

                if(teacher.getPassword().equals(strPassword)){
                    matchPassword = true;
                }

                if(matchEmail && matchPassword){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("teacherID", String.valueOf(teacher.getID()));
                    editor.putInt("admin", teacher.getAdmin());
                    editor.commit();

                    Intent intent = new Intent(Login.this, Menue.class);
                    intent.putExtra(teacherID, teacherID);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(v.getContext(), R.string.wrongInput, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(Login.this, settings.class);
                Intent intent = new Intent(Login.this, baar.class);
                startActivity(intent);
            }
        });
    }

    private void createDatabase(Context c){
        List<Teacher> list = new ArrayList<>();
        list = TeacherDataSource.getStaticAllTeacherList(c);
    }
  /*  private boolean Connection() {
        boolean Wifi = false;
        boolean Mobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo NI : netInfo) {
            if (NI.getTypeName().equalsIgnoreCase("WIFI")) {
                if (NI.isConnected()) {
                    Wifi = true;
                }
            }
            if (NI.getTypeName().equalsIgnoreCase("MOBILE"))
                if (NI.isConnected()) {
                    Mobile = true;
                }
        }


        return Mobile;
    }*/


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
