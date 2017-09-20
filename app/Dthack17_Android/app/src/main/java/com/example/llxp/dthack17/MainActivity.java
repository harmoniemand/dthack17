package com.example.llxp.dthack17;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;

import java.io.Console;

public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private MyLocationListener mLocationListener;
    private float currentShakeLevel = 0.0f;
    private double currentLongtitude = 0.0f;
    private double currentLatitude = 0.0f;
    private float[] shakeLevelAvg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shakeLevelAvg = new float[20];
        for(int i = 0;i < 20;i++)
        {
            shakeLevelAvg[i] = -1.0f;
        }

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(float count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                //handleShakeEvent(count);

                //Log.d("shake", Float.toString(count));
                currentShakeLevel = count;
                boolean slavgSet = false;
                for(int i = 0;i < 20;i++)
                {
                    if(shakeLevelAvg[i] == -1.0f)
                    {
                        shakeLevelAvg[i] = currentShakeLevel;
                        slavgSet = true;
                        break;
                    }
                }
                if(slavgSet == false)
                {
                    float[] tempSlavg = new float[20];
                    for(int i = 1;i < 20;i++)
                    {
                        tempSlavg[i] = shakeLevelAvg[i-1];
                    }
                    tempSlavg[0] = currentShakeLevel;
                    for(int i = 0;i < 20;i++)
                    {
                        shakeLevelAvg[i] = tempSlavg[i];
                    }
                }

                int counter = 0;
                for(int i = 0;i < 20;i++)
                {
                    if(shakeLevelAvg[i] != -1.0f)
                    {
                        counter++;
                    }
                }

                float sum = 0.0f;
                for(int i = 0;i < 20;i++)
                {
                    if(shakeLevelAvg[i] != -1.0f)
                    {
                        sum += shakeLevelAvg[i];
                    }
                }

                currentShakeLevel = sum/counter;

                if(currentShakeLevel >= (currentShakeLevel+currentShakeLevel*0.5))
                {
                    CallAPI api = new CallAPI();
                    api.execute("{\"acceleration\":"+currentShakeLevel+", \"longitude\":"+currentLongtitude+", \"latitude\":"+currentLatitude+", \"device\": \"android\"}");
                }

                //CallAPI api = new CallAPI();
                //api.execute("{\"acceleration\":"+currentShakeLevel+", \"longitude\":"+currentLongtitude+", \"latitude\":"+currentLatitude+"}");
            }
        });

        Log.d("initialization", "...");

        /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("error", "gps permission not granted");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);

        ((MyLocationListener)locationListener).setOnShakeListener(new MyLocationListener.OnLocationListener() {
            @Override
            public void LocationChanged(double longtitude, double latitude) {
                Log.d("gps", Double.toString(longtitude) + ", " + Double.toString(latitude));
            }
        });*/

        LocationManager locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        ((MyLocationListener) locationListener).setOnShakeListener(new MyLocationListener.OnLocationListener() {
            @Override
            public void LocationChanged(double longtitude, double latitude) {
                if(longtitude != 0)
                {
                    currentLongtitude = longtitude;
                }
                if(latitude != 0)
                {
                    currentLatitude = latitude;
                }
                Log.d("gps", Double.toString(longtitude) + ", " + Double.toString(latitude));
                //if(currentShakeLevel >= 1.2f)
                //{
                    //CallAPI api = new CallAPI();
                    //api.execute("{\"acceleration\":"+currentShakeLevel+", \"longitude\":"+longtitude+", \"latitude\":"+longtitude+"}");
                //}
                //currentLongtitude = longtitude;
                //currentLatitude = latitude;
            }
        });

        // Define a listener that responds to location updates
        /*LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location loc) {
                // Called when a new location is found by the network location provider.
                Log.i("NOTIFICATION","onLocationChenged Triggered");

                Toast msg = Toast.makeText(getApplicationContext(), "Lon: " + Double.toString(loc.getLongitude()) + " Lat: " + Double.toString(loc.getLatitude()), Toast.LENGTH_SHORT);
                msg.show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };*/

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locmgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    /*
    // Here we store the current values of acceleration, one for each axis
    private float xAccel;
    private float yAccel;
    private float zAccel;

    // And here the previous ones
    private float xPreviousAccel;
    private float yPreviousAccel;
    private float zPreviousAccel;

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    // Used to suppress the first shaking
    private boolean firstUpdate = true;

    //What acceleration difference would we assume as a rapid movement?
    private final float shakeThreshold = 1.5f;

    // Has a shaking motion been started (one direction)
    private boolean shakeInitiated = false;

    private SensorManager mySensorManager;
    private long mShakeTimestamp;
    private int mShakeCount;

    // The SensorEventListener lets us wire up to the real hardware events
    private final SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {

            //if (mListener != null) {
                float x = se.values[0];
                float y = se.values[1];
                float z = se.values[2];

                float gX = x / SensorManager.GRAVITY_EARTH;
                float gY = y / SensorManager.GRAVITY_EARTH;
                float gZ = z / SensorManager.GRAVITY_EARTH;

                // gForce will be close to 1 when there is no movement.
                float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

                if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                    final long now = System.currentTimeMillis();
                    // ignore shake events too close to each other (500ms)
                    if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                        return;
                    }

                    // reset the shake count after 3 seconds of no shakes
                    if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                        mShakeCount = 0;
                    }

                    mShakeTimestamp = now;
                    mShakeCount++;

                    //mListener.onShake(mShakeCount);
                //}
            }

            //Log.v(se.sensor.getName(), se.sensor.toString());
            updateAccelParameters(se.values[0], se.values[1], se.values[2]);   // (1)
            if ((!shakeInitiated) && isAccelerationChanged()) {                                      // (2)
                shakeInitiated = true;
            } else if ((shakeInitiated) && isAccelerationChanged()) {                              // (3)
                executeShakeAction();
            } else if ((shakeInitiated) && (!isAccelerationChanged())) {                           // (4)
                shakeInitiated = false;
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //Log.v(sensor.getName(), Float.toString(sensor.getPower()));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // (1)
        mySensorManager.registerListener(mySensorEventListener, mySensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL); // (2)
    }

    // Store the acceleration values given by the sensor
    private void updateAccelParameters(float xNewAccel, float yNewAccel,
                                       float zNewAccel) {
                // we have to suppress the first change of acceleration, it results from first values being initialized with 0
        if (firstUpdate) {
            xPreviousAccel = xNewAccel;
            yPreviousAccel = yNewAccel;
            zPreviousAccel = zNewAccel;
            firstUpdate = false;
        } else {
            xPreviousAccel = xAccel;
            yPreviousAccel = yAccel;
            zPreviousAccel = zAccel;
        }
        xAccel = xNewAccel;
        yAccel = yNewAccel;
        zAccel = zNewAccel;
    }

    // If the values of acceleration have changed on at least two axises, we are probably in a shake motion
    private boolean isAccelerationChanged() {
        float deltaX = Math.abs(xPreviousAccel - xAccel);
        float deltaY = Math.abs(yPreviousAccel - yAccel);
        float deltaZ = Math.abs(zPreviousAccel - zAccel);
        return (deltaX > shakeThreshold && deltaY > shakeThreshold)
                || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
                || (deltaY > shakeThreshold && deltaZ > shakeThreshold);
    }

    private void executeShakeAction() {
		// Save the cheerleader, save the world
		//   or do something more sensible...
        Log.d("debug", "executeShakeAction");
    }*/
}
