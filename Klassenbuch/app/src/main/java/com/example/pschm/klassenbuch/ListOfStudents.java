package com.example.pschm.klassenbuch;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pschm.klassenbuch.SqLite.AttandanceDataSource;
import com.example.pschm.klassenbuch.SqLite.InClass;
import com.example.pschm.klassenbuch.SqLite.InClassDataSource;
import com.example.pschm.klassenbuch.SqLite.Informed;
import com.example.pschm.klassenbuch.SqLite.InformedDataSource;
import com.example.pschm.klassenbuch.SqLite.KlassenbuchDbHelper;
import com.example.pschm.klassenbuch.SqLite.Mark;
import com.example.pschm.klassenbuch.SqLite.MarkDataSource;
import com.example.pschm.klassenbuch.SqLite.Student;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;
import com.example.pschm.klassenbuch.SqLite.Unit;
import com.example.pschm.klassenbuch.SqLite.UnitDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class ListOfStudents extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static TextView tv;
    private static TextView tvCooporationTitle;
    private static TextView tvCooporation;
    private static TextView tvHomeworkTitle;
    private static TextView tvHomework;
    private static TextView tvExamTitle;
    private static TextView tvExam;
    private static TextView tvAttendanceTitle;
    private static TextView tvAttendance;

    private static LinearLayout llTable;

    private static int textsize = 20;
    private static String teacherID;
    private static List<Student> StudentList;
    private static List<Unit> UnitList;
    private static String[] StudentNameList;
    private static Student student;
    private static Mark mark;
    private static int check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Benutzeroberfläche der Hauptactivity anzeigen
        setContentView(R.layout.activity_list_of_students);
        this.setTheme(R.style.MyTextViewStyle);
        sharedPreferences = getSharedPreferences("spFile", 0);
        teacherID = sharedPreferences.getString("teacherID", null);

        StudentList = StudentDataSource.getStudentListByTeacherID(this, teacherID);
        StudentNameList = new  String[StudentList.size()];
        for(int i=0; i!= StudentList.size(); i++){
            Student student = StudentList.get(i);
            StudentNameList[i] = student.getName() + ", " + student.getVorname();
        }

        tv=(TextView)findViewById(R.id.textView);

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

            setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, StudentNameList));//StudentDataSource.getAllStudentsNames(getActivity())));//new String[]{"eins", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei", "zwei", "drei"}));

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
                DetailsFragment details = (DetailsFragment)
                        getFragmentManager()
                                .findFragmentById(R.id.details);
                if (details == null || details.getIndex() != index) {
                    // neues Fragment passend zum selektierten
                    // Eintrag erzeugen und anzeigen
                    details = DetailsFragment.newInstance(index);
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
                intent.setClass(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsFragment.INDEX, index);
                startActivity(intent);
            }
        }
    }

    public static class DetailsFragment extends Fragment {

        public static final String INDEX = "index";

        public static DetailsFragment newInstance(int index) {
            DetailsFragment f = new DetailsFragment();
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
            ScrollView scroller = null;
            // View nur erzeugen, wenn das Fragment
            // angezeigt werden wird
            if (container != null) {
                tvCooporationTitle = new TextView(getActivity());
                tvCooporation = new TextView(getActivity());
                tvHomeworkTitle = new TextView(getActivity());
                tvHomework = new TextView(getActivity());
                tvExamTitle = new TextView(getActivity());
                tvExam = new TextView(getActivity());
                tvAttendanceTitle = new TextView(getActivity());
                tvAttendance = new TextView(getActivity());
                check = 0;

                if(StudentList.size() != 0) {
                    student = StudentList.get(getIndex());
                    tv.setText(
                            getString(R.string.surname) + ": " + student.getName() + "\n"
                                    + getString(R.string.name) + ": " + student.getVorname() + "\n"
                                    + getString(R.string.street) + ": " + student.getStreet() + " " + student.getHousenumber() + "\n"
                                    + getString(R.string.zipCode) + ": " + student.getZipcode() + "\n"
                                    + getString(R.string.locus) + ": " + student.getLocus() + "\n"
                    );

                    llTable = new LinearLayout(getActivity());
                    llTable.setOrientation(LinearLayout.VERTICAL);

                    UnitList = UnitDataSource.SelectUnitFromStudentByTeacher(getActivity(), String.valueOf(student.getID()), teacherID);
                    String[] strUnitList = new String[UnitList.size()];
                    for (int i = 0; i != UnitList.size(); i++) {
                        Unit unit = UnitList.get(i);
                        strUnitList[i] = unit.getTitle();
                    }
                    ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, strUnitList);

                    final Spinner spUnit = new Spinner(getActivity());
                    spUnit.setBackgroundColor(getResources().getColor(R.color.logoOrange100));
                    spUnit.setPopupBackgroundResource(R.color.logoOrange100);
                    spUnit.setAdapter(aa);
                    llTable.addView(spUnit);

                    if (UnitList.size() == 0) {
                        TextView tvNoUnit = new TextView(getActivity());
                        tvNoUnit.setText(getString(R.string.studentInNoUnit));
                        tvNoUnit.setTextColor(Color.parseColor("#000000"));
                        tvNoUnit.setTextSize(textsize);
                        llTable.addView(tvNoUnit);

                    } else {
                        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Unit unit = UnitList.get(spUnit.getSelectedItemPosition());

                                //Noten mündliche Mitarbeit
                                //_________________________
                                tvCooporationTitle.setText("\n" + getString(R.string.cooporation) + ":");
                                tvCooporationTitle.setPaintFlags(tvCooporationTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                tvCooporationTitle.setTypeface(null, Typeface.BOLD);

                                int countGood = MarkDataSource.CountMarkType(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()), teacherID, KlassenbuchDbHelper.MarkTypeOral, String.valueOf(KlassenbuchDbHelper.MarkSuper));
                                int countOkay = MarkDataSource.CountMarkType(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()), teacherID, KlassenbuchDbHelper.MarkTypeOral, String.valueOf(KlassenbuchDbHelper.MarkOkay));
                                int countBad = MarkDataSource.CountMarkType(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()), teacherID, KlassenbuchDbHelper.MarkTypeOral, String.valueOf(KlassenbuchDbHelper.MarkBad));
                                int numberOfReviews = countGood + countOkay + countBad;

                                if (countGood != 0 || countOkay != 0 || countBad != 0) {
                                    // Durchschnitt berechnent von mündliche Mitarbeit
                                    float mean = 0;
                                    if (countGood != 0 && countOkay != 0) {
                                        mean = (((float)countGood * (float)100) + ((float)countOkay * (float)50)) / (float)numberOfReviews;
                                    } else if (countGood != 0 && countOkay == 0) {
                                        mean = ((float)countGood * (float)100) / (float)numberOfReviews;
                                    } else if (countGood == 0 && countOkay != 0) {
                                        mean = ((float)countOkay * 50) / (float)numberOfReviews;
                                    } else {
                                        mean = 0;
                                    }

                                    tvCooporation.setText(
                                            getString(R.string.numberOfReviews) + ": " + numberOfReviews + "\n" +
                                                    getString(R.string.good) + " (100)     : " + countGood + "\n" +
                                                    getString(R.string.okay) + " (50)     : " + countOkay + "\n" +
                                                    getString(R.string.bad) + " (0) : " + countBad + "\n" +
                                                    getString(R.string.mean) + ": " + mean + "\n\n"
                                    );
                                } else {
                                    tvCooporation.setText(getString(R.string.noEntries) + "\n");
                                }

                                //Hausaufgaben
                                //______________________________________________________
                                tvHomeworkTitle.setText(getString(R.string.homework) + ":");
                                tvHomeworkTitle.setPaintFlags(tvHomeworkTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                tvHomeworkTitle.setTypeface(null, Typeface.BOLD);

                                int countPresent = MarkDataSource.CountMarkType(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()), teacherID, KlassenbuchDbHelper.MarkTypeHomework, String.valueOf(KlassenbuchDbHelper.MarkHomeworkYES));
                                int countForget = MarkDataSource.CountMarkType(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()), teacherID, KlassenbuchDbHelper.MarkTypeHomework, String.valueOf(KlassenbuchDbHelper.MarkHomeworkNO));
                                float percentage = 0;

                                if (countPresent != 0 || countForget != 0) {
                                    //Prozentualer Anteil der Hausaufgaben berechenen
                                    if (countPresent != 0) {
                                        percentage = ((float)countPresent * (float)100) / ((float)countForget + (float)countPresent);
                                    } else {
                                        percentage = 0;
                                    }

                                    tvHomework.setText(
                                            getString(R.string.present) + " : " + countPresent + "\n" +
                                                    getString(R.string.forget) + " : " + countForget + "\n" +
                                                    getString(R.string.percentage) + " : " + percentage + "%\n\n"
                                    );
                                } else {
                                    tvHomework.setText(getString(R.string.noEntries) + "\n");
                                }

                                //Klausuren
                                //__________________________________________________
                                tvExamTitle.setText(getString(R.string.exams));
                                tvExamTitle.setPaintFlags(tvExamTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                tvExamTitle.setTypeface(null, Typeface.BOLD);

                                List<Mark> Marklist = MarkDataSource.getExams(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()), teacherID, KlassenbuchDbHelper.MarkTypeExam);
                                int sumMark = 0;
                                if (Marklist.size() != 0) {
                                    for (int j = 0; j != Marklist.size(); j++) {
                                        mark = Marklist.get(j);
                                        sumMark = sumMark + mark.getMark();
                                        String text = getString(R.string.exammark) + ": " + mark.getMark() + "\n";
                                        tvExam.setText(tvExam.getText() + text);
                                    }
                                    float examMean = sumMark / Marklist.size();
                                    tvExam.setText(tvExam.getText() + "\n" + getString(R.string.mean) + ": " + examMean + "\n\n");
                                } else {
                                    tvExam.setText(getString(R.string.noEntries) + "\n");
                                }

                                // Anwesendheit
                                //_______________________
                                tvAttendanceTitle.setText(getString(R.string.attendance) + ":");
                                tvAttendanceTitle.setPaintFlags(tvAttendanceTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                tvAttendanceTitle.setTypeface(null, Typeface.BOLD);

                                int countDelays = AttandanceDataSource.CountDelay(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()));
                                int sumDelays = AttandanceDataSource.SumDelays(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()));
                                //CountAttendance(...) sind die Anwesendheitsstunden wo der Schüler zuspät gekommen ist noch nicht mitberechent
                                //deswegen muss countDelays mit CountAttendance(...) addiert werden
                                int countAttendancePresent = countDelays + AttandanceDataSource.CountAttendance(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()), String.valueOf(KlassenbuchDbHelper.AttendancePresent));
                                int countAttendanceAbsent = AttandanceDataSource.CountAttendance(getActivity(), String.valueOf(student.getID()), String.valueOf(unit.getID()), String.valueOf(KlassenbuchDbHelper.AttendanceAbsent));

                                if (countDelays != 0 || countAttendanceAbsent != 0 || countAttendancePresent != 0) {
                                    float meanAttendance = 0;
                                    if (countAttendancePresent != 0 && countAttendanceAbsent != 0) {
                                        meanAttendance = ((float) countAttendancePresent/((float)countAttendanceAbsent + (float)countAttendancePresent)) * (float) 100;
                                    } else if (countAttendancePresent != 0 && countAttendanceAbsent == 0) {
                                        meanAttendance = 100;
                                    } else {
                                        meanAttendance = 0;
                                    }

                                    tvAttendance.setText(
                                            getString(R.string.presentdays) + " : " + countAttendancePresent + "\n" +
                                                    getString(R.string.absentdays) + " : " + countAttendanceAbsent + "\n" +
                                                    getString(R.string.present2) + " in % : " + meanAttendance + "\n" +
                                                    getString(R.string.delays) + " : " + sumDelays + "\n"
                                    );
                                } else {
                                    tvAttendance.setText(getString(R.string.noEntries) + "\n");
                                }

                                if (check == 0) {
                                    llTable.addView(tvCooporationTitle);
                                    llTable.addView(tvCooporation);
                                    llTable.addView(tvHomeworkTitle);
                                    llTable.addView(tvHomework);
                                    llTable.addView(tvExamTitle);
                                    llTable.addView(tvExam);
                                    llTable.addView(tvAttendanceTitle);
                                    llTable.addView(tvAttendance);
                                    check = 1;
                                }
                            }

                            public void onNothingSelected(AdapterView<?> adapterView) {
                                return;
                            }
                        });
                    }

                }
                else{

                    tv.setText(getString(R.string.noStudents) + "\n" + getString(R.string.contactedAdmin));
                }
            }
            return llTable;
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
                DetailsFragment details = new DetailsFragment();
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

