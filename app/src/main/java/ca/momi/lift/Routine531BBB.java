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

import java.util.ArrayList;
import java.util.List;

import static ca.momi.lift.MainActivity.PREFERENCE_FILE_KEY;

public class Routine531BBB {

    static private double getTrainingMax531(Double weight){
        return ((weight))*0.9;
    }



    static private double getfailureWeight(double curweight) {
        return AssignedExcers.getPercentOfWeight(curweight, 0.9);
    }

    static private double getPercentWorkSet(int weekNum, int setNum){
        switch (weekNum) {
            case 0:
                switch (setNum) {
                    case 0:
                        return 0.65;
                    case 1:
                        return 0.75;
                    case 2:
                        return 0.85;
                }
            case 1:
                switch (setNum) {
                    case 0:
                        return 0.70;
                    case 1:
                        return 0.80;
                    case 2:
                        return 0.90;
                }
            case 2:
                switch (setNum) {
                    case 0:
                        return 0.75;
                    case 1:
                        return 0.85;
                    case 2:
                        return 0.95;
                }
            case 3:
                switch (setNum) {
                    case 0:
                        return 0.40;
                    case 1:
                        return 0.50;
                    case 2:
                        return 0.60;
                }
        }
        return 0;
    }


    static private double getPercentageWarmUp(int setNum){
        switch (setNum){
            case 0:
                return 0.4;
            case 1:
                return 0.5;
            case 2:
                return 0.6;
        }
        return 0;
    }

    static private double downSetsPercentage(int month){
        switch (month){
            case 0:
                return 0.5;
            case 1:
                return 0.6;
            case 2:
                return 0.6;
        }
        return 0;
    }
    static public List<NextExcersize> nextRoutineWeights (Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        AssignedExcers assExcer = new AssignedExcers();

        int numPreviousExcersizes = ExternalStore.getNumLastWorkoutFiles();
        int TOTAL_ROUTINE = 4;
        int nextExcersize = numPreviousExcersizes % TOTAL_ROUTINE + 1;

        List<String> slistNewExcersizes = assExcer.getExcersizes("Day "+nextExcersize);

        int IDX_OF_LAST_WEEK = 3;


        int week = numPreviousExcersizes / TOTAL_ROUTINE;

        int month= week / 4;

        List<NextExcersize> nextExcersizes = new ArrayList<>();

        // If haven't been running for more than a week, just return the weight. No need to check
        // for failure.
        // if (numPreviousExcersizes - IDX_OF_LAST_WEEK =< 0){
        // Always increment
            for (int i = 0; i < slistNewExcersizes.size(); i++){
                if (slistNewExcersizes.get(i).equals("Assistance")) {
                    int[] reps = assExcer.getReps(slistNewExcersizes.get(i));

                    nextExcersizes.add(new NextExcersize(slistNewExcersizes.get(i), zeroArray(reps.length)));
                }
                else {
                    // Bug with supplement
                    String curExcersize = slistNewExcersizes.get(i);

                    if (isSupplement(curExcersize)) {
                        curExcersize = curExcersize.substring(0, curExcersize.lastIndexOf(" - Supplement"));

                    }

                    double excerWeight = Double.valueOf(sharedPref.getString(curExcersize + "1RM", "0"));
                    double trainingMax = getTrainingMax531(excerWeight);
                    int[] reps = assExcer.getReps(slistNewExcersizes.get(i));
                    double[] weights = new double[reps.length];
                    if (isSupplement(slistNewExcersizes.get(i))){
                        for (int idx =0; idx< reps.length; idx++){
                            weights[idx] = AssignedExcers.getPercentOfWeight(trainingMax, downSetsPercentage(month));
                        }
                    } else {
                        int WARMUP_SETS = 3;
                        for (int idx =0; idx< WARMUP_SETS; idx++){
                            weights[idx] = AssignedExcers.getPercentOfWeight(trainingMax, getPercentageWarmUp(idx));
                        }
                        for(int idx=0; idx < reps.length - WARMUP_SETS; idx++){
                            weights[idx + WARMUP_SETS] = AssignedExcers.getPercentOfWeight(trainingMax, getPercentWorkSet(week,idx));
                        }

                    }
                    nextExcersizes.add(new NextExcersize(slistNewExcersizes.get(i), weights));
                }

            }


        // }

        return nextExcersizes;

        // TODO: check failure
        // LastWorkout lastWeekWorkout= ExternalStore.getLastidxProperties(IDX_OF_LAST_WEEK);


    }


    private static double[] zeroArray(int length){

        double[] A = new double[length];

        for (int i = 0; i < length; i++){
            A[i] = 0;
        }

        return A;

    }


    public static boolean isSupplement(String excersize){
        if(excersize.length() < "Supplment".length()){
            return false;
        }
        return excersize.substring(excersize.length()-"Supplement".length()).equals("Supplement");
    }

}
