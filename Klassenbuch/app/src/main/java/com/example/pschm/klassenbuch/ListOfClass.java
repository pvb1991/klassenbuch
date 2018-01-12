package com.example.pschm.klassenbuch;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.pschm.klassenbuch.Adapter.studentListAttapter;
import com.example.pschm.klassenbuch.SqLite.ClassDataSource;
import com.example.pschm.klassenbuch.SqLite.InClass;
import com.example.pschm.klassenbuch.SqLite.InClassDataSource;
import com.example.pschm.klassenbuch.SqLite.Informed;
import com.example.pschm.klassenbuch.SqLite.InformedDataSource;
import com.example.pschm.klassenbuch.SqLite.Student;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;
import com.example.pschm.klassenbuch.SqLite.UnitDataSource;

import java.util.ArrayList;
import java.util.List;

public class ListOfClass extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static TextView tv;
    private static TableLayout table;
    private static int spalten = 3;
    private static String teacherID;
    private static int textsize = 20;
    private static Button btn_enter_mark;
    private static LinearLayout llLayout;
    private static List<Informed> InformedList;
    private static List<InClass> InClassList;
    private static List<Student> StudentList;
    private static Informed informed;
    private static String[] classUnit;
    private static String[] strStudentList;
    private static ListView lvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Benutzeroberfläche der Hauptactivity anzeigen
        setContentView(R.layout.activity_list_of_class);
        this.setTheme(R.style.MyTextViewStyle);
        sharedPreferences = getSharedPreferences("spFile", 0);
        teacherID = sharedPreferences.getString("teacherID", null);

        tv=(TextView)findViewById(R.id.textView);
        btn_enter_mark = (Button) findViewById(R.id.btn_enter_mark);
        InformedList = new ArrayList<>();
        InformedList = InformedDataSource.getAllStaticInformedFromTeacher(this, teacherID);
        classUnit = new String[InformedList.size()];

        if(InformedList.size() != 0) {
            for (int i = 0; i != InformedList.size(); i++) {
                informed = InformedList.get(i);
                classUnit[i] = ClassDataSource.getClassWithID(this, String.valueOf(informed.getClassID())) + "\n" + UnitDataSource.getUnitWithID(this, String.valueOf(informed.getUnitID()));
            }
        }
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
            setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, classUnit));//new String[]{"eins", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei"}));

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
        public void onListItemClick(ListView l, View v,
                                    int position, long id) {
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
                ListOfClass.DetailsFragment details = (ListOfClass.DetailsFragment)
                        getFragmentManager()
                                .findFragmentById(R.id.details);
                if (details == null || details.getIndex() != index) {
                    // neues Fragment passend zum selektierten
                    // Eintrag erzeugen und anzeigen
                    details = ListOfClass.DetailsFragment.newInstance(index);
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
                intent.setClass(getActivity(), ListOfClass.DetailsActivity.class);
                intent.putExtra(ListOfClass.DetailsFragment.INDEX, index);
                startActivity(intent);
            }
        }
    }

    public static class DetailsFragment extends Fragment {

        public static final String INDEX = "index";

        public static ListOfClass.DetailsFragment newInstance(int index) {
            ListOfClass.DetailsFragment f = new ListOfClass.DetailsFragment();
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


                lvList = new ListView(getActivity());
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if(InformedList.size() != 0) {
                    tv.setText(classUnit[getIndex()]);
                    informed = InformedList.get(getIndex());
                    InClassList = InClassDataSource.getStaticAllInClass(getActivity(), String.valueOf(informed.getClassID()));
                    strStudentList = new String[InClassList.size()];
                    StudentList = new ArrayList<>();
                    for (int i = 0; i != InClassList.size(); i++) {
                        InClass inClass = InClassList.get(i);
                        Student student = StudentDataSource.getStudent(getActivity(), String.valueOf(inClass.getStudentID()));
                        StudentList.add(student);
                        strStudentList[i] = student.getName() + ", " + student.getVorname();
                    }

                    studentListAttapter attapter = new studentListAttapter(getActivity(), R.layout.listview_with_button, StudentList, teacherID, String.valueOf(informed.getUnitID()));
                    lvList.setAdapter(attapter);
                    lvList.setLayoutParams(param2);

                    btn_enter_mark.setText(R.string.enterExam);
                    btn_enter_mark.setBackgroundResource(R.drawable.ic_orange_button);
                    btn_enter_mark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog.DialogInsertExamMark(getActivity(), classUnit[getIndex()], StudentList, teacherID, String.valueOf(informed.getUnitID()));
                        }
                    });
                }
                else{
                    btn_enter_mark.setVisibility(View.INVISIBLE);
                    tv.setText(getString(R.string.noClass) + "\n" + getString(R.string.contactedAdmin));
                    strStudentList = new String[0];
                }
            }
            return lvList;
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
                ListOfClass.DetailsFragment details = new ListOfClass.DetailsFragment();
                details.setArguments(getIntent().getExtras());
                getFragmentManager().beginTransaction().
                        add(android.R.id.content, details).commit();
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