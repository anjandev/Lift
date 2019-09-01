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
import android.content.SharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final RadioGroup programGroup = findViewById(R.id.programs);
        radioUtils.setRadioGroup(programGroup, AssignedExcers.routNames(), this);

        Button setProgram = findViewById(R.id.setProgram);
        final RadioButton selectedRadio = findViewById(programGroup.getCheckedRadioButtonId());


        setProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( programGroup.getCheckedRadioButtonId() == -1){
                    Snackbar.make(view, "Please select a workout routine", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                if (selectWorkoutNeedingStartWeight(selectedRadio) & anyWeightEnterEmpty()) {
                    Snackbar.make(view, "Please enter a starting weight for Squat, Overhead press, " +
                            " bench, and Deadlift", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                SharedPreferences sharedPref = view.getContext().getSharedPreferences(
                                               MainActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("program", selectedRadio.getText().toString());

                if(selectWorkoutNeedingStartWeight(selectedRadio)){
                    editor.putString("Squat1RM", ((TextView) findViewById(R.id.begSquat)).getText().toString());
                    editor.putString("Deadlift1RM", ((TextView) findViewById(R.id.begDeadLift)).getText().toString());
                    editor.putString("Overhead Press1RM", ((TextView) findViewById(R.id.begOverhead)).getText().toString());
                    editor.putString("Bench Press1RM", ((TextView) findViewById(R.id.begBench)).getText().toString());
                }

                editor.commit();
                System.exit(0);
            }
        });


        programGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedButton = findViewById(radioGroup.getCheckedRadioButtonId());
                userEnterWeightVisibleOrInvis(selectedButton);
            }

        });

        setWeightVisible(false);

    }

    private boolean anyWeightEnterEmpty(){
        return (textEmpty((TextView) findViewById(R.id.begSquat))
                | textEmpty((TextView) findViewById(R.id.begDeadLift))
                | textEmpty((TextView) findViewById(R.id.begOverhead))
                | textEmpty((TextView) findViewById(R.id.begBench)));
    }

    private boolean selectWorkoutNeedingStartWeight(RadioButton selectedRadio) {
        if (selectedRadio == null){
            // no radio button selected
            return false;
        }

        return selectedRadio.getText().equals(AssignedExcers.FIVE_31_BBB)
               || selectedRadio.getText().equals(AssignedExcers.PHRAK_GSPL);
    }


    private boolean textEmpty (TextView textInput) {
        return textInput.getText().toString().equals("");
    }

    private void setWeightVisible(boolean setVis){
        EditText squat = findViewById(R.id.begSquat);
        EditText deadlift = findViewById(R.id.begDeadLift);
        EditText overhead = findViewById(R.id.begOverhead);
        EditText bench = findViewById(R.id.begBench);

        if (setVis) {
            squat.setVisibility(View.VISIBLE);
            deadlift.setVisibility(View.VISIBLE);
            overhead.setVisibility(View.VISIBLE);
            bench.setVisibility(View.VISIBLE);
            return;
        }

        squat.setVisibility(View.INVISIBLE);
        deadlift.setVisibility(View.INVISIBLE);
        bench.setVisibility(View.INVISIBLE);
        overhead.setVisibility(View.INVISIBLE);

    }

    private void userEnterWeightVisibleOrInvis(RadioButton selectedRadio){
        EditText squat = findViewById(R.id.begSquat);
        EditText deadlift = findViewById(R.id.begDeadLift);
        EditText overhead = findViewById(R.id.begOverhead);
        EditText bench = findViewById(R.id.begBench);

        setWeightVisible(selectWorkoutNeedingStartWeight(selectedRadio));


        squat.setHint(excerWeightEnterText(selectedRadio, "Squat "));
        deadlift.setHint(excerWeightEnterText(selectedRadio,"Deadlift "));
        overhead.setHint(excerWeightEnterText(selectedRadio,"Overhead "));
        bench.setHint(excerWeightEnterText(selectedRadio, "Bench "));


    }

    private String excerWeightEnterText(RadioButton selectedRadio, String excerName){
        if (selectedRadio.getText().equals(AssignedExcers.FIVE_31_BBB)){
            return excerName + "Real 1RM Weight";
        } else if (selectedRadio.getText().equals(AssignedExcers.PHRAK_GSPL)) {
            return excerName + "Starting Weight";
        }

        return null;

    }

}
