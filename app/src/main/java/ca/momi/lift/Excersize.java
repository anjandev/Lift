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

/**
 * Created by anjan on 4/28/2017.
 */

public class Excersize {

    public final String uom = "lb";

    public static int NUM_OF_SETS = 5;

    public set curset[];

    public String excersizeName;
    public int setsDone;

    // UI Elements related to excersize
    public int seekSets;
    public int seekReps;
    public int weightUI;


    public set[] makeSets() {
        set setObj[] = new set[NUM_OF_SETS];

        for(int i = 0; i < NUM_OF_SETS; i++){
            setObj[i] = new set();
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

    public set get_set(int setNum){
        // given a setNum. Return the set
        return this.curset[setNum];
    }

    public int get_seekSets(){
        return this.seekSets;
    }

    public int get_seekReps(){ return seekReps; }

    public int get_weightUI(){ return weightUI; }


    public Excersize(String excersize_sName, int seekSets1, int seekReps1, int weightUI1){

        this.excersizeName = excersize_sName;

        this.setsDone = 0;

        // TODO: generalize this
        this.seekSets = seekSets1;
        this.seekReps = seekReps1;
        this.weightUI = weightUI1;

        this.curset = makeSets();

    }
}
