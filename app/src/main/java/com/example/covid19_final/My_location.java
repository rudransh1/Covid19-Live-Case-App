package com.example.covid19_final;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class My_location extends AppCompatActivity {
    private int REQUEST_LOCATION = 99;
    private Button button3, button4, button5;
    private static TextView sub, district, state, count, t9, t10, t11, t12;

    private LocationManager locationManager;
    private String provider;
    private MyLocationListener mylistener;
    private Criteria criteria;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        t9 = findViewById(R.id.textView9);
        t10 = findViewById(R.id.textView10);
        t11 = findViewById(R.id.textView11);
        t12 = findViewById(R.id.textView12);
        sub = findViewById(R.id.sub);
        district = findViewById(R.id.district);
        state = findViewById(R.id.state);
        count = findViewById(R.id.country);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   //default

        // user defines the criteria


        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria


        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(My_location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(My_location.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(My_location.this, new String[]
                    {ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        final Location location = locationManager.getLastKnownLocation(provider);

        mylistener = new MyLocationListener();


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (location != null) {
                    Double lat = location.getLatitude();
                    Double lon = location.getLongitude();

                    Geocoder geocoder = new Geocoder(My_location.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(lat, lon, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String cit = addresses.get(0).getLocality();
                    String cityName = "";
                    for (int i = 0; i < cit.length(); i++) {
                        if (cit.charAt(i) == '-') {
                            break;
                        }
                        cityName += cit.charAt(i);

                    }
                    String stateName = addresses.get(0).getAdminArea();
                    String countryName = addresses.get(0).getCountryName();
                    String sbb = addresses.get(0).getPostalCode();
                    district.setText(cityName);
                    state.setText(stateName);
                    count.setText(countryName);
                    sub.setText(sbb);
                    parsed2 process2 = new parsed2();
                    process2.function(stateName, cityName);
                    process2.execute();
                    mylistener.onLocationChanged(location);
                } else {
                    // leads to the settings because there is no last known location
                    Log.d("my_tag", "hello");
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(My_location.this).create();
                    alertDialog.setTitle("Obtaining Location ...");
                    alertDialog.setMessage("00:12");
                    alertDialog.show();   //

                    new CountDownTimer(12000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            alertDialog.setMessage("00:" + (millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            alertDialog.dismiss();
                        }
                    }.start();
                }
                Log.d("this_tag", "Hello");
                // location updates: at least 1 meter and 200millsecs change
                if (ActivityCompat.checkSelfPermission(My_location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(My_location.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
                if(location!=null) {
                    Double lat = location.getLatitude();
                    Double lon = location.getLongitude();

                    Geocoder geocoder = new Geocoder(My_location.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(lat, lon, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String cit = addresses.get(0).getLocality();
                    String cityName="";
                    for(int i=0;i<cit.length();i++){
                        if(cit.charAt(i)=='-'){
                            break;
                        }
                        cityName+=cit.charAt(i);

                    }
                    String stateName = addresses.get(0).getAdminArea();
                    String countryName = addresses.get(0).getCountryName();
                    String sbb = addresses.get(0).getPostalCode();
                    district.setText(cityName);
                    state.setText(stateName);
                    count.setText(countryName);
                    sub.setText(sbb);
                    parsed2 process2 = new parsed2();
                    process2.function(stateName, cityName);
                    process2.execute();

                }
                else {
                    Toast.makeText(My_location.this, "Please open your location", Toast.LENGTH_SHORT).show();
                }


            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indian();
            }
        });
    }

    private void home() {
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void indian() {
        Intent intent =new Intent(this, india.class);
        startActivity(intent);
    }
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // Initialize the location fields


//            Toast.makeText(My_location.this, "" + location.getLatitude() + location.getLongitude(),
//                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Toast.makeText(My_location.this, provider + "'s status changed to " + status + "!",
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
//            Toast.makeText(My_location.this, "Provider " + provider + " enabled!",
//                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
//            Toast.makeText(My_location.this, "Provider " + provider + " disabled!",
//                    Toast.LENGTH_SHORT).show();
        }
    }
    public class parsed2 extends AsyncTask<Void,Void,Void> {

        //private ProgressDialog pDialog;
        String data ="";
        String u,v,w,z;
        String cit,stat;
        protected  void function(String x, String y){
            stat=x;
            cit=y;
        }
        private AlertDialog mydialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mydialog =new AlertDialog.Builder(My_location.this)
                    //.setTitle("Delete entry")
                    .setMessage("Getting Data ...")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.


                    // A null listener allows the button to dismiss the dialog and take no further action.
                    //.setNegativeButton(android.R.string.no, null)
                    .setCancelable(true)
                    .setIcon(android.R.drawable.btn_star)
                    .show();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url2 = new URL("https://api.covid19india.org/state_district_wise.json");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line="";
                int j=0;

                while((line=bufferedReader.readLine()) != null){
                    data = data + line;
                    j++;
                }
                Log.d("this_tag", String.valueOf(j));


                JSONObject JA=new JSONObject(data);
                JSONObject swa=JA.getJSONObject(stat);
                JSONObject swm= swa.getJSONObject("districtData");
                JSONObject sw= swm.getJSONObject(cit);
                Log.d("this_tag", "done");

                z= (String) sw.getString("confirmed");
                u= (String) sw.getString("active");
                v= (String) sw.getString("recovered");
                w= (String) sw.getString("deceased");

                Log.d("this_tag", "completed");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mydialog.dismiss();
            Log.d("this_tag", ""+z);
            My_location.t9.setText(this.z);
            My_location.t10.setText(this.u);
            My_location.t11.setText(this.v);
            My_location.t12.setText(this.w);

        }

    }
}
