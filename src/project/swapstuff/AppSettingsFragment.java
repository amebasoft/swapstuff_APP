package project.swapstuff;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import project.swapstuff.model.ControlDB;
import project.swapstuff.model.GPSTracker;
import project.swapstuff.model.Utills;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AppSettingsFragment extends Fragment {

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("destroy", "hiiiiiiiiiiiiiiiiiiiiiiiiiiiii");

		// if (Utills.km != km) {
		// Utills.km = km;
		//
		// Utills.latitud = gpsTracker.getLatitude();
		// Utills.longitud = gpsTracker.getLongitude();
		// if(Utills.haveNetworkConnection(getActivity()))
		// {
		// new asyncUpdateKM().execute();
		// }
		// else
		// {
		// Utills.showToast(getActivity(), "No network connection..!");
		// }
		//
		//
		// gpsTracker.stopUsingGPS();
		//
		// SharedPreferences shared = getActivity().getSharedPreferences("",
		// Context.MODE_PRIVATE);
		// Editor ed = shared.edit();
		// ed.putInt("km", km);
		// ed.commit();
		// }

		getActivity().getActionBar().setDisplayShowCustomEnabled(false);

		super.onDestroy();
	}

	public AppSettingsFragment() {
	}

	public AppSettingsFragment(Context conn) {

		this.con = conn;
	}

	SeekBar uiC_seekDistance;
	TextView uiC_txtvDistance;
	Context con;
	Button uiC_btnDelprofile;
	SharedPreferences shared;
	String profileID = "";
	int km;

	ToggleButton uiC_tgbtnNotify;
	ToggleButton uiC_tgbtnMatchNotify;

	// LocationListener milocListener = null;
	// LocationManager milocManager = null;
	// static Double latitud = 1.1;
	// static Double longitud = 1.1;
	// private String proveedor;
	// Location location; // location
	GPSTracker gpsTracker;
	
	Toast toast;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_appsettings,
				container, false);

		// -----------action bar
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayOptions(actionBar.getDisplayOptions()
				| ActionBar.DISPLAY_SHOW_CUSTOM);
		TextView tView = new TextView(actionBar.getThemedContext());
		tView.setText("Save");
		tView.setTextSize(20);
		tView.setTextColor(Color.WHITE);
//		tView.setBackgroundResource(R.drawable.savebtn_bg);
		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);
		layoutParams.rightMargin = 22;
		tView.setLayoutParams(layoutParams);
		actionBar.setCustomView(tView);
		tView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			
					
					if (Utills.haveNetworkConnection(getActivity())) 
					{
						Utills.km = km;
						Utills.latitud = gpsTracker.getLatitude();
						Utills.longitud = gpsTracker.getLongitude();
						
						new asyncUpdateKM().execute();
						
						SharedPreferences shared = getActivity()
								.getSharedPreferences("", Context.MODE_PRIVATE);
						Editor ed = shared.edit();
						ed.putInt("km", km);
						ed.putString("noti", Utills.NotificationEnabled+"");
						ed.putString("matchnoti", Utills.MatchNotificationEnabled+"");
						ed.commit();
						
						
						if(toast!=null)
						{
							toast.cancel();
						}
						
						toast=Toast.makeText(getActivity(), "Settings Saved ..!", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER,0, 0);
						toast.show();
						
						
						
					} else
					{
						if(toast!=null)
						{
							toast.cancel();
						}
						
						toast=Toast.makeText(getActivity(), "No network connection..!", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER,0, 0);
						toast.show();
					}
					gpsTracker.stopUsingGPS();

					
					
//					SharedPreferences shared = getActivity()
//							.getSharedPreferences("",
//									Context.MODE_PRIVATE);
//					Editor ed = shared.edit();
//					ed.putString("noti", "0");
//					ed.commit();
					
