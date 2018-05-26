package com.californiadreamshostel.officetv.NETWORKING.JSON;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Job is to simply fetch a Json String,
 * from the requested URL and return the result
 */
public class JsonDownloadTask extends AsyncTask<String, Integer, String> {

    public static final String TAG = JsonDownloadTask.class.getSimpleName();

    private static final String REQUEST_TYPE = "GET";

    private ResponseListener listener;

    public JsonDownloadTask(@NonNull final ResponseListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... urls) {

        final String data = download(urls[0]);
            if(data == null) return "";

       Log.i("OfficeTv", "ENd  of DoinBackground#AsyncTask<>. DATA: " + data);

        return data;
    }

    @Nullable
    private String download(@NonNull final String url){

        Reader dataReader;
        String data = "";

        try{

            final HttpURLConnection connection = (HttpURLConnection)
                    new URL(url).openConnection();

            connection.setRequestMethod(REQUEST_TYPE);
            connection.connect();

            dataReader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));


            Log.i("JsonDownloadTask", getLogMsg(connection));

            connection.disconnect();

            final String tempData = readData(new StringBuilder(), ((BufferedReader)dataReader), "", true);

            if(tempData != null)
                data = tempData;

            //Should move to finally?
            dataReader.close();

        }catch (MalformedURLException ex){
            ex.printStackTrace();
            return null;
        }catch (IOException ioException){
            ioException.printStackTrace();
            return null;
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onResponse(result);
    }


    //Recursively comb through the Bytes, to compose a compacted string
    @Nullable
    private String readData(final StringBuilder b, final BufferedReader reader,
                            @Nullable String data, boolean startFlag) throws IOException{

        Log.i("OfficeTv", "Reading data from the Buffered Reader. @ Data: "+data);

        if(data == null)
            return b.toString();

        if(!startFlag)
            b.append(data);

        return readData(b, reader, reader.readLine(), false);
    }

    private static String getLogMsg(final HttpURLConnection connection) throws IOException{
        return "The requested data from resource: " + connection.getURL().getPath() +
                " Returned a " + String.valueOf(connection.getResponseCode()) + " Code" + " With a message of:"
                + connection.getResponseMessage();
    }

}




