package com.example.pschm.klassenbuch;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pschm.klassenbuch.Adapter.classListAttapter;
import com.example.pschm.klassenbuch.Adapter.roomListAttapter;
import com.example.pschm.klassenbuch.Adapter.stringListAttapter;
import com.example.pschm.klassenbuch.Adapter.teacherListAttapter;
import com.example.pschm.klassenbuch.Adapter.unitListAttapter;
import com.example.pschm.klassenbuch.SqLite.Class;
import com.example.pschm.klassenbuch.SqLite.ClassDataSource;
import com.example.pschm.klassenbuch.SqLite.InClassDataSource;
import com.example.pschm.klassenbuch.SqLite.InformedDataSource;
import com.example.pschm.klassenbuch.SqLite.Room;
import com.example.pschm.klassenbuch.SqLite.RoomDataSource;
import com.example.pschm.klassenbuch.SqLite.Student;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;
import com.example.pschm.klassenbuch.SqLite.Teacher;
import com.example.pschm.klassenbuch.SqLite.TeacherDataSource;
import com.example.pschm.klassenbuch.SqLite.Unit;
import com.example.pschm.klassenbuch.SqLite.UnitDataSource;
import com.example.pschm.klassenbuch.Adapter.onlyStudentListAttapter;
import java.util.ArrayList;
import java.util.List;

public class admin extends AppCompatActivity {
    private static LinearLayout llDetails;
    private static TextView tvTitle;
    public static ListView lvList;
    private static Button btnAdd;

    public static List<Class> ClassArrayAdapterList;
    public static List<Teacher> TeacherArrayAdapterList;
    public static List<Student> StudenArrayAdapterList;
    public static List<Room> RoomArrayAdapterList;
    public static List<Unit> UnitArrayAdapterList;
    public static List<String> inClassArrayAdapterList;
    public static List<String> informedArrayAdapterList;

    public static stringListAttapter informedArrayAdapter;
    public static stringListAttapter inClassArrayAdapter;
    public static unitListAttapter UnitArrayAdapter;
    public static roomListAttapter RoomArrayAdapter;
    public static classListAttapter ClassArrayAdapter;
    public static onlyStudentListAttapter StudenArrayAdapter;
    public static teacherListAttapter TeacherArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Benutzeroberfläche der Hauptactivity anzeigen
        this.setTheme(R.style.MyTextViewStyle);
        setContentView(R.layout.activity_admin);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        lvList = (ListView) findViewById(R.id.ListView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
    }

    /**
     * Dieses Fragment zeigt eine Auswahlliste
     */
    public static class AuswahlFragment extends ListFragment {

        private static final String STR_ZULETZT_SELEKTIERT =
                "zuletztSelektiert";

        boolean zweiSpaltenModus;
        int zuletztSelektiert = 0;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Liste aus einem String-Array befüllen

            setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, getResources().getStringArray(R.array.adminarea)));

            // prüfen, ob das Details-Fragment direkt eingebunden
            // werden kann (dazu muss es im Layout ein Element mit
            // der Id details geben)
            View detailsFrame = getActivity().findViewById(R.id.details);
            zweiSpaltenModus = detailsFrame != null &&
                    detailsFrame.getVisibility() == View.VISIBLE;

            if (savedInstanceState != null) {
                // ggf. zuletztSelektiert wiederherstellen
                zuletztSelektiert =
                        savedInstanceState.getInt(STR_ZULETZT_SELEKTIERT, 0);
            }

            if (zweiSpaltenModus) {
                // Im Zweispalten-Modus invertiert die View das
                // selektierte Element
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                // Details anzeigen
                detailsAnzeigen(zuletztSelektiert);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            // zuletzt selektierten Eintrag merken
            outState.putInt(STR_ZULETZT_SELEKTIERT, zuletztSelektiert);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            detailsAnzeigen(position);
        }

        /**
         * Diese Hilfsmethode zeigt die Details zu dem selektierten
         * Element an; entweder wird ein Fragment "in place" in der
         * aktuellen Activity angezeigt, oder es wird eine neue gestartet
         */
        void detailsAnzeigen(int index) {
            zuletztSelektiert = index;
            if (zweiSpaltenModus) {
                // "in place"-Darstellung
                getListView().setItemChecked(index, true);
                admin.DetailsFragment details = (admin.DetailsFragment)
                        getFragmentManager()
                                .findFragmentById(R.id.details);
                if (details == null || details.getIndex() != index) {
                    // neues Fragment passend zum selektierten
                    // Eintrag erzeugen und anzeigen
                    details = admin.DetailsFragment.newInstance(index);
                    FragmentTransaction ft =
                            getFragmentManager().beginTransaction();
                    ft.replace(R.id.details, details);
                    // einen Übergang darstellen
                    ft.setTransition(
                            FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            } else {
                // neue Activity starten
                Intent intent = new Intent();
                intent.setClass(getActivity(), admin.DetailsActivity.class);
                intent.putExtra(admin.DetailsFragment.INDEX, index);
                startActivity(intent);
            }
        }
    }

    public static class DetailsFragment extends Fragment {

        public static final String INDEX = "index";

        public static admin.DetailsFragment newInstance(int index) {
            admin.DetailsFragment f = new admin.DetailsFragment();
            Bundle args = new Bundle();
            args.putInt(INDEX, index);
            f.setArguments(args);
            return f;
        }

        public int getIndex() {
            return getArguments().getInt(INDEX, 0);
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {

            // View nur erzeugen, wenn das Fragment
            // angezeigt werden wird
            if (container != null) {
                llDetails = new LinearLayout(getActivity());
                btnAdd.setWidth(btnAdd.getHeight());

                switch (getIndex()){
                    //Klassen
                    case 0:
                        tvTitle.setText(getResources().getStringArray(R.array.adminarea)[getIndex()]);
                        ClassArrayAdapterList = ClassDataSource.getStaticAllClassList(getActivity());
                        ClassArrayAdapter = new classListAttapter(getActivity(), R.layout.listview_only_text, ClassArrayAdapterList);
                        //List<RoomOrder> ClassArrayAdapterList = RoomOrderDataSource.getStaticAllRoomList(getActivity(),101);
                        //ArrayAdapter<RoomOrder> ClassArrayAdapter = new ArrayAdapter<> (getActivity(), android.R.layout.simple_list_item_multiple_choice, ClassArrayAdapterList);
                        lvList.setAdapter(ClassArrayAdapter);

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.DialogNewClass(getActivity());
                                ClassArrayAdapter.notifyDataSetChanged();
                            }
                        });
                        ClassArrayAdapter.notifyDataSetChanged();
                        break;

                    // Student
                    case 1:
                        tvTitle.setText(getResources().getStringArray(R.array.adminarea)[getIndex()]);

                        StudenArrayAdapterList = StudentDataSource.getStaticAllStudentList(getActivity());
                        StudenArrayAdapter = new onlyStudentListAttapter(getActivity(), R.layout.listview_only_text, StudenArrayAdapterList);
                        lvList.setAdapter(StudenArrayAdapter);

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.DialogNewStudent(getActivity());

                            }
                        });
                        break;

                    //Räume
                    case 2:
                        tvTitle.setText(getResources().getStringArray(R.array.adminarea)[getIndex()]);
                        RoomArrayAdapterList = RoomDataSource.getStaticAllRoomList(getActivity());
                        RoomArrayAdapter = new roomListAttapter(getActivity(), R.layout.listview_only_text , RoomArrayAdapterList);
                        lvList.setAdapter(RoomArrayAdapter);

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.DialogNewRoom(getActivity());

                            }
                        });
                        break;

                    //Lehrer
                    case 3:
                        tvTitle.setText(getResources().getStringArray(R.array.adminarea)[getIndex()]);
                        TeacherArrayAdapterList = TeacherDataSource.getStaticAllTeacherList(getActivity());
                        TeacherArrayAdapter = new teacherListAttapter (getActivity(), R.layout.listview_only_text, TeacherArrayAdapterList);
                        lvList.setAdapter(TeacherArrayAdapter);

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.DialogNewTeacher(getActivity());

                            }
                        });
                        break;

                    //Fächer
                    case 4:
                        tvTitle.setText(getResources().getStringArray(R.array.adminarea)[getIndex()]);
                        UnitArrayAdapterList = UnitDataSource.getStaticAllUnitList(getActivity());
                        UnitArrayAdapter = new unitListAttapter (getActivity(), R.layout.listview_only_text, UnitArrayAdapterList);
                        lvList.setAdapter(UnitArrayAdapter);

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.DialogNewSubject(getActivity());
                            }
                        });
                        break;

                    //Schüler/ Klasse
                    case 5:
                        tvTitle.setText(getResources().getStringArray(R.array.adminarea)[getIndex()]);
                        inClassArrayAdapterList = new ArrayList<>();
                        String[] strInClass = InClassDataSource.getStaticAllInClass(getActivity());
                        for(int i=0; i!=strInClass.length; i++){
                            inClassArrayAdapterList.add(strInClass[i]);
                        }
                        inClassArrayAdapter = new stringListAttapter(getActivity(), R.layout.listview_only_text, inClassArrayAdapterList);

                        lvList.setAdapter(inClassArrayAdapter);

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.DialogStudentClass(getActivity());
                            }
                        });
                        break;

                    //Lehrer/ Klasse
                    case 6:
                        tvTitle.setText(getResources().getStringArray(R.array.adminarea)[getIndex()]);
                        informedArrayAdapterList = new ArrayList<>();
                        String[] strInformedeList = InformedDataSource.getStaticAllInformed(getActivity());
                        for(int i=0; i!=strInformedeList.length; i++){
                            informedArrayAdapterList.add(strInformedeList[i]);
                        }

                        informedArrayAdapter = new stringListAttapter (getActivity(), R.layout.listview_only_text, informedArrayAdapterList);

                        lvList.setAdapter(informedArrayAdapter);

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.DialogTeacherUnit(getActivity());
                            }
                        });
                        break;

                    default:
                        break;
                }
            }
            return llDetails;
        }
    }

    public static class DetailsActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                finish();
                return;
            }
            if (savedInstanceState == null) {
                admin.DetailsFragment details = new admin.DetailsFragment();
                details.setArguments(getIntent().getExtras());
                getFragmentManager().beginTransaction().
                        add(android.R.id.content, details).commit();
            }
        }
    }

/*
    public void onResume() {
        super.onResume();
        ClassArrayAdapterList.clear();
        ClassArrayAdapterList = ClassDataSource.getStaticAllClassList(this);
        ClassArrayAdapter.notifyDataSetChanged();
        //items = dbHelper.getItems(); // reload the items from database
        //adapter.notifyDataSetChanged();
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
