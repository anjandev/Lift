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
import java.util.List;

public class AssignedExcers {
    // This file holds all the logic on what to do with a workout's failure, how to increase weights, routines etc.

    public String routName;
    public String program;

    // TODO: Change these to arraylists
    public List<String> excersizes = new ArrayList<>();
    public List<String> routineDescriber = new ArrayList<>();


    public int workoutOptionsnum;


    public long[] rest(){
        final long MINUTE_TO_MILLI = 60*1000;
        long[] A = {Math.round(1.50*MINUTE_TO_MILLI), 3*MINUTE_TO_MILLI};
        return A;
    }

    public int[] getReps (String excersize){
        if (this.program.equals("5x5")){
            if (excersize.equals("Deadlift")) {
                int[] A = {5};
                return A;
            } else {
                int[] A = {5, 5, 5, 5, 5};
                return A;
            }
        }
        if (this.program.equals("531BBB")) {
            if(excersize.substring(8).equals("Supplement")) {
                int[] A = {10, 10, 10, 10, 10};
                return A;
            } else if(excersize.equals("Assistance")){
                int[] A = {25};
                return A;
            } else {
                int[] A = {5, 5, 3, 5, 5, 5};
                return A;
            }
        }

        return null;
    }

    private boolean successfulExcer(Excersize excer) {
        // Check if you did all sets and you did all reps for all sets

        if (excer.setsDone != excer.numOfSets){
            return false;
        }

        for (int i = 0; i < excer.numOfSets; i++){
            if(excer.numOfReps[i] != excer.curset.get(i).reps){
                return false;
            }
        }
        
        return true;
    }

    public class NextExcersize {
        public String excersizeName;
        public double excersizeWeight;


        NextExcersize(String excer, double weight){
            excersizeName = excer;
            excersizeWeight = weight;
        }
    }
    
    
    private Excersize findExcersize(String excersizeName, List<Excersize> excersizes){
        // Given list of excersizes, returns excersize with matching routName
        for (Excersize excer : excersizes) {
            if (excersizeName.equals(excer.excersizeName)) {
                return excer;
            }
        }
        return null;
    }

    private List<NextExcersize> begExcer (List<String> excersizes){
        // quick function to initialize multiple excercise when user hasnt put a value before
        List<NextExcersize> lExcersizes = new ArrayList<>();

        for (String excersize : excersizes) {
            NextExcersize curEx = new NextExcersize("", 0);
            curEx.excersizeName = excersize;
            lExcersizes.add(curEx);
        }

        return lExcersizes;
    }

    private double getWeightInc(){
        // TODO: Add more complex increments. IE. User doesnt have 5 lb weights.

        if(Excersize.uom == "lb"){
            return 5;
        }

        // Must be kg
        return 2.5;
    }


    public List<NextExcersize> nextRoutineWeightsCheck(String[] sExcersizes){
        List<NextExcersize> nextRoutWeight = this.nextRoutineWeights();

        if (nextRoutWeight != null){
            return nextRoutWeight;
        }

        List<NextExcersize> emptyNextRoutWeights = new ArrayList<>();

        for(int i=0; i< sExcersizes.length; i++){
            emptyNextRoutWeights.add(new NextExcersize(sExcersizes[i], 0));
        }
        return emptyNextRoutWeights;
    }

