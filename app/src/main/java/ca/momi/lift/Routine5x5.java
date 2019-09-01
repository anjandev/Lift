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

import static ca.momi.lift.AssignedExcers.smallestWeightKg;
import static ca.momi.lift.AssignedExcers.smallestWeightLb;


public class Routine5x5 {


    public static double[] weights(double weight, int numSets){
        // Mistake. deadlift doesnt have weight.
        double[] A = new double[numSets];

        for (int i = 0; i < numSets; i++){
            A[i] = weight;
        }
        return  A;

    }

    public static List<NextExcersize> nextRoutineWeights() {

        List<NextExcersize> nextExcersizes = new ArrayList<>();
        LastWorkout lastwork = ExternalStore.getLastidxProperties(0);
        LastWorkout secondlastwork = ExternalStore.getLastidxProperties(1);

        AssignedExcers assExcer = new AssignedExcers();

        // Check if weight should be incremented
        if (lastwork == null) {
            // beginning regiment

            List<String> sNextExcersizes = assExcer.getExcersizes((new AssignedExcers()).routineDescriber.get(0));

            nextExcersizes = begExcer(sNextExcersizes);

            if (Excersize.uom.equals("lb")) {
                nextExcerSetWeight(nextExcersizes, 45, 0);
                nextExcerSetWeight(nextExcersizes, 45, 1);
                nextExcerSetWeight(nextExcersizes, 65, 2);
            }

            final double LB_TO_KG = 2.2;

            if (Excersize.uom.equals("kg")) {
                nextExcerSetWeight(nextExcersizes, Math.round(nextExcersizes.get(0).excersizeWeight[0] / LB_TO_KG), 0);
                nextExcerSetWeight(nextExcersizes, Math.round(nextExcersizes.get(1).excersizeWeight[0] / LB_TO_KG), 1);
                nextExcerSetWeight(nextExcersizes, Math.round(nextExcersizes.get(2).excersizeWeight[0] / LB_TO_KG), 2);
            }

            return nextExcersizes;
        }


        if (secondlastwork == null) {
            // beginning regiment
            List<String> sNextExcersizes = assExcer.getExcersizes((new AssignedExcers()).routineDescriber.get(1));

            nextExcersizes = begExcer(sNextExcersizes);

            if (findExcersize(sNextExcersizes.get(0), lastwork.excersizesDone).success()) {

                nextExcerSetWeight(nextExcersizes, 45+ getWeightInc(nextExcersizes.get(0).excersizeName), 0);
                if (Excersize.uom.equals("kg")) {
                    nextExcerSetWeight(nextExcersizes, 20+ getWeightInc(nextExcersizes.get(0).excersizeName), 0);
                }
            } else {
                nextExcerSetWeight(nextExcersizes, 45, 0);
                if (Excersize.uom.equals("kg")) {
                    nextExcerSetWeight(nextExcersizes, 20, 0);
                }
            }


            if (Excersize.uom.equals("lb")) {
                nextExcerSetWeight(nextExcersizes, 45, 1);
                nextExcerSetWeight(nextExcersizes, 95, 2);
            }

            if (Excersize.uom.equals("kg")) {
                nextExcerSetWeight(nextExcersizes, Math.round(nextExcersizes.get(1).excersizeWeight[0]), 1);
                nextExcerSetWeight(nextExcersizes, 40, 2);
            }

            return nextExcersizes;
        }
        List<String> sNextExcersizes = assExcer.getExcersizes((new AssignedExcers()).routineDescriber.get(assExcer.nextRoutineIdx(lastwork.routineIdx)));
        nextExcersizes.add(checkandIncWeight(sNextExcersizes.get(0),lastwork));
        nextExcersizes.add(checkandIncWeight(sNextExcersizes.get(1),secondlastwork));
        nextExcersizes.add(checkandIncWeight(sNextExcersizes.get(2),secondlastwork));

        // TODO: Check if weight should be decremented
        return nextExcersizes;
    }


    private static void nextExcerSetWeight(List<NextExcersize> nextExcer, double weight, int idx){
        nextExcer.get(idx).excersizeWeight = weights(weight, nextExcer.get(idx).excersizeWeight.length);
    }

    private static NextExcersize checkandIncWeight(String excerName, LastWorkout lastWorkout){
        List<Excersize> excersizes = lastWorkout.excersizesDone;

        Excersize curEx = findExcersize(excerName, excersizes);

        NextExcersize newEx;
        if (curEx.success()){
            newEx = new NextExcersize(curEx.excersizeName,  weights(curEx.curset.get(0).weight + getWeightInc(excerName), curEx.numOfSets));
        } else {
            newEx = new NextExcersize(curEx.excersizeName, weights(curEx.curset.get(0).weight, curEx.numOfSets));

        }
        return newEx;
    }

    private static Excersize findExcersize(String excersizeName, List<Excersize> excersizes){
        // Given list of excersizes, returns excersize with matching routName
        for (Excersize excer : excersizes) {
            if (excersizeName.equals(excer.excersizeName)) {
                return excer;
            }
        }
        return null;
    }

    private static List<NextExcersize> begExcer (List<String> excersizes){
        // quick function to initialize multiple excercise when user hasnt put a value before
        List<NextExcersize> lExcersizes = new ArrayList<>();

        AssignedExcers assexcer = new AssignedExcers();

        for (int i =0; i < excersizes.size(); i++) {
            NextExcersize curEx = new NextExcersize(excersizes.get(i), weights(0, assexcer.getReps(excersizes.get(i)).length));
            lExcersizes.add(curEx);
        }

        return lExcersizes;
    }

    private static double getWeightInc(String excersize){
        // TODO: Add more complex increments. IE. User doesnt have 5 lb weights.

        if (Excersize.uom == "lb") {
            if (excersize.equals("Deadlift")){
                return 2 * smallestWeightLb;
            }
            return smallestWeightLb;
        } else {
            // Must be kg
            if (excersize.equals("Deadlift")){
                return 2 * smallestWeightKg;
            }
            return smallestWeightKg;
        }
    }


}
