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

import project.swapstuff.AppSettingsFragment.asyncUpdateKM;
import project.swapstuff.MatchingScreen.asyncLikeDislike;
import project.swapstuff.model.CommonUtilities;
import project.swapstuff.model.Utills;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Contact_For_Report extends Activity {

	
	String ItemIDtoAbuse="",finishOrNot="";
	EditText uiC_edReportMsg;
	Button uiC_btnReport;
	
	
	Toast toast;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact__for__report);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#0B90A8")));
		
		
		getActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>Contact us</font>")));
		
		
		
		
		 ActionBar actionBar = getActionBar();
		    actionBar.setDisplayOptions(actionBar.getDisplayOptions()
		            | ActionBar.DISPLAY_SHOW_CUSTOM);
		    ImageButton imgbtnclose = new ImageButton(actionBar.getThemedContext());
		    imgbtnclose.setBackgroundResource(0);
		  imgbtnclose.setImageResource(android.R.drawable.ic_delete);
		    ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
		            ActionBar.LayoutParams.WRAP_CONTENT,
		            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
		                    | Gravity.CENTER_VERTICAL);
		    layoutParams.rightMargin = 22;
		    imgbtnclose.setLayoutParams(layoutParams);
		    actionBar.setCustomView(imgbtnclose);
		    imgbtnclose.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		
		
		
		
		
		
		
		Intent getintent=getIntent();
		ItemIDtoAbuse=getintent.getStringExtra("id");
		finishOrNot=getintent.getStringExtra("finish");
		
		
		
		uiC_btnReport=(Button)findViewById(R.id.uiC_btnReport);
		uiC_edReportMsg=(EditText)findViewById(R.id.uiC_edReportmsg);
		
		uiC_btnReport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				
				if(Utills.haveNetworkConnection(Contact_For_Report.this))
				{
					if(uiC_edReportMsg.getText().toString().trim().equals(""))
					{
						if(toast!=null)
						{
							toast.cancel();
						}
						toast=Toast.makeText(Contact_For_Report.this, "Please enter some message..!", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER,0, 0);
						toast.show();
					}
					else
					{
						
						InputMethodManager imm = (InputMethodManager)getSystemService(
							      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(uiC_edReportMsg.getWindowToken(), 0);
						
						new asyncReportAbuse().execute();
					}
				}
				else
				{
					if(toast!=null)
					{
						toast.cancel();
					}
					toast=Toast.makeText(Contact_For_Report.this, "No network connection..!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER,0, 0);
					toast.show();
				}
				
				
				
			
				
			}
		});
		
		
		
		
		
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	class asyncReportAbuse extends AsyncTask<Void, Void, Void>
	{

		ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			pd=ProgressDialog.show(Contact_For_Report.this, "", "Loading..");
			
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {       
			 String result = "";
			 
		     String HostUrl=Utills.URL+"ItemMatchs/SaveItemMatch"; 
//		     String HostUrl="http://116.193.163.156:8012/ItemMatchs/SaveItemMatch"; 
		 	
		      HttpClient httpClient = new DefaultHttpClient();
		      HttpPost httpPost = new HttpPost(HostUrl);

		     try
		     {  
		       List<NameValuePair> params1 = new LinkedList<NameValuePair>();
//
//		       "ItemMatchID": 1,
//		        "ItemID": 1,
//		        "ProfileIdBy": 13,
//		        "Distance": 0,
//		        "IsLikeDislikeAbuseBy": 1,
//		        "DateTimeCreated": "2014-09-23T17:30:35.8"
		       
		       params1.add(new BasicNameValuePair("ItemMatchID","-1"));
		       params1.add(new BasicNameValuePair("ItemID", ItemIDtoAbuse));
		       params1.add(new BasicNameValuePair("ProfileIdBy",(Utills.id).replace("\"", "")));
		       params1.add(new BasicNameValuePair("Distance","0"));
		       params1.add(new BasicNameValuePair("IsLikeDislikeAbuseBy",3+""));
		       params1.add(new BasicNameValuePair("DateTimeCreated",""));
		       params1.add(new BasicNameValuePair("AbuseMessage",uiC_edReportMsg.getText().toString()+""));
		       

		       Log.i("json response", ""+params1.toString());
		       
		       httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		       httpPost.setHeader("Accept", "application/json");

		       HttpEntity entityLike = new UrlEncodedFormEntity(params1, "UTF-8");
		       httpPost.setEntity(entityLike);

		       ResponseHandler<String> handlerlike = new BasicResponseHandler();
		       result =httpClient.execute(httpPost,handlerlike);
		       
		       
		       
		       Log.i("json response", ""+result.toString());

		       
		       
		       
		       
		       
		     }catch (ClientProtocolException e)
		             {  
		                 e.printStackTrace();
		                 Log.e("TAG", "ClientProtocolException in callWebService(). " + e.getMessage());
		             }
		             catch (IOException e)
		             {  
		                 e.printStackTrace();
		                 Log.e("TAG", "IOException in callWebService(). " + e.getMessage());
		             }
		     catch (Exception e) {
		    	 e.printStackTrace();
			}

		   return null;

		         }
		@Override
		protected void onPostExecute(Void result) {
			
			
//			if(itemposition==0)
//			{	
//				ShowAnim(1);
//				itemposition=2;	
//			}
//			else
//			{
//				ShowAnim(itemposition);
//				itemposition++;	
//			}
			
		
			
			
		
		pd.dismiss();
		
		CommonUtilities.FragmentToOpen=0;
		
		Toast.makeText(Contact_For_Report.this, "Item Reported Successfully..!", Toast.LENGTH_LONG).show();
		if(finishOrNot.equals("y"))
		{
			Intent goto_mainMenu = new Intent(Contact_For_Report.this,
					MainActivitySwapStuff.class);
			goto_mainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(goto_mainMenu);
			finish();
		}
		else
		{
			Intent goto_mainMenu = new Intent(Contact_For_Report.this,
					MainActivitySwapStuff.class);
			goto_mainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(goto_mainMenu);
			finish();
		}
			
			super.onPostExecute(result);
		}
		
	}

}
