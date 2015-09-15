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
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchMaps extends ActionBarActivity {

private static List<Doctor> doctorList;
private static String searchJob;
//private static String searchGender;
private static String speciality;
//private static Spinner genderspinner;
private static Button button2;
private static Button button1;
private static String check;
private static AutoCompleteTextView specialityName;
List<String> names;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_search_maps);
		if(!isNetworkAvailable())
		{			
		  	showSettingsAlert("Internet");		  	
		}
		else
		{
		check="";		
		names=new ArrayList<String>();								 	
		//initializing doctorlist,intent
		doctorList=new ArrayList<Doctor>();		
		
		//search button
		button2=(Button)findViewById(R.id.buttonsearchdoc);		
		button2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button_performed();
			}
		});
		
	
	  //adding second gender spinner
	  		//genderspinner = (Spinner) findViewById(R.id.Spinner02);
	  		//String genderspec[]={"male","female"};
	  		//array adapter for adding string
	  		//ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.spinner2_item_text,genderspec);
	  	    //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	    //calling nothingclass for setting the default value on spinner
	  	    //genderspinner.setAdapter( new NothingSelectedSpinnerAdapter(adapter2, R.layout.contact_spinner2_row_nothing_selected,this));
			ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
			//query.whereEqualTo("Gender",value22 );							
			query.findInBackground(new FindCallback<ParseObject>() {						
				@Override
				public void done(List<ParseObject> la,
						com.parse.ParseException e) {
					// TODO Auto-generated method stub
					 if(la!=null)
					 {								 
						 for(int i=0;i<la.size();i++)
						 {
							 String s=la.get(i).getString("Job");									 
							 if(i==la.size()-1)
							 {
								 check="end";
								 method_done(check);
								 break;
							 }
							 else if(!(names.contains(s)))
							 {
								 method_done(s); 
							 }
						  };
					  }	                  						
				}
			});				
			specialityName = (AutoCompleteTextView) findViewById(R.id.specialityName);
			specialityName.setOnClickListener(new View.OnClickListener() {			  			
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//String value22 = (String) genderspinner.getSelectedItem();						
							specialityName.showDropDown();
						}
						});
	  	    //setting home button. it directs to dashboard
	  	    button1=(Button)findViewById(R.id.buttonHome);
			button1.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(SearchMaps.this,DashboardActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);				
					startActivity(i);
					finish();
				}
			});
	}
	}	
	public void method_done(String s)
	{
		if(s.equals("end"))
		{
  		  ArrayAdapter<String> nameadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
  		 specialityName.setAdapter(nameadapter);		  	  	  	    	  	    	  	    	  
		}
		else
		{
			//Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
			if(!names.contains(s))
			{
				names.add(s);
			}
			
		}					
   }

	@Override
	public void onBackPressed() {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
        
	} 

	//button for extracting data from database and showing on listview
	public void button_performed()
	{
		if(!isNetworkAvailable())
		{			
		  	showSettingsAlert("Internet");		  	
		}
		else
		{					
			//searchGender=(String)genderspinner.getSelectedItem();			
			speciality = specialityName.getText().toString();				      	
			ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
			query.whereEqualTo("Job", speciality);			
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> la,
						com.parse.ParseException e) {
					// TODO Auto-generated method stub
					 if(la!=null)
					 {
						 //startActivity(intent);
						 if(la.size()==0)
						 {
							 Toast.makeText(getApplicationContext(), "No doctor of these characteristics found", Toast.LENGTH_LONG).show();
						 }
						 else
						 {
							 //Toast.makeText(getApplicationContext(), "Locating All the Hospitals with this Speciality on Maps", Toast.LENGTH_LONG).show();							 
							 Intent intent2=new Intent(SearchMaps.this,MapsActivity.class);
							 //intent2.putExtra("gender", searchGender);
							 intent2.putExtra("job", speciality);
						     startActivity(intent2);								 
						 }
						
	                  }
	                  else{//handle the error}
	                	  Toast.makeText(getApplicationContext(), "There is no doctor of this category ", Toast.LENGTH_LONG).show();
	                       }					
				}
			});
		}
	}
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
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchMaps.this);
		 alertDialog.setTitle(provider + " Settings");
		 alertDialog.setMessage(provider + " is not enabled! Please Check your Internet Connection");
		 alertDialog.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int which) {
		 //dialog.cancel();			
		        //android.os.Process.killProcess(android.os.Process.myPid());		       
			 Intent back=new Intent(SearchMaps.this,DashboardActivity.class);
			 back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(back);
				finish();		     
		 }});
		 alertDialog.show();				
		 
		}   
}

