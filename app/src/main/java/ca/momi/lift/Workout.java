package ca.momi.lift;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Workout extends AppCompatActivity {
    // for debugging
    private static final String TAG = Workout.class.getSimpleName();
    static public final int NUM_OF_WORKOUTS = 3;

    // Provided that the Map should be shared across every instance of your class, then you need to make it static. You then use a static initialiser block to instantiate and populate it:
    // https://stackoverflow.com/questions/33775273/how-to-declare-hashmap-in-global-and-add-values-into-it-only-for-the-first-time
    // The static initializer is a static {} block of code inside java class, and run only one time before the constructor or main method is called.
    // "Final" will not allow the variable to be changed
    static final String[] workoutA = {"squat", "bench", "barbellRow"};
    public static Map<String, Excersize> excersizes = new HashMap<String, Excersize>();
    public static Map<String, Integer> ids = new HashMap<String, Integer>();


    private void doneSet(View view, int excersizeIdx) {
        Excersize excersize = excersizes.get(workoutA[excersizeIdx]);

        SeekBar numOfSets = (SeekBar) findViewById(excersize.get_seekSets());
        SeekBar numOfReps = (SeekBar) findViewById(excersize.get_seekReps());
        EditText weight = (EditText) findViewById(excersize.get_weightUI());

        String sWeight = weight.getText().toString();

        if(sWeight.matches("")){
            Snackbar.make(view, "Please enter a weight before pressing done set", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if(numOfSets.getProgress() == 0){
            // set weight here too. Check if empty field
            excersize.doneSet(numOfSets.getProgress(), numOfReps.getProgress());
            numOfSets.setProgress(numOfSets.getProgress() + 1);
            numOfSets.refreshDrawableState();

        }
        else if(numOfSets.getProgress() < numOfSets.getMax()){
            excersize.doneSet(numOfSets.getProgress(), numOfReps.getProgress());
            numOfSets.setProgress(numOfSets.getProgress() + 1);
            numOfSets.refreshDrawableState();
            // TODO: Add timer
        }
        else{
            Snackbar.make(view, "You're done your five sets for " + workoutA[excersizeIdx], Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    // Mapping the ids of the ui elements is not independant of number of excersizes and names of
    // excersizes.
    // TODO: write a more general algorithm for this
    public void doneSquat(View view) {
        this.doneSet(view, 0);
    }

    public void doneBench(View view) {
        this.doneSet(view, 1);
    }

    public void donebarbellRow(View view) {
        this.doneSet(view, 2);
    }

    /* private void setSeekBarChangeListen(int excersizeIdx, SeekBar setSeekBar){

        final Excersize excersize = excersizes.get(workoutA[excersizeIdx]);
        final SeekBar numOfReps = (SeekBar)findViewById(ids.get(workoutA[excersizeIdx] + "RepsSeekBar"));

        setSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                int progress;
                if(progresValue > excersize.get_setsDone()) {
                    progress = excersize.get_setsDone();
                }
                else{
                    progress = progresValue;


                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
    }*/


    // Mapping the ids of the ui elements is not independant of number of excersizes and names of
    // excersizes.
    // TODO: write a more general algorithm for this
    private void mapIds(){
        // Create mapping of elements and ids
        // Map<String, Integer> ids = new HashMap<String, Integer>();
        this.ids.put("squatsetSeekbar",R.id.squatSetSeekbar);
        this.ids.put("squatWeight",R.id.squatWeight);
        this.ids.put("squatRepsSeekBar",R.id.squatRepsSeekBar);

        this.ids.put("benchsetSeekbar",R.id.benchSetSeekbar);
        this.ids.put("benchWeight",R.id.benchWeight);
        this.ids.put("benchRepsSeekBar",R.id.benchRepsSeekBar);

        this.ids.put("barbellRowsetSeekbar",R.id.barbellRowSetSeekBar);
        this.ids.put("barbellRowWeight",R.id.barbellRowWeight);
        this.ids.put("barbellRowRepsSeekBar",R.id.barbellRowRepsSeekBar);
    }




    public void checkStoragePermissionAndWrite(Activity thisActivity, String fileName, String text) {

        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Log.d(TAG, "Error: external storage is unavailable");
            return;
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "Error: external storage is read only.");
            return;
        }
        Log.d(TAG, "External storage is not read only or unavailable");



        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


            try {
                ExternalStore.writeTextToExtStorage(fileName, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Permission has already been granted

            try {
                ExternalStore.writeTextToExtStorage(fileName, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Add plate calculator
        // TODO: Use listener and don't allow user to go to sets they havent done yet
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouta);

        mapIds();

        for(int i = 0; i < NUM_OF_WORKOUTS; i++) {
            excersizes.put(workoutA[i], new Excersize(workoutA[i], ids.get(workoutA[i] + "setSeekbar"), ids.get(workoutA[i] + "RepsSeekBar"), ids.get(workoutA[i] + "Weight")));
        }

        long day = getIntent().getLongExtra("day", 0);
        long month = getIntent().getLongExtra("month", 0);
        long year = getIntent().getLongExtra("year", 0);

        final String dateString = year + "-" + month + "-" + day;

        Button doneWork = (Button) findViewById(R.id.doneWork);

        TextView bDate = (TextView) findViewById(R.id.date);

        bDate.setText(dateString);

        doneWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String workoutSessionText = "* Workout\n"
                                          + "SCHEDULED: <" + dateString + ">\n";
                for(int i = 0; i < NUM_OF_WORKOUTS; i++) {
                    // TODO: To generalize. Change workoutA to something else
                    workoutSessionText += ExternalStore.makeExcersizeString(excersizes.get(workoutA[i]));
                }
                checkStoragePermissionAndWrite((Activity) v.getContext(),  dateString, workoutSessionText);
            }
        });
    }
}
