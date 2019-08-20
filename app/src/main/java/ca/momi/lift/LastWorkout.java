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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LastWorkout {
    public String routineName;
    public List<Excersize> excersizesDone;
    public int routineIdx;
    public String program;


    private int getIdxBeg(String searchTxt, String sub){
        return searchTxt.indexOf(sub) + sub.length();
    }

    private List<Excersize> textToExcersizes(String text){
        String[] lines = text.split("\n");

        final int PROGRAM_LINE = 2;
        String programLine = lines[PROGRAM_LINE];
        program = programLine.substring(getIdxBeg(programLine, "- Program: "));

        final int FIRST_EXCERSIZE_LINE = 3;

        List<Excersize> excersizes = new ArrayList<>();

        Pattern pExcersizeNom = Pattern.compile("^- *");


        for(int i=FIRST_EXCERSIZE_LINE; i< lines.length; i++) {
            Matcher mExcersizeNom = pExcersizeNom.matcher(lines[i]);

            if (mExcersizeNom.find()) {
                excersizes.add(new Excersize(lines[i].substring(mExcersizeNom.end())));
            } else {
                Excersize curExcersize =  excersizes.get(excersizes.size()-1);

                String curLine = lines[i];

                float weight = Float.parseFloat(curLine.substring(getIdxBeg(curLine, "= "), curLine.indexOf(curExcersize.uom)));
                int reps = Integer.parseInt(curLine.substring(getIdxBeg(curLine, "done "), curLine.indexOf("reps") - " ".length()));

                curExcersize.doneSet(reps,weight);

                excersizes.set(excersizes.size()-1, curExcersize);
            }
        }

        return excersizes;

    }

    public LastWorkout(String sentRoutineName, String content) {
        // TODO: add error handling to check if no matches
        routineName = sentRoutineName;
        excersizesDone = textToExcersizes(content);

        String[] routines = new AssignedExcers(program).routineDescriber;

        for (int i = 0; i < routines.length; i++){
            if(routineName.equals(routines[i])) {
                routineIdx = i;
                break;
            }
        }
    }
}