    public List<NextExcersize> nextRoutineWeights(){
        List<NextExcersize> nextExcersizes = new ArrayList<>();
        LastWorkout lastwork = ExternalStore.getLastWorkoutProperties(0);
        LastWorkout secondlastwork = ExternalStore.getLastWorkoutProperties(1);

        // TODO: Add program checking
        if (program.equals("5x5")) {
            // Check if weight should be incremented
            if (lastwork == null) {
                // beginning regiment

                List<String> sNextExcersizes = getExcersizes((new AssignedExcers("5x5")).routineDescriber.get(0));

                nextExcersizes = begExcer(sNextExcersizes);

                if (Excersize.uom.equals("lb")) {
                    nextExcersizes.get(0).excersizeWeight = 45;
                    nextExcersizes.get(1).excersizeWeight = 45;
                    nextExcersizes.get(2).excersizeWeight = 65;
                }

                final double LB_TO_KG = 2.2;

                if (Excersize.uom.equals("kg")) {
                    nextExcersizes.get(0).excersizeWeight = Math.round(nextExcersizes.get(0).excersizeWeight / LB_TO_KG);
                    nextExcersizes.get(1).excersizeWeight = Math.round(nextExcersizes.get(1).excersizeWeight / LB_TO_KG);
                    nextExcersizes.get(2).excersizeWeight = Math.round(nextExcersizes.get(2).excersizeWeight / LB_TO_KG);
                }

                return nextExcersizes;
            }


            if (secondlastwork == null) {
                // beginning regiment

                List<String> sNextExcersizes = getExcersizes((new AssignedExcers("5x5")).routineDescriber.get(1));

                nextExcersizes = begExcer(sNextExcersizes);

                if (successfulExcer(findExcersize(sNextExcersizes.get(0), lastwork.excersizesDone))) {
                    nextExcersizes.get(0).excersizeWeight = 45 + getWeightInc();
                    if (Excersize.uom.equals("kg")) {
                        nextExcersizes.get(0).excersizeWeight = 20 + getWeightInc();
                    }
                } else {
                    nextExcersizes.get(0).excersizeWeight = 45;
                    if (Excersize.uom.equals("kg")) {
                        nextExcersizes.get(0).excersizeWeight = 20;
                    }
                }


                if (Excersize.uom.equals("lb")) {
                    nextExcersizes.get(1).excersizeWeight = 45;
                    nextExcersizes.get(2).excersizeWeight = 95;
                }

                if (Excersize.uom.equals("kg")) {
                    nextExcersizes.get(1).excersizeWeight = Math.round(nextExcersizes.get(1).excersizeWeight);
                    nextExcersizes.get(2).excersizeWeight = 40;
                }

                return nextExcersizes;
            }
        List<String> sNextExcersizes = getExcersizes((new AssignedExcers("5x5")).routineDescriber.get(nextRoutineIdx(lastwork.routineIdx)));
        nextExcersizes.add(checkandIncWeight(sNextExcersizes.get(0),lastwork));
        nextExcersizes.add(checkandIncWeight(sNextExcersizes.get(1),secondlastwork));
        nextExcersizes.add(checkandIncWeight(sNextExcersizes.get(2),secondlastwork));

        // TODO: Check if weight should be decremented
        return nextExcersizes;
        } else if (program.equals("531BBB")) {
            // I dont wanna program the weight increase logic yet
        }
        return null;
    }

    private NextExcersize checkandIncWeight(String excerName, LastWorkout lastWorkout){
        List<Excersize> excersizes = lastWorkout.excersizesDone;

        Excersize curEx = findExcersize(excerName, excersizes);

        NextExcersize newEx;
        if (lastWorkout.program.equals("5x5")) {
            if (successfulExcer(curEx)){
                newEx = new NextExcersize(curEx.excersizeName, curEx.curset.get(0).weight + getWeightInc());
            } else {
                newEx = new NextExcersize(curEx.excersizeName, curEx.curset.get(0).weight);

            }
            return newEx;
        }
        else {
            return null;
        }
    }

    public int nextRoutineIdx(int curRoutine) {

        if (curRoutine + 1 >= workoutOptionsnum) {
            return 0;
        }
        else {
            return curRoutine +1;
        }

    }

    public List<String> getExcersizes(String sentName){
        this.routName = sentName;

        switch(program) {
            case "5x5":
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
                break;
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
            case "531BBB":
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
        }
        return excersizes;
    }


    public AssignedExcers(String program) {
        this.program = program;

        if(program.equals("5x5")) {
            routineDescriber.add("Bench Press, Barbell Row");
            routineDescriber.add("Overhead Press, Deadlift");
        } else if (program.equals("madcow")) {
            routineDescriber.add("Day 1");
            routineDescriber.add("Day 2");
            routineDescriber.add("Day 3");
        } else if (program.equals("531BBB")) {
            routineDescriber.add("Day 1");
            routineDescriber.add("Day 2");
            routineDescriber.add("Day 3");
            routineDescriber.add("Day 4");
        }

        workoutOptionsnum = routineDescriber.size();
    }
}
