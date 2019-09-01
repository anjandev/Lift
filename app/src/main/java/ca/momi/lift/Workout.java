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
    Excersize[] listOfExcersizes;

    private void didSet(View view, Excersize excersize) {

        SeekBar numOfSets = excersize.seekSets;
        SeekBar numOfReps = excersize.seekReps;
        EditText weight = excersize.weightUI;

        String sWeight = weight.getText().toString();

        if(sWeight.matches("")){
            Snackbar.make(view, "Please enter a weight before pressing done Set", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

        String[] slistOfExcersizes = getIntent().getStringArrayExtra("Excersizes");
        final String routineName = getIntent().getStringExtra("RoutineName");

        final String programName = getIntent().getStringExtra("program");

        long day = getIntent().getLongExtra("day", 0);
        long month = getIntent().getLongExtra("month", 0);
        long year = getIntent().getLongExtra("year", 0);

        final String dateString = year + "-" + appendZero(String.valueOf(month)) + "-" + appendZero(String.valueOf(day));
        TextView bDate = findViewById(R.id.date);
        bDate.setText(dateString);

        LinearLayout ll = findViewById(R.id.stuff);
        LastWorkout todaysWorkout = ExternalStore.getPropFromFile(dateString + ".txt");
        listOfExcersizes = new Excersize[slistOfExcersizes.length];
        List<NextExcersize>  metaNext = new AssignedExcers().nextRoutineWeightsCheck(slistOfExcersizes, this.getBaseContext());

        if (todaysWorkout != null) {
            // Resume today's workout
            for (int i = 0; i < slistOfExcersizes.length; i++) {
                listOfExcersizes[i] = todaysWorkout.excersizesDone.get(i);
            }
        } else {
            for (int i = 0; i < slistOfExcersizes.length; i++) {
                Excersize excer = new Excersize(slistOfExcersizes[i], (metaNext.get(i).excersizeWeight));
                listOfExcersizes[i] = (excer);

            }
        }

        for (int i =0; i < slistOfExcersizes.length; i++) {
            createExcerUI(listOfExcersizes[i], ll);
        }

        Button doneWork = new Button(this);
        doneWork.setText("Done Workout");
        ll.addView(doneWork);


         doneWork.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String workoutSessionText = "* " + routineName + "\n"
                                           + "SCHEDULED: <" + dateString + ">\n";

                 workoutSessionText += "- Program: " + programName + "\n";
                 for(int i = 0; i < listOfExcersizes.length; i++) {
                     workoutSessionText += ExternalStore.makeExcersizeString(listOfExcersizes[i]);
                 }
                 checkStoragePermissionAndWrite((Activity) v.getContext(),  dateString, workoutSessionText);
             }
         });
    }


    private String appendZero(String original){
        if(original.length() == 1){
            return "0" + original;
        }
        else {
            return original;
        }
    }
}
