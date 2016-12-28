package ch.ti8m.android.calculator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import ch.ti8m.android.calculator.util.Checker;

/**
 * Application activity.
 *
 * @author Cosimo Damiano Prete
 * @since 26/12/2016
 */
public class CalculatorActivity extends AppCompatActivity {
    private static final String LOG_TAG = CalculatorActivity.class.getCanonicalName();

    /**
     * Called when the activity is created.
     * @param savedInstanceState dictionary containing eventually saved values.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflates the layout, so the view
        //For further information: https://developer.android.com/guide/topics/resources/accessing-resources.html
        setContentView(R.layout.activity_calculator);

        //Click listener to use with the buttons
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Prints out the ID's name of the pressed button we gave in the .XML layout file.
                //Example: if the ID of the button '0' is '0464664687568'... we will get 'button0'.
                //For further information see the link above.
                Log.i(LOG_TAG, getResources().getResourceEntryName(v.getId()));
            }
        };

        //Retrieves the root view from the layout using the view's ID
        final View rootView = findViewById(R.id.rootView);

        //Initializes the buttons inside the root views
        initButtons(rootView, onClickListener);
    }

    private void initButtons(@NonNull View root,
                             @Nullable View.OnClickListener onClickListener) {
        //Just to make the code more robust.
        //Theoretically you should know how to invoke your private methods.
        Checker.notNull(root, "Root View");

        //Recovers all the touchable elements inside the root View object
        final ArrayList<View> touchableViews = root.getTouchables();
        for(final View touchable : touchableViews) {

            //Control necessary to exclude the TextView
            if(touchable instanceof Button) {

                //Cast the View to Button
                final Button button = (Button) touchable;

                //Set the click listener to the Button
                button.setOnClickListener(onClickListener);
            }
        }
    }
}
