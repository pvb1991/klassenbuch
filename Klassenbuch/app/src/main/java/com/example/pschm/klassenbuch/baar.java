package com.example.pschm.klassenbuch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pschm.klassenbuch.SqLite.ClassDataSource;
import com.example.pschm.klassenbuch.SqLite.StudentDataSource;
import com.example.pschm.klassenbuch.SqLite.Teacher;
import com.example.pschm.klassenbuch.SqLite.TeacherDataSource;

public class baar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baar);

        final TextView text = (TextView) findViewById(R.id.text);

        String strText = "Ich habe für Sie einen Button erstellt, wenn Sie dort drauf drücken werden für Sie Daten(2 Lehrer, 2, Klassen und 10 Schüler) in die Datenbank eingefügt \n"+
                "Wenn Sie komplett alles selber eingeben wollen, beim ersten Start der App wird die Datenbank direkt angelegt mit einem \n" +
                "standard Admin!\n\n" +
                "E-Mail: admin \n" +
                "Passwort: admin \n\n" +
                "Viel Spaß beim ausprobieren.";

        text.setText(strText);

        Button btn = (Button) findViewById(R.id.db);
        btn.setText("Daten einfügen");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createbaar(v.getContext());
                createStudent(v.getContext());
                createClass(v.getContext());
                text.setText("Es wurden für Sie zwei Accounts angelegt :\n"+
                        "Normaler Lehrer Account:\n" +
                        "E-Mail: thomas.baar@htw-berlin.de \n" +
                        "Passwort: 123456 \n\n" +
                        "Admin Account:\n" +
                        "E-Mail: baar@klassenbuch.de \n" +
                        "Passwort: 123456 \n\n" +
                        "Und es wurden 2 Klassen und 10 Schüler angelegt");
            }
        });
    }

    public static void createbaar(Context c){
        TeacherDataSource tds = new TeacherDataSource(c);
        tds.open();
        tds.createTeacher("Thomas", "Baar", "Wilhelminenhofstraße", "75", 12459, "Berlin","thomas.baar@htw-berlin.de", "123456", 0);
        tds.createTeacher("Thomas2", "Baar2", "Wilhelminenhofstraße", "75", 12459, "Berlin","baar@klassenbuch.de", "123456", 1);
        tds.close();
    }

    public static void createStudent(Context c){
        StudentDataSource tds = new StudentDataSource(c);
        tds.open();
        tds.createStudent("Donald", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Micky", "Mouse", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Mini", "Mouse", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Gustav", "Gans", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Dagobert", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Tick", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Trick", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Track", "Duck", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Gustav", "Gans", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.createStudent("Franz", "Gans", "Wilhelminenhofstraße", "75", 12459, "Berlin");
        tds.close();
    }

    public static void createClass(Context c){
        ClassDataSource tds = new ClassDataSource(c);
        tds.open();
        tds.createClass("10A");
        tds.createClass("10B");
        tds.close();
    }
}
