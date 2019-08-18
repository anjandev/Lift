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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ExternalStore {
    // Access org storage files

    private static final String TAG = Workout.class.getSimpleName();


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
        File path = new File(Environment.getExternalStorageDirectory()+ "/Lift");
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

    static public String makeExcersizeString(Excersize excersize){
        String post = "- " + excersize.excersizeName + "\n";

        for(int i =0; i < excersize.setsDone; i++) {
            post += "   - Rep " + i + " = " + excersize.curset[i].weight + excersize.uom + " done "
                    + excersize.curset[i].reps + " reps\n";
        }
        return post;
    }

    static public void getLastWorkoutProperties() {
        // TODO: Error checking if folder doesnt exist
        File path = new File(Environment.getExternalStorageDirectory() + "/Lift");
        File[] files = path.listFiles();
        Arrays.sort(files);

        File lastWorkoutFile = files[files.length-1];

        StringBuilder content = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(lastWorkoutFile));
            String curline;

            while ((curline = br.readLine()) != null) {
                content.append(curline);
                content.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //TODO: ADD error handling
        }
    }

}
