package com.example.dicdog1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Search_doctors extends ActionBarActivity {
	
private  Button button1;
private  List<Doctor> doctorList;
private  String searchJob;
private  String searchGender;
private  String searchName;
private  Spinner spinnerHospital;
private  Spinner genderspinner;
private  Button button2;
private  Intent intent;
private  String check;
private  String searchHospital;
private  AutoCompleteTextView doctorName;

List<String> hospspec;
List<String> names;
public  Intent intent2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
				actionBar.hide();
		setContentView(R.layout.activity_search_doctors);
		if(!isNetworkAvailable())
		{			
		  	showSettingsAlert("Internet");		  	
		}
		else
		{
		check="";
		hospspec=new ArrayList<String>();
		names=new ArrayList<String>();			
		hospspec=DashboardActivity.hospital_list;		
		intent2=new Intent(Search_doctors.this,SelectedDoctor.class);
		//initializing doctorlist,intent
		doctorList=new ArrayList<Doctor>();
		intent=new Intent(Search_doctors.this,DoctorsList.class);
		doctorName = (AutoCompleteTextView) findViewById(R.id.doctorNameautoComplete);
		doctorName.setOnClickListener(new View.OnClickListener() {	 	
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
			doctorName.showDropDown();
			}
			});						
		//search button
		button2=(Button)findViewById(R.id.buttonsearchdoc);		
		button2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button_performed();
			}
		});
		spinnerHospital=(Spinner)findViewById(R.id.spinner_hospital);
		//array adapter for adding string
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_text, hospspec);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    //calling nothingclass for setting the default value on spinner
	    spinnerHospital.setAdapter( new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner3_row_nothing_selected,this));
	
	  //adding second gender spinner
	  		genderspinner = (Spinner) findViewById(R.id.Spinner02);
	  		String genderspec[]={"male","female"};
	  		//array adapter for adding string
	  		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.spinner2_item_text,genderspec);
	  	    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	    //calling nothingclass for setting the default value on spinner
	  	    genderspinner.setAdapter( new NothingSelectedSpinnerAdapter(adapter2, R.layout.contact_spinner2_row_nothing_selected,this));
       	  	    
	  	  spinnerHospital.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String value11 = (String) spinnerHospital.getSelectedItem();
					String value22 = (String) genderspinner.getSelectedItem();
					ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");				
					query.whereEqualTo("Hospital", value11);
					query.setLimit(1000);
					query.findInBackground(new FindCallback<ParseObject>() {						
						@Override
						public void done(List<ParseObject> la,
								com.parse.ParseException e) {
							// TODO Auto-generated method stub
							 if(la!=null)
							 {								 
								 for(int i=0;i<la.size();i++)
								 {
									 String s=la.get(i).getString("Name");									 
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
			                  else
			                  {//handle the error}
			                	  Toast.makeText(getApplicationContext(), "There is no doctor of this category ", Toast.LENGTH_LONG).show();
			                  }						
						}
					});					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
				
			});
	  	     
	    
	  	  
	  	    //setting home button. it directs to dashboard
	  	    button1=(Button)findViewById(R.id.buttonHome);
			button1.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(Search_doctors.this,DashboardActivity.class);
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
	  		  // 	Create the adapter and set it to the AutoCompleteTextView 
  		  ArrayAdapter<String> nameadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
  		 doctorName.setAdapter(nameadapter);
		  	  	  	    	  	    	  	    	  	
		}
		else
		{
		names.add(s);
			
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
			
			if(genderspinner.getSelectedItem()==null || spinnerHospital.getSelectedItem()==null )
			{
				Toast.makeText(getApplicationContext(), "Select All categories", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Finding Doctors", Toast.LENGTH_SHORT).show();
				searchGender=(String)genderspinner.getSelectedItem();
				searchHospital = (String) spinnerHospital.getSelectedItem();
				searchName = doctorName.getText().toString();				      	
				ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
				query.whereEqualTo("Gender", searchGender);
				query.whereEqualTo("Hospital", searchHospital);
				query.whereEqualTo("Name", searchName);
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
								
								 Toast.makeText(getApplicationContext(), "No doctor of these characteristics found in this hospital ", Toast.LENGTH_LONG).show();
							 }
							 else
							 { 
								 intent2.putExtra("doctor", searchName);
								 startActivity(intent2);
							    
							 }						
		                  }
		                  else{//handle the error
		                	 
		                	  Toast.makeText(getApplicationContext(), "There is no doctor of this category ", Toast.LENGTH_LONG).show();
		                       }				
					}
				});		
			}
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
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_doctors.this);
		 alertDialog.setTitle(provider + " Settings");
		 alertDialog.setMessage(provider + " is not enabled! Please Check your Internet Connection");
		 alertDialog.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int which) {
		 //dialog.cancel();			
			 Intent back=new Intent(Search_doctors.this,DashboardActivity.class);
			 back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(back);
				finish();
		     //.os.Process.killProcess(android.os.Process.myPid());		     	 
		     //finish();
		     
		 }});
		 alertDialog.show();				
		 
		}  
 }

