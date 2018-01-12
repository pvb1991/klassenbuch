package com.example.pschm.klassenbuch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pschm.klassenbuch.SqLite.AttandanceDataSource;
import com.example.pschm.klassenbuch.SqLite.Attendance;
import com.example.pschm.klassenbuch.SqLite.ClassDataSource;
import com.example.pschm.klassenbuch.SqLite.InClass;
import com.example.pschm.klassenbuch.SqLite.InClassDataSource;
import com.example.pschm.klassenbuch.SqLite.Informed;
import com.example.pschm.klassenbuch.SqLite.InformedDataSource;
import com.example.pschm.klassenbuch.SqLite.KlassenbuchDbHelper;
import com.example.pschm.klassenbuch.SqLite.MarkDataSource;
import com.example.pschm.klassenbuch.SqLite.RoomOrder;
import com.example.pschm.klassenbuch.SqLite.RoomOrderDataSource;
import com.example.pschm.klassenbuch.SqLite.SeatingPlan;
import com.example.pschm.klassenbuch.SqLite.SeatingPlanDataSource;
import com.example.pschm.klassenbuch.SqLite.Student;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;
import com.example.pschm.klassenbuch.SqLite.UnitDataSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StartUnit extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final int NUM_ROWS = 9;
    private static final int NUM_COLS = 7;
    private static final int NUM_Switches = 30;
    private static String[][] StudentIdOnPlace = new String[NUM_ROWS][NUM_COLS];
    private static String teacherID;
    private static String unitID;
    private static String[] StudentIdList;
    private static String[] StudentNameList;
    private static String classID;
    private static Informed informed;
    private static List<Student> studentList;
    private static List<Student> studentAbsentList;
    private static List<Student> studentPresentList;
    private static List<SeatingPlan> SeatingPlanList;
    private static List<Informed> InformedList;
    private static Long milli;
    private static DateFormat DF_DATE = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
    private static MenuItem ItemDelay;
    private boolean control = false;
    private boolean homeworkControl = false;
    private static Switch switches[] = new Switch[NUM_Switches];
    private static Switch switcher = null;
    private static Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];
    public static Button ratingButtons[][];
    public static LinearLayout llRatingButtons[][];
    private static Button[] chooseUnitButtons;
    private static Button button = null;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_unit);
        sharedPreferences = getSharedPreferences("spFile", 0);
        teacherID = sharedPreferences.getString("teacherID", null);
        milli=System.currentTimeMillis();
        studentList = new ArrayList<>();
        populateButtons();
        chooseUnit();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start_unit, menu);
        ItemDelay = menu.findItem(R.id.btnDelay);

        return super.onCreateOptionsMenu(menu);
    }

    private void populateButtons() {

        TableLayout table = (TableLayout) findViewById(R.id.tableForButtons);

        for (int row = 0; row != NUM_ROWS; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            table.addView(tableRow);

            for (int col = 0; col != NUM_COLS; col++){
                final int FINAL_COL = col;
                final int FINAL_ROW = row;

                // Tisch Buttons
                button = new Button(this);
                button.setBackgroundResource(R.drawable.ic_orange_button);
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));

               // button.setText(getString(R.string.table));
                button.setVisibility(View.INVISIBLE);
                //button.setAlpha((float) 0.25);
                tableRow.addView(button);
                buttons[row][col] = button;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ButtonClicked(FINAL_COL, FINAL_ROW);
                    }
                });
            }

        }
    }

    private void ButtonClicked(int col, int row) {
        SeatingPlan seatingPlan = new SeatingPlan();
        Student student = new Student();
        for(int i=0; i!=SeatingPlanList.size(); i++){
            seatingPlan = SeatingPlanList.get(i);
            if(seatingPlan.getPosX()==col && seatingPlan.getPosY()==row && seatingPlan.getStudentID()!=0){
                student = StudentDataSource.getStudent(this, String.valueOf(seatingPlan.getStudentID()));
            }
        }

        openDialog.DialogStudentDetails(this, student, teacherID, unitID );

    }

    private void chooseUnit(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartUnit.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_choose_unit, null);
        this.setTheme(R.style.MyTextViewStyle);

        LinearLayout ll = (LinearLayout) mView.findViewById(R.id.ll);
        final AlertDialog dialog;
        InformedList = InformedDataSource.getAllStaticInformedFromTeacher(this, teacherID);
        chooseUnitButtons = new Button[InformedList.size()];

        //Dialog Anzeigen
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();


        for(int i=0; i!=InformedList.size(); i++){
            final int FINAL_ROW = i;
            informed = InformedList.get(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if(i==0) {
                params.setMargins(0, 0, 0, 10);
            }
            else{
                params.setMargins(0, 10, 0, 10 );
            }

            button = new Button(this);
            button.setBackgroundResource(R.drawable.ic_orange_button_without_border);

            button.setText(ClassDataSource.getClassWithID(this, String.valueOf(informed.getClassID())) + "\n" +
                           UnitDataSource.getUnitWithID(this, String.valueOf(informed.getUnitID())) );

            ll.addView(button, params);
            chooseUnitButtons[FINAL_ROW]= button;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    informed = InformedList.get(FINAL_ROW);
                    unitID = String.valueOf(informed.getUnitID());
                    classID = String.valueOf(informed.getClassID());
                    LoadSeating();

                    if(SeatingPlanList.size()!=0) {
                        setButtonsVISIBLE();
                        AttendanceControll();
                        setSeatingPlan(v.getContext());
                    }
                    else{
                        openSeatingPlanNotExist();
                    }

                    dialog.dismiss();
                }
            });
        }
    }

    private void LoadSeating(){
        SeatingPlanList = SeatingPlanDataSource.getStaticSeatingPlanList(this, unitID, teacherID);
    }

    private void setButtonsVISIBLE(){
        SeatingPlan seatingPlaan = SeatingPlanList.get(0);
        List<RoomOrder> OrderList = RoomOrderDataSource.getStaticAllRoomList(this, Integer.valueOf(String.valueOf(seatingPlaan.getRoomnumber())));

        for(int i=0; i!=OrderList.size(); i++){
            RoomOrder roomOrder = OrderList.get(i);
            Log.d(StartUnit.class.getSimpleName(),"" + roomOrder.toString() );
            if(roomOrder.getBocked() == 1) {
                button = buttons[roomOrder.getPosY()][roomOrder.getPosX()];
                button.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setSeatingPlan(Context c){
        SeatingPlan seatingPlan = new SeatingPlan();
        Student student = new Student();
        for(int i=0; i!=SeatingPlanList.size(); i++){
            seatingPlan = SeatingPlanList.get(i);
            student = StudentDataSource.getStudent(c, String.valueOf(seatingPlan.getStudentID()));
            button = buttons[seatingPlan.getPosY()][seatingPlan.getPosX()];
            button.setText(student.getName() + ", " + student.getVorname());
        }
    }

    private void setAbsentStudentOf50 (){
        for(int i=0; i!=studentAbsentList.size(); i++){
            Student student = studentAbsentList.get(i);
            for(int j=0; j!=SeatingPlanList.size(); j++){
                SeatingPlan seatingPlan = SeatingPlanList.get(j);
                if(seatingPlan.getStudentID() == student.getID()){
                    button = buttons[seatingPlan.getPosY()][seatingPlan.getPosX()];
                    button.setAlpha((float) 0.5);
                    break;
                }
            }
        }
    }

    private void openSeatingPlanNotExist(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartUnit.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_no_exist, null);

        TextView tv = (TextView) mView.findViewById(R.id.textView);
        tv.setText(getString(R.string.noSeatingPlan));

        Button btnBreak = (Button) mView.findViewById(R.id.btnBreak);
        btnBreak.setText(getString(R.string.back));

        Button btnNext = (Button) mView.findViewById(R.id.btnNext);
        btnNext.setText(getString(R.string.createSittingPlan));

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartUnit.this, Menue.class);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartUnit.this, NewClass.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //Dailog Anwesenheit
    private void AttendanceControll(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartUnit.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_attendance_controll, null);
        this.setTheme(R.style.MyTextViewStyle);

        TextView tvTitel = (TextView) mView.findViewById(R.id.textViewTitel);
        tvTitel.setText(R.string.attendancelist);

        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.lLayout);
        RadioGroup.LayoutParams layoutParamsSwitcher = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        layoutParamsSwitcher.setMargins(0,0,0,15);

        //Button Speichern
        Button btnSave = new Button(this);
        btnSave.setText(R.string.save);
        btnSave.setBackgroundResource(R.drawable.ic_orange_button);
        layout.addView(btnSave);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        List<InClass> inClassList = new ArrayList<>();
        studentList = new ArrayList<>();
        studentAbsentList = new ArrayList<>();
        studentPresentList = new ArrayList<>();

        inClassList = InClassDataSource.getStaticAllInClass(this, classID);
        for(int i=0; i!=inClassList.size(); i++){
            InClass inClass = inClassList.get(i);
            Student student = StudentDataSource.getStudent(this, String.valueOf(inClass.getStudentID()));
            studentList.add(student);
        }

        //Switcher für Anwesenheit
        for(int i=0; i != studentList.size(); i++) {
            final int FINAL_ROW = i;
            Student student = studentList.get(i);
            switcher= new Switch(this);
            switcher.setText(student.getName() + ", " + student.getVorname());
            switcher.setChecked(true);
            //Standard Orange bloß auf 25% transparent
            switcher.setBackgroundColor(getResources().getColor(R.color.logoOrange25));
            switches[FINAL_ROW]= switcher;
            layout.addView(switcher, layoutParamsSwitcher);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0; i!=studentList.size(); i++){
                        Student student = studentList.get(i);
                        switcher = switches[i];
                        if(switcher.isChecked()){
                            Log.d(StartUnit.class.getSimpleName(), "Ja");
                            //Date date = new Date();
                            //date.setTime(System.currentTimeMillis());
                            //Log.d(StartUnit.class.getSimpleName(), DF_DATE.format(date));
                            insertAttendance(i, KlassenbuchDbHelper.AttendancePresent);
                            studentPresentList.add(student);
                        }
                        else{
                            Log.d(StartUnit.class.getSimpleName(), "Nein");
                            insertAttendance(i, KlassenbuchDbHelper.AttendanceAbsent);
                            studentAbsentList.add(student);
                        }
                    }
                    setAbsentStudentOf50();

                    if(studentAbsentList.size() != 0){
                        ItemDelay.setVisible(true);
                    }
                    else{
                        ItemDelay.setVisible(false);
                    }
                    control=true;
                    dialog.dismiss();
                    //btnControll.setText(R.string.homeworkscontroll);
                }
            });
        }
    }

    private void insertAttendance(int pos, int delay){
        Student student = studentList.get(pos);
        AttandanceDataSource.StaticCreateAttendance(this, unitID, String.valueOf(student.getID()),milli, delay);
    }

    //Dailog Hausaufgabe
    //nutzt die selbe Dialog-Activity wie AttendanceControll!!!
    private void HomeworkControll(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartUnit.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_attendance_controll, null);

        TextView tvTitel = (TextView) mView.findViewById(R.id.textViewTitel);
        tvTitel.setText(R.string.homework);

        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.lLayout);
        RadioGroup.LayoutParams layoutParamsSwitcher = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        layoutParamsSwitcher.setMargins(0,0,0,15);

        //Button Speichern
        Button btnSave = new Button(this);
        btnSave.setText(R.string.save);
        btnSave.setBackgroundResource(R.drawable.ic_orange_button);
        layout.addView(btnSave);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        switches = new Switch[studentPresentList.size()];
        //Switcher für Hausaufgaben
        for(int i=0; i != studentPresentList.size(); i++) {
            final int FINAL_ROW = i;
            Student student = studentPresentList.get(i);
            switcher= new Switch(this);
            switcher.setText(student.getName() + ", " + student.getVorname());
            switcher.setChecked(true);
            //Standard Orange bloß auf 25% transparent
            switcher.setBackgroundColor(getResources().getColor(R.color.logoOrange25));
            switches[FINAL_ROW]= switcher;
            layout.addView(switcher, layoutParamsSwitcher);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0; i!=studentPresentList.size(); i++){
                        switcher = switches[i];
                        if(switcher.isChecked()){
                            insertMark(i, KlassenbuchDbHelper.MarkHomeworkYES, KlassenbuchDbHelper.MarkTypeHomework);
                        }
                        else{
                            insertMark(i, KlassenbuchDbHelper.MarkHomeworkNO, KlassenbuchDbHelper.MarkTypeHomework);
                        }
                    }
                    homeworkControl = true;
                    control=true;
                    dialog.dismiss();
                }
            });
        }
    }

    private void insertMark(int pos, int yORn, String type){
        Student student = studentPresentList.get(pos);
        MarkDataSource.staticCreatMark(this, String.valueOf(student.getID()), teacherID, unitID, type, yORn, null);
    }

    private void StudentRating(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartUnit.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_start_unit_rating, null);
        this.setTheme(R.style.MyTextViewStyle);

        ratingButtons = new Button[3][studentPresentList.size()];
        llRatingButtons = new LinearLayout[3][studentPresentList.size()];

        TextView tvTitel = (TextView) mView.findViewById(R.id.textViewTitel);
        tvTitel.setText(R.string.oralGrade);

        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.layout);

        TableLayout tbLayout = new TableLayout(this);

        for (int row = 0; row != studentPresentList.size(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            tbLayout.addView(tableRow);
            Student student = studentPresentList.get(row);

            for (int col = 0; col != 4; col++) {
                final int FinalRow = row;
                final int FinalCol = col - 1;
                button = new Button(this);

                if(col==0) {
                    TextView tvName = new TextView(this);
                    tvName.setText(student.getName() + ", " + student.getVorname());
                    tvName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 3.0f));
                    tableRow.addView(tvName);
                }

                if(col==1) {
                    button.setBackgroundResource(R.drawable.smiley_schlecht);
                    button.setAlpha((float) 0.5);
                    ratingButtons[0][FinalRow] = button;

                    tableRow.addView(button);
                }

                if(col==2) {
                    button.setBackgroundResource(R.drawable.smiley_ok);
                    ratingButtons[1][FinalRow] = button;

                    tableRow.addView(button);
                }

                if(col==3) {
                    button.setBackgroundResource(R.drawable.smiley_gut);
                    button.setAlpha((float) 0.5);
                    ratingButtons[2][FinalRow] = button;

                    tableRow.addView(button);
                }


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       button = ratingButtons[0][FinalRow];
                       button.setAlpha((float) 0.5);
                       button = ratingButtons[1][FinalRow];
                       button.setAlpha((float) 0.5);
                       button = ratingButtons[2][FinalRow];
                       button.setAlpha((float) 0.5);

                       button = ratingButtons[FinalCol][FinalRow];
                       button.setAlpha((float) 1.0);
                    }
                });
            }

        }
