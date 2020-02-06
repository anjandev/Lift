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
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import static ca.momi.lift.MainActivity.PREFERENCE_FILE_KEY;

public class Routine531BBB {

    final static int  TOTAL_ROUTINE = 4;

    static private double getTrainingMax531(Double weight){
        return ((weight))*0.9;
    }

    static private double getWeightInc(String excersize){
        if (AssignedExcers.isUpperBody(excersize)){
            return AssignedExcers.getSmallestWeightForUOM();
        } else {
            return 2*AssignedExcers.getSmallestWeightForUOM();
        }
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

    static private double downSetsPercentage(){
        // this abstraction exists so that we can get user's down set percentage from settings and
        // do more complex calculations.
        // That setting has not been implemented so here we are....
        return 0.5;
    }


    static public List<NextExcersize> nextRoutineWeights (Context context){

        AssignedExcers assExcer = new AssignedExcers();

        int numPreviousExcersizes = ExternalStore.getNumLastWorkoutFiles();
        int nextExcersize = numPreviousExcersizes % TOTAL_ROUTINE + 1;

        List<String> slistNewExcersizes = assExcer.getExcersizes("Day "+nextExcersize);

        List<NextExcersize> nextExcersizes = new ArrayList<>();

        boolean lastDaySavedFailed = false;

        int curWeekRelativeMonth = getCurWeekRelativeMonth();

        for (int i = 0; i < slistNewExcersizes.size(); i++){
            if (slistNewExcersizes.get(i).equals(AssignedExcers.ASSISTANCE)) {
                int[] reps = assExcer.getReps(slistNewExcersizes.get(i));

                nextExcersizes.add(new NextExcersize(slistNewExcersizes.get(i), zeroArray(reps.length)));
            }
            else {
                // Bug with supplement
                String curExcersize = slistNewExcersizes.get(i);

                if (isSupplement(curExcersize)) {
                    curExcersize = curExcersize.substring(0, curExcersize.lastIndexOf(AssignedExcers.SUPPLEMENT));
                }

                SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                double excerWeight = Double.valueOf(sharedPref.getString(curExcersize + "1RM", "0"));

                String lastDay = sharedPref.getString("Last Day Saved", "null");
                if (lastDay.equals(String.valueOf(nextExcersize)) & i == 0){
                    // If this is true, user already opened this activity. As such, we had already incremented
                    // weight. Only check on the first item in excersizes list.
                    lastDaySavedFailed = true;
                }

                if (curWeekRelativeMonth == 0 & (nextExcersize == 1 || nextExcersize == 2) & !lastDaySavedFailed){
                    // If first week of the new month, increment weight and save to memory.
                    // Only increment on days 1 or 2 because if I allowed all days you work out first
                    // week, I would increment weight twice in the first week of the month.
                    // Todo: ask user if they want to progress or look at previous data.
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Last Day Saved", String.valueOf(nextExcersize));

                    excerWeight += getWeightInc(curExcersize);
                    editor.putString(curExcersize + "1RM", String.valueOf(excerWeight));
                    editor.apply();
                }

                double trainingMax = getTrainingMax531(excerWeight);
                int[] reps = assExcer.getReps(slistNewExcersizes.get(i));
                double[] weights = new double[reps.length];
                if (isSupplement(slistNewExcersizes.get(i))){
                    for (int idx =0; idx< reps.length; idx++){
                        weights[idx] = AssignedExcers.getPercentOfWeight(trainingMax, downSetsPercentage());
                    }
                } else {
                    int WARMUP_SETS = 3;
                    for (int idx =0; idx< WARMUP_SETS; idx++){
                        weights[idx] = AssignedExcers.getPercentOfWeight(trainingMax, getPercentageWarmUp(idx));
                    }
                    for(int idx=0; idx < reps.length - WARMUP_SETS; idx++){
                        weights[idx + WARMUP_SETS] = AssignedExcers.getPercentOfWeight(trainingMax, getPercentWorkSet(curWeekRelativeMonth,idx));
                    }

                }
                nextExcersizes.add(new NextExcersize(slistNewExcersizes.get(i), weights));
            }

        }



        return nextExcersizes;

        // TODO: check failure
        // LastWorkout lastWeekWorkout= ExternalStore.getLastidxProp(IDX_OF_LAST_WEEK);


    }

    private static int getCurWeekRelativeMonth() {
        int numPreviousExcersizes = ExternalStore.getNumLastWorkoutFiles();

        final int curWeek = numPreviousExcersizes / TOTAL_ROUTINE;

        final int month= curWeek / TOTAL_ROUTINE;

        final int WEEKS_IN_MONTH = 4;
        // First week of the month would be 0, second week 1, etc.
        return curWeek - month * WEEKS_IN_MONTH;
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

    public static String getComment(Context context) {

        String comment = "";

        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        Resources res = context.getResources();

        comment += res.getString(R.string.Week) + ": " + getCurWeekRelativeMonth() + "\n";
        comment += res.getString(R.string.Squat531Max) + ": " + sharedPref.getString(AssignedExcers.SQUAT + "1RM", "0") + "\n";
        comment += res.getString(R.string.Bench531Max) + ": " + sharedPref.getString(AssignedExcers.BENCH + "1RM", "0") + "\n";
        comment += res.getString(R.string.Deadlift531Max) + ": " + sharedPref.getString(AssignedExcers.DEADLIFT + "1RM", "0") + "\n";
        comment += res.getString(R.string.Overhead531Max) + ": " + sharedPref.getString(AssignedExcers.OVERHEAD + "1RM", "0") + "\n";


        return comment;
    }

}
