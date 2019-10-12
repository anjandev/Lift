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
import android.provider.MediaStore;
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
        final RadioGroup uomGroup = findViewById(R.id.uom);
        radioUtils.setRadioGroup(programGroup, AssignedExcers.routNames(), this);

        Button setProgram = findViewById(R.id.setProgram);

        final EditText smallestPlate = findViewById(R.id.smallestWeightPlate);

        setProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( programGroup.getCheckedRadioButtonId() == -1){
                    Snackbar.make(view, "Please select a workout routine", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                RadioButton selectedProgramButton = findViewById(programGroup.getCheckedRadioButtonId());

                if (selectedProgramButton.getText().equals("531BBB") & (textEmpty((TextView) findViewById(R.id.begSquat))
                   | textEmpty((TextView) findViewById(R.id.begDeadLift))
                   | textEmpty((TextView) findViewById(R.id.begOverhead))
                   | textEmpty((TextView) findViewById(R.id.begBench)))) {
                    Snackbar.make(view, "Please enter a starting weight for Squat, Overhead press, " +
                            " bench, and Deadlift", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                if ( uomGroup.getCheckedRadioButtonId() == -1){
                    Snackbar.make(view, "Please select a unit of measure",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                if (smallestPlate.getText().toString().equals("")) {
                    Snackbar.make(view, "Please enter the smallest plate you have",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                SharedPreferences sharedPref = view.getContext().getSharedPreferences(
                                               MainActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("program", selectedProgramButton.getText().toString());

                RadioButton uomSelected = findViewById(uomGroup.getCheckedRadioButtonId());
                editor.putString("uom", uomSelected.getText().toString());

                editor.putString("smallestPlate", smallestPlate.getText().toString());


                if(selectedProgramButton.getText().equals("531BBB")){
                    editor.putString("Squat1RM", ((TextView) findViewById(R.id.begSquat)).getText().toString());
                    editor.putString("Deadlift1RM", ((TextView) findViewById(R.id.begDeadLift)).getText().toString());
                    editor.putString("Overhead Press1RM", ((TextView) findViewById(R.id.begOverhead)).getText().toString());
                    editor.putString("Bench Press1RM", ((TextView) findViewById(R.id.begBench)).getText().toString());
                    editor.putString("Last Day Saved", "null");
                }

                editor.commit();
                System.exit(0);
            }
        });


        programGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Button selectedButton = findViewById(radioGroup.getCheckedRadioButtonId());
                if(selectedButton.getText().toString().equals("531BBB")){
                    setWeightBBB531Visible(true);
                } else {
                    setWeightBBB531Visible(false);
                }
            }

        });

        setWeightBBB531Visible(false);
        smallestPlate.setVisibility(View.INVISIBLE);

        uomGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                smallestPlate.setVisibility(View.VISIBLE);
                RadioButton selectedButton = findViewById(radioGroup.getCheckedRadioButtonId());
                smallestPlate.setHint("Weight of smallest "+ selectedButton.getText() + " plate");
            }
        });
    }


    private boolean textEmpty (TextView textInput) {
        return textInput.getText().toString().equals("");
    }

    private void setWeightBBB531Visible (boolean setVisi){
        if (setVisi) {
            findViewById(R.id.begSquat).setVisibility(View.VISIBLE);
            findViewById(R.id.begDeadLift).setVisibility(View.VISIBLE);
            findViewById(R.id.begOverhead).setVisibility(View.VISIBLE);
            findViewById(R.id.begBench).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.begSquat).setVisibility(View.INVISIBLE);
            findViewById(R.id.begDeadLift).setVisibility(View.INVISIBLE);
            findViewById(R.id.begOverhead).setVisibility(View.INVISIBLE);
            findViewById(R.id.begBench).setVisibility(View.INVISIBLE);
        }
    }

}
