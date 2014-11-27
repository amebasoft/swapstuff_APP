package project.swapstuff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import project.swapstuff.adapter.RoundedCornerBitmap;
import project.swapstuff.model.Utills;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class YourMatchFragment extends Fragment {

	public YourMatchFragment(Context conn) {

		this.con = conn;
	}

	public YourMatchFragment() {

	}

	Context con;
	ListView uiC_listVmatch;
	ImageView uic_txtVv;
	static String[] itmID, Distance, DateTimeCreated, itemName,
			othersPRofileid, MatchID, imgs, ChatMessage,MessageCount;
	static String []imgbytes;
	ProgressDialog pdialog;
	SharedPreferences shared ;
	
	Toast toast;
	
//	@Override
//	public void onResume() {
//		 getActivity().registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
//		super.onResume();
//	}
//	public void onPause() {
//		super.onPause();
//		 getActivity().unregisterReceiver(mMessageReceiver);
//	};
//	 
//	 BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//		    @Override
//		    public void onReceive(Context context, Intent intent) {
//
//		        // Extract data included in the Intent
//		        String message = intent.getStringExtra("message");
//System.out.println("msg is"+message);
//		        //do other stuff here
//		    }
//		};
	@Override
	public void onDestroy() {
		getActivity().getActionBar().setDisplayShowCustomEnabled(false);
		super.onDestroy();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_yourmatch,
				container, false);

		
		 shared = getActivity().getSharedPreferences("",getActivity().MODE_PRIVATE);
		 Utills.id=shared.getString("id", "4");
			Utills.km=shared.getInt("km", 1);
			Utills.NotificationEnabled=shared.getString("noti", "1");
			Utills.MatchNotificationEnabled=shared.getString("matchnoti", "1");
			Utills.FbID=shared.getString("fbid", "amebasoftwares");
			Utills.itemid=shared.getString("itemid", "2");
		
		if(Utills.haveNetworkConnection(getActivity()))
		{
			
			new asyncMatchList().execute();

		}
		else
		{
			if(toast!=null)
			{
				toast.cancel();
			}
			
			toast=Toast.makeText(getActivity(), "No network connection..!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,0, 0);
			toast.show();
		}
		
		
		uiC_listVmatch = (ListView) rootView
				.findViewById(R.id.uiC_listvYourmatch);
		 uic_txtVv=(ImageView)rootView.findViewById(R.id.uiC_textViewNote);
		 
		 
		 
//		 ACTION BAR REFRESH BUTTON
		 ActionBar actionBar = getActivity().getActionBar();
			actionBar.setDisplayOptions(actionBar.getDisplayOptions()
					| ActionBar.DISPLAY_SHOW_CUSTOM);
			ImageButton imgbtnR = new ImageButton(actionBar.getThemedContext());
//			tView.setText("Save");
			imgbtnR.setBackground(null);
			imgbtnR.setImageResource(android.R.drawable.stat_notify_sync);
			ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
					ActionBar.LayoutParams.WRAP_CONTENT,
					ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
							| Gravity.CENTER_VERTICAL);
			layoutParams.rightMargin = 22;
			imgbtnR.setLayoutParams(layoutParams);
			actionBar.setCustomView(imgbtnR);
			imgbtnR.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(Utills.haveNetworkConnection(getActivity()))
					{
						
						
						new asyncMatchList().execute();

					}
					else
					{
						if(toast!=null)
						{
							toast.cancel();
						}
						
						toast=Toast.makeText(getActivity(), "No network connection..!", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER,0, 0);
						toast.show();
					}
				}
			});
//		 ACTION BAR REFRESH BUTTON
		 
		 
		 
		 
		 
		return rootView;
	}
	

	class adaptermatchlist extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemName.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View vu, ViewGroup vugrp) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) con
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vu = inflater.inflate(R.layout.matchlist_adapter, vugrp, false);
			LinearLayout layout = (LinearLayout) vu
					.findViewById(R.id.layoutadapter);
			LinearLayout layoutChat = (LinearLayout) vu
					.findViewById(R.id.uiC_layoutGotoChat);

			if (position % 2 == 0) {
				layout.setBackgroundColor(Color.parseColor("#ffffff"));
			} else {
				layout.setBackgroundColor(Color.parseColor("#CCCCCC"));
			}

			TextView uiC_txtvName = (TextView) vu
					.findViewById(R.id.uiC_txtvName);

			final TextView uiC_txtvDate = (TextView) vu
					.findViewById(R.id.uiC_txtvmachedDate);
			ImageButton uiC_imgbtnCancel = (ImageButton) vu
					.findViewById(R.id.uiC_imgbtncancel);
			ImageView uiC_imgV = (ImageView) vu
					.findViewById(R.id.uiC_imgVThumbnail);

			RoundedCornerBitmap rounded = new RoundedCornerBitmap(con);

			// Bitmap bitmap = ((BitmapDrawable)
			// getResources().getDrawable(R.drawable.img)).getBitmap();
			String dateS=DateTimeCreated[position].substring(0, DateTimeCreated[position].indexOf("T"));
