package project.swapstuff;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import project.swapstuff.model.CommonUtilities;
import project.swapstuff.model.GPSTracker;
import project.swapstuff.model.Utills;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Login extends Activity implements OnClickListener {

	Button uiC_btnGuest, uiC_btnFb;
	SharedPreferences shared;
	String profileID="";
	 GPSTracker gpsTracker;
	 
	 String regId="",registrationStatus="";
	 String RegGCM="";
	 
	 Toast toast;

	 GoogleCloudMessaging gcm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_login);


		
		uiC_btnGuest = (Button) findViewById(R.id.uiC_btnguest);
		uiC_btnFb = (Button) findViewById(R.id.uiC_btnfb);
		uiC_btnGuest.setOnClickListener(this);
		uiC_btnFb.setOnClickListener(this);

		
		
		

		StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(pol);
		
		
		
		
//		registerClient();
		getRegisterationID();

	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		getRegisterationID();
		
		gpsTracker=new GPSTracker(Login.this);

//		Utills.CreateDB(Login.this);
		if (Utills.haveNetworkConnection(Login.this))
		{

			
			
			

			if (v == uiC_btnGuest) {

				
				if (!gpsTracker.canGetLocation()) 
				{
					gpsTracker.showSettingsAlert();
					
				} 
				else 
				{
					Utills.lati=gpsTracker.getLatitude()+"";
					Utills.longi=gpsTracker.getLongitude()+"";
					
						
					
						if(regId.equals(""))
						{
							getRegisterationID();
							Log.e("gcm", regId+"");	
							
						}
						else
						{
							Log.i("gcm", regId+"");
							RegGCM=regId;
							
							new asyncSaveProfile().execute();
						}
						
						

				
				}



			}
			else if (v == uiC_btnFb) 
			{
				if (!gpsTracker.canGetLocation()) 
				{
					gpsTracker.showSettingsAlert();
					
				} 
				else 
				{
					Utills.lati=gpsTracker.getLatitude()+"";
					Utills.longi=gpsTracker.getLongitude()+"";


						if(regId.equals(""))
						{
							getRegisterationID();
							
						}
						else
						{
							Log.i("gcm", CommonUtilities.GCM_ID+"");
							CommonUtilities.GCM_ID=regId;
							RegGCM=regId;
							
							Intent postOnFacebookWallIntent = new Intent(this,
									FacebookConnect.class);
							startActivity(postOnFacebookWallIntent);
						}

					
				}

				
				
			}

		} else {
			if(toast!=null)
			{
				toast.cancel();
			}
			
			toast=Toast.makeText(Login.this, "No network connection..!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,0, 0);
			toast.show();		}

	}

	

	
	 class asyncSaveProfile extends AsyncTask<Void, Void, Void>
	{
		 
		 
		 boolean Error=false; 
		 
		ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
		
			pd=ProgressDialog.show(Login.this, "", "Connecting..");
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {       
			 String result = "";
			 
		     String HostUrl=Utills.URL+"Profiles/SaveProfile"; 
//		     String HostUrl="http://116.193.163.156:8012/Profiles/SaveProfile"; 
		 	
		      HttpClient httpClient = new DefaultHttpClient();
		      HttpPost httpPost = new HttpPost(HostUrl);

		      
		     
		      
		      
		     try
		     {  
		       List<NameValuePair> params1 = new LinkedList<NameValuePair>();

		      
//		       "ProfileId": 231,
//		       "Username": "test",
//		       "Latitude": 30.71,
//		       "Longitude": 76.69,
//		       "DateTimeCreated": "2014-10-13T19:24:36.657",
//		       "Distance": 20,
//		       "GCM_RegistrationID": "APA91bGxDhXZefRa1Z6oUesCshZWBI2PvktYCZV2dJ8MhxXRhoV6H0hqGM9fcmqcqsWTlvGouSJbmZO-AogAZ9yFO0bS8Tb_PONWkVpdclt1nxwA2hqMjbQmCS5nOHFUtFbQJf1p3qHWAw6mYtFUpbM_zN_bYEftHA",
//		       "ItemMatchNotification": true,
//		       "ChatNotification": true
		       params1.add(new BasicNameValuePair("ProfileId","-1"));
		       params1.add(new BasicNameValuePair("Username", "test"));
		       params1.add(new BasicNameValuePair("Latitude",gpsTracker.getLatitude()+""));
		       params1.add(new BasicNameValuePair("Longitude",gpsTracker.getLongitude()+""));
		       params1.add(new BasicNameValuePair("DateTimeCreated",""));
		       params1.add(new BasicNameValuePair("Distance","100"));
		       params1.add(new BasicNameValuePair("GCM_RegistrationID",RegGCM));
		      
		       params1.add(new BasicNameValuePair("ItemMatchNotification","1"));
		       params1.add(new BasicNameValuePair("ChatNotification","1"));

		       Log.e("Request ProfileID", params1.toString()+"");
		       
		       
		       httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		       httpPost.setHeader("Accept", "application/json");

		       HttpEntity entity = new UrlEncodedFormEntity(params1, "UTF-8");
		       httpPost.setEntity(entity);

		       ResponseHandler<String> handler = new BasicResponseHandler();
		       result =httpClient.execute(httpPost,handler);
		       
		       profileID=result;
		       Log.e("Response", profileID+"");
		                         }
		             catch (ClientProtocolException e)
		             {  
//		                 e.printStackTrace();
		                 Error=true;
		                 
		                 
		             }
		             catch (IOException e)
		             {  
//		                 e.printStackTrace();
		                
		             }

		   return null;

		         }
		@Override
		protected void onPostExecute(Void result) {
			
			pd.dismiss();
			
			if(profileID.contains("-"))
			{
				Utills.showToast(Login.this, "Error");
			}
			else
			{
				if(Error)
				{
					Utills.showToast(Login.this, "Error");
				}
				else
				{
					
				Toast.makeText(Login.this, "Profile created Successfully..!" , Toast.LENGTH_SHORT).show();
				
				Utills.km=100;
				Utills.id=profileID;
				shared = getSharedPreferences("", MODE_PRIVATE);
				Editor ed = shared.edit();

				ed.putString("id",profileID );
				ed.putInt("km", 100);
				ed.putString("noti","1");
				ed.putString("matchnoti","1");
				ed.putString("fbid", Utills.FbID);
				ed.commit();
				Intent gotoHome = new Intent(getApplicationContext(),
						ProfileCreate.class);
				startActivity(gotoHome);
				
				finish();
			}
				
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	
	
	
	
	
	
	
	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

	
	
	
		
	
	@SuppressWarnings("unchecked")
	public void getRegisterationID() {
		
		  new AsyncTask() {
			   @Override
			   protected Object doInBackground(Object... params) {
			    // TODO Auto-generated method stub
			    String msg = "";
			    try {
			     if (gcm == null) {
			      gcm = GoogleCloudMessaging.getInstance(Login.this);
			     }
			     regId = gcm.register(CommonUtilities.SENDER_ID);
			     Log.d("in async task", regId);
			  
			         // try
			     msg = "Device registered, registration ID=" + regId;
			    
			    } catch (IOException ex) {
			     msg = "Error :" + ex.getMessage();
			  
			    }
			    return msg;
			   }
			  }.execute(null, null, null);
		
	}
	
	
	
	

}