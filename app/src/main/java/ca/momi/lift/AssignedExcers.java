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

public class AssignedExcers {
    // This file holds all the logic on what to do with a workout's failure, how to increase weights, routines etc.

    public String name;
    public String program;

    public String[] excersizes = new String[3];
    public String[] routineDescriber = new String[2];

    public int workoutOptionsnum;

    public int nextRoutineIdx(int curRoutine) {

        if (curRoutine + 1 >= workoutOptionsnum) {
            return 0;
        }
        else {
            return curRoutine +1;
        }

    }

    public String[] getExcersizes(String sentName){
        this.name = sentName;

        switch(name) {
            case "Bench Press, Barbell Row":
                excersizes[0] = "Squat";
                excersizes[1] = "Bench Press";
                excersizes[2] = "Bent-over Row";
                break;
            case "Overhead Press, Deadlift":
                excersizes[0] = "Squat";
                excersizes[1] = "Overhead Press";
                excersizes[2] = "Deadlift";
                break;
            case "Day 1":
                excersizes[0] = "Squat";
                excersizes[1] = "Bench Press";
                excersizes[2] = "Bent-over Row";
                break;
            case "Day 2":
                excersizes[0] = "Squat";
                excersizes[1] = "Overhead Press";
                excersizes[2] = "Deadlift";
                break;
            case "Day 3":
                excersizes[0] = "Squat";
                excersizes[1] = "Bench Press";
                excersizes[2] = "Bent-over Row";
                break;
        }
        return excersizes;
    }


    public AssignedExcers(String program) {
        this.program = program;
        if(program.equals("5x5")) {
            workoutOptionsnum = 2;
            routineDescriber[0] = "Bench Press, Barbell Row";
            routineDescriber[1] = "Overhead Press, Deadlift";
        } else if (program.equals("madcow")) {
            workoutOptionsnum = 3;
            String[] newRoutineDescriber = new String[3];
            newRoutineDescriber[0] = "Day 1";
            newRoutineDescriber[1] = "Day 2";
            newRoutineDescriber[2] = "Day 3";
            routineDescriber = newRoutineDescriber;
        }
    }
}