//					Utills.showToast(getActivity(), "Settings Saved ..!");
				

			}
		});
		// ---------------------

		// shared = con.getSharedPreferences("", con.MODE_PRIVATE);
		profileID = Utills.id;
		km = Utills.km;

		StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(pol);

		gpsTracker = new GPSTracker(getActivity());

		if (gpsTracker.canGetLocation()) {
			Log.i("location", gpsTracker.getLatitude() + "");
		} else {
			gpsTracker.showSettingsAlert();
		}



		uiC_seekDistance = (SeekBar) rootView
				.findViewById(R.id.uiC_seekdistance);
		uiC_txtvDistance = (TextView) rootView.findViewById(R.id.uiC_txtvKm);
		uiC_btnDelprofile = (Button) rootView.findViewById(R.id.uiC_btndel);
		uiC_tgbtnNotify = (ToggleButton) rootView
				.findViewById(R.id.uiC_tgbtnmsgs);
		uiC_tgbtnMatchNotify = (ToggleButton) rootView
				.findViewById(R.id.uiC_tgbtnMatchNotify);

		uiC_seekDistance.setProgress(km);
		uiC_txtvDistance.setText(km + "" + " km");
		uiC_seekDistance
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {

					}

					@Override
					public void onProgressChanged(SeekBar arg0, int kms,
							boolean arg2) {

						uiC_txtvDistance.setText(String.valueOf(kms) + " km");
						km = kms;

					}
				});

		uiC_btnDelprofile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Utills.haveNetworkConnection(getActivity())) {

					
					
					new AlertDialog.Builder(getActivity())
				    .setTitle("Warning !")
				    .setMessage("Are you sure,You want to delete account ?")
				    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
			
//				        -------------
				        	if (Utills.FbID.equals("test"))
							{
								new deleteProfile().execute();
							}
							else
							{
								shared = getActivity().getSharedPreferences("",
										Context.MODE_PRIVATE);
								Editor ed = shared.edit();
								ed.clear();
								ed.commit();
								ControlDB.deleteuserdetails(getActivity());
								Utills.FbID = "test";
								Utills.showToast(getActivity(),
										"Profile deleted Successfully !");

								Intent gotologin = new Intent(getActivity(),
										SplashScreen.class);
								getActivity().startActivity(gotologin);
								getActivity().finish();

							}
//				        	-------------
				        }
				     })
				    .setNegativeButton("No", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				           dialog.dismiss();
				        }
				     })
				    .setIcon(android.R.drawable.ic_dialog_alert)
				     .show();
					
					
					

				} else {
					if(toast!=null)
					{
						toast.cancel();
					}
					
					toast=Toast.makeText(getActivity(), "No network connection..!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER,0, 0);
					toast.show();
				}

			}
		});

		if (Utills.NotificationEnabled.equals("1")) {
			uiC_tgbtnNotify.setChecked(true);
		} else {
			uiC_tgbtnNotify.setChecked(false);
		}

		if (Utills.MatchNotificationEnabled.equals("1")) {
			uiC_tgbtnMatchNotify.setChecked(true);
		} else {
			uiC_tgbtnMatchNotify.setChecked(false);
		}

		uiC_tgbtnNotify
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

					
							Utills.latitud = gpsTracker.getLatitude();
							Utills.longitud = gpsTracker.getLongitude();
							if (isChecked) {
								Utills.NotificationEnabled = "1";
//								new asyncUpdateKM().execute();
//
//								SharedPreferences shared = getActivity()
//										.getSharedPreferences("",
//												Context.MODE_PRIVATE);
//								Editor ed = shared.edit();
//								ed.putString("noti", "1");
//								ed.commit();
							} else {
								Utills.NotificationEnabled = "0";
//								new asyncUpdateKM().execute();
//
//								SharedPreferences shared = getActivity()
//										.getSharedPreferences("",
//												Context.MODE_PRIVATE);
//								Editor ed = shared.edit();
//								ed.putString("noti", "0");
//								ed.commit();

							}

					
					}
				});

		uiC_tgbtnMatchNotify
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean checked) {
						// TODO Auto-generated method stub

						

							Utills.latitud = gpsTracker.getLatitude();
							Utills.longitud = gpsTracker.getLongitude();
							if (checked) {
								Utills.MatchNotificationEnabled = "1";
//								new asyncUpdateKM().execute();
//
//								SharedPreferences shared = getActivity()
//										.getSharedPreferences("",
//												Context.MODE_PRIVATE);
//								Editor ed = shared.edit();
//								ed.putString("matchnoti", "1");
//								ed.commit();

							} else {
								Utills.MatchNotificationEnabled = "0";
//								new asyncUpdateKM().execute();
//
//								SharedPreferences shared = getActivity()
//										.getSharedPreferences("",
//												Context.MODE_PRIVATE);
//								Editor ed = shared.edit();
//								ed.putString("matchnoti", "0");
//								ed.commit();

							}

					

					}
				});

		return rootView;
	}

	class deleteProfile extends AsyncTask<Void, Void, Void> {

		String ResponseString = "g";

		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getActivity(), "", "Deleting Account..");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				profileID = profileID.replace("\"", "");

				DefaultHttpClient httpclient = new DefaultHttpClient();

				HttpGet httppost = new HttpGet(
						"http://116.193.163.158:8083/Profiles/DeleteProfile/"
								+ profileID);
				Log.e("request json",
						"http://116.193.163.158:8083/Profiles/DeleteProfile/"
								+ profileID + "");

				httppost.setHeader("Content-type", "application/json");
				// httppost.setHeader("Accept", "application/json");

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String retSrc = EntityUtils.toString(entity);
					// parsing JSON
					// JSONObject result = new JSONObject(retSrc); //Convert
					// String to JSON Object

					Log.e("returned json", retSrc + "");

					if (retSrc.equals("ERROR")) {
						ResponseString = "ERROR";
					}

					// JSONArray tokenList = result.getJSONArray("names");
					// JSONObject oj = tokenList.getJSONObject(0);
					// String token = oj.getString("Username");
				}

			} catch (UnsupportedEncodingException e) {

				ResponseString = "ERROR";
				e.printStackTrace();
			} catch (ClientProtocolException e) {

				ResponseString = "ERROR";
				e.printStackTrace();
			} catch (IOException e) {
				ResponseString = "ERROR";

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			pd.dismiss();
			if (ResponseString.trim().contains("ERROR")) {
				Log.e("returned json", ResponseString + "");
				Utills.showToast(getActivity(), "Error..!");
			} else {
				// ControlDB.deleteuserdetails(con);

				shared = getActivity().getSharedPreferences("",
						Context.MODE_PRIVATE);
				Editor ed = shared.edit();
				ed.clear();
				ed.commit();
//				ControlDB.deleteuserdetails(getActivity());

				Utills.FbID = "test";

				Utills.showToast(getActivity(), "Profile deleted Successfully");

				Intent gotologin = new Intent(getActivity(), SplashScreen.class);
				getActivity().startActivity(gotologin);
				getActivity().finish();

			}

			super.onPostExecute(result);
		}

	}

	// --------------update km according to profileID
	class asyncUpdateKM extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String result = "";

			String HostUrl = "http://116.193.163.158:8083/Profiles/SaveProfile";
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
				params1.add(new BasicNameValuePair("Distance", km + ""));
				params1.add(new BasicNameValuePair("ItemMatchNotification",
						Utills.MatchNotificationEnabled + ""));
				params1.add(new BasicNameValuePair("ChatNotification",
						Utills.NotificationEnabled + ""));

				Log.e("Response update km", params1.toString() + "");

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				HttpEntity entity = new UrlEncodedFormEntity(params1, "UTF-8");
				httpPost.setEntity(entity);

				ResponseHandler<String> handler = new BasicResponseHandler();
				result = httpClient.execute(httpPost, handler);

				Log.e("Response", result + "");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.e("TAG", "ClientProtocolException in callWebService(). "
						+ e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("TAG",
						"IOException in callWebService(). " + e.getMessage());
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
		}

	}

	// get lattitude and longitude)
	public class GetLatitude2 implements LocationListener {

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

}
