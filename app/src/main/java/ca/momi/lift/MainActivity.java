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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import java.util.Calendar;
import java.util.Date;

import static java.lang.Long.parseLong;

public class MainActivity extends AppCompatActivity {

    public String program = "madcow";

    private void setRadioGroup(){

        RadioGroup routinesRadGroup = (RadioGroup) findViewById(R.id.routines);
        AssignedExcers assExcersize = new AssignedExcers(this.program);

        for(int i = 0; i < assExcersize.routineDescriber.length; i++){
            RadioButton button = new RadioButton(this);
            button.setText(assExcersize.routineDescriber[i]);
            routinesRadGroup.addView(button);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do Nothing.
                // TODO: Always make the addition today
            }
        });


        final CalendarView wCal = (CalendarView) findViewById(R.id.cal);
        Button bEditDate = (Button) findViewById(R.id.edDate);

        long date = wCal.getDate();

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

                RadioGroup routinesRadGroup = (RadioGroup) findViewById(R.id.routines);
                int idx = (routinesRadGroup.getCheckedRadioButtonId());

                AssignedExcers assExcersize = new AssignedExcers(program);
                String[] routine = assExcersize.getExcersizes(assExcersize.routineDescriber[idx-1]);

                workoutIntent.putExtra("RoutineName", assExcersize.routineDescriber[idx-1]);
                workoutIntent.putExtra("Excersizes", routine);
                workoutIntent.putExtra("program", program);

                if(selectedDay[0] == 0 && selectedMonth[0] == 0 & selectedYear[0] == 0){
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

        setRadioGroup();


        ExternalStore.getLastWorkoutProperties();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
