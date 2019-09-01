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
import android.preference.PreferenceGroup;

import java.util.ArrayList;
import java.util.List;

public class AssignedExcers {
    // This file holds all the logic on what to do with a workout's failure, how to increase weights, routines etc.

    public String routName;
    public String program;

    // TODO: Change these to arraylists
    public List<String> excersizes = new ArrayList<>();
    public List<String> routineDescriber = new ArrayList<>();


    public int workoutOptionsnum;

    public static final double smallestWeightLb = 5;
    public static final double smallestWeightKg = 2.5;


    public static final String PHRAK_GSPL = "Phrak’s GSLP";
    public static final String FIVE_x_5 = "5x5";
    public static final String FIVE_31_BBB = "531BBB";

    public static List<String> routNames () {
        List<String> routineNames = new ArrayList<>();
        routineNames.add(FIVE_x_5);
        routineNames.add(FIVE_31_BBB);
        routineNames.add(PHRAK_GSPL);
        // routineNames.add("madcow");
        return routineNames;
    }

    public static List<Set> getAmrap(List<Set> sets){
        // Amrap: as many sets as possible

        // By default, Amrap is false for a set. Therefore, in this function, we only define sets
        // for which Amrap is true
        switch (MainActivity.program) {
            case PHRAK_GSPL:
                sets.get(sets.size()-1).AMRAP = true;
        }

        return sets;
    }

    public long[] rest(){
        final long MINUTE_TO_MILLI = 60*1000;
        long[] A = {Math.round(1.50*MINUTE_TO_MILLI), 3*MINUTE_TO_MILLI};
        return A;
    }

    public int[] getReps (String excersize){
        if (this.program.equals(FIVE_x_5)) {
            if (excersize.equals("Deadlift")) {
                int[] A = {5};
                return A;
            } else {
                int[] A = {5, 5, 5, 5, 5};
                return A;
            }
        } else if (this.program.equals(FIVE_31_BBB)) {
            if(Routine531BBB.isSupplement(excersize)) {
                int[] A = {10, 10, 10, 10, 10};
                return A;
            } else if(excersize.equals("Assistance")){
                int[] A = {25};
                return A;
            } else {
                int[] A = {5, 5, 3, 5, 5, 5};
                return A;
            }
        } else if (this.program.equals(PHRAK_GSPL)) {
            int[] A = {5, 5, 5};
            return A;
        }
        return null;
    }

    public List<NextExcersize> nextRoutineWeightsCheck(String[] sExcersizes, Context context){
        List<NextExcersize> nextRoutWeight = this.nextRoutineWeights(context);

       if (nextRoutWeight != null){
            return nextRoutWeight;
       }

       List<NextExcersize> emptyNextRoutWeights = new ArrayList<>();

       for(int i=0; i< sExcersizes.length; i++){
           emptyNextRoutWeights.add(new NextExcersize(sExcersizes[i], Routine5x5.weights(0,getReps(sExcersizes[i]).length)));
       }

       return emptyNextRoutWeights;
    }

    public List<NextExcersize> nextRoutineWeights(Context context){

        if (MainActivity.program.equals(FIVE_x_5)) {
            return Routine5x5.nextRoutineWeights();
        } else if (MainActivity.program.equals(FIVE_31_BBB)) {
            return Routine531BBB.nextRoutineWeights(context);
        }
        
        return null;
    }


    public int nextRoutineIdx(int curRoutine) {

        if (curRoutine + 1 >= workoutOptionsnum) {
            return 0;
        }
        else {
            return curRoutine +1;
        }

    }

    public List<String> getExcersizes(String sentName) {
        this.routName = sentName;

        switch (program) {
            case FIVE_x_5:
                switch (routName) {
                    case "Bench Press, Barbell Row":
                        excersizes.add("Squat");
                        excersizes.add("Bench Press");
                        excersizes.add("Bent-over Row");
                        break;
                    case "Overhead Press, Deadlift":
                        excersizes.add("Squat");
                        excersizes.add("Overhead Press");
                        excersizes.add("Deadlift");
                        break;
                }
                return excersizes;
            case "madcow":
                switch (routName) {
                    case "Day 1":
                        excersizes.add("Squat");
                        excersizes.add("Bench Press");
                        excersizes.add("Bent-over Row");
                        break;
                    case "Day 2":
                        excersizes.add("Squat");
                        excersizes.add("Overhead Press");
                        excersizes.add("Deadlift");
                        break;
                    case "Day 3":
                        excersizes.add("Squat");
                        excersizes.add("Bench Press");
                        excersizes.add("Bent-over Row");
                        break;
                }
                return excersizes;
            case FIVE_31_BBB:
                switch (routName) {
                    case "Day 1":
                        excersizes.add("Overhead Press");
                        excersizes.add("Bench Press - Supplement");
                        excersizes.add("Assistance");
                        break;
                    case "Day 2":
                        excersizes.add("Deadlift");
                        excersizes.add("Squat - Supplement");
                        excersizes.add("Assistance");
                        break;
                    case "Day 3":
                        excersizes.add("Bench Press");
                        excersizes.add("Overhead Press - Supplement");
                        excersizes.add("Assistance");
                        break;
                    case "Day 4":
                        excersizes.add("Squat");
                        excersizes.add("Deadlift - Supplement");
                        excersizes.add("Assistance");
                        break;
                }
                return excersizes;
            case PHRAK_GSPL:
                switch (routName) {
                    case "Day A":
                        excersizes.add("Barbell Rows");
                        excersizes.add("Bench Press");
                        excersizes.add("Squats");
                        break;
                    case "Day B":
                        excersizes.add("Chinups");
                        excersizes.add("Overhead Press");
                        excersizes.add("Deadlifts");
                        break;
                }
                return excersizes;
        }
        return null;
    }


    public AssignedExcers(){
            this.program = MainActivity.program;

            if (program.equals(FIVE_x_5)) {
                routineDescriber.add("Bench Press, Barbell Row");
                routineDescriber.add("Overhead Press, Deadlift");
            } else if (program.equals("madcow")) {
                routineDescriber.add("Day 1");
                routineDescriber.add("Day 2");
                routineDescriber.add("Day 3");
            } else if (program.equals(FIVE_31_BBB)) {
                routineDescriber.add("Day 1");
                routineDescriber.add("Day 2");
                routineDescriber.add("Day 3");
                routineDescriber.add("Day 4");
            } else if (program.equals(PHRAK_GSPL)) {
                routineDescriber.add("Day A");
                routineDescriber.add("Day B");
            }

            workoutOptionsnum = routineDescriber.size();
    }
}
