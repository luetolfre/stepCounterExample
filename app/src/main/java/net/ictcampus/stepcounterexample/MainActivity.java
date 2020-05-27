package net.ictcampus.stepcounterexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Example of the Usage of the Step Counter Sensor
 *
 * The Step Counter Sensor is primarily used to count the steps that are being taken by the
 * user of the device since the last reboot.
 * A similar sensor is the step detector sensor, that triggers an event each time a step is taken.
 *
 * Android treats these sensors as a logically separate sensors, but they are based on the accelerometer.
 * All of them are highly battery optimized and consume very low power.
 *
 * It is defined as an Sensor#REPORTING_MODE_ON_CHANGE sensor.
 *
 * The value of the step counter sensor is a float with a fractional part that is always 0 ( e.g. 42.0)
 * The event timestamp represents the time at which the last step was taken.
 *
 * To continuously track the number over time, the Application needs to stay registered for the sensor.
 * To save power, the listener can be unregistered or switched from continuous into batch mode
 * (by specifying a latency > 0) to group the event in batches.
 * Therefore even when the Application Processor is in suspend mode it will report the count when the AP is awake.
 *
 * This sensor requires permission android.permission.ACTIVITY_RECOGNITION.
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


    /**
     * create a new view in night mode, read from activity_main.xml
     * Sensor manager gets all the services available on the phone
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewSteps = findViewById(R.id.stepCounter);
        // set the app to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // get every System Service available on the phone
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // get the STEP_COUNTER from the Sensor Manager which constant value is the 19
        if (mSensorManager != null) {
            //myStepSensor = mSensorManager.getDefaultSensor(19);
            myStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }
        else {
            Log.e("sensor", "NullPointer on mSensorManager, could not find any Sensor Manager ");
        }
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
            // Add EventListener, returns true if Listener is successfully added and enabled.
            // Listener listens to an event which then calls the Sensor Changed Method and logs the number
            // this class implements the Event Listener, which is listening to the Step Sensor in a certain interval
            boolean listenerIsRegistered = mSensorManager.registerListener(this, myStepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (listenerIsRegistered) { Log.v("steps", "listener registered");}
        } else {
            Log.v("steps", "nope, there is no sensor.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myStepSensor != null) {
            // take off the Listener of the Sensor (while Sensor is counting by itself in the BG, power saving reasons)
            mSensorManager.unregisterListener(this);
            Log.v("steps", "listener unregistered");
        }
    }

    /**
     * Updates the TextView with current steps and logs the mode steps.
     * @param event sensor changes
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // parse the float to int for better user experience
        textViewSteps.setText(String.valueOf((int) event.values[0]));

        // log timestamp in nanoseconds
        // Log.v("steps", String.valueOf(event.timestamp));

        // log stepcount
        Log.v("steps", String.valueOf((int) event.values[0]));

    }

    /**
     * If the accuracy of the Sensor changes, this method gets called for example when using the
     * Step Sensor for several different walking conditions like running or hiking when the impact
     * on the Sensor is not the same
     * (util method when implementing Sensor Event Listener in class)
     * @param sensor step sensor
     * @param accuracy the value the accuracy should get changed to
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v("accuracy", "the accuracy got changed (now you may be running or hiking)");
    }
}
