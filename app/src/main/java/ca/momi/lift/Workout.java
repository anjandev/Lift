package ca.momi.lift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
    public static Map<String, Integer> ids = new HashMap<String, Integer>();


    private void doneSet(View view, int excersizeIdx) {
        Excersize excersize = excersizes.get(workoutA[excersizeIdx]);

        SeekBar numOfSets = (SeekBar) findViewById(excersize.get_seekSets());
        SeekBar numOfReps = (SeekBar) findViewById(excersize.get_seekReps());
        EditText weight = (EditText) findViewById(excersize.get_weightUI());

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

    }
}
