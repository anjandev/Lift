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
    // for debugging
    private static final String TAG = Workout.class.getSimpleName();
    static public final int NUM_OF_WORKOUTS = 3;

    // Provided that the Map should be shared across every instance of your class, then you need to make it static. You then use a static initialiser block to instantiate and populate it:
    // https://stackoverflow.com/questions/33775273/how-to-declare-hashmap-in-global-and-add-values-into-it-only-for-the-first-time
    // The static initializer is a static {} block of code inside java class, and run only one time before the constructor or main method is called.
    // "Final" will not allow the variable to be changed
    static final String[] workoutA = {"squat", "bench", "barbellRow"};
    public static Map<String, Excersize> excersizes = new HashMap<String, Excersize>();

    // Mapping the ids of the ui elements is not independant of number of excersizes
    // TODO: write a more general algorithm for this
    private SeekBar work1;
    private SeekBar work2;
    private SeekBar work3;

    static {
        for(int i = 0; i < NUM_OF_WORKOUTS; i++) {
            excersizes.put(workoutA[i], new Excersize(workoutA[i]));
        }
    }

    // Mapping the ids of the ui elements is not independant of number of excersizes and names of
    // excersizes.
    // TODO: write a more general algorithm for this
    private Map<String, Integer> mapIds(View view){
        // Create mapping of elements and ids
        Map<String, Integer> ids = new HashMap<String, Integer>();
        ids.put("squatsetSeekbar",R.id.squatSetSeekbar);
        ids.put("squatWeight",R.id.squatWeight);
        ids.put("squatRepsSeekBar",R.id.squatRepsSeekBar);
        return ids;
    }

    private void doneSet(View view, int excersizeIdx) {
        /* TODO: change scope so that mapIds function doesnt run everytime done set.
           This is hard because the view must be passed into mapIds
         */
        Map<String, Integer> ids = mapIds(view);

        SeekBar numOfSets = (SeekBar)findViewById(ids.get(workoutA[excersizeIdx] + "setSeekbar"));
        SeekBar numOfReps = (SeekBar)findViewById(ids.get(workoutA[excersizeIdx] + "RepsSeekBar"));
        EditText weight = (EditText)findViewById(ids.get(workoutA[excersizeIdx] + "Weight"));

        Excersize excersize = excersizes.get(workoutA[excersizeIdx]);

        if(numOfSets.getProgress() == 0){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Add plate calculator
        // TODO: Use listener and don't allow user to go to sets they havent done yet
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouta);

    }
}
