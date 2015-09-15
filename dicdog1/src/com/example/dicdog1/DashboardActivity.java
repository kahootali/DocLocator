package com.example.dicdog1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DashboardActivity extends ActionBarActivity {
private Button button1;
private Button buttontime;
private Button buttonDoctors;
private int buttonCount;
public static List<String> hospital_list=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar=getSupportActionBar();
		bar.hide();
		setContentView(R.layout.activity_dashboard);		
		buttonCount=0;				
		 if(!isNetworkAvailable())		
		 {
			//Toast.makeText(getApplicationContext(),"Network Problem.Please Check Your Internet Connection & try again", Toast.LENGTH_LONG).show();
		  	showSettingsAlert("Internet");		  			
		 }							
		  else
		  {			  
				ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
				query.whereExists("Hospital");
				query.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(List<ParseObject> la,
							com.parse.ParseException e) {
						// TODO Auto-generated method stub
						 if(la!=null){
							 
							 for(int i=0;i<la.size();i++)
								{
								 String s=la.get(i).getString("Hospital");
								 if(!(hospital_list.contains(s)))
								 {
									 hospital_list.add(s);
									 //Toast.makeText(getApplicationContext(), hospital_list.get(0), Toast.LENGTH_LONG).show();									 
								 }};}
		                  else{//handle the error}
		                	  Toast.makeText(getApplicationContext(),"Network Problem.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
		                	  showSettingsAlert("Internet");
		                            }
						
					}
				});				
			if(hospital_list.isEmpty())
			{
				try {
				    Thread.sleep(2000);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}			
		Button buttonLoc=(Button)findViewById(R.id.buttonLocation);
		buttonLoc.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(DashboardActivity.this,SearchMaps.class);					
				if(isNetworkAvailable())
				{
					startActivity(i);
					finish();
				}
				else
				{
					//Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
					showSettingsAlert("Internet");
				}
						      
			}
		});					
		button1=(Button)findViewById(R.id.buttonHosp);
		button1.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(DashboardActivity.this,DoctorsActivity.class);				
				if(isNetworkAvailable())
				{
					startActivity(i);
					finish();
				}
				else
					showSettingsAlert("Internet");
					//Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();				
				//finish();
			}
		});			
		buttonDoctors=(Button)findViewById(R.id.buttonDoctor);			
		buttonDoctors.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(DashboardActivity.this,Search_doctors.class);				
				if(isNetworkAvailable())
				{
					startActivity(i);
					finish();
				}
				else
					showSettingsAlert("Internet");
					//Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();				
				//finish();		      
			}
		});			
		buttontime=(Button)findViewById(R.id.buttontime);		
		buttontime.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(DashboardActivity.this,SearchingByTime.class);				
				if(isNetworkAvailable())
				{
					startActivity(i);
					finish();
				}
				else
					showSettingsAlert("Internet");
					//Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
				//finish();		      
			}
		});		
	  }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.dashboard, menu);
		getMenuInflater().inflate(R.menu.dashboard, menu);	
		return true;
	}
	public void onResume()
	{
		super.onResume();			 
	};
	public void onPause()
	{
		super.onPause();		
	};
	 protected void onStop() 
	 {
		 super.onStop();
		 
	 };
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.help) {
			onHelp();
			return true;
		}
		if (id == R.id.About) {
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("About Us")
            .setMessage("DocDig is an app that lets you find doctors in Islamabad "
            		+ "\n\n\n Sponsored By Code for Pakistan")
            .setPositiveButton("Ok", null).setInverseBackgroundForced(true).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}		
	public void onHelp() {
	       new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Help")
	               .setMessage("DocDig is an app that lets you find doctors in Islamabad "
	               		+ "through following options..\n\n"
	               		+ "1. Search By Doctor --  In this option you would enter the Hospital name and "
	               		+ "the name of the doctor and the gender and the details of that doctor would be shown\n\n"
	               		+ "2. Search By Time -- You would select the hospital name, speciality, gender and the required date and time "
	               		+ "and all the doctors of that category will be shown\n\n"
	               		+ "3. Search By Hospital -- In this option, you would enter the hospital name, speciality and gender and the "
	               		+ "list of doctors would be shown")
	               .setPositiveButton("Okay, Got it", null).show();
	   }	
	public Animation inFromRightAnimation()
	{
	    Animation inFromRight = new TranslateAnimation(
	            Animation.RELATIVE_TO_PARENT, +1.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f);
	    inFromRight.setDuration(240);
	    inFromRight.setInterpolator(new AccelerateInterpolator());
	    return inFromRight;
	}
	public Animation outToRightAnimation()
	{
	    Animation outtoLeft = new TranslateAnimation(
	            Animation.RELATIVE_TO_PARENT, -1.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoLeft.setDuration(240);
	    outtoLeft.setInterpolator(new AccelerateInterpolator());
	    return outtoLeft;
	}	
	public void onBackPressed()
	{
	    if(buttonCount >= 1)
	    {
	        //Intent intent = new Intent(Intent.ACTION_MAIN);
	        //intent.addCategory(Intent.CATEGORY_HOME);
	        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);	        
	        buttonCount=0;
	        //startActivity(intent);	        
	        //android.os.Process.killProcess(android.os.Process.myPid());
	        finish();
	        System.exit(0);	        
	    }
	    else
	    {
	        Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
	        buttonCount++;
	    }
	}
	/*private boolean isNetworkAvailable() {
		ConnectivityManager cm =
		        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();		
		return isConnected; 	    
 	}*/
	private boolean isNetworkAvailable() {
		 Runtime runtime = Runtime.getRuntime();
		    try {

		        Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
		        int     exitValue = ipProcess.waitFor();
		        return (exitValue == 0);

		    } catch (IOException e)          { e.printStackTrace(); } 
		      catch (InterruptedException e) { e.printStackTrace(); }

		    return false; 	    
	}
	public void showSettingsAlert(String provider) {
		
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
		 alertDialog.setTitle(provider + " Settings");
		 alertDialog.setMessage(provider + " is not enabled! Please try again");
		 alertDialog.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int which) {
		 //dialog.cancel();			 	       
		        buttonCount=0;		        	       
		        //android.os.Process.killProcess(android.os.Process.myPid());
		        finish();
		        System.exit(0);
		     
		 }});
		 alertDialog.show();
		 
		}   
}