package com.example.llxp.dthack17;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by llxp on 19.09.17.
 */

public class MyLocationListener implements LocationListener {

    private OnLocationListener mListener;

    public void setOnShakeListener(MyLocationListener.OnLocationListener listener) {
        this.mListener = listener;
    }
    public interface OnLocationListener {
        public void LocationChanged(double longtitude, double latitude);
    }
    @Override
    public void onLocationChanged(Location location) {
        if(mListener != null) {
            mListener.LocationChanged(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
