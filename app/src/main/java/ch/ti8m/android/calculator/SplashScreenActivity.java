package ch.ti8m.android.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity to create the splash screen and to start the real application.
 *
 * @author Cosimo Damiano Prete
 * @since 26/12/2016
 */
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     * @param savedInstanceState dictionary containing eventually saved values.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Object used to start an activity from another.
        Intent intent = new Intent(this, CalculatorActivity.class);

        // Starts the other activity.
        startActivity(intent);

        // Destroys the actual activity.
        finish();
    }
}