//					String TimeS=DateTimeCreated[position].substring(DateTimeCreated[position].indexOf("T")+1,DateTimeCreated[position].length());
			
			String dateFormatted=Utills.parseDateToddMMyyyy(dateS);
			
			Log.i("json response", "" + itemName[position]);
			uiC_txtvName.setText(itemName[position]);
			
			if(MessageCount[position].equals("0") ^ MessageCount[position].equals(""))
			{
				uiC_txtvDate.setText("Matched on : "+dateFormatted+"");
			}
			else
			{
				
				uiC_txtvDate.setText("*"+ChatMessage[position]);
				uiC_txtvDate.setTextColor(Color.RED);
				uiC_txtvDate.setTypeface(null,Typeface.BOLD);
			}
			
			
			try {
				
				int rounded_value = 100;    

				DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().displayer(new RoundedBitmapDisplayer(rounded_value)).build();

				
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).defaultDisplayImageOptions(options).build(); 
				ImageLoader.getInstance().init(config);                 
				ImageLoader.getInstance().displayImage(imgs[position], uiC_imgV,options);
//			 Bitmap bitmap =Utills.StringToBitMap(imgbytes[position]);
//			 uiC_imgV.setImageBitmap(Utills.StringToBitMap(imgs[position]));

//			 uiC_imgV.setImageBitmap(rounded.getCroppedBitmap(bitmap, 90));
			 
			 
			} catch (Exception e) {
				e.printStackTrace();
			}
			catch (OutOfMemoryError e) {
				// TODO: handle exception
			}
			// Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
			// Bitmap photo = Bitmap.createScaledBitmap(yourSelectedImage, 300,
			// 300, false);
			// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			// photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

			 uiC_imgbtnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					

					new AlertDialog.Builder(getActivity())
				    .setTitle("Exit")
				    .setMessage("Are you sure you want to remove this item?")
				    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        
				        	
				        	if(Utills.haveNetworkConnection(getActivity()))
				        	{
				        		new asynlikedislike(position).execute();
				        	}
				        	else
				        	{
				        		if(toast!=null)
								{
									toast.cancel();
								}
								
								toast=Toast.makeText(getActivity(), "No network connection..!", Toast.LENGTH_LONG);
								toast.setGravity(Gravity.CENTER,0, 0);
								toast.show();
				        	}
				        	
				        	
				        	
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
			 
			layoutChat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String dateS=DateTimeCreated[position].substring(0, DateTimeCreated[position].indexOf("T"));
				
					String dateFormatted=Utills.parseDateToddMMyyyy(dateS);
					uiC_txtvDate.setTextColor(Color.BLACK);
					uiC_txtvDate.setTypeface(null,Typeface.ITALIC);
					uiC_txtvDate.setText("Matched on : "+dateFormatted+"");
					
					Intent gotochat = new Intent(con, ChatScreen_.class);
					gotochat.putExtra("itemid", itmID[position]);
					gotochat.putExtra("matchid", MatchID[position]);
					gotochat.putExtra("Title", itemName[position]);
					gotochat.putExtra("pid", othersPRofileid[position]);
					gotochat.putExtra("km", Distance[position]);
					Utills.Imagebytee=imgs[position];
