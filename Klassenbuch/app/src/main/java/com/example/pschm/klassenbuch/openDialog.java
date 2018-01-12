package com.example.pschm.klassenbuch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pschm.klassenbuch.SqLite.AttandanceDataSource;
import com.example.pschm.klassenbuch.SqLite.Class;
import com.example.pschm.klassenbuch.SqLite.ClassDataSource;
import com.example.pschm.klassenbuch.SqLite.InClass;
import com.example.pschm.klassenbuch.SqLite.InClassDataSource;
import com.example.pschm.klassenbuch.SqLite.Informed;
import com.example.pschm.klassenbuch.SqLite.InformedDataSource;
import com.example.pschm.klassenbuch.SqLite.KlassenbuchDbHelper;
import com.example.pschm.klassenbuch.SqLite.Mark;
import com.example.pschm.klassenbuch.SqLite.MarkDataSource;
import com.example.pschm.klassenbuch.SqLite.Room;
import com.example.pschm.klassenbuch.SqLite.RoomDataSource;
import com.example.pschm.klassenbuch.SqLite.Student;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;
import com.example.pschm.klassenbuch.SqLite.Teacher;
import com.example.pschm.klassenbuch.SqLite.TeacherDataSource;
import com.example.pschm.klassenbuch.SqLite.Unit;
import com.example.pschm.klassenbuch.SqLite.UnitDataSource;

import java.util.List;

/**
 * Created by pschm on 20.12.2017.
 */

public class openDialog {
    private static openDialog oD = new openDialog();

    public static final String LOG_TAG_STUDENT = Student.class.getSimpleName();
    public static final String LOG_TAG_TEACHER = Teacher.class.getSimpleName();
    public static final String LOG_TAG_CLASS = Class.class.getSimpleName();
    public static final String LOG_TAG_ROOM = Room.class.getSimpleName();
    public static final String LOG_TAG_SUBJECT = Unit.class.getSimpleName();
    public static final String LOG_TAG_INCLASS = InClass.class.getSimpleName();
    public static final String LOG_TAG_INFORMED = Informed.class.getSimpleName();
    public static final String LOG_TAG_MARK = Mark.class.getSimpleName();

    private StudentDataSource studentDataSource;
    private TeacherDataSource teacherDataSource;
    private ClassDataSource classDataSource;
    private RoomDataSource roomDataSource;
    private UnitDataSource subjectDataSource;
    private InClassDataSource inClassDataSource;
    private InformedDataSource informedDataSource;
    private MarkDataSource markDataSource;

    public static EditText etVorname;
    public static EditText etName;
    public static EditText etStreet;
    public static EditText etHousnumber;
    public static EditText etZipCode;
    public static EditText etLocus;
    public static EditText etEmail;
    public static EditText etPassword;
    public static EditText etClassname;
    public static EditText etFloor;
    public static EditText etRoomnumber;
    public static EditText etSubject;
    public static EditText[] editTexts;

    public static TextView tv;
    public static TextView tvCooporationTitle;
    public static TextView tvCooporation;
    public static TextView tvHomeworkTitle;
    public static TextView tvHomework;
    public static TextView tvExamTitle;
    public static TextView tvExam;
    public static TextView tvAttendanceTitle;
    public static TextView  tvAttendance;
    public static int check = 0;
    public static List<Unit> UnitList;
    public static List<Student> studentList;
    public static List<Class> classList;
    public static List<InClass> inClassList;
    public static Mark mark;

    public static LinearLayout llTable;
    public static Spinner spStudent;
    public static Spinner spClass;
    public static Spinner spTeacher;
    public static Spinner spUnit;


    public static void DialogNewStudent(final Context c){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_new_student, null);
        c.setTheme(R.style.MyTextViewStyle);


        Button btnSave = (Button) mView.findViewById(R.id.btnSave);
        btnSave.setText(R.string.save);

