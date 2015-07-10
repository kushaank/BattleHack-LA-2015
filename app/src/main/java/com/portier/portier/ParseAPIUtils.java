package com.portier.portier;

import android.content.Context;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class ParseAPIUtils 
{
	public static ParseUser user;
	
	public static boolean getUser()
	{
		user = ParseUser.getCurrentUser();
		if(user == null)
		{
			return false;
		}
		return true;
	}
	
	public static void login(String un, String pw, String dn, final Context context)
	{
		user = new ParseUser();
		user.setUsername(dn);
		user.setPassword(pw);
		user.setEmail(un);
		user.add(Constants.DISPLAY_NAME, dn);
		
		ParseUser.logInInBackground(dn, pw, new LogInCallback()
		{
			  public void done(ParseUser user, ParseException e) 
			  {
				  if (e == null) 
				    {
				    	Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
				    } 
				    else 
				    {
				    	Toast.makeText(context, "Invalid Login", Toast.LENGTH_SHORT).show();
				    }
			  }
			});
		getUser();
	}
	
	public static void register(String un, String pw, String dn, final Context context)
	{
		user = new ParseUser();
		user.setUsername(dn);
		user.setPassword(pw);
		user.setEmail(un);
		 
		user.signUpInBackground(new SignUpCallback() 
		{
		  public void done(ParseException e) 
		  {
		    if (e == null) 
		    {
		    	Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show();
		    } 
		    else 
		    {
		    	Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();
		    }
		  }
		});
	}
}
