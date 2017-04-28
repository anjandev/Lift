package ca.momi.lift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.support.design.widget.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class Workout extends AppCompatActivity {

    public String[] workoutA = {"squat", "bench", "barbellRow"};

    public Map<String, Integer> mapIds(View view){
        // Create mapping of elements and ids
        Map<String, Integer> ids = new HashMap<String, Integer>();
        ids.put("squatSeekbar",R.id.squatSetSeekbar);
        return ids;
    }


    public void doneSet(View view, int workoutNum) {

        Map<String, Integer> ids = mapIds(view);

        int seekbarId = ids.get(workoutA[workoutNum] + "Seekbar");

        SeekBar numOfSets = (SeekBar)findViewById(seekbarId);

        if(numOfSets.getProgress() < numOfSets.getMax()){
            numOfSets.setProgress(numOfSets.getProgress() + 1);
            numOfSets.refreshDrawableState();
        }
        else{
            Snackbar.make(view, "You're done your five sets for " + workoutA[workoutNum], Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void doneSquat(View view) {
        doneSet(view, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouta);




    }
}
