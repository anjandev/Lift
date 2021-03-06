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

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class Workout extends AppCompatActivity {
    // for debugging
    private static final String TAG = Workout.class.getSimpleName();

    private  WorkTimer currentWorkTimer;
    Excersize[] lexcersizes;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_SD = 1;

    private Button doneWork;

    private boolean pausing = false;

    protected void onResume() {
        super.onResume();
        pausing = false;
    }

    protected void onPause() {
        super.onPause();
        pausing = true;
        doneWork.performClick();
    }

    private void didSet(View view, Excersize excersize) {

        SeekBar numOfSets = excersize.seekSets;
        SeekBar numOfReps = excersize.seekReps;
        EditText weight = excersize.weightUI;

        String sWeight = weight.getText().toString();

        if(sWeight.equals("0.0") | sWeight.isEmpty()){
            Snackbar.make(view, "Please enter a weight before pressing done set." +
                    " Remember weight of bar or your weight.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else if(numOfSets.getProgress() < numOfSets.getMax()){
            excersize.doneSet(numOfReps.getProgress(), Float.parseFloat(weight.getText().toString()));
            numOfSets.setProgress(numOfSets.getProgress() + 1);
            numOfSets.refreshDrawableState();

            final View finalView =view;
            WorkTimer work = new WorkTimer(finalView, currentWorkTimer);

            currentWorkTimer = work;
        }

        else{
            String sets;
            if (excersize.numOfSets == 1){
                sets = "set";
            } else{
                sets = "sets";

            }
            Snackbar.make(view, "You're done your " + excersize.numOfSets + " "+ sets + " for " + excersize.excersizeName, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public boolean checkStoragePermissionAndWrite(Activity thisActivity, String fileName, String text) {

        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Log.d(TAG, "Error: external storage is unavailable");
            return false;
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "Error: external storage is read only.");
            return false;
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
                        MY_PERMISSIONS_REQUEST_WRITE_SD);

            }


            try {
                ExternalStore.writeTextToExtStorage(fileName, text);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Permission has already been granted

            try {
                ExternalStore.writeTextToExtStorage(fileName, text);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    private void createExcerUI(final Excersize excer, LinearLayout ll) {

        final int MARGIN_TOP = 8;
        final int MARGIN_LEFT = 8;
        final int MARGIN_BOTTOM = 0;
        final int MARGIN_RIGHT = 0;

        LinearLayout header = new LinearLayout(this);


        TextView title = new TextView(this);
        title.setText(excer.excersizeName);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.BELOW, R.id.date);
        title.setIncludeFontPadding(true);

        titleParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);

        header.addView(title, titleParams);


        EditText weight = new EditText(this);
        RelativeLayout.LayoutParams weightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        weight.setText(String.valueOf(excer.setsToDo.get(excer.setsDone).weight));
        weightParams.addRule(RelativeLayout.RIGHT_OF, title.getId());
        weightParams.addRule(RelativeLayout.ALIGN_TOP, title.getId());
        weightParams.addRule(RelativeLayout.ALIGN_BOTTOM, title.getId());

        weightParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);

        weight.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        header.addView(weight, weightParams);


        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        headerParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);

        ll.addView(header, headerParams);


        LinearLayout setsHolder = new LinearLayout(this);
        SeekBar setsUI = new SeekBar(this);

        setsUI.setEnabled(false);

        RelativeLayout.LayoutParams setsTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        setsTextParams.addRule(RelativeLayout.BELOW, header.getId());
        setsTextParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);

        RelativeLayout.LayoutParams setsSlideParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        setsSlideParams.addRule(RelativeLayout.BELOW, setsHolder.getId());
        setsSlideParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);

        setsUI.setMax(excer.numOfSets);
        setsUI.setProgress(excer.setsDone);

        TextView setsNum = new TextView(this);
        setsNum.setText(String.valueOf(excer.setsDone));

        final TextView finalSetsNum = setsNum;
        setsUI.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                finalSetsNum.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        TextView setsLabel = new TextView(this);
        setsLabel.setText("Sets Done");

        setsHolder.addView(setsLabel, setsTextParams);
        setsHolder.addView(setsNum, setsTextParams);
        setsHolder.addView(setsUI, setsSlideParams);
        ll.addView(setsHolder);

        final TextView repsNum = new TextView(this);
        repsNum.setText(String.valueOf(excer.setsToDo.get(excer.setsDone).reps));

        LinearLayout repsHolder = new LinearLayout(this);
        final SeekBar repsUI = new SeekBar(this);
        RelativeLayout.LayoutParams repsLabelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        repsLabelParams.addRule(RelativeLayout.BELOW, setsHolder.getId());
        repsLabelParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);

        final RelativeLayout.LayoutParams repsSlideParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        repsSlideParams.addRule(RelativeLayout.BELOW, setsHolder.getId());
        repsSlideParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);

        repsUI.setMax(excer.setsToDo.get(excer.setsDone).reps);
        repsUI.setProgress(excer.setsToDo.get(excer.setsDone).reps);

        repsUI.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                repsNum.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });



        Button doneSet = new Button(this);
        RelativeLayout.LayoutParams doneSetParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        doneSetParams.addRule(RelativeLayout.RIGHT_OF, repsUI.getId());
        doneSetParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT,MARGIN_BOTTOM);

        doneSet.setText("Done Set");
        doneSet.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  didSet(v, excer);
              }
        });


        Button AMRAP = new Button(this);
        RelativeLayout.LayoutParams AMRAPParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        AMRAP.setText("As many reps as possible set: Add +1");
        AMRAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repsUI.setMax(repsUI.getMax()+1);
                repsUI.setProgress(repsUI.getMax()+1);
            }
        });

        if (excer.curset.get(0).AMRAP) {
            AMRAP.setVisibility(View.VISIBLE);
        } else {
            AMRAP.setVisibility(View.INVISIBLE);
        }

        LinearLayout doneSetHolder = new LinearLayout(this);

        TextView repsLabel = new TextView(this);
        repsLabel.setText("Reps Done");


        repsHolder.addView(repsLabel,repsLabelParams);
        repsHolder.addView(repsNum,repsLabelParams);
        repsHolder.addView(repsUI,repsSlideParams);

        ll.addView(repsHolder);

        doneSetHolder.addView(doneSet, doneSetParams);
        doneSetHolder.addView(AMRAP, AMRAPParams);

        ll.addView(doneSetHolder);

        excer.setUI(setsUI, repsUI, weight, title,doneSet, repsNum, AMRAP);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Add plate calculator
        // TODO: Use listener and don't allow user to go to sets they havent done yet
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouta);

        String[] slExcersizes = getIntent().getStringArrayExtra("Excersizes");
        final String routineName = getIntent().getStringExtra("RoutineName");

        final String programName = getIntent().getStringExtra("program");

        final String dateString = getDateString();

        TextView bDate = findViewById(R.id.date);
        bDate.setText(dateString);

        LinearLayout ll = findViewById(R.id.stuff);
        lexcersizes = new Excersize[slExcersizes.length];

        LastWorkout todaysWorkout = ExternalStore.getLastidxProp(0);

        boolean lastWorkoutOnPause;

        if (todaysWorkout == null ) {
            lastWorkoutOnPause = false;
        } else {
            lastWorkoutOnPause = todaysWorkout.onPause;
        }

        // Resume today's workout
        if (lastWorkoutOnPause) {
            for (int i = 0; i < slExcersizes.length; i++) {
                lexcersizes[i] = todaysWorkout.excersizesDone.get(i);
            }
        } else {
            List<NextExcersize>  metaNext = new AssignedExcers().nextRoutineWeightsCheck(slExcersizes, this.getBaseContext());
            for (int i = 0; i < slExcersizes.length; i++) {
                lexcersizes[i] = new Excersize(slExcersizes[i], metaNext.get(i).excersizeWeight);
            }
        }

        for (int i =0; i < slExcersizes.length; i++) {
            createExcerUI(lexcersizes[i], ll);
        }

        final Button doneWork = new Button(this);
        doneWork.setText("Done Workout");
        ll.addView(doneWork);

        doneWork.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                writeData(routineName, dateString, programName, pausing, v);
             }
        });

        this.doneWork = doneWork;
        pausing = false;
    }

    private void writeData(String routineName, String dateString, String programName, boolean onPause, View v) {
        // TODO: split into multiple functions
        String workoutSessionText = "* " + routineName + "\n"
                + "SCHEDULED: <" + dateString + ">\n";

        workoutSessionText += "- Program: " + programName + "\n";
        for(int i = 0; i < lexcersizes.length; i++) {
            workoutSessionText += ExternalStore.makeExcersizeString(lexcersizes[i]);
        }


        workoutSessionText += getResources().getString(R.string.start_comment);
        workoutSessionText += AssignedExcers.getComment(v.getContext());
        // OnPause text is always the last word. Otherwise parser doesnt work
        if (onPause) {
            workoutSessionText += LastWorkout.onPausetxt;
        }

        if (currentWorkTimer != null & !onPause) {
            currentWorkTimer.timer.cancel();
        }

        if (checkStoragePermissionAndWrite((Activity) v.getContext(),  dateString, workoutSessionText)){
            if (!onPause) {
                System.exit(0);
            }
        } else {
            Log.d(TAG, "writeData: failed. Try again");
        }
    }


    private String appendZero(String original){
        if(original.length() == 1){
            return "0" + original;
        }
        else {
            return original;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_SD:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doneWork.performClick();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request.

    }

    private String getDateString() {

        long day = getIntent().getLongExtra("day", 0);
        long month = getIntent().getLongExtra("month", 0);
        long year = getIntent().getLongExtra("year", 0);

        return year + "-" + appendZero(String.valueOf(month)) + "-" + appendZero(String.valueOf(day));
    }
}
