package net.ictcampus.stepcounterexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Example of the Step Counter Sensor
 *
 * A constant describing a step counter sensor.
 *
 * A sensor of this type returns the number of steps taken by the user
 * since the last reboot while activated. The value is returned as a float
 * (with the fractional part set to zero) and is reset to zero only on a system reboot.
 * The timestamp of the event is set to the time when the last step for that event was taken.
 * This sensor is implemented in hardware and is expected to be low power.
 * If you want to continuously track the number of steps over a long period of time,
 * do NOT unregister for this sensor, so that it keeps counting steps in the background
 * even when the AP is in suspend mode and report the aggregate count when the AP is awake.
 * Application needs to stay registered for this sensor because step counter does not
 * count steps if it is not activated. This sensor is ideal for fitness tracking applications.
 * It is defined as an Sensor#REPORTING_MODE_ON_CHANGE sensor.
 *
 * This sensor requires permission android.permission.ACTIVITY_RECOGNITION.
 *
 * See SensorEvent.values for more details.
 *
 * Constant Value: 19 (0x00000013)
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    /**
     * Sensor Manager to manage the sensors
     */
    SensorManager mSensorManager;
    /**
     * Sensor TYPE_STEP_COUNTER
     * Constant Value: 19 (0x00000013)
     */
    Sensor myStepSensor;
    /**
     * TextView to display the Steps.
     */
    TextView textViewSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewSteps = findViewById(R.id.stepCounter);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        //myStepSensor = mSensorManager.getDefaultSensor(19);
        myStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    /**
     * adds a new listener to the Sensor to recognize sensor change events
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.v("steps", "is there a sensor?");
        // only if Sensor Type_Step_Counter is recognized.
        if (myStepSensor != null) {
            Log.v("steps", "yay, there is a step sensor");
            // Add Eventlistener, returns true if Listener is successfully added and enabled.
            mSensorManager.registerListener(this, myStepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.v("steps", "nope, there is no sensor.");
        }
    }

    /**
     * Updates the Textview with current steps and logs the mode steps.
     * @param event sensor changes
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.v("StepCount", String.valueOf(event.values[0]));
        textViewSteps.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
