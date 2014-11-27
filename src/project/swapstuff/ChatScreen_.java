package project.swapstuff;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.image.SmartImageView;

import project.swapstuff.model.Utills;
import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatScreen_ extends Activity {

	int abTitleId;

	static String otherItemID,distnce, otherprofileid,titleItemS;

	Button uiC_btnSend;
	EditText uiC_edChatmsg;
	String chatMsg;
	LinearLayout uiC_layoutChat;

	ScrollView uiC_scroll;
	
	static boolean Refresh=true;
	
	String DateTemp="02-October";
	
	
	ProgressBar uiC_Psending;
	
	
	Toast toast;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Utills.chatActive=false;
		Refresh=false;
		super.onBackPressed();
	}

	@Override
	protected void onRestart() {
		this.overridePendingTransition(R.anim.top_to_bottomin,
				R.anim.top_to_bottomout);
		
		Utills.chatActive=true;
		super.onRestart();
	}
	
	@Override
	protected void onStop() {
		Utills.chatActive=false;
		Refresh=false;
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		
		Utills.chatActive=false;
		Refresh=false;
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		Utills.chatActive=true;
		if(!Refresh)
		{
			Refresh=true;
			 new asyncRefreshChat().execute();
		}
		
		
		
		
		super.onResume();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		Utills.chatActive=true;
//		
//		if(!Refresh)
//		{
//			Refresh=true;
//			 new asyncRefreshChat().execute();
//		}
		
		
		
		
		super.onRestoreInstanceState(savedInstanceState);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_screen_);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#0B90A8")));
		Intent getinten = getIntent();
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		
		
		Utills.chatActive=true;
		
		Refresh=true;
		
		
		
		NotificationManager notifManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.cancelAll();
		
		
		uiC_Psending=(ProgressBar)findViewById(R.id.uiC_progressBSendMsg);
		
		uiC_layoutChat = (LinearLayout) findViewById(R.id.uiC_layoutChat);
		
		
		titleItemS=getinten.getStringExtra("Title");
		otherItemID = getinten.getStringExtra("itemid");
//		MAtchid = getinten.getStringExtra("matchid");
		distnce = getinten.getStringExtra("km");
		otherprofileid = getinten.getStringExtra("pid");
		
//		try {
//			Bitmap bitmp=Utills.StringToBitMap(imgThumbnail);
//			
//			Drawable drawIncon=new BitmapDrawable(bitmp);
//			getActionBar().setIcon(drawIncon);
//			getActionBar().setHomeButtonEnabled(true);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		

