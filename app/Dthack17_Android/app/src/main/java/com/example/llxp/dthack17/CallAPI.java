package com.example.llxp.dthack17;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by llxp on 19.09.17.
 */

public class CallAPI extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlString = "https://dthack17.de/api/sensors"; // URL to call

        String data = strings[0]; //data to post

        DataOutputStream out = null;
        try {

            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoOutput(true);

            out = new DataOutputStream(urlConnection.getOutputStream());

            //BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

            //writer.write(data);

            out.writeBytes(data);

            //writer.flush();

            //writer.close();

            //out.close();

            out.flush();
            out.close();

            urlConnection.connect();

            Log.d("asyncTask", "success");

            Log.d("asyncTask", String.valueOf(urlConnection.getResponseCode()));
            return "";


        } catch (Exception e) {

            System.out.println(e.getMessage());

            return e.getMessage();
            //Log.d("error", e.getMessage());
        }
    }
}
