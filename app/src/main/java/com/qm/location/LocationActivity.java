package com.qm.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LocationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前位置
        LocationManager locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);//位置服务对象
        LocationListener locationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                //位置变化时 获取新位置
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d("onLocationChanged:", " location.toString()");
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }


        };
    }
}
