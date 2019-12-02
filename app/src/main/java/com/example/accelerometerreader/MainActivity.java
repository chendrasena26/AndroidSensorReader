package com.example.accelerometerreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private Sensor accelerometer, mGyro;
    private SensorManager sensorManager;
    private FileWriter writer;

    TextView xValue, yValue, zValue, xGyroValue, yGyroValue, zGyroValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xValue = (TextView) findViewById(R.id.xValue);
        yValue = (TextView) findViewById(R.id.yValue);
        zValue = (TextView) findViewById(R.id.zValue);

        xGyroValue = (TextView) findViewById(R.id.xGyroValue);
        yGyroValue = (TextView) findViewById(R.id.yGyroValue);
        zGyroValue = (TextView) findViewById(R.id.zGyroValue);

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null) {
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer listener");
        }

//        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        if(mGyro != null) {
//            sensorManager.registerListener(MainActivity.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
//            Log.d(TAG, "onCreate: Registered gyroscope listener");
//        }
    }

    public void onStartClick(View view) {

    }

    public void onStopClick(View view) throws IOException {
        sensorManager.unregisterListener(MainActivity.this);
        writer.close();
    }

    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "OnSensorChanged: X: " + sensorEvent.values[0] + "Y: " + sensorEvent.values[1] + "Z: " + sensorEvent.values[2]);

        xValue.setText("xValue: " + sensorEvent.values[0]);
        yValue.setText("yValue: " + sensorEvent.values[1]);
        zValue.setText("zValue: " + sensorEvent.values[2]);

        String entryX = xValue.getText().toString() + ",";
        String entryY = yValue.getText().toString() + ",";
        String entryZ = zValue.getText().toString() + "\n";

        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/jajaja");
            Boolean dirsMade = dir.mkdir();
            Log.v("Accel", dirsMade.toString());
            File file = new File(dir, "output.csv");

            FileOutputStream f = new FileOutputStream(file, true);
            try {
                f.write(entryX.getBytes());
                f.write(entryY.getBytes());
                f.write(entryZ.getBytes());
                f.flush();
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
