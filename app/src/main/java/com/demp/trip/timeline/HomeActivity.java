package com.demp.trip.timeline;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demp.trip.timeline.classes.LocationHistoryData;
import com.demp.trip.timeline.services.GoogleService;
import com.demp.trip.timeline.services.LocationUpdaterService;
import com.demp.trip.timeline.services.MyLocationService;
import com.demp.trip.timeline.sqlite.SqliteLocationTimeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
{
    Typeface app_font_type;
    ActionBar actionBar;
    TextView txt_actionTitle;

    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    TextView txt_status;
    TextView txt_latitude;
    TextView txt_longitude;
    TextView txt_address;
    TextView txt_city;
    TextView txt_state;
    TextView txt_country;
    TextView txt_pincode;

    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Double latitude,longitude;
    Geocoder geocoder;

    Button btn_start;

    SqliteLocationTimeline sqlite_location_history;

    LocationManager location_manager;

    ArrayList<LocationHistoryData> array_location_history = new ArrayList<LocationHistoryData>();

    GoogleService mTimelineService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setview();
    }

    private void setview()
    {
        setContentView(R.layout.activity_home);

        app_font_type = Typeface.createFromAsset(getAssets(), AppHelper.app_font_path);

        setUpActionBar();

        mTimelineService = new GoogleService(HomeActivity.this);

        location_manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE);
        if (!location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            NoGPSOnDialog();
        }

        sqlite_location_history = new SqliteLocationTimeline(this);
        sqlite_location_history.openToWrite();

        array_location_history = (ArrayList<LocationHistoryData>) sqlite_location_history.getLocationHistoryList();

        txt_status = findViewById(R.id.location_txt_status);
        txt_latitude = findViewById(R.id.location_txt_latitude);
        txt_longitude = findViewById(R.id.location_txt_longitude);
        txt_address = findViewById(R.id.location_txt_address);
        txt_city = findViewById(R.id.location_txt_city);
        txt_state = findViewById(R.id.location_txt_state);
        txt_country = findViewById(R.id.location_txt_country);
        txt_pincode = findViewById(R.id.location_txt_pincode);

        txt_status.setTypeface(app_font_type);
        txt_latitude.setTypeface(app_font_type);
        txt_longitude.setTypeface(app_font_type);
        txt_address.setTypeface(app_font_type);
        txt_city.setTypeface(app_font_type);
        txt_state.setTypeface(app_font_type);
        txt_country.setTypeface(app_font_type);
        txt_pincode.setTypeface(app_font_type);

        geocoder = new Geocoder(this, Locale.getDefault());
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        medit = mPref.edit();

        btn_start = findViewById(R.id.home_btn_start);
        btn_start.setTypeface(app_font_type);
        btn_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (boolean_permission)
                {
                    Intent mServiceIntent = new Intent(HomeActivity.this, GoogleService.class);
                    startService(mServiceIntent);

                    /*if (!isMyServiceRunning(mTimelineService.getClass()))
                    {
                        Log.e("Timeline Service :","Service Start!");
                        startService(mServiceIntent);
                    }
                    else
                    {
                        Log.e("Timeline Service :","Service Running!");
                    }*/

                    /*Intent intent = new Intent(HomeActivity.this, GoogleService.class);
                    startService(intent);*/
                }
                else
                {
                    String toastMessage = "Please enable the gps!";
                    AppClass.ShowErrorToast(HomeActivity.this,toastMessage);
                }
            }
        });

        CheckPermission();

        //StartServices();
    }

    private void StartServices()
    {
        if (boolean_permission)
        {
            Intent mServiceIntent = new Intent(HomeActivity.this, GoogleService.class);
            startService(mServiceIntent);

            /*if (!isMyServiceRunning(mTimelineService.getClass()))
            {
                Log.e("Timeline Service :","Service Start!");
                startService(mServiceIntent);
            }
            else
            {
                Log.e("Timeline Service :","Service Running!");
            }*/
            //startService(intent);
        }
        else
        {
            String toastMessage = "Please enable the gps!";
            AppClass.ShowErrorToast(HomeActivity.this,toastMessage);
            //Toast.makeText(getApplicationContext(), "Please enable the gps!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                Log.e("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.e("isMyServiceRunning?", false+"");
        return false;
    }

    int GPS_REQUEST = 1;
    private void NoGPSOnDialog()
    {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        Intent gps_intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(gps_intent, GPS_REQUEST);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST)
        {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
            {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Log.e("GPS Activity Result :: ", "GPS Activity Result Call...");
                // Do something with the contact here (bigger example below)
                //GetLocation();
            }
        }
    }

    private void CheckPermission()
    {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)))
            {


            }
            else
            {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSIONS);

            }
        }
        else
        {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case REQUEST_PERMISSIONS:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    boolean_permission = true;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));
            String location_update = intent.getStringExtra("location");

            List<Address> addresses = null;

            try
            {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String cityName = addresses.get(0).getLocality().trim();
                String stateName = addresses.get(0).getAdminArea().trim();
                String countryName = addresses.get(0).getCountryName().trim();
                String pinCode = addresses.get(0).getPostalCode().trim();

                txt_status.setText(location_update);

                txt_address.setText(address);
                txt_city.setText(cityName);
                txt_state.setText(stateName);
                txt_country.setText(countryName);
                txt_pincode.setText(pinCode);

            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }


            txt_latitude.setText(latitude+"");
            txt_longitude.setText(longitude+"");
            txt_address.getText();
        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void setUpActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        txt_actionTitle = (TextView)findViewById(R.id.toolbar_title);
        txt_actionTitle.setTypeface(app_font_type);
        txt_actionTitle.setText("Trip Timeline");

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blank_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                BackScreen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        BackScreen();
    }

    private void BackScreen()
    {
        finish();
    }
}
