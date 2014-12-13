package project.swapstuff;

import java.io.IOException;
import java.util.ArrayList;
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
import project.swapstuff.model.ControlDB;
import project.swapstuff.model.GPSTracker;
import project.swapstuff.model.Profile_info;
import project.swapstuff.model.Utills;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

	SharedPreferences shared;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);

		StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(pol);

		GPSTracker gpsTracker;
		gpsTracker = new GPSTracker(SplashScreen.this);
		if (gpsTracker.canGetLocation()) {
			
			Utills.latitud = gpsTracker.getLatitude();
			Utills.longitud = gpsTracker.getLongitude();
			
		}
		CommonUtilities.FragmentToOpen=0;
		// final ArrayList<Profile_info> infoUser=
		// ControlDB.SelectFromUserDetails(SplashScreen.this);
		shared = getSharedPreferences("", MODE_PRIVATE);

		Thread background = new Thread() {
			public void run() {

				try {

					sleep(2 * 1000); // 2 seconds
					
					
					SharedPreferences SharedPref_StartUp=getSharedPreferences("Startup", MODE_PRIVATE);
					if(!SharedPref_StartUp.contains("tutorial"))
					{
						Intent gotoHome = new Intent(getApplicationContext(),
								TestViewPager.class);
						startActivity(gotoHome);
						finish();
					}
					else
					{
					
					if (shared.contains("id")) {
						Utills.id = shared.getString("id", "4");
						Utills.km = shared.getInt("km", 1);

						Utills.NotificationEnabled = shared.getString("noti",
								"1");
						Utills.MatchNotificationEnabled = shared.getString(
								"matchnoti", "1");

						Utills.FbID = shared.getString("fbid", "test");

						if (shared.contains("itemid"))

						{

							Utills.itemid = shared.getString("itemid", "2");
						

							new asyncUpdateLOCATION().execute();
							System.out.println("location" + Utills.longitud  + "");
							
							Intent gotoHome = new Intent(
									getApplicationContext(),
									MainActivitySwapStuff.class);
							startActivity(gotoHome);
							finish();
						} else {
							Intent gotoHome = new Intent(
									getApplicationContext(),
									ProfileCreate.class);
							startActivity(gotoHome);
							finish();
						}

					} else {
						Intent gotoHome = new Intent(getApplicationContext(),
								Login.class);
						startActivity(gotoHome);
						finish();
					}
					
					
					
					
					}
					
					
					
					


				} catch (Exception e) {

				}
			}
		};

		background.start();

	}

	// LOCATION UPADTE
	class asyncUpdateLOCATION extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String result = "";

			String HostUrl = Utills.URL+"Profiles/SaveProfile";
			// String HostUrl =
			// "http://116.193.163.156:8012/Profiles/SaveProfile";

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();

				params1.add(new BasicNameValuePair("ProfileId", (Utills.id)
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("Username", Utills.FbID));
				params1.add(new BasicNameValuePair("Latitude", Utills.latitud
						+ ""));
				params1.add(new BasicNameValuePair("Longitude", Utills.longitud
						+ ""));
				params1.add(new BasicNameValuePair("DateTimeCreated", ""));
				params1.add(new BasicNameValuePair("Distance", Utills.km + ""));
				params1.add(new BasicNameValuePair("ItemMatchNotification",
						Utills.MatchNotificationEnabled + ""));
				params1.add(new BasicNameValuePair("ChatNotification",
						Utills.NotificationEnabled + ""));

			

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				HttpEntity entity = new UrlEncodedFormEntity(params1, "UTF-8");
				httpPost.setEntity(entity);

				ResponseHandler<String> handler = new BasicResponseHandler();
				result = httpClient.execute(httpPost, handler);

			
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
		}

	}

}
