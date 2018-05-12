package com.californiadreamshostel.officetv.NETWORK;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonDownloaderTask extends AsyncTask<String, Integer, String>{

    public static final String EMPTY = "EmptyResult";

    private JsonDownloadPackage downloadPackage;
    private int id;

    public JsonDownloaderTask(@NonNull final JsonDownloadPackage downloadPackage, final int id){
        super();
        this.downloadPackage = downloadPackage;
        this.id = id;
    }

    //Method responsible for actually fetching the data
    @Override
    protected String doInBackground(String... strings) {
        return download(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        downloadPackage.listener.onResult(s, id);
    }

    private String download(final String url){

        BufferedReader stream  = null;
        HttpURLConnection connection = null;
        final StringBuilder json = new StringBuilder();

        String result = EMPTY;

        try{
            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            connection.connect();

        final int response = connection.getResponseCode();

        if(response != HttpURLConnection.HTTP_OK)
            throw new IOException();

        final InputStream inputStream = connection.getInputStream();

        final Reader reader = new InputStreamReader(inputStream, "UTF-8");

        stream = new BufferedReader(reader);

        String currentLine = null;

        while( (currentLine = stream.readLine()) != null)
            json.append(currentLine);

        } catch(MalformedURLException e){
            result = EMPTY;
        } catch (IOException e){
            result = EMPTY;
        } finally{
            if(stream != null)
                try {
                    stream.close();
                }catch(IOException a){
                a.printStackTrace();
                }
            if(connection != null)
            connection.disconnect();

        }

        if(json.length() > 0)
            return json.toString();

        return EMPTY;
    }

}
