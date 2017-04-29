package ca.momi.lift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Workout extends AppCompatActivity {
    private static final String TAG = Workout.class.getSimpleName();


    // The static initializer is a static {} block of code inside java class, and run only one time before the constructor or main method is called.
    // "Final" will not allow the variable to be changed
    static public final String[] workoutA = {"squat", "bench", "barbellRow"};
    static public final int NUM_OF_WORKOUTS = 3;

    // Provided that the Map should be shared across every instance of your class, then you need to make it static. You then use a static initialiser block to instantiate and populate it:
    // https://stackoverflow.com/questions/33775273/how-to-declare-hashmap-in-global-and-add-values-into-it-only-for-the-first-time

    private Map<String, Excersize> createMap() {
        Map<String, Excersize> excersizes = new HashMap<String, Excersize>();
        excersizes.put("squat", new Excersize("squat"));
        return excersizes;
    }


    private Map<String, Integer> mapIds(View view){
        // Create mapping of elements and ids
        Map<String, Integer> ids = new HashMap<String, Integer>();
        ids.put("squatsetSeekbar",R.id.squatSetSeekbar);
        ids.put("squatWeight",R.id.squatWeight);
        ids.put("squatRepsSeekBar",R.id.squatRepsSeekBar);
        return ids;
    }

    public void doneSet(View view, int workoutNum) {
        /* TODO: change scope so that mapIds function doesnt run everytime done set.
           This is hard because the view must be passed into mapIds
         */


        Map<String, Integer> ids = mapIds(view);
        // Map<String, Excersize> excersizes = createMap();

        SeekBar numOfSets = (SeekBar)findViewById(ids.get(workoutA[workoutNum] + "setSeekbar"));
        SeekBar numOfReps = (SeekBar)findViewById(ids.get(workoutA[workoutNum] + "RepsSeekBar"));
        // EditText weight = (EditText)findViewById(ids.get(workoutA[workoutNum] + "Weight"));
        


        //Excersize excersize = excersizes.get(workoutA[workoutNum]);

        if(numOfSets.getProgress() == 0){
            // set weight here too. Check if empty field
            //excersize.doneSet(numOfSets.getProgress(), numOfReps.getProgress());
            numOfSets.setProgress(numOfSets.getProgress() + 1);
            numOfSets.refreshDrawableState();

        }
        else if(numOfSets.getProgress() < numOfSets.getMax()){
            //excersize.doneSet(numOfSets.getProgress(), numOfReps.getProgress());
            numOfSets.setProgress(numOfSets.getProgress() + 1);
            numOfSets.refreshDrawableState();
            // TODO: Add timer
        }
        else{
            Snackbar.make(view, "You're done your five sets for " + workoutA[workoutNum], Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void doneSquat(View view) {
        this.doneSet(view, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Add plate calculator
        // TODO: Use listener and don't allow user to go to sets they havent done yet
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouta);
    }
}
