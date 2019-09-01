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

import java.util.ArrayList;
import java.util.List;

public class RoutinePhrak {

    static public List<NextExcersize> nextRoutineWeights (){
        AssignedExcers assExcer = new AssignedExcers();

        int numPreviousExcersizes = ExternalStore.getNumLastWorkoutFiles();

        if (numPreviousExcersizes == 0 || numPreviousExcersizes == 1){
            return null;
        }

        List<String> slistNewExcersizes = assExcer.getExcersizes("Day "+
                                                                 nextExcersize(numPreviousExcersizes));

        LastWorkout metaLastWorkout = ExternalStore.getLastidxProperties(1);

        List<NextExcersize> nextExcersizes = new ArrayList<>();

        for (int i = 0; i < slistNewExcersizes.size(); i++){

            double[] weights = makeWeights(getNextWeight(metaLastWorkout.excersizesDone.get(i)));
            nextExcersizes.add(new NextExcersize(slistNewExcersizes.get(i), weights));
        }

        return nextExcersizes;

    }


    static private double[] makeWeights(double weight){
        double[] A = new double[3];
        final int TOTAL_SETS = 3;

        for (int i =0; i < TOTAL_SETS; i++){
            A[i] = weight;
        }
        return A;
    }

    static private double getNextWeight(Excersize excer){
        Double weightDone = excer.setsToDo.get(0).weight;

        if (fail(excer)) {
            return deload(weightDone);
        } else if (moreThan10AMRAP(excer)){
            return weightDone + 2*weightToAdd(excer);
        }

        // you did the bare minimum. Progress as normal
        return weightDone+weightToAdd(excer);

    }

    static private double weightToAdd(Excersize excer){
        String name = excer.excersizeName;

        // Check if upper body workout
        if (name.equals("Barbell Rows") || name.equals("Bench Press") || name.equals("Chinups")
            || name.equals("Overhead Press")){
            if (excer.uom.equals("lb")){
                return AssignedExcers.smallestWeightLb;
            } else {
                return AssignedExcers.smallestWeightKg;
            }
        }

        // else: the workout is lower body
        if (excer.uom.equals("lb")){
            return AssignedExcers.smallestWeightLb*2;
        } else {
            return AssignedExcers.smallestWeightKg * 2;
        }

    }

    static private boolean moreThan10AMRAP(Excersize excer){
        for (Set set: excer.curset) {
            if (set.AMRAP) {
                if (set.reps > 10){
                    return true;
                }
            }
        }
        return false;
    }

    static private boolean fail(Excersize excer){

        int totalRepsDone =0;

        for (int i =0; i < excer.curset.size(); i++){
            totalRepsDone += excer.curset.get(i).reps;
        }

        if (totalRepsDone < 15){
            return true;
        }
        return false;
    }

    static private double deload(double currentWeight){
        return AssignedExcers.getPercentOfWeight(currentWeight, 0.9);
    }


    static private String nextExcersize(int numPrevWorkouts){
        if (numPrevWorkouts % 2 == 0) {
            return "A";
        } else {
            return "B";
        }
    }
}
