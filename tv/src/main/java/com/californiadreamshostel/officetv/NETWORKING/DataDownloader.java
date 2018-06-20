package com.californiadreamshostel.officetv.NETWORKING;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Downloads a string of data from a REST Api Endpoint
 */

public class DataDownloader {

    public static final String TAG = DataDownloader.class.getSimpleName();

    public static final String ERROR_TAG = TAG+"_ERROR";

    public static final String  REQUEST_TYPE = "GET";

    public static final String NO_DATA = "DataDownloader_Empty";

    @Nullable
    public static String download(@NonNull final URL url) throws IOException{

        Reader dataReader;
        String data = NO_DATA;

        final HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(REQUEST_TYPE);
        connection.connect();

        dataReader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));

        Log.i(TAG, getLogMsg(connection));

        final String tempData = readData(new StringBuilder(), ((BufferedReader)dataReader), "", true);

        if(tempData != null)
            data = tempData;

        //Should move to finally?
        connection.disconnect();
        dataReader.close();

    return data;
    }

    /**
     * Constructs input from a series of bytes into a compact string
     * by recursively combing through a Reader object and appeneding the string
     * value of the byte.
     *
     * @param b Holds the parsed string data
     * @param reader The source of data bytes
     * @param data The string representation of the next line of bytes
     * @param startFlag Flag to toggle the StringBuilder. Should be false when a client calls it
     * @return
     * @throws IOException
     */
    @Nullable
    private static String readData(final StringBuilder b, final BufferedReader reader,
                            @Nullable String data, boolean startFlag) throws IOException{

        Log.i(TAG, "Reading data from the Buffered Reader. @ Data: " + data);

        if(data == null)
            return b.toString();

        if(!startFlag)
            b.append(data);

        return readData(b, reader, reader.readLine(), false);
    }

    private static String getLogMsg(final HttpURLConnection connection) throws IOException{
        return "The requested data from resource: " + connection.getURL().toString() +
                " Returned a " + String.valueOf(connection.getResponseCode()) + " Code" + " With a message of:"
                + connection.getResponseMessage();
    }


}
