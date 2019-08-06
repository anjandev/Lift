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

import java.util.ArrayList;

/**
 * Created by anjan on 4/28/2017.
 */

public class Excersize {

    public static int NUM_OF_SETS = 5;
    static final String[] workoutA = {"squat", "bench", "barbellRow"};

    public int[] sets = new int[NUM_OF_SETS];

    public String excersizeName;
    public int weight;
    public int setsDone;

    // UI Elements related to excersize
    public int seekSets;
    public int seekReps;
    public int weightUI;

    public void doneSet(int setNum, int repsDone){
        this.set_reps(setNum, repsDone);
        setsDone = setsDone + 1;
    }

    public int get_reps(int setNum){
        // set 1 is 0
        return sets[setNum];
    }

    private void set_reps(int setNum, int repsDone){
        this.sets[setNum] = repsDone;
    }

    public int get_setsDone(){
        return this.setsDone;
    }

    public void set_weight(int weightsDone){
        this.weight = weightsDone;

    }

    public int get_seekSets(){
        return seekSets;
    }

    public int get_seekReps(){
        return seekReps;
    }

    public int get_weightUI(){
        return weightUI;
    }


    public Excersize(String excersize_sName, int seekSets1, int seekReps1, int weightUI1){

        this.excersizeName = excersize_sName;

        this.setsDone = 0;

        this.seekSets = seekSets1;
        this.seekReps = seekReps1;
        this.weightUI = weightUI1;

        // zero data in array
        for(int i = 0; i < NUM_OF_SETS; i++){
            this.sets[i] = 0;
        }

    }
}
