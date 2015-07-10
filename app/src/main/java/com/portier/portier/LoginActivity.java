package com.portier.portier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;


public class LoginActivity extends Activity
{

    private Button loginB;
    private Button registerB;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText displayNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        loginB = (Button) findViewById(R.id.loginButton);
        registerB = (Button) findViewById(R.id.registerButton);
        usernameEditText = (EditText) findViewById(R.id.userSignUpName);
        passwordEditText = (EditText) findViewById(R.id.userSignUpPassword);
        displayNameEditText = (EditText) findViewById(R.id.userSignUpDisplayname);

        loginB.setOnClickListener(loginListener);
        registerB.setOnClickListener(registerListener);

        Parse.initialize(this, Constants.APP_ID, Constants.CLIENT_KEY);
    }

    /**
     * OnClickListeners for Buttons
     */
    private View.OnClickListener loginListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String un = usernameEditText.getText().toString();
            String pw = passwordEditText.getText().toString();
            String dn = displayNameEditText.getText().toString();
            if(un.equals("") || pw.equals("") || dn.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ParseAPIUtils.login(un, pw, dn, getApplicationContext());
                //Login Successful
                Intent intent = new Intent(LoginActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener registerListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String un = usernameEditText.getText().toString();
            String pw = passwordEditText.getText().toString();
            String dn = displayNameEditText.getText().toString();
            if(un.equals("") || pw.equals("") || dn.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ParseAPIUtils.register(un, pw, dn, getApplicationContext());
            }
        }
    };

}