//
        layout.addView(tbLayout);

        //Button Speichern
        Button btnSave = new Button(this);
        btnSave.setText(R.string.save);
        btnSave.setBackgroundResource(R.drawable.ic_orange_button);
        layout.addView(btnSave);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int emotion = 0;
                for(int i=0; i!=studentPresentList.size(); i++) {
                    for(int j=0; j!=3; j++) {
                        button = ratingButtons[j][i];
                        if(button.getAlpha()==((float)1.0)){
                            if(j==0){ emotion = KlassenbuchDbHelper.MarkBad;}
                            if(j==1){ emotion = KlassenbuchDbHelper.MarkOkay;}
                            if(j==2){ emotion = KlassenbuchDbHelper.MarkSuper;}
                            Log.d(StartUnit.class.getSimpleName(), ""+emotion);
                        }
                    }
                    insertMark(i, emotion, KlassenbuchDbHelper.MarkTypeOral);
                }
                dialog.dismiss();
                Intent intent = new Intent(StartUnit.this, Menue.class);
                startActivity(intent);
                finish();
                //btnControll.setText(R.string.homeworkscontroll);
            }
        });
    }

    private void openStudentDelay(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartUnit.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_student_delay, null);

        TextView tv = (TextView) mView.findViewById(R.id.tvTitle);
        tv.setText(getString(R.string.noSeatingPlan));

        LinearLayout ll = (LinearLayout) mView.findViewById(R.id.svll);
        final EditText etDelays[] = new EditText[studentAbsentList.size()];
        final Switch switchDelays[] = new Switch[studentAbsentList.size()];

        for(int i=0; i!=studentAbsentList.size(); i++){
            final int FINAL_ROW = i;
            Student student = studentAbsentList.get(i);
            TextView tvName = new TextView(this);
            tvName.setText("\n" + student.getName() + ", " + student.getVorname());

            LinearLayout llstundent = new LinearLayout(this);
            llstundent.setOrientation(LinearLayout.HORIZONTAL);
            TextView tvDelay = new TextView(this);
            tvDelay.setText(getString(R.string.delays) + " in min:");

            EditText etDelay = new EditText(this);
            //etDelay.setText("0");
            etDelays[FINAL_ROW] = etDelay;

            llstundent.addView(tvDelay);
            llstundent.addView(etDelay);

            ll.addView(tvName);
            ll.addView(llstundent);
            if(homeworkControl) {
                Switch switchDelay = new Switch(this);
                switchDelay.setChecked(true);
                switchDelay.setText("test");
                switchDelays[FINAL_ROW] = switchDelay;
                ll.addView(switchDelay);
            }
        }

        Button btnBreak = (Button) mView.findViewById(R.id.btnBack);
        btnBreak.setText(getString(R.string.back));

        Button btnNext = (Button) mView.findViewById(R.id.btnSave);
        btnNext.setText(getString(R.string.enterDelay));

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Student> holderList = new ArrayList<>();

                //Felder abfragen
                for(int i=0; i!=studentAbsentList.size(); i++){
                    EditText et = etDelays[i];
                    Student student = studentAbsentList.get(i);
                    if(!String.valueOf(et.getText()).equals("0") || !String.valueOf(et.getText()).equals("")) {
                        AttandanceDataSource.updateAttendance(v.getContext(), String.valueOf(et.getText()), String.valueOf(milli), unitID, String.valueOf(student.getID()));
                        holderList.add(student);
                    }

                    if(homeworkControl){
                        Switch sw = switchDelays[i];
                        if(sw.isChecked()){
                            MarkDataSource.staticCreatMark(v.getContext(), String.valueOf(student.getID()), teacherID, unitID, KlassenbuchDbHelper.MarkTypeHomework, KlassenbuchDbHelper.MarkHomeworkYES, null);
                        }
                        else{
                            MarkDataSource.staticCreatMark(v.getContext(), String.valueOf(student.getID()), teacherID, unitID, KlassenbuchDbHelper.MarkTypeHomework, KlassenbuchDbHelper.MarkHomeworkNO, null);
                        }
                    }
                }

                //Verspätete Schüler aus AbsentListe entfernen
                for(int i=0; i!=holderList.size(); i++){
                    Student holderstudent = holderList.get(i);
                    for(int j=0; j!=studentAbsentList.size(); j++){
                        Student absentstudent = studentAbsentList.get(j);
                        if(absentstudent.getID() == holderstudent.getID()){
                            studentAbsentList.remove(j);
                            break;
                        }
                    }

                    //Button von verspäteten Schüler auf Alpha 100 setzen
                    for(int k=0; k!=SeatingPlanList.size(); k++){
                        SeatingPlan seatingPlan = SeatingPlanList.get(k);
                        if(holderstudent.getID() == seatingPlan.getStudentID()){
                            button = buttons[seatingPlan.getPosY()][seatingPlan.getPosX()];
                            button.setAlpha((float) 1.0);
                        }
                    }
                }

                //Anwesendheitsliste Aktuellisieren
                studentPresentList = new ArrayList<>();
                boolean bpresent = true;

                for(int i=0; i!=studentList.size(); i++){
                    Student student = studentList.get(i);
                    bpresent = true;

                    for(int j=0; j!=studentAbsentList.size(); j++){
                            Student absentStudent = studentAbsentList.get(j);
                            if(absentStudent.getID() == student.getID()){
                                bpresent = false;
                            }
                    }

                    if(bpresent){
                        studentPresentList.add(student);
                    }
                }

                //Wenn alle schüler da sind den Zuspät-Button "verstecken"
                if(studentAbsentList.size() == 0){
                    ItemDelay.setVisible(false);
                }
                dialog.dismiss();
            }
        });
    }

    //Actionbar clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //--------------- Break Button ----------------------------------------
            case R.id.btnDelay:
                if(studentAbsentList.size()!=0) {
                    openStudentDelay();
                }

                return true;

            //--------------- Break Button ----------------------------------------
            case R.id.btnControll:
                if(studentPresentList.size()!=0) {
                    if (control) {
                        HomeworkControll();
                    } else {
                        AttendanceControll();
                    }
                }

                return true;

            //--------------- Save Button ----------------------------------------
            case R.id.btnEnding:
                if(studentList.size()!=0) {
                    if (control) {
                        StudentRating();
                    } else {
                        AttendanceControll();
                    }
                }

                return true;

            //---------------  ----------------------------------------

            default:
                return super.onOptionsItemSelected(item);
        }
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
