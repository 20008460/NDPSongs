package sg.edu.rp.c346.id20008460.ndpsongs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.CursorAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class ShowSongs extends AppCompatActivity {


    Button btnFiveStars;

    ListView lvSong;
    ArrayAdapter<Song> aa ;
    ArrayAdapter<Integer> aaYears; // store all the years
    ArrayList<Song> alSong  , alDuplicate , alContainFiveStar;
    ArrayList<Integer> alYear ;
    Spinner spnYears;
    HashSet<Integer> hs;

    //
    private String filteredYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_songs);

        btnFiveStars = findViewById(R.id.button5stars);
        lvSong = findViewById(R.id.lvSongs);
        spnYears = findViewById(R.id.spinner);

        DBHelper dbh = new DBHelper(ShowSongs.this);

        alSong = new ArrayList<Song>();
        alDuplicate = new ArrayList<Song>();
        alYear = new ArrayList<Integer>();

        alSong.addAll(dbh.getAllSong());

        for (int i = 0; i < alSong.size(); i++) {
            alYear.add(alSong.get(i).getYear());
        }

        Collections.sort(alYear);


        hs = new HashSet();
        hs.addAll(alYear);

        alYear.clear();
        alYear.addAll(hs);

        aa = new ArrayAdapter<Song>(this,
                android.R.layout.simple_list_item_1, alSong);

        aaYears = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, alYear);
        spnYears.setAdapter(aaYears);

        lvSong.setAdapter(aa);



        lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long identity) {
                Song data = alSong.get(position);
                Intent i = new Intent(ShowSongs.this,
                        ModifySongs.class);

                int starValue = alSong.get(position).getStars();
                i.putExtra("data", data);
                i.putExtra("star", starValue);
                startActivity(i);
            }
        });

        btnFiveStars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(ShowSongs.this);
                alSong.clear();
                alDuplicate.clear();
                int keyword = 5;

                alSong.addAll(dbh.getAllSong(keyword));

                for (int i = 0 ; i < alSong.size() ; i ++) {
                    if (alSong.get(i).toString().contains(filteredYear) == true) {
                        alDuplicate.add(alSong.get(i));
                    }
                }

                alSong.clear();
                for (int h = 0 ; h < alDuplicate.size() ; h ++) {
                    alSong.add(alDuplicate.get(h));
                }

                alDuplicate.clear();
                aa.notifyDataSetChanged();
            }
        });

        spnYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
                String text = mySpinner.getSelectedItem().toString(); // get the year of the spinner
//                Toast.makeText(getApplicationContext(), position+1 + " : " + text , Toast.LENGTH_LONG).show();

                int yearCount = spnYears.getAdapter().getCount();
                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor prefeditor = prefs.edit();

                prefeditor.putInt("spnIndex" , position);

                int year = Integer.parseInt(text); // change it into int (year)
                DBHelper dbh = new DBHelper(ShowSongs.this);
                int keyword = year;

                for (int i = 0 ; i < yearCount ; i ++) {
                    if (position == i) {
                        alSong.clear();
                        alSong.addAll(dbh.filterYear(keyword));
                        filteredYear = Integer.toString(keyword);

                    }
                }

                prefeditor.putInt("year" , keyword );
                aa.notifyDataSetChanged();
                prefeditor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        int x = prefs.getInt("spnIndex" , 0);
        int year = prefs.getInt("year" , 0);
        String strYear = Integer.toString(year);

        spnYears.setSelection(x); // let it remain at the spinner selection

        alSong.clear();
        alDuplicate.clear();

        DBHelper dbh = new DBHelper(ShowSongs.this);
        alSong.addAll(dbh.getAllSong());

        alYear.clear();


        for (int i = 0 ; i < alSong.size() ; i ++) {
            alYear.add(alSong.get(i).getYear());
        }

        for (int t = 0 ; t < alSong.size() ; t ++) {
            if (alSong.get(t).toString().contains(strYear) == true) {
                alDuplicate.add(alSong.get(t));
            }
        }

        alSong.clear();
        for (int h = 0 ; h < alDuplicate.size() ; h ++) {
            alSong.add(alDuplicate.get(h));
        }
        aa.notifyDataSetChanged();

        hs.clear();
        hs.addAll(alYear);

        alYear.clear();
        alYear.addAll(hs);

        Collections.sort(alYear);

        aaYears.notifyDataSetChanged();


    }



}