package sg.edu.rp.c346.id20008460.ndpsongs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnInsert, btnShow ;
    EditText etTitle, etSinger , etYear;
    ArrayList<Song> al;
    RadioGroup rgStar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInsert = findViewById(R.id.buttonInsert);
        btnShow = findViewById(R.id.buttonShow);

        etTitle = findViewById(R.id.etTitle);
        etSinger = findViewById(R.id.etSinger);
        etYear = findViewById(R.id.etYear);

        rgStar = findViewById(R.id.radioGroup);
        al = new ArrayList<Song>();


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String singer = etSinger.getText().toString();
                int year = Integer.parseInt(etYear.getText().toString());

                int starRating = rgStar.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(starRating);
                int stars = Integer.parseInt(radioButton.getText().toString());

                DBHelper dbh = new DBHelper(MainActivity.this);
                long inserted_id = dbh.insertSong(title,singer,year, stars);
                Toast.makeText(MainActivity.this , "Item has been successfully inserted" , Toast.LENGTH_LONG).show();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show = new Intent(MainActivity.this, ShowSongs.class);

                DBHelper dbh = new DBHelper(MainActivity.this);
                al.clear();
                al.addAll(dbh.getAllSong());

                show.putExtra("data", al);
                startActivity(show);
            }
        });


    }

}