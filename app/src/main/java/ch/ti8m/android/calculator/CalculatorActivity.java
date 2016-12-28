package ch.ti8m.android.calculator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private static final String VALUE_KEY = "value";
    private static final String OPERATION_KEY = "operation";

    private TextView resultTextView;

    private Double value;
    private String operation;

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

        //Retrieves the text field from the layout using the view's ID
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        //Retrieves the root view from the layout using the view's ID
        final View rootView = findViewById(R.id.rootView);

        //Click listener to use with the buttons
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets the View's ID
                int viewId = v.getId();

                //Gets the name associated the given View's ID
                final String viewIdName = getResources().getResourceEntryName(viewId);

                //Prints out the ID's name of the pressed button we gave in the .XML layout file.
                //Example: if the ID of the button '0' is '0464664687568'... we will get 'button0'.
                //For further information see the link above.
                Log.d(LOG_TAG, viewIdName);

                switch (viewId) {
                    //For the values from 0 to 9 we simply need to update the result TextView
                    case R.id.button0:
                    case R.id.button1:
                    case R.id.button2:
                    case R.id.button3:
                    case R.id.button4:
                    case R.id.button5:
                    case R.id.button6:
                    case R.id.button7:
                    case R.id.button8:
                    case R.id.button9:
                        setTextViewValue(v.getTag().toString());
                        break;
                    //For the operational buttons (+, -, *, /, =) we need to calculate a result until this moment
                    case R.id.buttonPlus:
                    case R.id.buttonMinus:
                    case R.id.buttonTimes:
                    case R.id.buttonDiv:
                    case R.id.buttonEqual:
                        calculateIntermediateResult(v.getTag().toString());
                        break;
                    case R.id.buttonClear:
                        clearAll();
                        break;

                    default:
                        Log.e(LOG_TAG, "Unknown View ID " + viewId + " with name '" + viewIdName + "'.");
                }
            }
        };

        //Initializes the buttons inside the root views
        initButtons(rootView, onClickListener);

        resetResultTextView();
    }

    /**
     * Called between onPause and onStop methods to save the computational state
     * @param outState dictionary where to save the computational state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(VALUE_KEY, value);
        outState.putString(OPERATION_KEY, operation);

        super.onSaveInstanceState(outState);
    }

    /**
     * Called between onStart and onResume to restore the (eventually) saved computational state.
     * @param savedInstanceState the dictionary containing the computational state.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            value = (Double) savedInstanceState.getSerializable(VALUE_KEY);
            operation = savedInstanceState.getString(OPERATION_KEY);
        }
    }

    /**
     * Called after onStart
     */
    @Override
    protected void onResume() {
        super.onResume();

        if(value != null) {
            setResultTextViewCurrentValue();
        }
    }

    /**
     * Initializes all the buttons present in the root View
     * @param root the root View
     * @param onClickListener the click listener to set to the buttons
     */
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

    /**
     * Updates the result TextView field with the digit pressed
     * @param number the digit pressed
     */
    private void setTextViewValue(@NonNull final String number) {
        //Just to make the code more robust.
        //Theoretically you should know how to invoke your private methods.
        Checker.notNull(number, "Number pressed");

        final double currentValue = Double.parseDouble(resultTextView.getText().toString());
        if(currentValue == 0d) {
            resultTextView.setText(number);
        } else {
            resultTextView.append(number);
        }

        Log.d(LOG_TAG, "Updated TextView value: " + resultTextView.getText());
    }

    /**
     * Calculates the result until the current moment
     * @param oper the operation chosen
     */
    private void calculateIntermediateResult(@NonNull final String oper) {
        //Just to make the code more robust.
        //Theoretically you should know how to invoke your private methods.
        Checker.notNull(oper, "Operation");

        //Retrieves the current number present in the result TextView
        final double number = Double.parseDouble(resultTextView.getText().toString());

        //If the value stored is null (initial state when we didn't do any operation yet)...
        if(value == null) {
            //...then store the value, reset the text field and go on.
            value = number;
            resetResultTextView();
        } else {
            //...otherwise perform the previous chose operation...
            switch (operation) {
                case "+":
                    value += number;
                    break;
                case "-":
                    value -= number;
                    break;
                case "*":
                    value *= number;
                    break;
                case "/":
                    value /= number;
                    break;
                default:
                    throw new IllegalStateException("Unknown operation: " + operation);
            }
        }

        //...set the calculated value in the result TextView...
        setResultTextViewCurrentValue();

        Log.d(LOG_TAG, "Current value: " + value);

        //...and store the actual operation if it's different then '=" (we are still in an intermediate step)
        if(!oper.equals("=")) {
            operation = oper;
        } else { //...otherwise clear the value stored (we are in the final step)
            value = null;
        }
    }

    /**
     * Resets everything
     */
    private void clearAll() {
        value = null;
        operation = null;

        resetResultTextView();
    }

    private void resetResultTextView() {
        setResultTextViewText(0d);
    }

    private void setResultTextViewCurrentValue() {
        setResultTextViewText(value);
    }

    private void setResultTextViewText(@Nullable final Double value) {
        String text = null;
        if(value != null) {
            text = Double.toString(value);
        }
        resultTextView.setText(text);
    }
}
