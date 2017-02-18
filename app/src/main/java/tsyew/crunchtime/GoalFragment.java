package tsyew.crunchtime;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
// Code was adapted from http://www.truiton.com/2015/06/android-tabs-example-fragments-viewpager/
public class GoalFragment extends Fragment {
    // Reference hashmap for exercises (amt/time to burn 100 cal)
    private HashMap<String, Integer> exerciseMap;
    // user enters # of calories he wants to burn
    private EditText mCalories;
    // press this button to calculate necessary exercises
    private Button mCalculateButton;
    // text view shows all equivalent exercises and quantities
    private TextView mExercises;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Update reference hashmap for exercises based on reps (amt to burn 100 cal)
        exerciseMap = new HashMap<String, Integer>();
        exerciseMap.put("Pushup", 350);
        exerciseMap.put("Situp", 200);
        exerciseMap.put("Squats", 225);
        exerciseMap.put("Pullup", 100);
        // Update reference hashmap for exercises based on time (amt to burn 100 cal)
        exerciseMap.put("Leg-lift", 25);
        exerciseMap.put("Plank", 25);
        exerciseMap.put("Jumping Jacks", 10);
        exerciseMap.put("Cycling", 12);
        exerciseMap.put("Walking", 20);
        exerciseMap.put("Jogging", 12);
        exerciseMap.put("Swimming", 13);
        exerciseMap.put("Stair-Climbing", 15);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.goal_fragment, container, false);
        // Initialize UI elements
        mCalories = (EditText) view.findViewById(R.id.calories);
        mCalculateButton = (Button) view.findViewById(R.id.calculateButton);
        mExercises = (TextView) view.findViewById(R.id.exercises);

        // make list scrollable to account for horizontal orientation
        mExercises.setMovementMethod(new ScrollingMovementMethod());

        // Calculate button listener
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make the soft keyboard retract
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                try {
                    // get entered value (reps or minutes)
                    String tmp = mCalories.getText().toString();
                    float goal = Float.parseFloat(tmp);

                    // now show what exercises should be done
                    String exerciseList = "To burn "+String.valueOf(goal)+" calories, you could:\r\n";
                    for (HashMap.Entry<String,Integer> entry: exerciseMap.entrySet()){
                        String exercise = entry.getKey();
                        int val = entry.getValue();
                        int equiv = Math.round(goal/100*val);
                        String line = "";
                        if (exercise.matches("Pushup")){
                            line = "* Do "+String.valueOf(equiv)+" pushups.";
                        } else if (exercise.matches("Situp")){
                            line = "* Do "+String.valueOf(equiv)+" situps.";
                        } else if (exercise.matches("Squats")){
                            line = "* Do "+String.valueOf(equiv)+" squats.";
                        } else if (exercise.matches("Pullup")){
                            line = "* Do "+String.valueOf(equiv)+" pullups.";
                        } else if (exercise.matches("Leg-lift")){
                            line = "* Do a leg-lift for "+String.valueOf(equiv)+" minutes.";
                        } else if (exercise.matches("Plank")){
                            line = "* Plank for "+String.valueOf(equiv)+" minutes.";
                        } else if (exercise.matches("Jumping Jacks")){
                            line = "* Do jumping jacks for "+String.valueOf(equiv)+" minutes.";
                        } else if (exercise.matches("Cycling")){
                            line = "* Cycle for "+String.valueOf(equiv)+" minutes.";
                        } else if (exercise.matches("Walking")){
                            line = "* Walk for "+String.valueOf(equiv)+" minutes.";
                        } else if (exercise.matches("Jogging")){
                            line = "* Jog for "+String.valueOf(equiv)+" minutes.";
                        } else if (exercise.matches("Swimming")){
                            line = "* Swim for "+String.valueOf(equiv)+" minutes.";
                        } else if (exercise.matches("Stair-Climbing")){
                            line = "* Climb the stairs for "+String.valueOf(equiv)+" minutes.";
                        }
                        exerciseList += line + "\r\n";
                    }
                    mExercises.setText(exerciseList);
                } catch (Exception e) {
                    // in case the user entered nothing or an invalid value like "2."
                    Toast.makeText(getActivity().getBaseContext(),
                            "Invalid Input!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return view;
    }


}