//		
		Log.e("request json", "" + otherItemID + distnce);
		
		uiC_scroll = ((ScrollView) findViewById(R.id.uiC_scrllView));
		
		if(Utills.haveNetworkConnection(ChatScreen_.this))
		{
			new asyncReceiveMsg().execute();
		}
		else
		{
			if(toast!=null)
			{
				toast.cancel();
			}
			
			toast=Toast.makeText(ChatScreen_.this, "No network connection..!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,0, 0);
			toast.show();
		}
		
		
		
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			View customView = getLayoutInflater().inflate(
					R.layout.actionbar_title, null);

			final TextView customTitle = (TextView) customView
					.findViewById(R.id.actionbarTitle);
			final TextView customKM = (TextView) customView
					.findViewById(R.id.actionbarKM);
			ImageButton btnCloseChat=(ImageButton)customView.findViewById(R.id.uiC_imgbtnCloseChat);
			SmartImageView imgThumbnail=(SmartImageView)customView.findViewById(R.id.imgThumbnail);
//			ImageView uiC_imgVThumbnail=(ImageView)customView.findViewById(R.id.uiC_imgVChatThumbnail);
			
			LinearLayout layoutTitle = (LinearLayout) customView
					.findViewById(R.id.layoutactionbartitle);
			
			
//			RoundedCornerBitmap roundround=new RoundedCornerBitmap(ChatScreen_.this);
//			
//			Bitmap bitmp=Utills.StringToBitMap(imgThumbnail);
//			
//			uiC_imgVThumbnail.setImageBitmap(roundround.getCroppedBitmap(bitmp, 60));
			imgThumbnail.setImageUrl(Utills.Imagebytee);
			
			customTitle.setText(titleItemS+"");
			customKM.setText(distnce + "-km away");
			
			
			
			// Change the font family (optional)
//			customTitle.setTypeface(Typeface.DEFAULT);
			
			imgThumbnail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Utills.chatActive=false;
					Log.e("request json", "" + otherItemID + distnce);
					Intent goto_others_info = new Intent(ChatScreen_.this,
							OtherUserInfo.class);
					goto_others_info.putExtra("itemid", otherItemID);
					goto_others_info.putExtra("km", distnce);
					goto_others_info.putExtra("title", titleItemS+"");
					startActivity(goto_others_info);
					
				}
			});
			btnCloseChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Utills.chatActive=false;
					Refresh=false;
					
					finish();
				}
			});
			

			
			
			 ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
			            ActionBar.LayoutParams.MATCH_PARENT,
			            ActionBar.LayoutParams.MATCH_PARENT, Gravity.RIGHT
			                    | Gravity.CENTER_VERTICAL);
			   
			    customView.setLayoutParams(layoutParams);
			actionBar.setCustomView(customView);
		}

		this.overridePendingTransition(R.anim.right_to_left,
				R.anim.right_to_left_out);

		uiC_edChatmsg = (EditText) findViewById(R.id.uiC_edMsg);
		uiC_btnSend = (Button) findViewById(R.id.uiC_btnSentChat);
		uiC_btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(Utills.haveNetworkConnection(ChatScreen_.this))
				{
					
				
				
				
				chatMsg = uiC_edChatmsg.getText().toString().trim();

				if(chatMsg.equals(""))
				{
					
				}
				else
				{
					uiC_edChatmsg.setText("");
					uiC_edChatmsg.setEnabled(false);
//					uiC_btnSend.setVisibility(View.INVISIBLE);
					uiC_Psending.setVisibility(View.VISIBLE);
					new asyncSendmsg().execute();	
				}
				

				
				}
				else
				{
					if(toast!=null)
					{
						toast.cancel();
					}
					
					toast=Toast.makeText(ChatScreen_.this, "No network connection..!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER,0, 0);
					toast.show();
				}
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case android.R.id.home:
			
		
		
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	// ----------Send chat msg

	class asyncSendmsg extends AsyncTask<Void, Void, Void> {

//		 ProgressDialog pd;

		@Override
		protected void onPreExecute() {

//			 pd=ProgressDialog.show(ChatScreen_.this, "", "Sending..");

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String result = "";

			String HostUrl = "http://116.193.163.158:8083/Chats/SaveChat";
//			String HostUrl = "http://116.193.163.156:8012/Chats/SaveChat";
			

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();
				// "ChatId": 1,
				// "ItemID": 41,
				// "ProfileIdBy": 69,
				// "ProfileIdTo": 70,
				// "ChatContent": "Hi, This is my Item you like it.",
				// "DateTimeCreated": "2014-09-29T12:03:29.5"

				params1.add(new BasicNameValuePair("ChatId", "-1"));
				params1.add(new BasicNameValuePair("ItemID", (Utills.itemid)
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileIdBy", (Utills.id)
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileIdTo",
						otherprofileid));
				params1.add(new BasicNameValuePair("ChatContent", chatMsg + ""));
				params1.add(new BasicNameValuePair("DateTimeCreated", ""));
				params1.add(new BasicNameValuePair("IsRead", "false"));
				
				
				Log.i("json request", "" + params1.toString());

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				HttpEntity entitymatch = new UrlEncodedFormEntity(params1,
						"UTF-8");
				httpPost.setEntity(entitymatch);

				ResponseHandler<String> handlermatch = new BasicResponseHandler();
				result = httpClient.execute(httpPost, handlermatch);

				Log.i("json response", "" + result.toString());

				Log.e("TAG", "" + result);
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

			
//			pd.dismiss();
			uiC_edChatmsg.setEnabled(true);
			
//			uiC_btnSend.setVisibility(View.VISIBLE);
			uiC_Psending.setVisibility(View.GONE);
			
			Calendar c = Calendar.getInstance();
			System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("dd-MMMM");
			String formattedDate = df.format(c.getTime());
			
			
			
			if(DateTemp.equalsIgnoreCase(formattedDate))
			{
			
			}
			else
			{
				DateTemp=formattedDate;
				
				LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				lpDate.gravity=Gravity.CENTER;
				
				TextView tvdate = new TextView(ChatScreen_.this);
				tvdate.setLayoutParams(lpDate);
				tvdate.setText(formattedDate + "");
				tvdate.setTextSize(22);
				tvdate.setPadding(5, 5, 5, 5);

				tvdate.setGravity(Gravity.CENTER);
				uiC_layoutChat.addView(tvdate);
			}
			
			
			
//			String msgWDdate = "<body><h1>"+chatMsg+"</h1><br>"	+ formattedDate+" "		+ "</body>";
			
			
			TextView tv = new TextView(ChatScreen_.this);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.RIGHT;
			lp.setMargins(10, 10, 0, 10);
			tv.setText(chatMsg);
			tv.setTextSize(17);
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(10, 10, 20, 10);
			tv.setLayoutParams(lp);
			
			
			tv.setBackgroundResource(R.drawable.chat_bable);
			uiC_layoutChat.addView(tv);

//			uiC_edChatmsg.setText("");
			
			  uiC_scroll.fullScroll(ScrollView.FOCUS_DOWN);
			
			super.onPostExecute(result);
		}

	}

	// --------------class for get chat msg
	
	class asyncReceiveMsg extends AsyncTask<Void, Void, Void> {

		 ProgressDialog pd;
		String resultchat = "";
		@Override
		protected void onPreExecute() {

			 pd=ProgressDialog.show(ChatScreen_.this, "", "Loading..");

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			

			String HostUrl = "http://116.193.163.158:8083/Chats/GetChatList";
//			String HostUrl = "http://116.193.163.156:8012/Chats/GetChatList";
//			while (true) {
				
			
			

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();

				params1.add(new BasicNameValuePair("ChatId", "-1"));
				params1.add(new BasicNameValuePair("ItemID", (Utills.itemid)
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileIdBy", (Utills.id)
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileIdTo",otherprofileid));
				params1.add(new BasicNameValuePair("ChatContent",""));
				params1.add(new BasicNameValuePair("DateTimeCreated", ""));

				Log.i("json request", "" + params1.toString());

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				HttpEntity entitymatch = new UrlEncodedFormEntity(params1,
						"UTF-8");
				httpPost.setEntity(entitymatch);

				ResponseHandler<String> handlermatch = new BasicResponseHandler();
				resultchat = httpClient.execute(httpPost, handlermatch);

				Log.i("json response get chat", "" + resultchat.toString());

			
				
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.e("TAG", "ClientProtocolException in callWebService(). "
						+ e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("TAG",
						"IOException in callWebService(). " + e.getMessage());
			}
			
			
//		}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			
			pd.dismiss();
			
			
			try {
			
			
			
			JSONArray jarrayChat;
			
				jarrayChat = new JSONArray(resultchat);
			
			
			for (int gchat = 0; gchat < jarrayChat.length(); gchat++) 
			{
				JSONObject jobjChat=jarrayChat.getJSONObject(gchat);
				
				
				String formtDate = Utills.parseDateToddMMyyyy(jobjChat.getString("DateTimeCreated"));
				
				
				
//				String msgWDdate = "<body><h1>"+ jobjChat.getString("ChatContent")+"</h1><br>"
//						+ formtDate+" "
//						+ "</body>";
				
				TextView tv = new TextView(ChatScreen_.this);
				
				if(DateTemp.equalsIgnoreCase(formtDate))
				{
				
				}
				else
				{
					LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lpDate.gravity=Gravity.CENTER;
					
					TextView tvdate = new TextView(ChatScreen_.this);
					tvdate.setLayoutParams(lpDate);
					tvdate.setText(formtDate + "");
					tvdate.setTextSize(22);
					tvdate.setPadding(5, 5, 5, 5);

					tvdate.setGravity(Gravity.CENTER);
					uiC_layoutChat.addView(tvdate);
				}
				
				
				DateTemp=formtDate;
				
				
				
				if(jobjChat.getString("ProfileIdBy").equals(Utills.id.replace("\"", "")))
				{
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					
					lp.gravity = Gravity.RIGHT;
					lp.setMargins(10, 10, 0, 10);
//					lp.setMargins(left, top, right, bottom)
					tv.setPadding(10, 10, 20, 10);
//					tv.setText(Html.fromHtml(msgWDdate));
					tv.setText(jobjChat.getString("ChatContent")); 
					tv.setTextSize(17);
					tv.setLayoutParams(lp);
					tv.setGravity(Gravity.CENTER);
					tv.setBackgroundResource(R.drawable.chat_bable);

				}
				else
				{
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp.gravity = Gravity.LEFT;
					lp.setMargins(10, 10, 0, 10);
					tv.setPadding(20, 10, 10, 10);
					tv.setText(jobjChat.getString("ChatContent"));
					tv.setTextSize(17);
					tv.setLayoutParams(lp);
					tv.setGravity(Gravity.CENTER);
					tv.setBackgroundResource(R.drawable.chat_bable_2);
//					uiC_layoutChat.setBackgroundColor(Color.parseColor("#ADD4BF"));
				}
				
							
				
							uiC_layoutChat.addView(tv);
							
				
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			  uiC_scroll.fullScroll(ScrollView.FOCUS_DOWN);
			
			  
			  new asyncRefreshChat().execute();
			  
//			TextView tv = new TextView(ChatScreen_.this);
//
//			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//			lp.gravity = Gravity.LEFT;
//			lp.setMargins(10, 10, 0, 0);
//			tv.setText("" + chatMsg);
//			tv.setTextSize(17);
//			tv.setLayoutParams(lp);
//
//			uiC_layoutChat.addView(tv);
//
//			uiC_edChatmsg.setText("");
//			
//			
//			new asyncReceiveMsg().execute();
			super.onPostExecute(result);
		}

	}
	
	
	
	
	class asyncRefreshChat extends AsyncTask<Void, Void, Void>
	{


		// ProgressDialog pd;
		String resultchatR = "";
		@Override
		protected void onPreExecute() {

			// pd=ProgressDialog.show(ChatScreen_.this, "", "Loading..");

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			
			String HostUrl = "http://116.193.163.158:8083/Chats/GetChatListUnread";
//			String HostUrl = "http://116.193.163.156:8012/Chats/GetChatListUnread";
			
//			while (true) {
				
			
			

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();

				
				
//				{ChatId: 0,ItemID: 70,ProfileIdBy: 106,ProfileIdTo: 97,ChatContent: "",DateTimeCreated: "',IsRead: false,CountMessage: null}
				
				
				params1.add(new BasicNameValuePair("ChatId", "-1"));
				params1.add(new BasicNameValuePair("ItemID", (Utills.itemid)
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileIdBy", (Utills.id)
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileIdTo",otherprofileid));
				params1.add(new BasicNameValuePair("ChatContent",""));
				params1.add(new BasicNameValuePair("DateTimeCreated", ""));

				Log.i("json request chatRefresh", "" + params1.toString());

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				HttpEntity entitymatch = new UrlEncodedFormEntity(params1,
						"UTF-8");
				httpPost.setEntity(entitymatch);

				ResponseHandler<String> handlermatch = new BasicResponseHandler();
				resultchatR = httpClient.execute(httpPost, handlermatch);

				Log.i("json response get chat", "" + resultchatR.toString());

			
				
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.e("TAG", "ClientProtocolException in callWebService(). "
						+ e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("TAG",
						"IOException in callWebService(). " + e.getMessage());
			}
			catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			
//		}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			
			
			
			
			try {
			
			
			
			JSONArray jarrayChat;
			
				jarrayChat = new JSONArray(resultchatR);
			
				
				
				
				
			
			for (int gchat = 0; gchat < jarrayChat.length(); gchat++) 
			{
				JSONObject jobjChat=jarrayChat.getJSONObject(gchat);
				
				String formtDate = Utills.parseDateToddMMyyyy(jobjChat.getString("DateTimeCreated"));
//				String msgWDdate = "<body><h1>"+ jobjChat.getString("ChatContent")+"</h1><br>"
//						+ formtDate+" "
//						+ "</body>";
				
				
				if(DateTemp.equalsIgnoreCase(formtDate))
				{
				
				}
				else
				{
					LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lpDate.gravity=Gravity.CENTER;
					
					TextView tvdate = new TextView(ChatScreen_.this);
					tvdate.setLayoutParams(lpDate);
					tvdate.setText(formtDate + "");
					tvdate.setTextSize(22);
					tvdate.setPadding(5, 5, 5, 5);
					tvdate.setGravity(Gravity.CENTER);
					uiC_layoutChat.addView(tvdate);
				}
				
				
				DateTemp=formtDate;
				
				
				
				TextView tv = new TextView(ChatScreen_.this);
				
				
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp.gravity = Gravity.LEFT;
					lp.setMargins(10, 10, 0, 10);
					tv.setPadding(20, 10, 10, 10);
					tv.setText(jobjChat.getString("ChatContent"));
					tv.setTextSize(17);
					tv.setLayoutParams(lp);
					tv.setGravity(Gravity.CENTER);
					tv.setBackgroundResource(R.drawable.chat_bable_2);
//					uiC_layoutChat.setBackgroundColor(Color.parseColor("#ADD4BF"));
				
					uiC_layoutChat.addView(tv);
					
					  uiC_scroll.fullScroll(ScrollView.FOCUS_DOWN);
				}
				
							
				
							
							
				
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			  
			  if(Refresh)
			  {
				  new asyncRefreshChat().execute();
			  }
			

			super.onPostExecute(result);
		}

	
	}
	
	
	
	

}
