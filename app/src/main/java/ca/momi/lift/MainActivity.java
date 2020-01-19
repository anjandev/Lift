//  <Lift: A free software weight lifting tracking app>
//  Copyright (C) <2019> <Anjandev Momi>
//  email: anjan@momi.ca
//
//  Lift is free software: you can redistribute it and/or modify it under the terms of the
//  GNU General Public License as published by the Free Software Foundation, either version 3 of
//  the License, or (at your option) any later version.
//
//  Lift is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  See the GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License along with this program.
//  If not, see <https://www.gnu.org/licenses/>.


package ca.momi.lift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final static String PREFERENCE_FILE_KEY = "myAppPreference";

    public static String program;
    public static String uom;

    public static double smallestWeightLb;
    public static double smallestWeightKg;

    public static Boolean DEBUGMODE = false;

    protected void onResume() {
        super.onResume();
        setNextWorkout();
    }

    private void getSavedPref(Context context){
          SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
          program = sharedPref.getString("program",AssignedExcers.FIVE_x_5);
          uom = sharedPref.getString("uom","lb");
          if (uom.equals("lb")) {
              smallestWeightLb = 2*Double.valueOf(sharedPref.getString("smallestPlate", "2.5"));
          } else {
              smallestWeightKg = 2*Double.valueOf(sharedPref.getString("smallestPlate", "1.25"));
          }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSavedPref(getBaseContext());

        final CalendarView wCal = findViewById(R.id.cal);
        Button bEditDate = findViewById(R.id.edDate);

        final long[] selectedYear = {0};
        final long[] selectedMonth = {0};
        final long[] selectedDay = {0};


        wCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDay[0] = dayOfMonth;
                selectedMonth[0] = month+1;
                selectedYear[0] = year;
            }
        });

        bEditDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent workoutIntent = new Intent(MainActivity.this, Workout.class);

                RadioGroup routinesRadGroup = findViewById(R.id.routines);
                int idx = (routinesRadGroup.getCheckedRadioButtonId());

                AssignedExcers assExcersize = new AssignedExcers();
                RadioButton selectedOption = findViewById(idx);
                List<String> excersizes = assExcersize.getExcersizes((String) selectedOption.getText());

                String[] excersizesArr = new String[excersizes.size()];
                excersizesArr = excersizes.toArray(excersizesArr);


                workoutIntent.putExtra("RoutineName", (String) selectedOption.getText());
                workoutIntent.putExtra("Excersizes", excersizesArr);
                workoutIntent.putExtra("program", program);

                if(selectedDay[0] == 0 && selectedMonth[0] == 0 & selectedYear[0] == 0 || !DEBUGMODE){
                    final long date = wCal.getDate();
                    final String dateString = DateFormat.format("yyyy-MM-dd", new Date(date)).toString();
                    workoutIntent.putExtra("year", Long.parseLong((String) dateString.subSequence(0, 4)));
                    workoutIntent.putExtra("month", Long.parseLong((String) dateString.subSequence(5, 7)));
                    workoutIntent.putExtra("day", Long.parseLong((String) dateString.subSequence(8, 10)));
                }
                else{
                    workoutIntent.putExtra("day", selectedDay[0]);
                    workoutIntent.putExtra("month", selectedMonth[0]);
                    workoutIntent.putExtra("year", selectedYear[0]);
                }

                startActivity(workoutIntent);
            }
        });

        RadioGroup routinesRadGroup = findViewById(R.id.routines);
        radioUtils.setRadioGroup(routinesRadGroup, (new AssignedExcers()).routineDescriber, this);
        setNextWorkout();

    }

    private void setNextWorkout () {
        // rewrite this so it's simplier if not onpause

        LastWorkout latestwork = ExternalStore.getLastidxProp(0);
        RadioGroup routinesRadGroup = findViewById(R.id.routines);

        if(!DEBUGMODE) {
            radioUtils.setRadioButtonsNotClickable(routinesRadGroup);
        }

        if (latestwork == null) {
            // no last workout
            RadioButton nextButton = (RadioButton) routinesRadGroup.getChildAt(0);
            nextButton.setChecked(true);
            return;
        }
        AssignedExcers assExcersize = new AssignedExcers();

        int nextIdx;
        if (latestwork.onPause) {
            nextIdx = assExcersize.routineDescriber.indexOf(latestwork.routineName);
        } else {
            int curIdx = -1;


            for (int i = 0; i < assExcersize.routineDescriber.size(); i++) {
                if (assExcersize.routineDescriber.get(i).equals(latestwork.routineName)) {
                    curIdx = i;
                }
            }

            nextIdx = assExcersize.nextRoutineIdx(curIdx);
        }


        RadioButton nextButton = (RadioButton) routinesRadGroup.getChildAt(nextIdx);
        nextButton.setChecked(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(MainActivity.this, settings.class);
            startActivity(settingIntent);
            return true;
        } else if (id == R.id.action_about) {
            Intent aboutIntent = new Intent(MainActivity.this, About.class);
            startActivity(aboutIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