        TextView tvTitel = (TextView) mView.findViewById(R.id.tvTittel);
        TextView tvVorname = (TextView) mView.findViewById(R.id.tvVorname);
        final TextView tvName = (TextView) mView.findViewById(R.id.tvName);
        TextView tvStreet = (TextView) mView.findViewById(R.id.tvStreet);
        TextView tvHousnumber = (TextView) mView.findViewById(R.id.tvHousenumber);
        TextView tvZipCode = (TextView) mView.findViewById(R.id.tvZipcode);
        TextView tvLocus = (TextView) mView.findViewById(R.id.tvLocus);

        tvTitel.setText(R.string.createNewStudent);
        tvVorname.setText(R.string.firstname);
        tvName.setText(R.string.name);
        tvStreet.setText(R.string.street);
        tvHousnumber.setText(R.string.housenumber);
        tvZipCode.setText(R.string.zipCode);
        tvLocus.setText(R.string.locus);

        etVorname = (EditText) mView.findViewById(R.id.etVorame);
        etName = (EditText) mView.findViewById(R.id.etName);
        etStreet = (EditText) mView.findViewById(R.id.etStreet);
        etHousnumber = (EditText) mView.findViewById(R.id.etHousenumber);
        etZipCode = (EditText) mView.findViewById(R.id.etZipcode);
        etLocus = (EditText) mView.findViewById(R.id.etLocus);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        oD.studentDataSource = new StudentDataSource(c);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etVorname.getText().toString() == null || etName.getText().toString() == null || etStreet.getText().toString() == null ||
                        etHousnumber.getText().toString() == null || etZipCode.getText().toString() == null || etLocus.getText().toString() == null){
                    //tvName.setText("uifsvjknvs");
                }
                else {
                    Log.d(LOG_TAG_STUDENT, "Die Datenquelle wird geöffnet.");
                    oD.studentDataSource.open();

                    Student student = oD.studentDataSource.createStudent(etVorname.getText().toString(), etName.getText().toString(), etStreet.getText().toString(),
                            etHousnumber.getText().toString(), Integer.parseInt(etZipCode.getText().toString()), etLocus.getText().toString());

                    Log.d(LOG_TAG_STUDENT, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
                    Log.d(LOG_TAG_STUDENT, "ID: " + student.getID() + ", Inhalt: " + student.toString());

                    Log.d(LOG_TAG_STUDENT, "Die Datenquelle wird geschlossen.");
                    oD.studentDataSource.close();

                    admin.StudenArrayAdapterList.clear();
                    admin.StudenArrayAdapterList.addAll(StudentDataSource.getStaticAllStudentList(c));
                    admin.StudenArrayAdapter.notifyDataSetChanged();
                    admin.lvList.setAdapter(admin.StudenArrayAdapter);
                    dialog.dismiss();
                }
            }
        });
    }

    public static void DialogNewTeacher(final Context c){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_new_teacher, null);
        c.setTheme(R.style.MyTextViewStyle);

        Button btnSave = (Button) mView.findViewById(R.id.btnSave);
        btnSave.setText(R.string.save);

        TextView tvTitel = (TextView) mView.findViewById(R.id.tvTittel);
        TextView tvVorname = (TextView) mView.findViewById(R.id.tvVorname);
        TextView tvName = (TextView) mView.findViewById(R.id.tvName);
        TextView tvStreet = (TextView) mView.findViewById(R.id.tvStreet);
        TextView tvHousnumber = (TextView) mView.findViewById(R.id.tvHousenumber);
        TextView tvZipCode = (TextView) mView.findViewById(R.id.tvZipcode);
        TextView tvLocus = (TextView) mView.findViewById(R.id.tvLocus);
        TextView tvEmail = (TextView) mView.findViewById(R.id.tvEmail);
        TextView tvPassword = (TextView) mView.findViewById(R.id.tvPassword);
        TextView tvAdmin = (TextView) mView.findViewById(R.id.tvAdmin);

        final CheckBox cbAdmin = (CheckBox) mView.findViewById(R.id.cbAdmin);

        tvTitel.setText(R.string.createNewStudent);
        tvVorname.setText(R.string.firstname);
        tvName.setText(R.string.name);
        tvStreet.setText(R.string.street);
        tvHousnumber.setText(R.string.housenumber);
        tvZipCode.setText(R.string.zipCode);
        tvLocus.setText(R.string.locus);
        tvEmail.setText(R.string.email);
        tvPassword.setText(R.string.password);
        tvAdmin.setText("");
        cbAdmin.setText(R.string.admin);

        etVorname = (EditText) mView.findViewById(R.id.etVorame);
        etName = (EditText) mView.findViewById(R.id.etName);
        etStreet = (EditText) mView.findViewById(R.id.etStreet);
        etHousnumber = (EditText) mView.findViewById(R.id.etHousenumber);
        etZipCode = (EditText) mView.findViewById(R.id.etZipcode);
        etLocus = (EditText) mView.findViewById(R.id.etLocus);
        etEmail = (EditText) mView.findViewById(R.id.etEmail);
        etPassword = (EditText) mView.findViewById(R.id.etPassword);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        oD.teacherDataSource = new TeacherDataSource(c);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = 0;
                if(cbAdmin.isChecked()){
                    check = 1;
                }
                Log.d(LOG_TAG_TEACHER, "Die Datenquelle wird geöffnet.");
                oD.teacherDataSource.open();

                Teacher teacher = oD.teacherDataSource.createTeacher(etVorname.getText().toString(), etName.getText().toString(), etStreet.getText().toString(),
                        etHousnumber.getText().toString(), Integer.parseInt(etZipCode.getText().toString()), etLocus.getText().toString(), etEmail.getText().toString(),
                        etPassword.getText().toString(), check);

                Log.d(LOG_TAG_TEACHER, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
                Log.d(LOG_TAG_TEACHER, "ID: " + teacher.getID() + ", Inhalt: " + teacher.toString());

                Log.d(LOG_TAG_TEACHER, "Die Datenquelle wird geschlossen.");
                oD.teacherDataSource.close();

                admin.TeacherArrayAdapterList.clear();
                admin.TeacherArrayAdapterList.addAll(TeacherDataSource.getStaticAllTeacherList(c));
                admin.TeacherArrayAdapter.notifyDataSetChanged();
                admin.lvList.setAdapter(admin.TeacherArrayAdapter);
                dialog.dismiss();
            }
        });
    }

    public static void DialogNewClass(final Context c){

        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_new_class, null);
        c.setTheme(R.style.MyTextViewStyle);

        Button btnSave = (Button) mView.findViewById(R.id.btnSave);
        btnSave.setText(R.string.save);

        TextView tvTitel = (TextView) mView.findViewById(R.id.tvTittel);
        TextView tvClass = (TextView) mView.findViewById(R.id.tvClassName);

        tvTitel.setText(R.string.createNewStudent);
        tvClass.setText(R.string.className);

        etClassname = (EditText) mView.findViewById(R.id.etClassName);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        oD.classDataSource = new ClassDataSource(c);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG_CLASS, "Die Datenquelle wird geöffnet.");
                oD.classDataSource.open();

                Class classClass = oD.classDataSource.createClass(etClassname.getText().toString());

                Log.d(LOG_TAG_CLASS, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
                Log.d(LOG_TAG_CLASS, "ID: " + classClass.getID() + ", Inhalt: " + classClass.toString());

                Log.d(LOG_TAG_CLASS, "Die Datenquelle wird geschlossen.");
                oD.classDataSource.close();

                admin.ClassArrayAdapterList.clear();
                admin.ClassArrayAdapterList.addAll(ClassDataSource.getStaticAllClassList(c));
                admin.ClassArrayAdapter.notifyDataSetChanged();
                admin.lvList.setAdapter(admin.ClassArrayAdapter);
                dialog.dismiss();
            }
        });
    }

    public static void DialogNewRoom(final Context c){

        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_new_room, null);
        c.setTheme(R.style.MyTextViewStyle);

        Button btnSave = (Button) mView.findViewById(R.id.btnSave);
        btnSave.setText(R.string.save);

        TextView tvTitle = (TextView) mView.findViewById(R.id.tvTittel);
        TextView tvFloor = (TextView) mView.findViewById(R.id.tvFloor);
        TextView tvRoomnumber = (TextView) mView.findViewById(R.id.tvRoomnumber);

        tvTitle.setText(R.string.createNewStudent);
        tvFloor.setText(R.string.floor);
        tvRoomnumber.setText(R.string.roomnumber);

        etFloor = (EditText) mView.findViewById(R.id.etFloor);
        etRoomnumber = (EditText) mView.findViewById(R.id.etRoomnumber);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        oD.roomDataSource = new RoomDataSource(c);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG_ROOM, "Die Datenquelle wird geöffnet.");
                oD.roomDataSource.open();

                Room room = oD.roomDataSource.createRoom(Long.valueOf(etRoomnumber.getText().toString()), Integer.parseInt(etFloor.getText().toString()));

                Log.d(LOG_TAG_ROOM, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
                Log.d(LOG_TAG_ROOM, "ID: " + room.getRoomnumber() + ", Inhalt: " + room.toString());

                Log.d(LOG_TAG_ROOM, "Die Datenquelle wird geschlossen.");
                oD.roomDataSource.close();

                admin.RoomArrayAdapterList.clear();
                admin.RoomArrayAdapterList.addAll(RoomDataSource.getStaticAllRoomList(c));
                admin.RoomArrayAdapter.notifyDataSetChanged();
                admin.lvList.setAdapter(admin.RoomArrayAdapter);
                dialog.dismiss();
            }
        });

    }

    public static void DialogNewSubject(final Context c){

        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_new_subject, null);
        c.setTheme(R.style.MyTextViewStyle);

        Button btnSave = (Button) mView.findViewById(R.id.btnSave);
        btnSave.setText(R.string.save);

        TextView tvTitle = (TextView) mView.findViewById(R.id.tvTittel);
        TextView tvSubject = (TextView) mView.findViewById(R.id.tvSubject);

        tvTitle.setText(R.string.createNewStudent);
        tvSubject.setText(R.string.subject);

        etSubject = (EditText) mView.findViewById(R.id.etSubject);


        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        oD.subjectDataSource = new UnitDataSource(c);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG_SUBJECT, "Die Datenquelle wird geöffnet.");
                oD.subjectDataSource.open();

                Unit unit = oD.subjectDataSource.createUnit(etSubject.getText().toString());

                Log.d(LOG_TAG_SUBJECT, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
                Log.d(LOG_TAG_SUBJECT, "ID: " + unit.getID() + ", Inhalt: " + unit.toString());

                Log.d(LOG_TAG_SUBJECT, "Die Datenquelle wird geschlossen.");
                oD.subjectDataSource.close();

                admin.UnitArrayAdapterList.clear();
                admin.UnitArrayAdapterList.addAll(UnitDataSource.getStaticAllUnitList(c));
                admin.UnitArrayAdapter.notifyDataSetChanged();
                admin.lvList.setAdapter(admin.UnitArrayAdapter);
                dialog.dismiss();
            }
        });
    }

    public static void DialogStudentClass(final Context c){

        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_student_class, null);
        c.setTheme(R.style.MyTextViewStyle);

        Button btnSave = (Button) mView.findViewById(R.id.btnSave);
        btnSave.setText(R.string.save);

        TextView tvTitle = (TextView) mView.findViewById(R.id.tvTittel);
        TextView tvStudent = (TextView) mView.findViewById(R.id.tvStudent);
        TextView tvClass = (TextView) mView.findViewById(R.id.tvClass);

        tvTitle.setText(R.string.createNewStudent);
        tvStudent.setText(R.string.name);
        tvClass.setText(R.string.className);

        spStudent = (Spinner) mView.findViewById(R.id.spStudent);
        spClass = (Spinner) mView.findViewById(R.id.spClass);

        //Schüler die schon in einer Klasse sind, aus der studentList entfernen
        studentList = StudentDataSource.getStaticAllStudentList(c);
        inClassList = InClassDataSource.getStaticAllinClass(c);
        for(int i=0; i!=inClassList.size(); i++){
            InClass inClass = inClassList.get(i);
            for(int j=0; j!=studentList.size(); j++){
                Student student = studentList.get(j);
                if(inClass.getStudentID() == student.getID()){
                    studentList.remove(j);
                    break;
                }
            }
        }

        String[] StudentList = new String[studentList.size()];
        for(int i=0; i!= studentList.size(); i++){
            Student student = studentList.get(i);
            StudentList[i] = student.getName() + ", " + student.getVorname();
        }

        ArrayAdapter aa = new ArrayAdapter(c, android.R.layout.simple_spinner_item, StudentList);
        spStudent.setAdapter(aa);

        classList = ClassDataSource.getStaticAllClassList(c);
        String[] ClassList = new String[classList.size()];
        for(int i=0; i!= classList.size(); i++){
            Class claass = classList.get(i);
            ClassList[i] = claass.getTitle();
        }

        ArrayAdapter ab = new ArrayAdapter(c, android.R.layout.simple_spinner_item, ClassList);
        spClass.setAdapter(ab);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        oD.inClassDataSource = new InClassDataSource(c);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class claass = classList.get(spClass.getSelectedItemPosition());
                Student student = studentList.get(spStudent.getSelectedItemPosition());

                Log.d(LOG_TAG_INCLASS, "Die Datenquelle wird geöffnet.");
                oD.inClassDataSource.open();

                InClass inClass = oD.inClassDataSource.createInClass(claass.getID(), student.getID());

                Log.d(LOG_TAG_INCLASS, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
                Log.d(LOG_TAG_INCLASS, "Eingabe: " + inClass.toString());

                Log.d(LOG_TAG_INCLASS, "Die Datenquelle wird geschlossen.");
                oD.inClassDataSource.close();

                admin.inClassArrayAdapterList.clear();
                String[] strInClass = InClassDataSource.getStaticAllInClass(c);
                for(int i=0; i!=strInClass.length; i++){
                    admin.inClassArrayAdapterList.add(strInClass[i]);
                }
                admin.inClassArrayAdapter.notifyDataSetChanged();
                admin.lvList.setAdapter(admin.inClassArrayAdapter);

                dialog.dismiss();
            }
        });
    }

    public static void DialogTeacherUnit(final Context c){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_teacher_uni, null);
        c.setTheme(R.style.MyTextViewStyle);

        Button btnSave = (Button) mView.findViewById(R.id.btnSave);
        btnSave.setText(R.string.save);

        TextView tvTitle = (TextView) mView.findViewById(R.id.tvTittel);
        TextView tvTeacher = (TextView) mView.findViewById(R.id.tvTeacher);
        TextView tvUnit = (TextView) mView.findViewById(R.id.tvUnit);
        TextView tvClass = (TextView) mView.findViewById(R.id.tvClass);

        tvTitle.setText(R.string.createNewStudent);
        tvTeacher.setText(R.string.name);
        tvUnit.setText(R.string.UnitName);
        tvClass.setText(R.string.className);

        spTeacher = (Spinner) mView.findViewById(R.id.spTeacher);
        spUnit = (Spinner) mView.findViewById(R.id.spUnit);
        spClass = (Spinner) mView.findViewById(R.id.spClass);

        final String[][] TeacherListPlusID = TeacherDataSource.getAllTeacherNamesPlusID(c);
        String[] TeacherList = new String[TeacherListPlusID[1].length];
        for(int i=0; i!= TeacherListPlusID[1].length; i++){
            TeacherList[i] = TeacherListPlusID[1][i];
        }
        ArrayAdapter aa = new ArrayAdapter(c, android.R.layout.simple_spinner_item, TeacherList);
        spTeacher.setAdapter(aa);

        final String[][] UnitListPlusID = UnitDataSource.getAllUnitNamesPlusID(c);
        String[] UnitList = new String[UnitListPlusID[1].length];
        for(int i=0; i!= UnitListPlusID[1].length; i++){
            UnitList[i] = UnitListPlusID[1][i];
        }
        ArrayAdapter ab = new ArrayAdapter(c, android.R.layout.simple_spinner_item, UnitList);
        spUnit.setAdapter(ab);

        final String[][] ClassListPlusID = ClassDataSource.getAllClassNamesPlusID(c);
        String[] ClassList = new String[ClassListPlusID[1].length];
        for(int i=0; i!= ClassListPlusID[1].length; i++){
            ClassList[i] = ClassListPlusID[1][i];
        }
        ArrayAdapter ac = new ArrayAdapter(c, android.R.layout.simple_spinner_item, ClassList);
        spClass.setAdapter(ac);

        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        oD.informedDataSource = new InformedDataSource(c);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG_INFORMED, "Die Datenquelle wird geöffnet.");
                oD.informedDataSource.open();

                Informed informed = oD.informedDataSource.createInformed(Long.parseLong(UnitListPlusID[0][spUnit.getSelectedItemPosition()]),
                        Long.parseLong(TeacherListPlusID[0][spTeacher.getSelectedItemPosition()]),
                        Long.parseLong(ClassListPlusID[0][spClass.getSelectedItemPosition()]));

                Log.d(LOG_TAG_INFORMED, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
                Log.d(LOG_TAG_INFORMED, "Eingabe: " + informed.toString());

                Log.d(LOG_TAG_INFORMED, "Die Datenquelle wird geschlossen.");
                oD.informedDataSource.close();

                admin.informedArrayAdapterList.clear();
                String[] strInformed = InformedDataSource.getStaticAllInformed(c);
                for(int i=0; i!=strInformed.length; i++){
                    admin.informedArrayAdapterList.add(strInformed[i]);
                }
                admin.informedArrayAdapter.notifyDataSetChanged();
                admin.lvList.setAdapter(admin.informedArrayAdapter);

                dialog.dismiss();
            }
        });
    }

    public static void DialogInsertExamMark(Context c, String title, final List<Student> studentList, final String teacherId, final String unitID){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_insert_exam_mark, null);
        c.setTheme(R.style.MyTextViewStyle);

        TextView tvtitle = (TextView) mView.findViewById(R.id.tvClassTitle);
        tvtitle.setText(mView.getResources().getString(R.string.insterExam) + "\n" + title);


        LinearLayout llscroll = (LinearLayout) mView.findViewById(R.id.llscroll);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        editTexts = new EditText[studentList.size()];
        for(int i=0; i!=studentList.size(); i++){
            LinearLayout ll = new LinearLayout(c);

            TextView tvname = new TextView(c);
            Student student = studentList.get(i);
            tvname.setText(student.getName() + ", " + student.getVorname());
            tvname.setLayoutParams(param);


            EditText et = new EditText(c);
            et.setLayoutParams(param);
            editTexts[i] = et;

            ll.addView(tvname);
            ll.addView(et);
            llscroll.addView(ll);
        }

        Button btn = (Button) mView.findViewById(R.id.button2);
        btn.setText(R.string.save);
        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        oD.markDataSource = new MarkDataSource(c);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i!=studentList.size(); i++){
                    EditText et = editTexts[i];
                    Student student = studentList.get(i);
                    Log.d(ListOfClass.class.getSimpleName(), String.valueOf(et.getText()));

                    Log.d(LOG_TAG_MARK, "Die Datenquelle wird geöffnet.");
                    oD.markDataSource.open();

                    Mark mark = oD.markDataSource.createMark(student.getID(), Long.valueOf(teacherId), Long.valueOf(unitID), KlassenbuchDbHelper.MarkTypeExam, Integer.valueOf(String.valueOf(et.getText())), null);

                    Log.d(LOG_TAG_MARK, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
                    Log.d(LOG_TAG_MARK, "Eingabe: " + mark.toString());

                    Log.d(LOG_TAG_MARK, "Die Datenquelle wird geschlossen.");
                    oD.markDataSource.close();
                    dialog.dismiss();
                }
            }
        });
    }

    public static void DialogStudentDetails(final Context c, final Student student, final String teacherID, final String unitID){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c); //admin.this
        View mView = inflater.inflate(R.layout.dialog_student_details, null);
        c.setTheme(R.style.MyTextViewStyle);

        tv = new TextView(c);
        tvCooporationTitle = new TextView(c);
        tvCooporation = new TextView(c);
        tvHomeworkTitle = new TextView(c);
        tvHomework = new TextView(c);
        tvExamTitle = new TextView(c);
        tvExam = new TextView(c);
        tvAttendanceTitle = new TextView(c);
        tvAttendance = new TextView(c);
        check = 0;


        tv.setText("Element #" +"\n"
                + c.getString(R.string.surname)         + ": "  + student.getName()     + "\n"
                + c.getString(R.string.name)             + ": "  + student.getVorname()     + "\n"
                + c.getString(R.string.street)          + ": "  + student.getStreet()     + " "  + student.getHousenumber()   + "\n"
                + c.getString(R.string.zipCode)                + ": "  + student.getZipcode()    + "\n"
                + c.getString(R.string.locus)                + ": "  + student.getLocus()     + "\n"
        );


        llTable = (LinearLayout) mView.findViewById(R.id.llTable);
        llTable.setOrientation(LinearLayout.VERTICAL);

        //Noten mündliche Mitarbeit
        //_________________________
        tvCooporationTitle.setText("\n" + c.getString(R.string.cooporation) + ":");
        tvCooporationTitle.setPaintFlags(tvCooporationTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvCooporationTitle.setTypeface(null, Typeface.BOLD);

        int countGood = MarkDataSource.CountMarkType(c, String.valueOf(student.getID()), String.valueOf(unitID), teacherID, KlassenbuchDbHelper.MarkTypeOral, String.valueOf(KlassenbuchDbHelper.MarkSuper));
        int countOkay = MarkDataSource.CountMarkType(c, String.valueOf(student.getID()), String.valueOf(unitID), teacherID, KlassenbuchDbHelper.MarkTypeOral, String.valueOf(KlassenbuchDbHelper.MarkOkay));
        int countBad = MarkDataSource.CountMarkType(c, String.valueOf(student.getID()), String.valueOf(unitID), teacherID, KlassenbuchDbHelper.MarkTypeOral, String.valueOf(KlassenbuchDbHelper.MarkBad));
        int numberOfReviews = countGood + countOkay + countBad;

        if (countGood != 0 || countOkay != 0 || countBad != 0) {
            // Durchschnitt berechnent von mündliche Mitarbeit
            float mean = 0;
            if (countGood != 0 && countOkay != 0) {
                mean = (((float)countGood * (float)100) + ((float)countOkay * (float)50)) / (float)numberOfReviews;
            } else if (countGood != 0 && countOkay == 0) {
                mean = ((float)countGood * (float)100) / (float)numberOfReviews;
            } else if (countGood == 0 && countOkay != 0) {
                mean = ((float)countOkay * (float)50) / (float)numberOfReviews;
            } else {
                mean = 0;
            }

            tvCooporation.setText(
                    c.getString(R.string.numberOfReviews) + ": " + numberOfReviews + "\n" +
                    c.getString(R.string.good) + " (100)     : " + countGood + "\n" +
                    c.getString(R.string.okay) + " (50)     : " + countOkay + "\n" +
                    c.getString(R.string.bad) + " (0) : " + countBad + "\n" +
                    c.getString(R.string.mean) + ": " + mean + "\n\n"
            );
        } else {
            tvCooporation.setText(c.getString(R.string.noEntries) +"\n");
        }

        //Hausaufgaben
        //______________________________________________________
        tvHomeworkTitle.setText(c.getString(R.string.homework) + ":");
        tvHomeworkTitle.setPaintFlags(tvHomeworkTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvHomeworkTitle.setTypeface(null, Typeface.BOLD);

        int countPresent = MarkDataSource.CountMarkType(c, String.valueOf(student.getID()), String.valueOf(unitID), teacherID, KlassenbuchDbHelper.MarkTypeHomework, String.valueOf(KlassenbuchDbHelper.MarkHomeworkYES));
        int countForget = MarkDataSource.CountMarkType(c, String.valueOf(student.getID()), String.valueOf(unitID), teacherID, KlassenbuchDbHelper.MarkTypeHomework, String.valueOf(KlassenbuchDbHelper.MarkHomeworkNO));
        float percentage = 0;

        if (countPresent != 0 || countForget != 0) {
            //Prozentualer Anteil der Hausaufgaben berechenen
            if (countPresent != 0) {
                percentage = ((float)countPresent * (float)100) / ((float)countForget + (float)countPresent);
            } else {
                percentage = 0;
            }

            tvHomework.setText(
                    c.getString(R.string.present) + " : " + countPresent + "\n" +
                    c.getString(R.string.forget) + " : " + countForget + "\n" +
                    c.getString(R.string.percentage) + " : " + percentage + "%\n\n"
            );
        }
        else {
            tvHomework.setText(c.getString(R.string.noEntries) + "\n");
        }

        //Klausuren
        //__________________________________________________
        tvExamTitle.setText(c.getString(R.string.exams));
        tvExamTitle.setPaintFlags(tvExamTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvExamTitle.setTypeface(null, Typeface.BOLD);

        List<Mark> Marklist = MarkDataSource.getExams(c, String.valueOf(student.getID()), String.valueOf(unitID), teacherID, KlassenbuchDbHelper.MarkTypeExam);
        int sumMark = 0;
        if (Marklist.size() != 0) {
            for (int j = 0; j != Marklist.size(); j++) {
                mark = Marklist.get(j);
                sumMark = sumMark + mark.getMark();
                String text = c.getString(R.string.exammark) + ": " + mark.getMark() + "\n";
                tvExam.setText(tvExam.getText() + text);
            }
            float examMean = sumMark / Marklist.size();
            tvExam.setText(tvExam.getText() + "\n" + c.getString(R.string.mean) + ": " + examMean + "\n\n");
        }
        else {
            tvExam.setText(c.getString(R.string.noEntries) + "\n");
        }

        // Anwesendheit
        //_______________________
        tvAttendanceTitle.setText(c.getString(R.string.attendance) + ":");
        tvAttendanceTitle.setPaintFlags(tvAttendanceTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvAttendanceTitle.setTypeface(null, Typeface.BOLD);

        int countDelays = AttandanceDataSource.CountDelay(c, String.valueOf(student.getID()), String.valueOf(unitID));
        int sumDelays = AttandanceDataSource.SumDelays(c, String.valueOf(student.getID()), String.valueOf(unitID));
        //CountAttendance(...) sind die Anwesendheitsstunden wo der Schüler zuspät gekommen ist noch nicht mitberechent
        //deswegen muss countDelays mit CountAttendance(...) addiert werden
        int countAttendancePresent = countDelays + AttandanceDataSource.CountAttendance(c, String.valueOf(student.getID()), String.valueOf(unitID), String.valueOf(KlassenbuchDbHelper.AttendancePresent));
        int countAttendanceAbsent = AttandanceDataSource.CountAttendance(c, String.valueOf(student.getID()), String.valueOf(unitID), String.valueOf(KlassenbuchDbHelper.AttendanceAbsent));

        if(countDelays !=0 || countAttendanceAbsent != 0 || countAttendancePresent != 0){
            float meanAttendance = 0;
            if(countAttendancePresent != 0 && countAttendanceAbsent != 0){
                meanAttendance = ((float)countAttendancePresent*(float)100)/((float) countAttendancePresent + (float)countAttendanceAbsent);
            }
            else if(countAttendancePresent != 0 && countAttendanceAbsent == 0){
                meanAttendance = 100;
            }
            else { meanAttendance = 0;}

            tvAttendance.setText(
                    c.getString(R.string.presentdays) + " : " + countAttendancePresent + "\n" +
                    c.getString(R.string.absentdays) + " : " + countAttendanceAbsent +  "\n" +
                    c.getString(R.string.present2) + " in % : " + meanAttendance + "\n" +
                    c.getString(R.string.delays) + " : " + sumDelays + "\n"
            );
        }
        else{
            tvAttendance.setText(c.getString(R.string.noEntries) +"\n");
        }

        if(check==0){
            llTable.addView(tvCooporationTitle);
            llTable.addView(tvCooporation);
            llTable.addView(tvHomeworkTitle);
            llTable.addView(tvHomework);
            llTable.addView(tvExamTitle);
            llTable.addView(tvExam);
            llTable.addView(tvAttendanceTitle);
            llTable.addView(tvAttendance);
            check= 1;
        }


        //Dialog Anzeigen
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}
