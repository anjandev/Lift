package ca.momi.lift;

import java.util.ArrayList;

/**
 * Created by anjan on 4/28/2017.
 */

public class Excersize {

    public static final int NUM_OF_SETS = 5;
    ArrayList<Integer> sets = new ArrayList<Integer>();
    String excersizeName;
    int weight;
    int setsDone;

    public void doneSet(int setNum, int repsDone){
        this.set_reps(setNum, repsDone);
        setsDone = setsDone + 1;
    }

    public int get_reps(int setNum){
        // set 1 is 0
        return sets.get(setNum);
    }

    private void set_reps(int setNum, int repsDone){
        sets.set(setNum, repsDone);
    }

    public int get_setsDone(){
        return setsDone;
    }

    public void set_weight(int weightsDone){
        weight = weightsDone;

    }

    public Excersize(String excersize_sName){

        excersizeName = excersize_sName;

        setsDone = 0;

        // zero data in array
        for(int i = 0; i < NUM_OF_SETS; i++){
            sets.set(i, 0);
        }

    }
}
