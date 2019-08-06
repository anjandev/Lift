package ca.momi.lift;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.Manifest.permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        if (!path.mkdirs()) {
            Log.e(TAG, "Directory not created");
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

    public static String makeExcersizeString(Excersize excersize){
        return excersize.excersizeName + "\n"+
                excersize.weight + "\n" +
                excersize.sets+ "\n";
    }


}
