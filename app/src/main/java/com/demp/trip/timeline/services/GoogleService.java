package com.demp.trip.timeline.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.demp.trip.timeline.AppClass;
import com.demp.trip.timeline.GPSTracker;
import com.demp.trip.timeline.HomeActivity;
import com.demp.trip.timeline.classes.SingleLocationHistoryData;
import com.demp.trip.timeline.sqlite.SqliteLocationTimeline;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GoogleService extends Service implements LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double mLatitude, mLongitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1 * 60 * 1000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;

    String current_date = "";
    String start_time = "";
    String end_time = "";

    Geocoder geocoder;
    private Location mLastLocation;

    SqliteLocationTimeline sqlite_location_history;

    ArrayList<SingleLocationHistoryData> array_single_location = new ArrayList<SingleLocationHistoryData>();

    GPSTracker gpsTracker;
    GoogleApiClient mGoogleApiClient;

    Context mContext;

    public GoogleService()
    {

    }

    public GoogleService(Context ctx)
    {
        mContext = ctx;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        geocoder = new Geocoder(this, Locale.getDefault());
        mContext = this;
        sqlite_location_history = new SqliteLocationTimeline(this);
        sqlite_location_history.openToWrite();

        startTimer();

        if(mGoogleApiClient==null)
        {
            mGoogleApiClient=new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mGoogleApiClient!=null&&mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }

        Intent broadcastIntent = new Intent("Service.Restart");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private void startTimer()
    {
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);
    }

    public void stoptimertask()
    {
        //stop the timer, if it's not already null
        if (mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        //Log.e("Location :","Location Changed!");
        //fn_update(location,"Location Changed!");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    private void GetCurrentLocation()
    {
        try
        {
            gpsTracker = new GPSTracker(this);
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnable && !isNetworkEnable)
            {

            }
            else
            {
            /*location = null;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling

                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            if (isNetworkEnable)
            {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                if (locationManager != null)
                {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null)
                    {

                        //Log.e("Network Latitude",location.getLatitude()+"");
                        //Log.e("Network Longitude",location.getLongitude()+"");

                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        fn_update(location,"Location Not Changed!");
                    }
                }
            }

            if (isGPSEnable)
            {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);

                if(locationManager == null)
                {
                    locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE);
                }

                if (locationManager!=null)
                {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null)
                    {
                        //Log.e("GPS Latitude",location.getLatitude()+"");
                        //Log.e("GPS Longitude",location.getLongitude()+"");

                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        fn_update(location,"Location Not Changed!");
                    }
                }
            }*/

                //mLatitude = gpsTracker.getLatitude();
                //mLongitude = gpsTracker.getLongitude();
                //Log.e("currentlat",""+mLatitude);
                //Log.e("currentlng",""+mLongitude);

                if(mLatitude != 0.0 && mLongitude != 0.0)
                {
                    Log.e("GPS Latitude :",String.valueOf(mLatitude));
                    Log.e("GPS Longitude :",String.valueOf(mLongitude));

                    current_date = AppClass.GetCurrentDateTime();

                    UpdateLocations(mLatitude,mLongitude,"Location Not Changed!");

                    List<Address> place_address = geocoder.getFromLocation(mLatitude, mLongitude, 1);
                    String place = place_address.get(0).getAddressLine(0).trim();
                    Log.e("Location :",place);
                    boolean is_exist = sqlite_location_history.CheckLocationExist(String.valueOf(mLatitude),String.valueOf(mLongitude),place);

                    int lastROWID = sqlite_location_history.GetLastInsertedRowID();

                    if(lastROWID == -1)
                    {
                        List<Address> addresses = null;

                        try
                        {
                            String place_latitude = String.valueOf(mLatitude).trim();
                            String place_longitude = String.valueOf(mLongitude).trim();

                            addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
                            String address = addresses.get(0).getAddressLine(0).trim();
                            String cityName = addresses.get(0).getLocality().trim();
                            String stateName = addresses.get(0).getAdminArea().trim();
                            String countryName = addresses.get(0).getCountryName().trim();
                            String pinCode = addresses.get(0).getPostalCode().trim();
                            //String place_name1=addresses.get(0).getSubAdminArea().trim();
                            //String place_name2=addresses.get(0).getSubLocality().trim();
                            //String place_name3=addresses.get(0).getSubThoroughfare().trim();

                            current_date = AppClass.current_date.trim();
                            start_time = AppClass.current_time.trim();

                            sqlite_location_history.InsertLocationData("-----",place_latitude,place_longitude,
                                    place,cityName,stateName,countryName,pinCode,
                                    start_time,"-----",current_date);

                            Log.e("Location Data :","Location Data Inserted!");

                        }
                        catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    else
                    {
                        array_single_location.clear();
                        array_single_location = (ArrayList<SingleLocationHistoryData>) sqlite_location_history.getSingleLocationHistoryList(lastROWID);
                        end_time = AppClass.current_time.trim();

                        int rowId = array_single_location.get(0).row_id;
                        String location_name = array_single_location.get(0).location_name.trim();
                        String location_latitude = array_single_location.get(0).location_latitude.trim();
                        String location_longitude = array_single_location.get(0).location_longitude.trim();
                        String location_address = array_single_location.get(0).location_address.trim();
                        String location_city = array_single_location.get(0).location_city.trim();
                        String location_state = array_single_location.get(0).location_state.trim();
                        String location_country = array_single_location.get(0).location_country.trim();
                        String location_pincode = array_single_location.get(0).location_pincode.trim();
                        String start_time = array_single_location.get(0).start_time.trim();
                        String endTime = end_time.trim();
                        String saved_date = array_single_location.get(0).saved_date.trim();

                        if(location_latitude.equals(String.valueOf(mLatitude)) && location_longitude.equals(String.valueOf(mLongitude)) || location_address.equals(place))
                        {
                            sqlite_location_history.UpdateLocationData(lastROWID,location_name,location_latitude,location_longitude,
                                    location_address,location_city,location_state,location_country,location_pincode,
                                    start_time,endTime,saved_date);

                            Log.e("Location Data :","Location Data Updated!");
                        }
                        else
                        {
                            sqlite_location_history.UpdateLocationData(lastROWID,location_name,location_latitude,location_longitude,
                                    location_address,location_city,location_state,location_country,location_pincode,
                                    start_time,endTime,saved_date);

                            Log.e("Location Data :","Location Updated!");

                            try
                            {
                                List<Address> addresses = null;
                                String place_latitude = String.valueOf(mLatitude).trim();
                                String place_longitude = String.valueOf(mLongitude).trim();

                                addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
                                String address = addresses.get(0).getAddressLine(0).trim();
                                String cityName = addresses.get(0).getLocality().trim();
                                String stateName = addresses.get(0).getAdminArea().trim();
                                String countryName = addresses.get(0).getCountryName().trim();
                                String pinCode = addresses.get(0).getPostalCode().trim();
                                //String place_name1=addresses.get(0).getSubAdminArea().trim();
                                //String place_name2=addresses.get(0).getSubLocality().trim();
                                //String place_name3=addresses.get(0).getSubThoroughfare().trim();

                                current_date = AppClass.current_date.trim();
                                start_time = AppClass.current_time.trim();

                                sqlite_location_history.InsertLocationData("-----",place_latitude,place_longitude,
                                        place,cityName,stateName,countryName,pinCode,
                                        start_time,"-----",current_date);

                                Log.e("Location Data :","Location Data Inserted!");

                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    }

                    /*if(is_exist)
                    {
                        //Log.e("Location Data :","Location Data Exist!");

                        int row_id = sqlite_location_history.GetLocationRowID(String.valueOf(mLatitude),String.valueOf(mLongitude),place);

                        array_single_location.clear();
                        array_single_location = (ArrayList<SingleLocationHistoryData>) sqlite_location_history.getSingleLocationHistoryList(row_id);
                        end_time = AppClass.current_time.trim();

                        int rowId = array_single_location.get(0).row_id;
                        String location_name = array_single_location.get(0).location_name.trim();
                        String location_latitude = array_single_location.get(0).location_latitude.trim();
                        String location_longitude = array_single_location.get(0).location_longitude.trim();
                        String location_address = array_single_location.get(0).location_address.trim();
                        String location_city = array_single_location.get(0).location_city.trim();
                        String location_state = array_single_location.get(0).location_state.trim();
                        String location_country = array_single_location.get(0).location_country.trim();
                        String location_pincode = array_single_location.get(0).location_pincode.trim();
                        String start_time = array_single_location.get(0).start_time.trim();
                        String endTime = end_time.trim();
                        String saved_date = array_single_location.get(0).saved_date.trim();

                        sqlite_location_history.UpdateLocationData(rowId,location_name,location_latitude,location_longitude,
                                location_address,location_city,location_state,location_country,location_pincode,
                                start_time,endTime,saved_date);

                        Log.e("Location Data :","Location Data Updated!");
                    }
                    else
                    {
                        List<Address> addresses = null;

                        try
                        {
                            String place_latitude = String.valueOf(mLatitude).trim();
                            String place_longitude = String.valueOf(mLongitude).trim();

                            addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
                            String address = addresses.get(0).getAddressLine(0).trim();
                            String cityName = addresses.get(0).getLocality().trim();
                            String stateName = addresses.get(0).getAdminArea().trim();
                            String countryName = addresses.get(0).getCountryName().trim();
                            String pinCode = addresses.get(0).getPostalCode().trim();
                            //String place_name1=addresses.get(0).getSubAdminArea().trim();
                            //String place_name2=addresses.get(0).getSubLocality().trim();
                            //String place_name3=addresses.get(0).getSubThoroughfare().trim();

                            //Log.d("placename",""+place_name1+"::"+place_name2+"::"+place_name3+"::"+cityName);

                            Log.e("GPS Latitude :",place_latitude);
                            Log.e("GPS Longitude :",place_longitude);

                            current_date = AppClass.current_date.trim();
                            start_time = AppClass.current_time.trim();

                            Log.e("Date :",current_date);
                            Log.e("Time :",start_time);

                            sqlite_location_history.InsertLocationData("-----",place_latitude,place_longitude,
                                    address,cityName,stateName,countryName,pinCode,
                                    start_time,"-----",current_date);

                            Log.e("Location Data :","Location Data Inserted!");

                        }
                        catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                    }*/
                }
                else
                {
                    Log.e("GPS Latitude :","0.0");
                    Log.e("GPS Longitude :","0.0");
                }
            }
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        mGoogleApiClient.connect();
    }

    /* @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(mGoogleApiClient!=null&&mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }*/


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @SuppressLint("MissingPermission")
    public void setUpMap()
    {
        /*if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           *//* ActivityCompat.requestPermissions(context, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);*//*
            return;
        }*/

        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable())
        {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                //currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                //placeMarkerOnMap(currentLocation);
                mLatitude=mLastLocation.getLatitude();
                mLongitude=mLastLocation.getLongitude();
                String lati=String.format("%.5f", mLatitude);
                String lngi=String.format("%.5f", mLongitude);
                mLatitude=Double.parseDouble(lati);
                mLongitude=Double.parseDouble(lngi);
                Log.d("curlat",""+mLatitude);
                Log.d("curlng",""+mLongitude);
                GetCurrentLocation();
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));


            }
        }
    }

    private class TimerTaskToGetLocation extends TimerTask
    {
        @Override
        public void run()
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    setUpMap();
                }
            });

        }
    }

    private void fn_update(Location location,String loc)
    {
        intent.putExtra("latitude",location.getLatitude()+"");
        intent.putExtra("longitude",location.getLongitude()+"");
        intent.putExtra("location",loc);
        sendBroadcast(intent);
    }

    private void UpdateLocations(double latitude,double longitude,String loc)
    {
        intent.putExtra("latitude",latitude+"");
        intent.putExtra("longitude",longitude+"");
        intent.putExtra("location",loc);
        sendBroadcast(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent(this, GoogleService.class);
        startService(intent);
    }
}
