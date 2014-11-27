package project.swapstuff;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import project.swapstuff.model.Utills;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class OtherUserInfo extends Activity {

	SmartImageView uiC_imgDp_other;
	TextView uiC_tvTitle, uiC_tvKM, uiC_tvDesc;
	// ImageButton uiC_imgbtnLike, uiC_imgbtnDislike;

	String itemID, DisTance, ItemTitle;

	Toast toast;
	
	@Override
	public void onBackPressed() {

		finish();
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_user_info);
		this.overridePendingTransition(R.anim.bottom_to_topout,
				R.anim.bottom_to_top);

		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#0B90A8")));

		getActionBar().setIcon(R.drawable.btn_back);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("Back");

		Intent getintentchat = getIntent();
		itemID = getintentchat.getStringExtra("itemid");
		DisTance = getintentchat.getStringExtra("km");
		ItemTitle = getintentchat.getStringExtra("title");
//		matchIID= getintentchat.getStringExtra("match");
		
//		Log.e("id other user", ItemTitle + itemID +matchIID+ "");
		// this.overridePendingTransition(R.anim.left_to_right_in,
		// R.anim.left_to_right_out);

		// uiC_imgbtnLike = (ImageButton)
		// findViewById(R.id.uiC_imgbtn_likeDetails);
		// uiC_imgbtnDislike = (ImageButton)
		// findViewById(R.id.uiC_imgbtn_closeDetails);
		uiC_imgDp_other = (SmartImageView) findViewById(R.id.uiC_imgVother_user_info);
		uiC_tvTitle = (TextView) findViewById(R.id.uiC_txtvtitleother_user_info);
		uiC_tvDesc = (TextView) findViewById(R.id.uiC_txtvdesc_other_user_info);
		uiC_tvKM = (TextView) findViewById(R.id.uiC_txtvKMother_user_info);

		
		if(Utills.haveNetworkConnection(OtherUserInfo.this))
		{
			new AsyncGetitemInfo().execute();
		}
		else
		{
			if(toast!=null)
			{
				toast.cancel();
			}
			
			toast=Toast.makeText(OtherUserInfo.this, "No network connection..!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,0, 0);
			toast.show();
		}
		
		
		
		

		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			View customView2 = getLayoutInflater().inflate(
					R.layout.actionbar_otheruser_info, null);

			final TextView customTitle = (TextView) customView2
					.findViewById(R.id.actionbarTitle_otheruser);
			ImageButton uiC_imgbtnV_report = (ImageButton) customView2
					.findViewById(R.id.imgbtnV_report);

			LinearLayout layoutTitle = (LinearLayout) customView2
					.findViewById(R.id.layoutactionbartitle);

			customTitle.setText((Html.fromHtml("<font color='#ffffff'>"+ItemTitle+"</font>")));

			// Change the font family (optional)
//			customTitle.setTypeface(Typeface.MONOSPACE);
			// SEt the custom view
			uiC_imgbtnV_report.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					new AlertDialog.Builder(OtherUserInfo.this)
				    .setTitle("Warning !")
				    .setMessage("Report Abuse..!")
				    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        
				        	Intent gotoContactscreen=new Intent(OtherUserInfo.this,Contact_For_Report.class);
				        	gotoContactscreen.putExtra("id", itemID);
				        	gotoContactscreen.putExtra("finish","n");
				        	startActivity(gotoContactscreen);
				        	
				        }
				     })
				    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				           dialog.dismiss();
				        }
				     })
				    .setIcon(android.R.drawable.ic_dialog_alert)
				     .show();
					
					
				}
			});
			 ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
			            ActionBar.LayoutParams.MATCH_PARENT,
			            ActionBar.LayoutParams.MATCH_PARENT, Gravity.RIGHT
			                    | Gravity.CENTER_VERTICAL);
			   
			 customView2.setLayoutParams(layoutParams);
//			actionBar.setCustomView(customView);
			actionBar.setCustomView(customView2);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case android.R.id.home:

			finish();

			break;

		}

		return super.onOptionsItemSelected(item);
	}

	// Get info of user
	class AsyncGetitemInfo extends AsyncTask<Void, Void, Void> {

		String title, htmlText, imgb;

		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(OtherUserInfo.this, "", "Loading..");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				DefaultHttpClient httpclient = new DefaultHttpClient();

				HttpGet httppost = new HttpGet(
						"http://116.193.163.158:8083/Items/GetItem/" + itemID);
				Log.e("request json",
						"http://116.193.163.158:8083/Items/GetItem/" + itemID
								+ "");

				httppost.setHeader("Content-type", "application/json");
				// httppost.setHeader("Accept", "application/json");

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String retSrc = EntityUtils.toString(entity);
					Log.e("returned json", retSrc + "");
					// parsing JSON
					JSONObject resultJson = new JSONObject(retSrc); // Convert

					title = "" + resultJson.getString("ItemTitle");
					Log.e("returned json", title + "");

					htmlText = "<body><b>Description:</b><br><i>"
							+ resultJson.getString("ItemDescription")
							+ "</i></body>";
					imgb = resultJson.getString("ItemImage");

					// JSONArray tokenList = result.getJSONArray("names");
					// JSONObject oj = tokenList.getJSONObject(0);
					// String token = oj.getString("Username");
				}

			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeException e) {

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			pd.dismiss();

			uiC_tvKM.setText(DisTance + "" + "-km away.");

			uiC_tvTitle.setText(title);
			uiC_tvDesc.setText(Html.fromHtml(htmlText));
			
//			uiC_imgDp_other.setImageBitmap(Utills.StringToBitMap(imgb));
			uiC_imgDp_other.setImageUrl(imgb);

			super.onPostExecute(result);
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
