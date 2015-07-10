package com.portier.portier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends Activity {

    private Button sendNPayBtn;
    private static Spinner mAtmosphere;
    private static Spinner mScene;
    private static Spinner mCity;
    private static EditText mQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_page);


        sendNPayBtn = (Button) findViewById(R.id.sendAndPayButton);
        mAtmosphere = (Spinner) findViewById(R.id.atmosphereSpinner);
        mScene = (Spinner) findViewById(R.id.sceneSpinner);
        mCity = (Spinner) findViewById(R.id.citySpinner);
        mQuestion = (EditText) findViewById(R.id.questionEditText);

        ArrayAdapter atmosAdapter = ArrayAdapter.createFromResource(this, R.array.atmosphere_array, R.layout.simple_spinner_item);
        ArrayAdapter sceneAdapter = ArrayAdapter.createFromResource(this, R.array.scene_array, R.layout.simple_spinner_item);
        ArrayAdapter cityAdapter = ArrayAdapter.createFromResource(this, R.array.city_array, R.layout.simple_spinner_item);
        mAtmosphere.setAdapter(atmosAdapter);
        mCity.setAdapter(cityAdapter);
        mScene.setAdapter(sceneAdapter);

        //Sets permission to send HTTP requests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sendNPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpPostAsyncTask().execute();
                Intent intent = new Intent(QuestionActivity.this, AnswersActivity.class);
                startActivity(intent);
            }
        });
    }

    public static List<NameValuePair> getHttpPostParams()
    {
        List<NameValuePair> mlist = new ArrayList<NameValuePair>();
        mlist.add(new BasicNameValuePair("from", ParseAPIUtils.user.getEmail()));
        mlist.add(new BasicNameValuePair("question", mQuestion.getText().toString()));
        mlist.add(new BasicNameValuePair("city", mCity.getSelectedItem().toString()));
        mlist.add(new BasicNameValuePair("scene", mScene.getSelectedItem().toString()));
        mlist.add(new BasicNameValuePair("atmosphere", mAtmosphere.getSelectedItem().toString()));
        return mlist;
    }
}
