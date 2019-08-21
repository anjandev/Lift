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
    public int[] numOfReps;

    public List<Set> curset;

    public String excersizeName;
    public int setsDone;

    // UI Elements related to excersize
    public TextView textUI;
    public SeekBar seekSets;
    public SeekBar seekReps;
    public EditText weightUI;
    public Button doneSetUI;


    public List<Set> makeSets() {

        List<Set> setObj = new ArrayList<>();

        for(int i = 0; i < numOfSets; i++){
            Set current = new Set();
            setObj.add(current);
        }
        return setObj;

    }

    public void doneSet(int repsDone, float weightDone){
        this.set_reps(this.setsDone, repsDone, weightDone);
        this.setsDone = setsDone + 1;
        if (seekReps != null & setsDone < numOfReps.length) {
            seekReps.setMax(numOfReps[setsDone]);
            seekReps.setProgress(numOfReps[setsDone]);
        }
    }

    private void set_reps(int setNum, int repsDone, float weightDone){
        // write reps to file memory
        Set currentSet = new Set();

        currentSet.reps = repsDone;
        currentSet.weight = weightDone;

        curset.add(currentSet);
    }

    public void setUI(SeekBar seekSets1, SeekBar seekReps1, EditText weightUI1, TextView textUI1, Button doneSetUI){
        this.textUI = textUI1;
        this.seekSets = seekSets1;
        this.seekReps = seekReps1;
        this.weightUI = weightUI1;
        this.doneSetUI = doneSetUI;
    }


    public Excersize(String excersize_sName){

        this.excersizeName = excersize_sName;

        this.setsDone = 0;

        this.curset = makeSets();


        AssignedExcers assignedExcers = new AssignedExcers(MainActivity.program);
        numOfReps = assignedExcers.getReps(excersize_sName);
        numOfSets = numOfReps.length;
    }
}
