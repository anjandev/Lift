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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class Workout extends AppCompatActivity {
    // for debugging
    private static final String TAG = Workout.class.getSimpleName();

    Excersize[] listOfExcersizes;

    private  WorkTimer currentWorkTimer;

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

    private void createExcerUI(final Excersize excer, LinearLayout ll, AssignedExcers.NextExcersize weightMeta) {

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

        titleParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT,MARGIN_BOTTOM);

        header.addView(title, titleParams);


        EditText weight = new EditText(this);
        RelativeLayout.LayoutParams weightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        weight.setText(String.valueOf(weightMeta.excersizeWeight));
        weightParams.addRule(RelativeLayout.RIGHT_OF, title.getId());
        weightParams.addRule(RelativeLayout.ALIGN_TOP, title.getId());
        weightParams.addRule(RelativeLayout.ALIGN_BOTTOM, title.getId());

        weightParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT,MARGIN_BOTTOM);

        weight.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        header.addView(weight, weightParams);


        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        headerParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT,MARGIN_BOTTOM);

        ll.addView(header,headerParams);


        LinearLayout setsHolder = new LinearLayout(this);
        SeekBar setsUI = new SeekBar(this);

        RelativeLayout.LayoutParams setsParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        setsParams.addRule(RelativeLayout.BELOW, header.getId());
        setsParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT,MARGIN_BOTTOM);
        setsUI.setMax(excer.numOfSets);

        setsHolder.addView(setsUI,setsParams);
        ll.addView(setsHolder,setsParams);


        LinearLayout repsHolder = new LinearLayout(this);
        SeekBar repsUI = new SeekBar(this);
        RelativeLayout.LayoutParams repsParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        repsParams.addRule(RelativeLayout.BELOW, setsHolder.getId());
        repsParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT,MARGIN_BOTTOM);
        repsUI.setMax(excer.numOfReps[0]);
        repsUI.setProgress(excer.numOfReps[0]);


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


        repsHolder.addView(repsUI,repsParams);

        ll.addView(repsHolder);

        ll.addView(doneSet,doneSetParams);

        excer.setUI(setsUI, repsUI, weight, title,doneSet);
    }

    private void assignExcerAddUI(String[] slistOfExcersizes){
        listOfExcersizes = new Excersize[slistOfExcersizes.length];

        for(int i = 0; i < slistOfExcersizes.length; i++) {
            listOfExcersizes[i] = new Excersize(slistOfExcersizes[i]);
        }
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
        TextView bDate = (TextView) findViewById(R.id.date);
        bDate.setText(dateString);

        LinearLayout ll = (LinearLayout) findViewById(R.id.stuff);
        assignExcerAddUI(slistOfExcersizes);

        List<AssignedExcers.NextExcersize>  metaNext = new AssignedExcers(programName).nextRoutineWeights();



        for (int i =0; i < listOfExcersizes.length; i++) {
            createExcerUI(listOfExcersizes[i], ll, getNextExcersizeMeta(metaNext, listOfExcersizes[i].excersizeName));
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

    private AssignedExcers.NextExcersize getNextExcersizeMeta(List<AssignedExcers.NextExcersize> excersizes, String excerName){

        int finalI = 0;

        for (int i = 0; i < excersizes.size(); i++ ) {
            if(excerName.equals(excersizes.get(i).excersizeName)){
                finalI = i;
                break;
            }
        }
        return excersizes.get(finalI);
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
