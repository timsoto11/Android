package com.example.timothy.pothole;
//basic app

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

//Tim's addition


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView XMov, YMov, ZMov, Pos, time, mag;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Button btnPaired;
    private ListView devicelist;
    private BluetoothAdapter myBluetooth = null;
    private Set pairedDevices;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize sensor
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

        btnPaired = (Button) findViewById(R.id.button);

        btnPaired.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList(); //method that will be called
            }
        });


        devicelist = (ListView) findViewById(R.id.listView);

        //initialize GPS
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Pos.setText("GPS: " + location.getLongitude() + " " + location.getLatitude());
                time.setText("Time:\n" + location.getTime());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Not in use
            }

            @Override
            public void onProviderEnabled(String provider) {
                //Not in use
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }

      /*  myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            //Show a mensag. that thedevice has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            //finish apk
            finish();
        } else {
            if (myBluetooth.isEnabled()) {
            } else {
                //Ask to the user turn the bluetooth on
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon, 1);
            }
        }*/

        mLocationManager.requestLocationUpdates("gps", 1000, 0, mLocationListener);
        Pos = (TextView) findViewById(R.id.Pos);
        time = (TextView) findViewById(R.id.time);
        //Text
        XMov = (TextView) findViewById(R.id.XMov);
        YMov = (TextView) findViewById(R.id.YMov);
        ZMov = (TextView) findViewById(R.id.ZMov);
        mag = (TextView) findViewById(R.id.mag);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in use
    }

    public void connection(){
      /*  new Thread(new Runnable() {
            public void run() {
                try{
                   // HttpClient
                    URL url = new URL("https://csce483-team4.herokuapp.com");
                    ///road_conditions/new_from_url/42069/30.619006/-96.338809/2
                    HttpURLConnection client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");
                    client.connect();
                    Log.d("CREATION", "maybe this is working");
                   // if(){

                   // }
                }
                catch(IOException a){
                    Log.d("CREATION", "Caught IOException");
                }
                catch(Exception a){
                    Log.d("CREATION", "Unhandled Exception");
                }
                finally {
                    if(client != null) // Make sure the connection is not null.
                        client.disconnect();
                }
            }
        }).start();*/
        //Toast.makeText(getApplicationContext(), "Supposedly this worked", Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onSensorChanged(SensorEvent event) {
        float a = event.values[0];
        float b = event.values[1];
        float c = event.values[2];

        XMov.setText("X: " + a);
        YMov.setText("Y: " + b);
        ZMov.setText("Z: " + c);

        float magnitude = (a * a) + (b * b) + (c * c);
        magnitude = (float) Math.sqrt(magnitude);
        if (magnitude < 8.0 || magnitude > 11.6) {
            mag.setText("1");
            connection();
        } else {
            mag.setText("0");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pairedDevicesList() {
     /*   pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            Toast.makeText(getApplicationContext(), "We found devices!", Toast.LENGTH_LONG).show();
            for (Object bt : pairedDevices) {
                list.add(((BluetoothDevice) bt).getName() + "\n" + ((BluetoothDevice) bt).getAddress()); //Get the device's name and the address
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked*/
        new Thread(new Runnable() {
            public void run() {
                try{
                    // HttpClient
                    URL url = new URL("https://csce483-team4.herokuapp.com/road_conditions/new_from_url/42069/30.619006/-96.338809/2");
                    ///road_conditions/new_from_url/42069/30.619006/-96.338809/2
                    HttpURLConnection client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");
                    client.setRequestProperty("Key", "42069");
                    client.setDoOutput(true);
                    OutputStreamWriter outputPost = new OutputStreamWriter(client.getOutputStream());
                    outputPost.write("");
                    outputPost.flush();
                    //outputPost.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        // Append server response in string
                        sb.append(line + "\n");
                    }


                    String text = sb.toString();



                    Log.d("CREATION", "maybe this is working");

                }
                catch(IOException a){
                    Log.d("CREATION", "Caught IOException");
                }
                catch(Exception a){
                    Log.d("CREATION", "Unhandled Exception");
                }
                finally {
                    if(client != null) // Make sure the connection is not null.
                        client.disconnect();
                }
            }
        }).start();

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            //Intent i = new Intent(DeviceList.this, ledControl.class);
            //Change the activity.
            //i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            // startActivity(i);
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.timothy.pothole/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.timothy.pothole/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
