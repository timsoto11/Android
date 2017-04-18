package com.example.timothy.pothole;

import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import com.example.timothy.pothole.MainActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Timothy on 4/14/2017.
 */
public class NetworkThread extends Thread {
        URL url = null;

    NetworkThread() {
       try {
           url = new URL("https://csce483-team4.herokuapp.com");
       }
       catch (MalformedURLException a){}

    }

    public void run() {
        HttpURLConnection client = null;
        try{
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.connect();

        }
        catch(IOException a){

        }
        catch (NetworkOnMainThreadException aa){

        }
        catch (Exception e){

            // Deal with e as you please.
            //e may be any type of exception at all.

        }
        finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
    }
       // Toast.makeText(getApplicationContext(), "Supposedly this worked", Toast.LENGTH_SHORT).show();


}
