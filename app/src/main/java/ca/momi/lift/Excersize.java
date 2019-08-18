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

/**
 * Created by anjan on 4/28/2017.
 */

public class Excersize {

    public final String uom = "lb";

    public static int NUM_OF_SETS = 5;
    public static int NUM_OF_REPS = 5;

    public Set curset[];

    public String excersizeName;
    public int setsDone;

    // UI Elements related to excersize
    public TextView textUI;
    public SeekBar seekSets;
    public SeekBar seekReps;
    public EditText weightUI;
    public Button doneSetUI;


    public Set[] makeSets() {
        Set setObj[] = new Set[NUM_OF_SETS];

        for(int i = 0; i < NUM_OF_SETS; i++){
            setObj[i] = new Set();
        }
        return setObj;

    }

    public void doneSet(int setNum, int repsDone, float weightDone){
        this.set_reps(setNum, repsDone, weightDone);
        this.setsDone = setsDone + 1;
    }

    private void set_reps(int setNum, int repsDone, float weightDone){
        // write reps to file memory
        this.curset[setNum].reps = repsDone;
        this.curset[setNum].weight = weightDone;
    }

    public Set get_set(int setNum){
        // given a setNum. Return the Set
        return this.curset[setNum];
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

    }
}
