package project.swapstuff;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.json.JSONException;
import org.json.JSONObject;

import project.swapstuff.model.CommonUtilities;
import project.swapstuff.model.Profile_info;
import project.swapstuff.model.Utills;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

@SuppressWarnings("deprecation")
public class FacebookConnect extends Activity {
	private static String FACEBOOK_APP_ID = "566342256830253";
//	private static String FACEBOOK_APP_ID = "969874523029786";
	
	// "769007259830480";
	private static final String[] PERMISSIONS = new String[] { "email,read_stream,user_about_me" };

	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";

	private Facebook facebook;
	String response;

	ProgressDialog pd;

	 LoginButton loginButton;
	Session fb_session = null;
	private AsyncFacebookRunner mAsyncRunner;

	public boolean saveCredentials(Facebook facebook) {
		Editor editor = getApplicationContext().getSharedPreferences(KEY,
				Context.MODE_PRIVATE).edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		return editor.commit();
	}

	public boolean restoreCredentials(Facebook facebook) {
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	@Override
	protected void onResume() {

		super.onResume();

		fb_session = Session.getActiveSession();
		if (fb_session != null) {
			if (fb_session.isOpened()) {

				// loginButton.setVisibility(View.GONE);
				pd = ProgressDialog
						.show(FacebookConnect.this, "", "Connecting");
				String fbToken = "" + fb_session.getAccessToken();

				Viewdetails();

			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_share_on_facebook);

		 loginButton=(LoginButton)findViewById(R.id.loginButton1);
		 loginButton.setReadPermissions( "email,read_stream,user_about_me" );
		 loginButton.setApplicationId(FACEBOOK_APP_ID);

		facebook = new Facebook(FACEBOOK_APP_ID);
		 restoreCredentials(facebook);

		mAsyncRunner = new AsyncFacebookRunner(facebook);

	}

	public void doNotShare(View button) {
		finish();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		try {
			Session.getActiveSession().onActivityResult(FacebookConnect.this,
					requestCode, resultCode, data);

			Log.e("Returned", "returned");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void share(View button) {
	// if (!facebook.isSessionValid()) {
	// loginAndPostToWall();
	// } else {
	// new LoadShare().execute();
	// //postToWall(messageToPost);
	// }
	// }

	public void loginAndPostToWall() {
		facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH,
				new LoginDialogListener());

	}

	class LoadShare extends AsyncTask<Void, Void, String> {

		// ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(FacebookConnect.this, "", "Connecting");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			Bundle parameters = new Bundle();
			// parameters.putString("message", messageToPost);
			parameters.putString("description", "topic share");
			try {

				JSONObject json = Util.parseJson(facebook.request("me"));
				String facebookID = json.getString("id");
				String facebookEmail = json.getString("email");
				// String faceBooklastName=json.getString("last_name");
				// String faceBookFirstName=json.getString("first_name");

				// showToast(facebookID);
				System.out.println("email ----->" + facebookEmail);
				System.out.println("email ----->" + facebookID);

				Utills.FbID = facebookEmail;

				System.out.println("email from utills ----->" + Utills.FbID);

			} catch (Exception e) {

				System.out.println("Exception while get user name----->" + e);
				e.printStackTrace();
				finish();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);

			// pd.dismiss();

			if (response == null || response.equals("")
					|| response.equals("false")) {

			} else {
				showToast(response);
			}
			// Utills.FbID=facebookEmail;
			SharedPreferences shared;
			shared = getSharedPreferences("", MODE_PRIVATE);
			Editor ed = shared.edit();
			ed.putString("fbid", Utills.FbID);
			ed.commit();
			new AsyncCheckExistence().execute();

		}

	}

	// -----------------check if user exist

	class AsyncCheckExistence extends AsyncTask<Void, Void, Void> {

		String ResponseString = "g";

		@Override
		protected void onPreExecute() {
			// pd=ProgressDialog.show(FacebookConnect.this, "", "Connecting");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				DefaultHttpClient httpclient = new DefaultHttpClient();

				HttpGet httppost = new HttpGet(Utills.URL
						+ "Profiles/GetProfileByUserName?UserName="
						+ Utills.FbID.replace("\"", ""));

				Log.e("request json", Utills.URL
						+ "Profiles/GetProfileByUserName?UserName=/"
						+ Utills.FbID.replace("\"", ""));

				httppost.setHeader("Content-type", "application/json");
				// httppost.setHeader("Accept", "application/json");

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String retSrc = EntityUtils.toString(entity);

					Log.e("returned json", retSrc + "");

					ResponseString = retSrc;

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

			JSONObject jobjFb;
			try {
				jobjFb = new JSONObject(ResponseString);

				SharedPreferences shared;
				shared = getSharedPreferences("", MODE_PRIVATE);
				Editor ed = shared.edit();
				ed.putString("fbid", Utills.FbID);
				ed.commit();

				ArrayList<Profile_info> infoToinsert = new ArrayList<Profile_info>();

				Utills.itemid = jobjFb.getString("ItemID");
				Utills.id = jobjFb.getString("ProfileID");
				Utills.km = 100;

				shared = getSharedPreferences("", MODE_PRIVATE);
				Editor ed1 = shared.edit();
				ed1.putString("itemid", jobjFb.getString("ItemID"));
				ed1.putString("id", jobjFb.getString("ProfileID"));
				ed1.putInt("km", 100);
				ed1.putString("noti", "1");
				ed1.putString("matchnoti", "1");
				ed1.commit();

				Profile_info p_info = new Profile_info();
				p_info.setTitle(jobjFb.getString("ItemTitle"));
				p_info.setDesc(jobjFb.getString("ItemDescription"));
				p_info.setImgDp(jobjFb.getString("ItemImage"));
				p_info.setUser_id("1");
				p_info.setItem_id("1");
				infoToinsert.add(p_info);
				
				ed1 = shared.edit();
				ed1.putString("imgITEM", jobjFb.getString("ItemImage"));
				ed1.putString("imgTITLE", jobjFb.getString("ItemTitle"));
				ed1.putString("imgDESC", jobjFb.getString("ItemDescription"));
				ed1.commit();
				
//				imgbytes = shared.getString("imgITEM", "");
//				itemTitleDB = shared.getString("imgTITLE", "");
//				itemDescDB = shared.getString("imgDESC", "");

				
				Intent gotoHome = new Intent(getApplicationContext(),
						MainActivitySwapStuff.class);
				gotoHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(gotoHome);
				
				new asyncUpdateNOTI().execute();
				
				finish();

			} catch (JSONException e) {
				new asyncSave().execute();
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}

	}

	
	
	
	
	
	
	
	
//	updatee noti 
	// --------------update noti according to profileID
		class asyncUpdateNOTI extends AsyncTask<Void, Void, Void> {

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
					params1.add(new BasicNameValuePair("Latitude", Utills.latitud+ ""));
					params1.add(new BasicNameValuePair("Longitude", Utills.longitud+ ""));
					params1.add(new BasicNameValuePair("DateTimeCreated", ""));
					params1.add(new BasicNameValuePair("Distance", "100"));
					params1.add(new BasicNameValuePair("ItemMatchNotification","1"));
					params1.add(new BasicNameValuePair("ChatNotification","1"));

					
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
//	end update noti
	
	
	
	
	
	
	
	
	class asyncSave extends AsyncTask<Void, Void, Void> {

		String resultf = "";

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			String HostUrl = Utills.URL + "Profiles/SaveProfile";

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();

				params1.add(new BasicNameValuePair("ProfileId", "-1"));
				params1.add(new BasicNameValuePair("Username", Utills.FbID));
				params1.add(new BasicNameValuePair("Latitude", Utills.lati + ""));
				params1.add(new BasicNameValuePair("Longitude", Utills.longi
						+ ""));
				params1.add(new BasicNameValuePair("DateTimeCreated", ""));
				params1.add(new BasicNameValuePair("Distance", "100"));
				params1.add(new BasicNameValuePair("GCM_RegistrationID",
						CommonUtilities.GCM_ID));
				params1.add(new BasicNameValuePair("ItemMatchNotification", "1"));
				params1.add(new BasicNameValuePair("ChatNotification", "1"));

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				HttpEntity entity = new UrlEncodedFormEntity(params1, "UTF-8");
				httpPost.setEntity(entity);

				ResponseHandler<String> handler = new BasicResponseHandler();
				resultf = httpClient.execute(httpPost, handler);

				Log.e("Response", resultf + "");
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

			pd.dismiss();

			if (resultf.contains("-")) {
				Utills.showToast(FacebookConnect.this, "Error");
			} else {

				Utills.showToast(FacebookConnect.this,
						"Profile Created Successfully..");

				SharedPreferences shared;
				Utills.km = 100;
				Utills.id = resultf;
				shared = getSharedPreferences("", MODE_PRIVATE);
				Editor ed = shared.edit();

				ed.putString("id", resultf);
				ed.putInt("km", 100);
				ed.putString("noti", "1");
				ed.putString("matchnoti", "1");
				ed.commit();
				Intent gotoHome = new Intent(getApplicationContext(),
						ProfileCreate.class);
				gotoHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(gotoHome);
				FacebookConnect.this.finish();
			}

			super.onPostExecute(result);
		}

	}

	// -----------------------------------

	class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			saveCredentials(facebook);
			if (facebook.isSessionValid()) {
				new LoadShare().execute();
				// postToWall(messageToPost);
			}
		}

		public void onFacebookError(FacebookError error) {
			showToast("Authentication with Facebook failed!");
			finish();
		}

		public void onError(DialogError error) {
			showToast("Authentication with Facebook failed internal error!");
			finish();
		}

		public void onCancel() {
			showToast("Authentication with Facebook cancelled!");
			finish();
		}
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	private void Viewdetails() {

		List<String> permissions = new ArrayList<String>();
		permissions.add("email");
		Request request = Request.newMeRequest(Session.getActiveSession(),
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							try {

								saveCredentials(facebook);

								Log.e("check details:", user.toString());
								Log.e("check response:", response.toString());

								String first_name = user.getFirstName();
								String FAcebook_id = user.getId().toString();
								String email = user.getProperty("email")
										.toString();
								Log.e("Email id", email.toString());

								Utills.FbID = email;
								SharedPreferences shared;
								shared = getSharedPreferences("", MODE_PRIVATE);
								Editor ed = shared.edit();
								ed.putString("fbid", Utills.FbID);
								ed.commit();
								new AsyncCheckExistence().execute();

							} catch (Exception e) {

								Log.d("Error",
										"Error parsing returned user data.");
								pd.dismiss();

							}

						}
					}

				});
		request.executeAsync();

	}

}
