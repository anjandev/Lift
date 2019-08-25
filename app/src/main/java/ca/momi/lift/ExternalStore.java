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

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ExternalStore {
    // Access org storage files

    private static final String TAG = Workout.class.getSimpleName();

    static final String LIFT_FOLDER = "/Lift";
    static final String SUCESS_STRING = "SUCCEEDED";
    static final String FAIL_STRING = "FAILED";

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static void writeTextToExtStorage(String fileName, String text) throws IOException {
        File path = new File(Environment.getExternalStorageDirectory() + LIFT_FOLDER + "/" + MainActivity.program);
        if (!path.isDirectory()) {
            if (!path.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }
        }

        File file = new File(path, fileName + ".txt");

        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
    }

    static public String makeExcersizeString(Excersize excersize) {

        String successOrFail;
        String didNotAttempt;

        if (excersize.success()){
            successOrFail = SUCESS_STRING;
            didNotAttempt= "";
        } else {
            successOrFail = FAIL_STRING;
            didNotAttempt= " did not attempt " + (excersize.setsToDo.size() - excersize.setsDone) + " sets";
        }

        String post = "- " + excersize.excersizeName + ": " + successOrFail +  didNotAttempt +"\n";

        for (int i = 0; i < excersize.setsToDo.size(); i++) {
            post += "   - Set " + i + " = " + excersize.curset.get(i).weight + excersize.uom + " done "
                    + excersize.curset.get(i).reps + " reps\n";
        }
        return post;
    }

    static public int getNumLastWorkoutFiles() {
        File path = new File(Environment.getExternalStorageDirectory() + LIFT_FOLDER + "/" + MainActivity.program);
        File[] files = path.listFiles();
        if (files == null) {
            return 0;
        }
        return files.length;
    }


    static public LastWorkout getLastidxProperties(int lastNum) {
        // Gets last workout number properties. If lastNum = 0, it's the latest saved workout.
        // TODO: Error checking if folder doesnt exist
        File path = new File(Environment.getExternalStorageDirectory() + LIFT_FOLDER + "/" + MainActivity.program);
        File[] files = path.listFiles();
        if(files ==null) {
            return null;
        }

        if(files.length ==0) {
            return null;
        }

        Arrays.sort(files);

        if(files.length-1-lastNum< 0) {
            return null;
        }

        File lastWorkoutFile = files[files.length - 1 - lastNum];

        return getFileProp(lastWorkoutFile);
    }


    static public LastWorkout getPropFromFile(String filename){

        File file = new File(Environment.getExternalStorageDirectory() + LIFT_FOLDER + "/" + MainActivity.program + "/" + filename);

        if (!file.exists()) {
            return null;
        }

        return getFileProp(file);

    }

    static public LastWorkout getFileProp(File file) {


        StringBuilder content = new StringBuilder();

        String firstLine = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String curline;

            while ((curline = br.readLine()) != null) {
                if(firstLine == null) {
                    firstLine = curline;
                }

                content.append(curline);
                content.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //TODO: ADD error handling
        }


        // TODO: This might be bug when user defines workouts. Tell user they can only define workouts with certain syntax
        LastWorkout lastwork = new LastWorkout(firstLine.substring(2), content.toString());
        if (!lastwork.program.equals(MainActivity.program)) {
            return null;
        }
        return lastwork;

    }

}
