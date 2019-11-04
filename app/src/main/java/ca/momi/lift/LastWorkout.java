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
    public boolean onPause;
    public static final String onPausetxt = "On Pause";


    private int getIdxBeg(String searchTxt, String sub){
        return searchTxt.indexOf(sub) + sub.length();
    }

    private int lengthTillComments(String[] lines){
        for (int i = 0; i< lines.length; i++){
            if (lines[i].equals("---")) {
                return i + 1;
            }
        }
        return lines.length;
    }

    private List<Excersize> textToExcersizes(String text){
        String[] lines = text.split("\n");

        final int PROGRAM_LINE = 2;
        String programLine = lines[PROGRAM_LINE];
        program = programLine.substring(getIdxBeg(programLine, "- Program: "));

        final int FIRST_EXCERSIZE_LINE = 3;

        List<Excersize> excersizes = new ArrayList<>();

        Pattern pExcersizeNom = Pattern.compile("^- *");
        String curExcersizeLine = lines[FIRST_EXCERSIZE_LINE];

        List<Set> curSets = new ArrayList<Set>();

        for(int i=FIRST_EXCERSIZE_LINE+1; i< lengthTillComments(lines); i++) {
            Matcher mExcersizeNom = pExcersizeNom.matcher(lines[i]);
            if (mExcersizeNom.find()) {

                excersizes = setSetsDoneAndAddExcer(excersizes,curSets,curExcersizeLine);

                curSets.clear();
                curExcersizeLine = lines[i];
                continue;
            }

            String curLine = lines[i];

            double weight = Float.parseFloat(curLine.substring(getIdxBeg(curLine, "= "), curLine.indexOf(Excersize.uom)));
            int reps = Integer.parseInt(curLine.substring(getIdxBeg(curLine, "done "), curLine.indexOf("reps") - " ".length()));
            curSets.add(new Set(reps, weight));
        }

        if (!curExcersizeLine.equals("---")) {
            excersizes = setSetsDoneAndAddExcer(excersizes, curSets, curExcersizeLine);
        }

        return excersizes;

    }


    private List<Excersize> setSetsDoneAndAddExcer(List<Excersize> excersizes, List<Set> curSets, String curExcersizeLine) {
        String curExcersizeName=  curExcersizeLine.substring(getIdxBeg(curExcersizeLine, "- "), curExcersizeLine.indexOf(":"));
        excersizes.add(new Excersize(curExcersizeName, setsToWeights(curSets)));

        Excersize latestExcer = excersizes.get(excersizes.size()-1);

        Pattern failed = Pattern.compile("FAILED");

        Matcher mFailed = failed.matcher(curExcersizeLine);
        int setsDone = latestExcer.setsToDo.size();
        if (mFailed.find()){
            int begIdx = getIdxBeg(curExcersizeLine, "attempt ");
            setsDone = latestExcer.setsToDo.size() - Integer.parseInt(curExcersizeLine.substring(begIdx, curExcersizeLine.indexOf(" sets")));
        }

        for (int iDone = 0; iDone < setsDone; iDone++){
            latestExcer.doneSet(curSets.get(iDone).reps, curSets.get(iDone).weight);
        }

        excersizes.set(excersizes.size()-1, latestExcer);


        return excersizes;
    }

    private double[] setsToWeights(List<Set> sets){
        double[] A = new double[sets.size()];
        for (int i = 0; i < sets.size(); i++){
            A[i] = sets.get(i).weight;
        }
        return A;
    }


    public LastWorkout(String sentRoutineName, String content) {
        // TODO: add error handling to check if no matches
        routineName = sentRoutineName;
        excersizesDone = textToExcersizes(content);

        String[] lines = content.split("\n");
        onPause = lines[lines.length-1].contentEquals(onPausetxt);

        List<String> routines = new AssignedExcers().routineDescriber;

        for (int i = 0; i < routines.size(); i++){
            if(routineName.equals(routines.get(i))) {
                routineIdx = i;
                break;
            }
        }
    }
}
