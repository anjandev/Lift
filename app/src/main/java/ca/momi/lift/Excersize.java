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

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anjan on 4/28/2017.
 */

public class Excersize {

    public static final String uom = "lb";

    // This is the number of sets and reps need to perform in order to consider excersize as "done"
    // and allow progress.
    // TODO: Add condition if madcow. Put in initialization
    public int numOfSets;
    public List<Set> setsToDo = new ArrayList<>();

    // This variable holds the info for the sets done
    public List<Set> curset = new ArrayList<>();

    public String excersizeName;
    public int setsDone;

    // UI Elements related to excersize
    public TextView textUI;
    public SeekBar seekSets;
    public SeekBar seekReps;
    public EditText weightUI;
    public TextView repsText;
    public Button doneSetUI;
    public Button AMRAP;


    public boolean didEverything(){
        if (this.setsDone < this.setsToDo.size()){
            return false;
        }

        for (int i = 0; i < this.setsToDo.size(); i++){
            if (this.curset.get(i).reps < this.setsToDo.get(i).reps){
                return false;
            }

            if (this.curset.get(i).weight < this.setsToDo.get(i).weight){
                return false;
            }
        }

        return true;
    }
    public void doneSet(int repsDone, double weightDone){
        this.set_reps(repsDone, weightDone);
        this.setsDone = setsDone + 1;
        if (seekReps != null & setsDone < setsToDo.size()) {
            seekReps.setMax(setsToDo.get(setsDone).reps);
            seekReps.setProgress(setsToDo.get(setsDone).reps);
        }
        if (weightUI != null & setsDone < setsToDo.size()) {
            weightUI.setText(String.valueOf(setsToDo.get(setsDone).weight));
        }
        if (AMRAP != null & setsDone < setsToDo.size()){
            if (curset.get(setsDone).AMRAP) {
                AMRAP.setVisibility(View.VISIBLE);
            } else {
                AMRAP.setVisibility(View.INVISIBLE);
            }
        }
        if (weightUI != null & weightUI.getText().toString().equals("0.0")){
            weightUI.setText(String.valueOf(curset.get(0).weight));
        }
    }

    private void set_reps(int repsDone, double weightDone){
        // write reps to file memory
        curset.set(setsDone, new Set(repsDone, weightDone));
    }

    public void setUI(SeekBar seekSets1, SeekBar seekReps1, EditText weightUI1, TextView textUI1,
                      Button doneSetUI, TextView sentRepsText, Button sentAMRAP){

        this.textUI = textUI1;
        this.seekSets = seekSets1;
        this.seekReps = seekReps1;
        this.weightUI = weightUI1;
        this.doneSetUI = doneSetUI;
        this.repsText = sentRepsText;
        this.AMRAP = sentAMRAP;

    }


    private void setSetsWeightToDo(double[] weight){
        AssignedExcers assignedExcers = new AssignedExcers();
        int[] reps = assignedExcers.getReps(excersizeName);
        for (int i = 0; i < reps.length; i++){
            setsToDo.add(new Set(reps[i], weight[i]));
        }
        numOfSets = setsToDo.size();

        for (int i=0; i < numOfSets; i++){
            curset.add(new Set(0, weight[i]));
        }

        setsToDo = AssignedExcers.getAmrap(setsToDo);
        curset = AssignedExcers.getAmrap(curset);
    }

    public Excersize(String excersize_sName, double[] weights){

        this.excersizeName = excersize_sName;

        this.setsDone = 0;

        setSetsWeightToDo(weights);


    }
}
