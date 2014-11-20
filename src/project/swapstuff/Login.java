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
import project.swapstuff.model.FacebookConnect;
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
//	LocationListener milocListener = null;
//	LocationManager milocManager = null;
//	Double latitud = 1.1;
//	Double longitud = 1.1;
//	private String proveedor;
//	Location location; // location
	SharedPreferences shared;
	String profileID="";
	 GPSTracker gpsTracker;
	 
	 String regId,registrationStatus;
	 String RegGCM="";
	 
	 Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_login);
//		getActionBar().setBackgroundDrawable(
//				new ColorDrawable(Color.parseColor("#0B90A8")));
//		getActionBar().setIcon(
//				new ColorDrawable(getResources().getColor(
//						android.R.color.transparent)));
		
//		 int titleId;
//		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//		      titleId = getResources().getIdentifier("action_bar_title", "id", "android");
//		  } else {
//		      titleId = android.R.id.title;
//		  }
//		TextView uiCtv=(TextView)findViewById(titleId);
//		uiCtv.setTextColor(Color.WHITE);
//		getActionBar().setTitle(titleId);
		
		uiC_btnGuest = (Button) findViewById(R.id.uiC_btnguest);
		uiC_btnFb = (Button) findViewById(R.id.uiC_btnfb);
		uiC_btnGuest.setOnClickListener(this);
		uiC_btnFb.setOnClickListener(this);

		
		
		
		
		
//		milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		Criteria req = new Criteria();
//		req.setCostAllowed(false); 
//		req.setAltitudeRequired(false);
//		req.setAccuracy(Criteria.ACCURACY_FINE);
//		proveedor = milocManager.getBestProvider(req, true);
//		milocListener = new GetLatitude();
//		milocManager.requestLocationUpdates(proveedor, 0, 0, milocListener);

		StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(pol);
		
		
		
		
		registerClient();

	}

	// get lattitude and longitude)
	public class GetLatitude implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {

			double latitud = loc.getLatitude();
			double longitud = loc.getLongitude();
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		
		gpsTracker=new GPSTracker(Login.this);
//		Utills.CreateDB(Login.this);
		Utills.CreateDB(Login.this);
		if (Utills.haveNetworkConnection(Login.this))
		{

			
			
			

			if (v == uiC_btnGuest) {

//				new asyncSaveProfile().execute();
				
				if (!gpsTracker.canGetLocation()) 
				{
					gpsTracker.showSettingsAlert();
					
				} 
				else 
				{
					Utills.lati=gpsTracker.getLatitude()+"";
					Utills.longi=gpsTracker.getLongitude()+"";
					
					
					 String SENDER_ID = "326171808899";

						GoogleCloudMessaging gcm = GoogleCloudMessaging
								.getInstance(Login.this);

						GCMRegistrar.register(Login.this, SENDER_ID);

						String regId = GCMRegistrar
								.getRegistrationId(Login.this)+"";
						Log.i("gcm", regId+"");
					
						if(regId.equals(""))
						{
							Log.e("gcm", regId+"");	
							Toast.makeText(Login.this, "Please wait...and try again..!", Toast.LENGTH_SHORT).show();
//							new asyncSaveProfile().execute();
						}
						else
						{
							Log.i("gcm", regId+"");
							RegGCM=regId;
							
							new asyncSaveProfile().execute();
						}
						
						
//					Log.i("response", insertLocationDetails()+"");
				
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

					 String SENDER_ID = "326171808899";

						GoogleCloudMessaging gcm = GoogleCloudMessaging
								.getInstance(Login.this);

						GCMRegistrar.register(Login.this, SENDER_ID);

						String regId = GCMRegistrar
								.getRegistrationId(Login.this)+"";
						
						CommonUtilities.GCM_ID=regId;
						Log.i("gcm", regId+"");
					
						if(regId.equals(""))
						{
							
							Log.e("gcm", regId+"");	
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
			 
		     String HostUrl="http://116.193.163.158:8083/Profiles/SaveProfile"; 
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
		                 e.printStackTrace();
		                 Error=true;
		                 
		                 Log.e("TAG", "ClientProtocolException in callWebService(). " + e.getMessage());
		             }
		             catch (IOException e)
		             {  
		                 e.printStackTrace();
		                 Log.e("TAG", "IOException in callWebService(). " + e.getMessage());
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
	
	
	
	
	
	
	
	
	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
//-------------------------
		public void registerClient() {
		try {
			// Check that the device supports GCM (should be in a try / catch)
			GCMRegistrar.checkDevice(this);

			// Check the manifest to be sure this app has all the required
			// permissions.
			GCMRegistrar.checkManifest(this);

			// Get the existing registration id, if it exists.
			regId = GCMRegistrar.getRegistrationId(this);

			System.out
					.println("******************************************************************");
			Log.v("Dashboard", "Device Id -> " + regId);
			System.out
					.println("******************************************************************");
			if (regId.equals("")) {

				registrationStatus = "Registering...";
				Log.e("status", "Exception ->" +registrationStatus);
				// register this device for this project
				GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
				regId = GCMRegistrar.getRegistrationId(this);

				registrationStatus = "Registration Acquired";
				Log.e("status", "Exception ->" +registrationStatus);
			} else {
				GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
				regId = GCMRegistrar.getRegistrationId(this);
				registrationStatus = "Already registered";
				Log.e("status", "Exception ->" +registrationStatus);
			}

		} catch (Exception e) {
			Log.e("Dashboard", "Exception -> " + e.toString());
			registrationStatus = e.getMessage();
		}
		Log.e("status", "Exception ->" +registrationStatus);
	}
//	------------------------
	
	
	
	
	
	
	
	
	
	

}