//					gotochat.putExtra("img", imgs[position]);
					startActivity(gotochat);
				}
			});
			return vu;
		}

	}

	// ----------------Get list of your matching items
	class asyncMatchList extends AsyncTask<Void, Void, Void> {

		

		@Override
		protected void onPreExecute() {

			pdialog=ProgressDialog.show(getActivity(), "", "Loading..");
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String result = "";

			String HostUrl = "http://116.193.163.158:8083/ItemMatchs/GetItemMatchList";
//			String HostUrl = "http://116.193.163.156:8012/ItemMatchs/GetItemMatchList";
		

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();


				params1.add(new BasicNameValuePair("ItemMatchID", "-1"));
				params1.add(new BasicNameValuePair("ItemID", (Utills.itemid).replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileIdBy", (Utills.id).replace("\"", "")));
				params1.add(new BasicNameValuePair("Distance", Utills.km+""));
				params1.add(new BasicNameValuePair("IsLikeDislikeAbuseBy", ""));
				params1.add(new BasicNameValuePair("DateTimeCreated", ""));
				params1.add(new BasicNameValuePair("ChatMessage", ""));
				params1.add(new BasicNameValuePair("MessageCount", ""));
				params1.add(new BasicNameValuePair("ItemTitle", ""));
				params1.add(new BasicNameValuePair("ItemImage", ""));
				
				
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
				// parsing JSON
				// JSONObject jsonResponse = new JSONObject(result); //Convert
				// String to JSON Object

				JSONArray jsnArray = new JSONArray(result);

				itmID = new String[jsnArray.length()];
				Distance = new String[jsnArray.length()];
				DateTimeCreated = new String[jsnArray.length()];
				itemName = new String[jsnArray.length()];
				othersPRofileid = new String[jsnArray.length()];
				MatchID = new String[jsnArray.length()];
				imgs = new String[jsnArray.length()];
				ChatMessage= new String[jsnArray.length()];
				MessageCount= new String[jsnArray.length()];
				
				for (int g = 0; g < jsnArray.length(); g++) {

					JSONObject oj = jsnArray.getJSONObject(g);

					itmID[g] = oj.getString("ItemID"); 
					String dstance = oj.getString("Distance");
					Distance[g]=dstance.substring(0, dstance.indexOf(".")+2);
					
					DateTimeCreated[g] = oj.getString("DateTimeCreated");
					itemName[g] = oj.getString("ItemTitle");
					othersPRofileid[g] = oj.getString("ProfileIdBy");
					MatchID[g] = oj.getString("ItemMatchID");
					imgs[g] = oj.getString("ItemImage");
					
					MessageCount[g]= oj.getString("MessageCount")+"";
					ChatMessage[g]= oj.getString("ChatMessage")+"";
					
					
					Log.e("TAG count", "" + MessageCount[g]);
					Log.e("TAG msg", "" + ChatMessage[g]);
					Log.e("TAG", "" + Distance[g]);
				}

			
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.e("TAG", "ClientProtocolException in callWebService(). "
						+ e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("TAG",
						"IOException in callWebService(). " + e.getMessage());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			
			try {
				
				pdialog.dismiss();
			
			if(imgs.length<=0)
			{
				
				
				uiC_listVmatch.setVisibility(View.GONE);
				uic_txtVv.setVisibility(View.VISIBLE);
				
			}
			else
			{
				uic_txtVv.setVisibility(View.GONE);
				
//				new LoadImage().execute();
				adaptermatchlist adapter = new adaptermatchlist();
				uiC_listVmatch.setAdapter(adapter);
			}
			
			
			
			} catch (Exception e) {
				pdialog.dismiss();
				
				uiC_listVmatch.setVisibility(View.GONE);
				uic_txtVv.setVisibility(View.VISIBLE);
				e.printStackTrace();
			}
			
			super.onPostExecute(result);
		}

	}
	
	
	
	
	
	class asynlikedislike extends AsyncTask<Void, Void, Void>
	
	{
		int idid;

		ProgressDialog pd;
		
		public asynlikedislike(int  itmid) {
			idid=itmid; 
		
		}
		@Override
		protected void onPreExecute() {
			
			pd=ProgressDialog.show(getActivity(), "", "Please wait..");
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {       
			 String result = "";
			 
		     String HostUrl="http://116.193.163.158:8083/ItemMatchs/SaveItemMatch"; 

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
		       params1.add(new BasicNameValuePair("ItemID", itmID[idid].replace("\"", "")));
		       params1.add(new BasicNameValuePair("ProfileIdBy",(Utills.id).replace("\"", "")));
		       params1.add(new BasicNameValuePair("Distance","0"));
		       params1.add(new BasicNameValuePair("IsLikeDislikeAbuseBy","2"));
		       params1.add(new BasicNameValuePair("DateTimeCreated",""));

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

		   return null;

		         }
		@Override
		protected void onPostExecute(Void result) {
			
			pd.dismiss();
			
			Utills.showToast(getActivity(), "Deleted Successfully");
			
			new asyncMatchList().execute();
			
			super.onPostExecute(result);
		}
		
	}
	
	
	
	
	
	
	
	
	class LoadImage extends AsyncTask<String, String, String> {
		
		Bitmap bitmap;
		
	    @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	           
	    }
	       protected String doInBackground(String... args) {
	    	  
	    	   try {
	    	   
	    	   imgbytes=new String[imgs.length];
	    	   
	    	   for (int g = 0; g < imgs.length; g++) {
				
			
	    	   
	        
	        	 URL ur = new URL("" + imgs[g]);
	             HttpURLConnection connection = (HttpURLConnection) ur
	               .openConnection();
	             connection.setDoInput(true);
	             connection.connect();
	             InputStream input = connection.getInputStream();

	             Bitmap mIcon_val = BitmapFactory.decodeStream(input);
	             
	             ByteArrayOutputStream baos = new ByteArrayOutputStream();
	             mIcon_val.compress(Bitmap.CompressFormat.JPEG, 70, baos);
	             
	             imgbytes[g] = Utills.BitMapToString(mIcon_val);
	        } 
	    	   }catch (Exception e) {
	              e.printStackTrace();
	        }catch (OutOfMemoryError e) {
	        	 e.printStackTrace();
			}
	    	   
	         
	    	   
	         
	         
	      return imgbytes[0];
	       }
	    
	       
	       @Override
	    protected void onPostExecute(String result) {
	    	   
	    	   pdialog.dismiss();
	    	   
	    	   adaptermatchlist adapter = new adaptermatchlist();
				uiC_listVmatch.setAdapter(adapter);
				
	    	super.onPostExecute(result);
	    }
	   }
	
	
	

}
