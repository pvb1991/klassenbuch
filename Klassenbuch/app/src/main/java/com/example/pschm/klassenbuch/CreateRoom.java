package com.example.pschm.klassenbuch;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import com.example.pschm.klassenbuch.SqLite.Room;
import com.example.pschm.klassenbuch.SqLite.RoomDataSource;
import com.example.pschm.klassenbuch.SqLite.RoomOrder;
import com.example.pschm.klassenbuch.SqLite.RoomOrderCommands;
import com.example.pschm.klassenbuch.SqLite.RoomOrderDataSource;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;
import com.example.pschm.klassenbuch.SqLite.Unit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateRoom extends AppCompatActivity {


    private static final int NUM_ROWS = 9;
    private static final int NUM_COLS = 7;
    private boolean[][] TablePos = new boolean[NUM_ROWS][NUM_COLS];
    private int roomNumber;
    private static List<RoomOrder> roomOrderList;
    private static Spinner spinnerActionBarOne = null;
    private static Spinner spinnerActionBarTwo = null;
    private static Button reset_btn = null;
    Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];
    private static Button button = null;



    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        populateButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_createroom, menu);

        MenuItem itemBtnSave = menu.findItem(R.id.action_save);
        MenuItem itemBtnClear = menu.findItem(R.id.action_clear);

        //Spinner floor
        MenuItem itemSpinnerOne = menu.findItem(R.id.spinner);
        spinnerActionBarOne = (Spinner) MenuItemCompat.getActionView(itemSpinnerOne);
        spinnerActionBarOne.setAdapter(setSpinnerActionBarOne());

        //Spinner Roomnumber
        MenuItem itemSpinnerTwo= menu.findItem(R.id.spinner2);
        spinnerActionBarTwo = (Spinner) MenuItemCompat.getActionView(itemSpinnerTwo);
        spinnerActionBarTwo.setAdapter(setSpinnerActionBarTwo("1"));

        spinnerActionBarOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerActionBarTwo.setAdapter(setSpinnerActionBarTwo(spinnerActionBarOne.getSelectedItem().toString()));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        spinnerActionBarTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roomNumber =  Integer.valueOf(spinnerActionBarTwo.getSelectedItem().toString());

                roomOrderList = new ArrayList<>();
                roomOrderList = getRoomOrderList();

                for(int row=0; row!=NUM_ROWS; row++){
                    for(int col=0; col!=NUM_COLS; col++){
                        button =buttons[row][col];
                        button.setAlpha((float) 0.25);
                    }
                }

                if(checkIfRoomDesignExist(roomNumber)){
                    for(int j=0; j!=roomOrderList.size(); j++){
                        RoomOrder roomOrder = roomOrderList.get(j);
                        Log.d(CreateRoom.class.getSimpleName(), roomOrder.toString());
                        button = buttons[roomOrder.getPosY()][roomOrder.getPosX()];
                        if(roomOrder.getBocked() == 1){
                            button.setAlpha((float) 1.0);
                        }
                        else{
                            button.setAlpha((float) 0.25);
                        }
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        if(spinnerActionBarTwo.getSelectedItemPosition() == -1 && spinnerActionBarOne.getSelectedItemPosition() == -1){
            itemBtnSave.setVisible(false);
            itemBtnClear.setVisible(false);
            itemSpinnerOne.setVisible(false);
            itemSpinnerTwo.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private List<RoomOrder> getRoomOrderList(){
        List<RoomOrder> r = new ArrayList<>();
        r= RoomOrderDataSource.getStaticAllRoomList(this, roomNumber);

        return r;
    }

    private ArrayAdapter setSpinnerActionBarOne(){
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, RoomDataSource.getStaticAllFloors(this));

        return aa;
    }

    private ArrayAdapter setSpinnerActionBarTwo(String floor){
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, RoomDataSource.getStaticAllRoomsInFloor(this, floor));

        return aa;
    }

    private void populateButtons() {
        TableLayout table = (TableLayout) findViewById(R.id.tableForButtons);
        //Arrays.fill(TablePos, Boolean.TRUE);
        int tableNumber = 0;

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
                tableNumber++;
                button.setText(getString(R.string.table)+ " " + tableNumber);
                button.setAlpha((float) 0.25);
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
        Button button = buttons[row][col];

        if (button.getAlpha() == (float) 1.0) {
            button.setAlpha((float) 0.25);
        }
        else {
            button.setAlpha((float) 1.0);
        }
    }

    //If clicked on ActionBar
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            //--------------- Break Button ----------------------------------------
            case R.id.action_clear:
                // Reset Button
                for(int i_row = 0; i_row != NUM_ROWS; i_row++ ){
                    for(int i_col = 0; i_col != NUM_COLS; i_col++){
                        reset_btn = buttons[i_row][i_col];
                        reset_btn.setAlpha((float) 0.25);
                    }
                }

                // Reset Spinner
                spinnerActionBarOne.setSelection(0);
                spinnerActionBarTwo.setSelection(0);
                return true;

            //--------------- Save Button ----------------------------------------
            case R.id.action_save:
                // check if Room in RoomOrder
                // if Room in RoomOrder then Update the List
                if(checkIfRoomDesignExist(roomNumber)){
                    RoomOrderDataSource.deleteRoomOrderInRoom(this, roomNumber);
                    saveRoomDesign(roomNumber);
                }
                else{
                    saveRoomDesign(roomNumber);
                }
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();

                return true;

            //---------------  ----------------------------------------
            case R.id.spinner:
                return true;

            case R.id.spinner2:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void saveRoomDesign(int roomNumber){
        int srow;
        int scol;
        for(srow = 0; srow != NUM_ROWS; srow++ ){
            for(scol = 0; scol != NUM_COLS; scol++){
                Button btn = buttons[srow][scol];
                Log.d(RoomOrderCommands.class.getSimpleName(), "_______________________________________");

                if(btn.getAlpha()==((float) 0.25)){
                    TablePos[srow][scol] = false;
                    RoomOrderCommands.InsertRoomOrder(this, roomNumber, scol, srow, 0);
                }
                else {
                    TablePos[srow][scol] = true;
                    RoomOrderCommands.InsertRoomOrder(this, roomNumber, scol, srow, 1);
                }
                Log.d(CreateRoom.class.getSimpleName(), "" + TablePos[srow][scol]);
            }
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

