package com.portier.portier;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by garland on 2/28/15.
 */
public class HttpPostAsyncTask extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://bumblebee-battle.herokuapp.com/send");
        try
        {
            httppost.setEntity(new UrlEncodedFormEntity(QuestionActivity.getHttpPostParams()));
            HttpResponse response = httpclient.execute(httppost);
            // write response to log
            Log.d("Http Post Response:", response.toString());
            int status = response.getStatusLine().getStatusCode();

            if (status == 200)
            {
                return null;
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void result)
    {
        Log.d("HTTP POST", "TASK DONE@@@");
    }
}
