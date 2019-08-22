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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final RadioGroup programGroup = findViewById(R.id.programs);
        radioUtils.setRadioGroup(programGroup, AssignedExcers.routNames(), this);

        Button setProgram = findViewById(R.id.setProgram);

        setProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( programGroup.getCheckedRadioButtonId() == -1){
                    Snackbar.make(view, "Please select a workout routine", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                RadioButton selectedRadio = findViewById(programGroup.getCheckedRadioButtonId());

                SharedPreferences sharedPref = view.getContext().getSharedPreferences(
                                               MainActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("program", selectedRadio.getText().toString());
                editor.commit();
                System.exit(0);
            }
        });

    }

}
