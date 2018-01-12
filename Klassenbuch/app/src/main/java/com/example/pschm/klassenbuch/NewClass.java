package com.example.pschm.klassenbuch;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pschm.klassenbuch.SqLite.Class;
import com.example.pschm.klassenbuch.SqLite.ClassDataSource;
import com.example.pschm.klassenbuch.SqLite.Informed;
import com.example.pschm.klassenbuch.SqLite.InformedDataSource;
import com.example.pschm.klassenbuch.SqLite.Room;
import com.example.pschm.klassenbuch.SqLite.RoomDataSource;
import com.example.pschm.klassenbuch.SqLite.RoomOrder;
import com.example.pschm.klassenbuch.SqLite.RoomOrderDataSource;
import com.example.pschm.klassenbuch.SqLite.SeatingPlan;
import com.example.pschm.klassenbuch.SqLite.SeatingPlanCommands;
import com.example.pschm.klassenbuch.SqLite.SeatingPlanDataSource;
import com.example.pschm.klassenbuch.SqLite.Student;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;
import com.example.pschm.klassenbuch.SqLite.Unit;
import com.example.pschm.klassenbuch.SqLite.UnitDataSource;

import java.util.ArrayList;
import java.util.List;



public class NewClass extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final int NUM_ROWS = 9;
    private static final int NUM_COLS = 7;
    private static String[] StudentNameList;
    private static String teacherID ;
    private static int admin;
    private static int roomNumber;
    private static List<RoomOrder> roomOrderList;
    private static List<SeatingPlan> seatingPlanList;
    private static List<Student> studentList;
    private static List <Informed> informedList;
    private static Student student;
    public static SeatingPlan seatingPlan;
    private int radiobuttonid = -1;
    private static Button button = null;
    private static Spinner spClass = null;
    private static Spinner spFloor = null;
    private static Spinner spRoom = null;

    Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class);
        this.setTheme(R.style.MyTextViewStyle);

        sharedPreferences = getSharedPreferences("spFile", 0);
        teacherID = sharedPreferences.getString("teacherID", null);
        admin = sharedPreferences.getInt("admin", 0);
        seatingPlanList = new ArrayList<>();
        roomOrderList = new ArrayList<>();
        studentList = new ArrayList<>();

        populateButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menue_new_class, menu);

        MenuItem itemBtnClear = menu.findItem(R.id.action_clear);
        MenuItem itemBtnSave = menu.findItem(R.id.action_save);

        //Spinner Class
        MenuItem itemSpClass = menu.findItem(R.id.spinner);
        spClass = (Spinner) MenuItemCompat.getActionView(itemSpClass);
        spClass.setAdapter(setSpUnitClass());

        //Spinner Floor
        MenuItem itemSpFloor= menu.findItem(R.id.spinner2);
        spFloor = (Spinner) MenuItemCompat.getActionView(itemSpFloor);
        spFloor.setAdapter(setSpFloor());

        //Spinner Roomnumber
        MenuItem itemSpRoom= menu.findItem(R.id.spinner3);
        spRoom = (Spinner) MenuItemCompat.getActionView(itemSpRoom);
        spRoom.setAdapter(setSpRoom("1"));

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                studentList = getStudentList();
                setSeatingPlanOnButtons();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        spFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter spString = null;
                spString = setSpRoom(spFloor.getSelectedItem().toString());
                spRoom.setAdapter(spString);
                setSeatingPlanOnButtons();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {return;}
        });

        spRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSeatingPlanOnButtons();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        if(spClass.getSelectedItemPosition() == -1 || (spFloor.getSelectedItemPosition() == -1 && spRoom.getSelectedItemPosition() == -1)){
            itemBtnSave.setVisible(false);
            itemBtnClear.setVisible(false);
            itemSpClass.setVisible(false);
            itemSpFloor.setVisible(false);
            itemSpRoom.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public Student getStudent(String studentID){
        Student student = new Student();
        student = StudentDataSource.getStudent(this, studentID);

        return student;
    }

    private boolean loadSeatingPlanAndCheck(){
        seatingPlanList = new ArrayList<>();

        if(spClass.getSelectedItemPosition() != -1) {
            Informed informed = informedList.get(spClass.getSelectedItemPosition());
            seatingPlanList = SeatingPlanDataSource.getStaticCheckSeatingPlan(this, String.valueOf(roomNumber) , String.valueOf(informed.getUnitID()), teacherID);
        }
        else {
            openUnitNotExist();
        }

        if(seatingPlanList.size() != 0){
            return true;
        }
        else{
            return false;
        }
    }

    private List<RoomOrder> getRoomOrderList(){
        List<RoomOrder> r = new ArrayList<>();
        r= RoomOrderDataSource.getStaticAllRoomList(this, roomNumber);

        return r;
    }

    public boolean checkIfRoomDesignExist(int roomNumber){
        List<RoomOrder> OrderList = RoomOrderDataSource.getStaticAllRoomList(this, roomNumber);

        if(OrderList.size() != 0){
            return true;

        }
        else{
            return false;
        }
    }

    private ArrayAdapter setSpUnitClass(){
        informedList = new ArrayList<>();
        informedList  = InformedDataSource.getAllStaticInformedFromTeacher(this, teacherID);

        String[] strInformedList = new String[informedList.size()];

        for(int i=0; i!=informedList.size(); i++){
            Informed informed = informedList.get(i);
            Unit unit = UnitDataSource.getUnit(this, String.valueOf(informed.getUnitID()));
            Class classclass = ClassDataSource.getClass( this, String.valueOf(informed.getClassID()));
            strInformedList[i] = classclass.getTitle() + " " + unit.getTitle();
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strInformedList);

        return aa;
    }

    private void setSeatingPlanOnButtons(){
        studentList = new ArrayList<>();
        studentList = getStudentList();

        roomNumber =  Integer.valueOf(spRoom.getSelectedItem().toString());

        roomOrderList = new ArrayList<>();
        roomOrderList = getRoomOrderList();

        // alle buttons auf INVISIBLE setzen, weil bei raumwechsel werden die alten button noch angezeigt
        for(int row=0; row!=NUM_ROWS; row++){
            for(int col=0; col!=NUM_COLS; col++){
                button =buttons[row][col];
                button.setText(getString(R.string.table));
                button.setVisibility(View.INVISIBLE);
            }
        }

        if(checkIfRoomDesignExist(roomNumber)){

            for(int j=0; j!=roomOrderList.size(); j++){
                RoomOrder roomOrder = roomOrderList.get(j);
                Log.d(CreateRoom.class.getSimpleName(), roomOrder.toString());
                button = buttons[roomOrder.getPosY()][roomOrder.getPosX()];
                if(roomOrder.getBocked() == 1){
                    button.setVisibility(View.VISIBLE);
                }
                else{
                    button.setVisibility(View.INVISIBLE);
                }
            }

            if(loadSeatingPlanAndCheck()) {

                // Sitzplätze mit gespeicherten Plätze belegen
                for (int i = 0; i != seatingPlanList.size(); i++) {
                    SeatingPlan seatingPlan = seatingPlanList.get(i);

                    button = buttons[seatingPlan.getPosY()][seatingPlan.getPosX()];
                    student = getStudent(String.valueOf(seatingPlan.getStudentID()));

                    button.setText(student.getName() + ", " + student.getVorname());

                    //Aus der StudentList entfernen
                    for(int j=0; j!= studentList.size(); j++){
                        Student holder = studentList.get(j);
                        if(holder.getID() == student.getID()){
                            studentList.remove(j);
                            break;
                        }
                    }
                }

            }
            else{
                for(int row=0; row!=NUM_ROWS; row++){
                    for(int col=0; col!=NUM_COLS; col++){
                        button = buttons[row][col];
                        button.setText(getString(R.string.table));
                    }
                }
            }
        }
        else{
            openRoomNotExist();
        }
    }

    private List<Student> getStudentList(){
        List<Student> ls = new ArrayList<>();

        if(spClass.getSelectedItemPosition() != -1) {
            Informed informed = informedList.get(spClass.getSelectedItemPosition());

            String ClassID = String.valueOf(informed.getClassID());

            ls = StudentDataSource.getStudentListWhereInClass(this, ClassID);
        }

        return ls;
    }

    private ArrayAdapter setSpFloor(){
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, RoomDataSource.getStaticAllFloors(this));

        return aa;
    }

    private ArrayAdapter setSpRoom(String floor){
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, RoomDataSource.getStaticAllRoomsInFloor(this, floor));

        return aa;
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

                button = new Button(this);
                button.setBackgroundResource(R.drawable.ic_orange_button);
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                button.setText(R.string.table);
                button.setVisibility(View.INVISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ButtonClicked(FINAL_COL, FINAL_ROW);
                    }
                });

                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    private void ButtonClicked(int col, int row) {
        button = buttons[row][col];
        StudentSeatedHere(row, col);
    }

    private void StudentSeatedHere(final int row, final int col){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewClass.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_new_class_seating, null);

        //Titel
        TextView textViewTitel = (TextView) mView.findViewById(R.id.textViewTitel);
        textViewTitel.setText(R.string.whereseatinghere);

        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.lLayout);

        final RadioGroup rg = new RadioGroup(this);
        layout.addView(rg);

        //Margin einstellung für Radio Button
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 20);



        StudentNameList = new String[studentList.size()];

        for(int i=0; i!=studentList.size(); i++){

            student = studentList.get(i);
            //Log.d(NewClass.class.getSimpleName(), "a            "+student.toString());
            StudentNameList[i] = student.getName() + ", " + student.getVorname();
        }

        //Radio Button List
        for(int i=0; i!=studentList.size(); i++) {

            RadioButton rb = new RadioButton(this);
            rb.setText(StudentNameList[i]);
            rb.setId(i);
            rg.addView(rb);
        }


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
                radiobuttonid = rg.getCheckedRadioButtonId();
                if(radiobuttonid !=  -1) {
                    Student selectedStudent = studentList.get(rg.getCheckedRadioButtonId());
                    button.setText(selectedStudent.getName() + ", " + selectedStudent.getVorname());

                    SeatingPlan spHold = checkIsPlaceReserved(row, col);

                    if(spHold.getStudentID() != 0) {

                        //wenn Platz belegt ist
                        addStudent(String.valueOf(spHold.getStudentID()));

                        for(int i=0; i != seatingPlanList.size(); i++){
                            seatingPlan = seatingPlanList.get(i);
                            if(seatingPlan.getPosX() == col && seatingPlan.getPosY() == row){
                                seatingPlanList.remove(i);

                                SeatingPlan newSeatingPlan = new SeatingPlan();
                                newSeatingPlan.setStudentID(selectedStudent.getID());
                                newSeatingPlan.setPosX(col);
                                newSeatingPlan.setPosY(row);
                                seatingPlanList.add(newSeatingPlan);
                                break;
                            }
                        }
                    }
                    else{
                        //wenn Platz frei ist
                        seatingPlan = new SeatingPlan();
                        seatingPlan.setStudentID(selectedStudent.getID());
                        seatingPlan.setPosX(col);
                        seatingPlan.setPosY(row);
                        seatingPlanList.add(seatingPlan);
                    }

                    for(int i=0; i!=studentList.size(); i++){
                        student = studentList.get(i);
                        if(student.getID() == selectedStudent.getID()){
                            studentList.remove(i);
                            break;
                        }
                    }

                    radiobuttonid = -1;
                }
                //Dialog schließen
                dialog.dismiss();
            }
        });
    }

    private void addStudent(String studentID){
        student = StudentDataSource.getStudent(this, studentID);

        studentList.add(student);
    }

    private SeatingPlan checkIsPlaceReserved(int row, int col){
        SeatingPlan returnSeatingPlan = new SeatingPlan();

        for(int i=0; i != seatingPlanList.size(); i++){
            SeatingPlan hold = seatingPlanList.get(i);
            if(hold.getPosX() == col && hold.getPosY() == row){
                return hold;
            }
        }
        return returnSeatingPlan;
    }

    public boolean checkIfRoomOrderExist(String teacherID){
        Informed informed = informedList.get(spClass.getSelectedItemPosition());
        List<SeatingPlan> SeatingPlanList = SeatingPlanDataSource.getStaticCheckSeatingPlan(this, spRoom.getSelectedItem().toString(), String.valueOf(informed.getUnitID()), teacherID);

        if(SeatingPlanList.size() != 0){
            return true;
        }
        else{
            return false;
        }
    }

    private void openRoomNotExist(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewClass.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_no_exist, null);

        TextView tv = (TextView) mView.findViewById(R.id.textView);
        tv.setText(getString(R.string.roomWithTheRoomnumber) + ": " + roomNumber + " " + getString(R.string.notExist) +"!\n");

        Button btnBreak = (Button) mView.findViewById(R.id.btnBreak);
        btnBreak.setText(getString(R.string.back));

        Button btnNext = (Button) mView.findViewById(R.id.btnNext);
        btnNext.setText(getString(R.string.createRoom));

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
                Intent intent = new Intent(NewClass.this, CreateRoom.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void openUnitNotExist(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewClass.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_no_exist, null);

        TextView tv = (TextView) mView.findViewById(R.id.textView);

        tv.setText(getString(R.string.roomWithTheRoomnumber) + ": " + roomNumber + " " + getString(R.string.notExist) +"!\n");

        Button btnBreak = (Button) mView.findViewById(R.id.btnBreak);
        btnBreak.setText(getString(R.string.back));

        Button btnNext = (Button) mView.findViewById(R.id.btnNext);

        if(admin == 1) {
            btnNext.setText(getString(R.string.adminArea));
            tv.setText(getString(R.string.noUnit) + "\n");
        }
        else{
            btnNext.setText(getString(R.string.okay));
            tv.setText(getString(R.string.noUnit) + "\n" + getString(R.string.contactedAdmin));
        }
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
                if(admin == 1) {
                    Intent intent = new Intent(NewClass.this, admin.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(NewClass.this, Menue.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    //If clicked on ActionBar
    public boolean onOptionsItemSelected(MenuItem item) {
        Informed informed;

        // Handle item selection
        switch (item.getItemId()) {
            //--------------- Break Button ----------------------------------------
            case R.id.action_clear:

                for(int row=0; row!=NUM_ROWS; row++){
                    for(int col=0; col!=NUM_COLS; col++){
                        button = buttons[row][col];
                        button.setText(getString(R.string.table));
                    }
                }

                seatingPlanList.clear();
                informed = informedList.get(spClass.getSelectedItemPosition());
                studentList = StudentDataSource.getStudentListWhereInClass(this, String.valueOf(informed.getClassID()));
                return true;

            //--------------- Save Button ----------------------------------------
            case R.id.action_save:
                if(checkIfRoomOrderExist(teacherID)){
                    informed = informedList.get(spClass.getSelectedItemPosition());
                    SeatingPlanDataSource.deleteSeatingPlan(this, spRoom.getSelectedItem().toString(), String.valueOf(informed.getUnitID()), teacherID);
                    insertSeatingPlan(this);
                }
                else {
                   insertSeatingPlan(this);
                }
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();


                return true;

            //---------------  ----------------------------------------
            case R.id.spinner:
                return true;

            case R.id.spinner2:
                return true;

            case R.id.spinner3:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static void insertSeatingPlan(Context c){
       for(int i=0; i!= seatingPlanList.size(); i++){
           SeatingPlan seatingPlan = seatingPlanList.get(i);
           Informed informed = informedList.get(spClass.getSelectedItemPosition());
           SeatingPlanCommands.InsertSeatingPlan(c, roomNumber, seatingPlan.getStudentID(), informed.getUnitID(), Long.valueOf(teacherID), seatingPlan.getPosX(), seatingPlan.getPosY());
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

