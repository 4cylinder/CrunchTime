package tsyew.crunchtime;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
// Code was adapted from http://www.truiton.com/2015/06/android-tabs-example-fragments-viewpager/
public class AchievementFragment extends Fragment {
    // Reference hashmap for exercises based on reps (amt to burn 100 cal)
    private HashMap<String, Integer> exerciseMap;
    // drop down list to select the specific exercise
    private Spinner mExerciseList;
    // user enters # of reps (or amount of time)
    private EditText mQuantity;
    // caption that dynamically switches between "reps" and "minutes"
    private TextView mUnits;
    // press this button to calculate the equivalents
    private Button mCalculateButton;
    // this big textview shows the calories burned and the list of equivalent exercises
    private TextView mResults;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.achievement_fragment, container, false);

        // initialize UI elements
        mExerciseList = (Spinner) view.findViewById(R.id.exerciseList);
        mQuantity = (EditText) view.findViewById(R.id.quantity);
        mUnits = (TextView) view.findViewById(R.id.units);
        mCalculateButton = (Button) view.findViewById(R.id.calculateButton);
        mResults = (TextView) view.findViewById(R.id.results);

        // make results scrollable to account for horizontal view
        mResults.setMovementMethod(new ScrollingMovementMethod());

        // populate drop down list (spinner)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.exercises_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when rendering the list of exercises
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the exercise list (spinner)
        mExerciseList.setAdapter(adapter);

        // make drop down list affect the units (switch between "reps" and "minutes")
        mExerciseList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String exercise = mExerciseList.getSelectedItem().toString();
                Log.d("AchievementFragment", "Selected exercise is "+exercise);
                if (exercise.matches("Pushup") || exercise.matches("Situp") || exercise.matches("Squats")
                        || exercise.matches("Pullup")){
                    mUnits.setText("reps of ");
                } else {
                    mUnits.setText("minutes of ");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // Calculate button listener
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make the soft keyboard retract
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                try {
                    // get entered value (reps or minutes)
                    String tmp = mQuantity.getText().toString();
                    float qty = Float.parseFloat(tmp);
                    String selected = mExerciseList.getSelectedItem().toString();
                    float burned = 0;
                    if (exerciseMap.get(selected)!=null) {
                        burned = qty/exerciseMap.get(selected)*100;
                    }

                    // now show result + equivalent exercises
                    String results = "You burned "+String.valueOf(Math.round(burned))+" calories!\r\n";
                    results += "You could also:\r\n";
                    for (HashMap.Entry<String,Integer> entry: exerciseMap.entrySet()){
                        if (!selected.matches(entry.getKey())) {
                            String exercise = entry.getKey();
                            int val = entry.getValue();
                            int equiv = Math.round(burned/100*val);
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
                            results += line + "\r\n";
                        }
                    }
                    mResults.setText(results);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}