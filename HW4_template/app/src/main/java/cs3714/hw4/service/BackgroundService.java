package cs3714.hw4.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import cs3714.hw4.socketio.SocketIO;
import io.socket.client.IO;
import io.socket.client.Socket;


import java.net.URISyntaxException;
import java.util.Calendar;

/**
 * Created by Andrey on 3/26/2017.
 */

public class BackgroundService extends Service implements SensorEventListener {




    // This is needed for the step count detection
    private SensorManager mSensorManager;

    // this will get you the username for uploading steps
    SharedPreferences prefs;


   // TODO: define a socket object
    private Socket mSocket;

    // These variables are used to detect the first step and to offset the old steps
    private float steps=0f;
    private float baseline_steps=0f;
    private int count=0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    // happenswhen service starts
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        try {
            mSocket = IO.socket("http://128.173.239.242/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // we need this for extracting username when 'emitting' steps to the server.
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        // TODO: We need to initialize an intent filter that will recognize 'ACTION_TIME_TICK'


        //TODO: We need to register our local broadcast receiver

        Log.d("background_service", "BackgroundService Started!");


        //sensor manager allows us to get access to all of the sensors that your device is offering you.
        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);

        // lets try and see if we can get the sensor that gives daily stepcount
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // if your device has this sensor then register the listener
        if (countSensor != null) {
            mSensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
        //if not then pop a toast that will let you know
        else {
            Toast.makeText(this, "TYPE_STEP_COUNTER not available", Toast.LENGTH_LONG).show();

        }

        IntentFilter filter = new IntentFilter(
                Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
        // TODO: Get the socket from the Application and then connect.



        // START_STICKY -- ? what does it mean? Research it.
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("background_service", "BackgroundService Stopped!");

        //TODO:inside onDestroy you need to 'unregister' the broadcast receiver
        unregisterReceiver(receiver);

        //TODO: also disconnect the mSocket

        super.onDestroy();
    }



    // this happens when steps are detected.
        @Override
    public void onSensorChanged(SensorEvent event) {


            // event.values[0] contains the float value of the steps for the day.
            // to start counting from 0 you need to manually offset the initial value.

            if(count==0){
            baseline_steps=event.values[0];

            steps=0;
            }
            count++;
            steps=event.values[0]-baseline_steps;


            Log.d("background_service", "Steps:"+steps);

    }


    //ignore this
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // extracing the string that the action is bringin in
            String action = intent.getAction();


            Log.d("hw4", "action received:" + action);
            JSONObject data = new JSONObject();
            try {
                data.put("username", prefs.getString("username", ""));
                data.put("delta", 0 + "");
                data.put("total", ((long) steps) + "");
                data.put("date", Calendar.getInstance().getTime() + "");
                data.put("epoch", Calendar.getInstance().getTimeInMillis());
                data.put("id", 999 + "");
                data.put("type", "steps");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.emit("steps", data);

        }
    };

}