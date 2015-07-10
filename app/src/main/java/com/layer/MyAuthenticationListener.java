package com.layer;

import android.os.AsyncTask;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerAuthenticationListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class MyAuthenticationListener implements LayerAuthenticationListener {

    private MainChatActivity main_activity;

    public MyAuthenticationListener(MainChatActivity ma){
        main_activity = ma;
    }

    //Called after layerClient.authenticate() executes
    //You will need to set up an Authentication Service to take a Layer App ID, User ID, and the
    //nonce to create a Identity Token to pass back to Layer
    public void onAuthenticationChallenge(final LayerClient client, final String nonce) {
        final String mUserId = MainChatActivity.getUserID();

        //Note: This Layer Authentication Service is for TESTING PURPOSES ONLY
        //When going into production, you will need to create your own web service
        //Check out https://developer.layer.com/docs/guides#authentication for guidance
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    HttpPost post = new HttpPost("https://layer-identity-provider.herokuapp.com/identity_tokens");
                    post.setHeader("Content-Type", "application/json");
                    post.setHeader("Accept", "application/json");

                    JSONObject json = new JSONObject()
                            .put("app_id", client.getAppId())
                            .put("user_id", mUserId)
                            .put("nonce", nonce );
                    post.setEntity(new StringEntity(json.toString()));

                    HttpResponse response = (new DefaultHttpClient()).execute(post);
                    String eit = (new JSONObject(EntityUtils.toString(response.getEntity())))
                            .optString("identity_token");

                    client.answerAuthenticationChallenge(eit);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).execute();
    }

    //Called when the user has successfully authenticated
    public void onAuthenticated(LayerClient client, String arg1) {

        //Start the conversation view after a successful authentication
        System.out.println("Authentication successful");
        if(main_activity != null)
            main_activity.onUserAuthenticated();
    }

    //Called when there was a problem authenticating
    //Common causes include a malformed identity token, missing parameters in the identity token, missing
    //or incorrect nonce
    public void onAuthenticationError(LayerClient layerClient, LayerException e) {
        System.out.println("There was an error authenticating: " + e);
    }

    //Called after the user has been deauthenticated
    public void onDeauthenticated(LayerClient client) {
        System.out.println("User is deauthenticated");
    }
}