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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.internal.lk;

import project.swapstuff.model.Utills;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MatchingScreen extends Fragment {

	
	float positionX,endX;
	
	public MatchingScreen() {
	}

	public MatchingScreen(Context conn) {

		this.con = conn;
	}

	Context con;

	ImageView uiC_imgVMatchingDP;
	TextView uiC_txtvTitle, uiC_txtvKM, uiC_txtvDesc,uiC_txtvSearch;
	ImageButton uiC_imgbtnLike, uiC_imgbtnDislike,uiC_ReportAbuse;
	
	LinearLayout uiC_layoutDetailsContaoner;
	
	
	String likeDislike="";

	String htmlText;
	
	static String[] itmID, Distance, DateTimeCreated, itemName,
	ItemDescription, ProfileID, imgs;
	
	static int itemposition=0;
	
	JSONArray jsnArray;
	
	boolean lk_dlk;
	
	FrameLayout uiClayout_frame;
	Toast toast;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_matchingscreen,
				container, false);
		uiC_imgVMatchingDP = (ImageView) rootView
				.findViewById(R.id.uiC_imgVMatchingScreen);
		uiC_imgbtnLike = (ImageButton) rootView.findViewById(R.id.uiC_imgbtn_like);
		uiC_imgbtnDislike = (ImageButton) rootView.findViewById(R.id.uiC_imgbtn_close);
		uiC_ReportAbuse = (ImageButton) rootView.findViewById(R.id.uiC_imgbtnReportAbuse);
		uiC_txtvTitle = (TextView) rootView.findViewById(R.id.uiC_txtVtitlematching);
		uiC_txtvDesc = (TextView) rootView.findViewById(R.id.uiC_txtvdescMatching);
		uiC_txtvKM = (TextView) rootView.findViewById(R.id.uiC_txtvKMmatching);
		uiC_txtvSearch = (TextView) rootView.findViewById(R.id.uiC_txtVSearch);
		uiC_layoutDetailsContaoner=(LinearLayout)rootView.findViewById(R.id.uiClayoutDetailContainer);
		uiClayout_frame=(FrameLayout)rootView.findViewById(R.id.Layout_frame);
		
		
	
		if(Utills.haveNetworkConnection(getActivity()))
		{
			new asyncNearbyUsers().execute();
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
			
			uiC_ReportAbuse.setVisibility(View.GONE);
			uiC_imgbtnLike.setVisibility(View.GONE);
			uiC_imgbtnDislike.setVisibility(View.GONE);
			uiC_txtvTitle .setVisibility(View.GONE);
			uiC_txtvDesc .setVisibility(View.GONE);
			uiC_txtvKM.setVisibility(View.GONE);
			uiC_imgVMatchingDP.setVisibility(View.GONE);
			uiC_txtvSearch.setVisibility(View.VISIBLE);
			
			uiC_layoutDetailsContaoner.setBackgroundColor(Color.TRANSPARENT);
		
		}
		
		
		uiC_imgbtnDislike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(Utills.haveNetworkConnection(getActivity()))
				{
					likeDislike="2";
					 Animation myRotation = AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left);
				        uiC_imgVMatchingDP.startAnimation(myRotation);
				    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
				new asyncLikeDislike().execute();
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
		
		
		
		uiC_imgbtnLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(Utills.haveNetworkConnection(getActivity()))
				{
					likeDislike="1";
					 Animation myRotation = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right_in);
				        uiC_imgVMatchingDP.startAnimation(myRotation);
				    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
				        
					new asyncLikeDislike().execute();
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

		
		
		uiC_ReportAbuse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(con, "Reported Abuse ! ", Toast.LENGTH_SHORT).show();
				

				
				new AlertDialog.Builder(getActivity())
			    .setTitle("Warning !")
			    .setMessage("Report item ?")
			    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        
//			        	likeDislike="3";
//						 Animation myRotation = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right_in);
//					        uiC_imgVMatchingDP.startAnimation(myRotation);
//					    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
//						new asyncLikeDislike().execute();
			        	
			        	
			        	
			        	
			        	Intent gotoContactscreen=new Intent(getActivity(),Contact_For_Report.class);
			        	gotoContactscreen.putExtra("id", itmID[itemposition].replace("\"", ""));
			        	gotoContactscreen.putExtra("finish","y");
			        	startActivity(gotoContactscreen);
//			        	if(itemposition==0)
//						{	
//							ShowAnim(1);
//							itemposition=2;	
//						}
//						else
//						{
//							ShowAnim(itemposition);
//							itemposition++;	
//						}
			        	
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
		
		
//		uiC_imgVMatchingDP.setOnTouchListener(new OnSwipeTouchListener() {
//			public void onSwipeTop() {
////				Toast.makeText(con, "top", Toast.LENGTH_SHORT).show();
//			}
//
//			public void onSwipeRight() {
//
//				Toast.makeText(con, "Like", Toast.LENGTH_SHORT).show();
////				uiC_imgbtnLike.setBackgroundColor(Color.parseColor("#3366AB"));
////				uiC_imgbtnDislike.setBackgroundColor(Color.TRANSPARENT);
//				likeDislike="1";
//				
////				ShowAnim();
//				
//			        Animation myRotation = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right_in);
//			        uiC_imgVMatchingDP.startAnimation(myRotation);
//			        
//			    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
//			        
//				new asyncLikeDislike().execute();
//				
//				
//			}
//
//			public void onSwipeLeft() {
//				Toast.makeText(con, "Dislike", Toast.LENGTH_SHORT).show();
////				uiC_imgbtnDislike.setBackgroundColor(Color.parseColor("#3366AB"));
////				uiC_imgbtnLike.setBackgroundColor(Color.TRANSPARENT);
//				likeDislike="2";
//				
//				
////				ShowAnim();
//				 Animation myRotation = AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left);
//			        uiC_imgVMatchingDP.startAnimation(myRotation);
//			        
//			    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
//			        
//				new asyncLikeDislike().execute();
////				if((itemName.length-1)>itemposition)
////				{
////					itemposition++;
////					new asyncLikeDislike().execute();
////				}
////				else
////				{
////					uiC_imgbtnLike.setVisibility(View.GONE);
////					uiC_imgbtnDislike.setVisibility(View.GONE);
////					uiC_txtvTitle .setVisibility(View.GONE);
////					uiC_txtvDesc .setVisibility(View.GONE);
////					uiC_txtvKM.setVisibility(View.GONE);
////					uiC_imgVMatchingDP.setVisibility(View.GONE);
////					uiC_txtvSearch.setVisibility(View.VISIBLE);
////				}
//				
//				
//			}
//
//			public void onSwipeBottom() {
////				Toast.makeText(con, "bottom", Toast.LENGTH_SHORT).show();
//			}
//
//			public boolean onTouch(View v, MotionEvent event) {
//				return gestureDetector.onTouchEvent(event);
//			}
//		});

		
		
		
		
		
		uiC_imgVMatchingDP.setOnTouchListener(new OnSwipeTouchListener()
		{
			
		
		public void onSwipeLeft() {
			Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
			System.out.println("left");
			super.onSwipeLeft();
		}
	
			public void onSwipeRight() {
			Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
			System.out.println("right");
				super.onSwipeRight();
			}	
			
			
			public boolean onTouch(View view, MotionEvent event) {
				PointF StartPT = new PointF();
					
//		    	 if(Math.abs(positionX)-Math.abs(StartPT.x) >100)
//		    	 {
//		    		 Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
//		 			System.out.println("right");
//		    	 }
		    	 
		    	 
				 ClipData data = ClipData.newPlainText("", "");
			        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			        view.startDrag(data, shadowBuilder, view, 0);
			       
//				return gestureDetector.onTouchEvent(event);
				return false;
			}
		

			
		});
		
		
		uiClayout_frame.setOnDragListener(new  MyDragListener());
		
		
		
		
		return rootView;
	}
	
	

	// Gesture detector class
	public class OnSwipeTouchListener implements OnTouchListener {

		final GestureDetector gestureDetector;

		public OnSwipeTouchListener() {
			gestureDetector = new GestureDetector(con, new GestureListener());
		}

		private final class GestureListener extends SimpleOnGestureListener {

			private static final int SWIPE_THRESHOLD = 100;
			private static final int SWIPE_VELOCITY_THRESHOLD = 100;

			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				boolean result = false;
				try {
					float diffY = e2.getY() - e1.getY();
					float diffX = e2.getX() - e1.getX();
					if (Math.abs(diffX) > Math.abs(diffY)) {
						if (Math.abs(diffX) > SWIPE_THRESHOLD
								&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
							if (diffX > 0) {
								onSwipeRight();
							} else {
								onSwipeLeft();
							}
						}
						result = true;
					} else if (Math.abs(diffY) > SWIPE_THRESHOLD
							&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffY > 0) {
							onSwipeBottom();
						} else {
							onSwipeTop();
						}
					}
					result = true;

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return result;
			}
		}

		public void onSwipeRight() {
			
			
		}

		public void onSwipeLeft() {
			
		}

		public void onSwipeTop() {
		}

		public void onSwipeBottom() {
		}

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
	    	
//	    	PointF StartPT = new PointF();
//	    	 positionX=StartPT.x;
	    	
//	    	 if(Math.abs(positionX)-Math.abs(StartPT.x) >70)
//	    	 {
//	    		 Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
//	 			System.out.println("right");
//	    	 }
	    	 
//	      
	        return false;
//	      } else {
//	        return false;
//	      }
	    }
	}

	// ----------------------Class to get list of near by users
	class asyncNearbyUsers extends AsyncTask<Void, Void, Void> {

		ProgressDialog pd;
		
		TransparentProgressDialog progressdialog=new TransparentProgressDialog(getActivity(), R.drawable.ic_search);
	
		@Override
		protected void onPreExecute() {
			
//			pd=ProgressDialog.show(getActivity(), "", "Loading..");
			progressdialog.show();
			
			uiC_imgbtnLike.setVisibility(View.GONE);
			uiC_imgbtnDislike.setVisibility(View.GONE);
			uiC_txtvTitle .setVisibility(View.GONE);
			uiC_txtvDesc .setVisibility(View.GONE);
			uiC_txtvKM.setVisibility(View.GONE);
			uiC_imgVMatchingDP.setVisibility(View.GONE);
			uiC_ReportAbuse.setVisibility(View.GONE);
			
			uiC_layoutDetailsContaoner.setBackgroundColor(Color.TRANSPARENT);
			
			uiC_txtvSearch.setVisibility(View.INVISIBLE);
			
			
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String result = "";

			String HostUrl = "http://116.193.163.158:8083/Items/GetItemsNearBy";
//			String HostUrl = "http://116.193.163.156:8012/Items/GetItemsNearBy";
		
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();
		
				  params1.add(new BasicNameValuePair("ItemID",Utills.itemid.replace("\"", "")));
			       params1.add(new BasicNameValuePair("ProfileID", Utills.id.replace("\"", "")));
			       params1.add(new BasicNameValuePair("ItemTitle",""));
			       params1.add(new BasicNameValuePair("ItemDescription",""));
			       params1.add(new BasicNameValuePair("ItemImage",""));
			       params1.add(new BasicNameValuePair("ItemDateTimeCreated",""));
			       params1.add(new BasicNameValuePair("IsActive","true"));

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

				jsnArray = new JSONArray(result);

				
				
				itmID = new String[jsnArray.length()];
				Distance = new String[jsnArray.length()];
//				DateTimeCreated = new String[jsnArray.length()];
				itemName = new String[jsnArray.length()];
				ItemDescription = new String[jsnArray.length()];
				ProfileID = new String[jsnArray.length()];
				imgs = new String[jsnArray.length()];

				for (int g = 0; g < jsnArray.length(); g++) {

					JSONObject oj = jsnArray.getJSONObject(g);

					itmID[g] = oj.getString("ItemID");
					
						String dis	= oj.getString("Distance");
					
						Distance[g] =dis.substring(0, dis.indexOf(".")+2);
//					DateTimeCreated[g] = oj.getString("DateTimeCreated");
					itemName[g] = oj.getString("ItemTitle");
					ItemDescription[g] = oj.getString("ItemDescription");
					ProfileID[g]=oj.getString("ProfileID");
					imgs[g] = oj.getString("ItemImage");

					Log.e("TAG", "" + itmID[g]);
					
				}
				itemposition=0;

				Log.e("TAG", "" + itemName[0]);
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.e("TAG", "ClientProtocolException in callWebService(). "
						+ e.getMessage());
				
			
				
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("TAG",
						"IOException in callWebService(). " + e.getMessage());
				;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			
			try {
//			pd.dismiss();
				
				uiC_imgbtnLike.setVisibility(View.VISIBLE);
				uiC_imgbtnDislike.setVisibility(View.VISIBLE);
				uiC_txtvTitle .setVisibility(View.VISIBLE);
				uiC_txtvDesc .setVisibility(View.VISIBLE);
				uiC_txtvKM.setVisibility(View.VISIBLE);
				uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
				uiC_ReportAbuse.setVisibility(View.VISIBLE);
				
				progressdialog.dismiss();
				
//				uiC_txtvSearch.setVisibility(View.VISIBLE);
			
			if(jsnArray.length()<=0)
			{


				uiC_imgbtnLike.setVisibility(View.GONE);
				uiC_imgbtnDislike.setVisibility(View.GONE);
				uiC_txtvTitle .setVisibility(View.GONE);
				uiC_txtvDesc .setVisibility(View.GONE);
				uiC_txtvKM.setVisibility(View.GONE);
				uiC_imgVMatchingDP.setVisibility(View.GONE);
				uiC_ReportAbuse.setVisibility(View.GONE);
				uiC_txtvSearch.setVisibility(View.VISIBLE);
			
				uiC_layoutDetailsContaoner.setBackgroundColor(Color.TRANSPARENT);
			
				
			}
			else
			{
			String DescText=ItemDescription[0];
			 htmlText = "<body><b>Description:</b><br><i>"+DescText+"</i></body>";
			uiC_txtvTitle.setText(itemName[0]);
			uiC_txtvKM.setText(Distance[0]+"-km away.");
			uiC_txtvDesc.setText(Html.fromHtml(htmlText));
			
			uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[0]));
			}
			} catch (Exception e) {
				e.printStackTrace();
				
				uiC_ReportAbuse.setVisibility(View.GONE);
				uiC_imgbtnLike.setVisibility(View.GONE);
				uiC_imgbtnDislike.setVisibility(View.GONE);
				uiC_txtvTitle .setVisibility(View.GONE);
				uiC_txtvDesc .setVisibility(View.GONE);
				uiC_txtvKM.setVisibility(View.GONE);
				uiC_imgVMatchingDP.setVisibility(View.GONE);
				uiC_txtvSearch.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}

	}
//-----------------------------------------------------------
	
	
	
	
	
//	-------------Like dislike or report abuse
	class asyncLikeDislike extends AsyncTask<Void, Void, Void>
	{

		ProgressDialog pd;
//		TransparentProgressDialog progressdialog=new TransparentProgressDialog(getActivity(), R.drawable.arrow);
		
		 String result = "";
		 
		 String resuluresponse="";
		 
		@Override
		protected void onPreExecute() {
			pd=ProgressDialog.show(getActivity(), "", "Loading..");
//			progressdialog.show();
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {       
			
			 
		     String HostUrl="http://116.193.163.158:8083/ItemMatchs/SaveItemMatch"; 
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
		       params1.add(new BasicNameValuePair("ItemID", itmID[itemposition].replace("\"", "")));
		       params1.add(new BasicNameValuePair("ProfileIdBy",(Utills.id).replace("\"", "")));
		       params1.add(new BasicNameValuePair("Distance","0"));
		       params1.add(new BasicNameValuePair("IsLikeDislikeAbuseBy",likeDislike));
		       params1.add(new BasicNameValuePair("DateTimeCreated",""));

		       Log.i("json response", ""+params1.toString());
		       
		       httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		       httpPost.setHeader("Accept", "application/json");

		       HttpEntity entityLike = new UrlEncodedFormEntity(params1, "UTF-8");
		       httpPost.setEntity(entityLike);

		       ResponseHandler<String> handlerlike = new BasicResponseHandler();
		       result =httpClient.execute(httpPost,handlerlike);
		       
		       
		       
		       Log.i("json response", ""+result.toString());

		       
		       resuluresponse=result.toString().replace("\"", "");
		       
		       
		       
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
			
			
			
			pd.dismiss();
			
			
			if(resuluresponse.equalsIgnoreCase("Matched"))
			{
				ShowDialog_matched();
			}
			else
			{
				if(itemposition==0)
				{	
					ShowAnim(1);
					itemposition=1;	
				}
				else
				{
					ShowAnim(itemposition+1);
					itemposition++;	
				}
			}
			
			
			
			
			
			
			
			
		
		
//		progressdialog.dismiss();
			
			super.onPostExecute(result);
		}
		
	}
	
	
//	popup matched
	public void ShowDialog_matched() 
	{
		
		SharedPreferences shared = getActivity().getSharedPreferences("",
				getActivity().MODE_PRIVATE);

		String imgbytes = shared.getString("imgITEM", "");
		
		final Dialog dialog=Utills.prepararDialog(getActivity(), R.layout.there_is_match);
		
		ImageView uiC_imgVmyItem=(ImageView)dialog.findViewById(R.id.uiCimgVitem1);
		ImageView uiC_imgVotherItem=(ImageView)dialog.findViewById(R.id.uiCimgVitem2);
		Button uiC_btnChat=(Button)dialog.findViewById(R.id.uiCbtnsendMsgMatch);
		Button uiC_btnCancel=(Button)dialog.findViewById(R.id.uiCbtnCancel);
		
		uiC_imgVmyItem.setImageBitmap(Utills.StringToBitMap(imgbytes));
		uiC_imgVotherItem.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
		
		uiC_btnChat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(Utills.haveNetworkConnection(getActivity()))
				{
					Intent gotochat = new Intent(getActivity(), ChatScreen_.class);
					gotochat.putExtra("itemid", itmID[itemposition]);
					gotochat.putExtra("Title", itemName[itemposition]);
					gotochat.putExtra("pid", ProfileID[itemposition]);
					gotochat.putExtra("km", Distance[itemposition]);
					Utills.Imagebytee=imgs[itemposition];
//					gotochat.putExtra("img", imgs[position]);
					
					if(itemposition==0)
					{	
						ShowAnim(1);
						itemposition=1;	
					}
					else
					{
						ShowAnim(itemposition+1);
						itemposition++;	
					}
					dialog.dismiss();
					startActivity(gotochat);
					
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
		uiC_btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(itemposition==0)
				{	
					ShowAnim(1);
					itemposition=1;	
				}
				else
				{
					ShowAnim(itemposition+1);
					itemposition++;	
				}
				
				dialog.dismiss();
				
			}
		});
		
		dialog.show();
		
	}

	public void ShowAnim(int position) {

		if((itemName.length)>position)
		{
			
		
			uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[position]));
		
	
		
		 Animation animupdown = AnimationUtils.loadAnimation(getActivity(), R.anim.top_to_bottomin);
		 uiC_txtvTitle.startAnimation(animupdown);
		 uiC_txtvKM.startAnimation(animupdown);
		 uiC_txtvDesc.startAnimation(animupdown);
		
		String DescText=ItemDescription[position];
		 htmlText = "<body><h1>Description:</h1><br><i>"+DescText+"</i></body>";
		uiC_txtvTitle.setText(itemName[position]);
		uiC_txtvKM.setText(Distance[position]+"-km away.");
		uiC_txtvDesc.setText(Html.fromHtml(htmlText));
		
//		uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
		
//		itemposition++;
		
		}
		else
		{
			
			uiC_imgbtnLike.setVisibility(View.GONE);
			uiC_imgbtnDislike.setVisibility(View.GONE);
			uiC_txtvTitle .setVisibility(View.GONE);
			uiC_txtvDesc .setVisibility(View.GONE);
			uiC_txtvKM.setVisibility(View.GONE);
			uiC_imgVMatchingDP.setVisibility(View.GONE);
			uiC_ReportAbuse.setVisibility(View.GONE);
			uiC_txtvSearch.setVisibility(View.VISIBLE);
			
			uiC_layoutDetailsContaoner.setBackgroundColor(Color.TRANSPARENT);
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	DRAG  LISTNER
	class MyDragListener implements OnDragListener {
		  Drawable enterShape = getResources().getDrawable(R.drawable.like_btn);
		  Drawable normalShape = getResources().getDrawable(R.drawable.dislike);
		  
		  
		  
		  @Override
		  public boolean onDrag(View v, DragEvent event) {
		    int action = event.getAction();
		    switch (event.getAction()) {
		    case DragEvent.ACTION_DRAG_STARTED:
		    // do nothing
		    	uiC_imgVMatchingDP.setVisibility(View.INVISIBLE);
		    	
		    	endX=event.getX();
		    	
		    	positionX=event.getX();
		    	
		    	 System.out.println("start"+endX);
		      break;
		    case DragEvent.ACTION_DRAG_ENTERED:
//		      v.setBackgroundDrawable(enterShape);
		      System.out.println("enter");
		      break;
		      
		      
		      
		      
		      
		      
		      
		    case DragEvent.ACTION_DRAG_EXITED:        
//		      v.setBackgroundDrawable(normalShape);
		    	if(toast!=null)
	    		{
    				toast.cancel();
    			}
		      System.out.println("exited");
		  
		      uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
//		      uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
//		      if(Math.abs(endX)-Math.abs(positionX)>100)
//		      {
//	    		 Toast.makeText(con, "left", Toast.LENGTH_SHORT).show();
//	    		 System.out.println("leftp");
//	    			
//	    		 		likeDislike="1";	 
//				    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
//				    	new asyncLikeDislike().execute();
//		      }
//    		 else if(Math.abs(endX)<Math.abs(positionX))
//    		 {
//    			 Toast.makeText(con, "right", Toast.LENGTH_SHORT).show();
//	    		 System.out.println("rightp");
//	    		 
//	    		 	likeDislike="2";
//			    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
//			    	new asyncLikeDislike().execute();
//	    		 
//    		 }
		      System.out.println("exited ended"+Math.abs(endX)+Math.abs(positionX)+"");
		      break;
		      
		      
		      
		      
		      
		      
		      
		      
		      
		    case DragEvent.ACTION_DROP:
		      // Dropped, reassign View to ViewGroup
		    	uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
		    	if(toast!=null)
	    		{
    				toast.cancel();
    			}
		    	 System.out.println("drop");
		    	try {
		    		 if(Math.abs(endX)-Math.abs(positionX)>150)
				      {
//			    		 Toast.makeText(con, "Dislike", Toast.LENGTH_SHORT).show();
			    		 System.out.println("leftp");
			    		 
			    		 	likeDislike="2";	 
//					    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
			    		 	if(Utills.haveNetworkConnection(getActivity()))
			    		 	{
			    		 		new asyncLikeDislike().execute();
			    		 	}
					    	
			    		 
				      }
		    		 else if(Math.abs(positionX)-Math.abs(endX)>150)
		    		 {
//		    			 Toast.makeText(con, "Like", Toast.LENGTH_SHORT).show();
			    		 System.out.println("rightp");
			    		 
			    		 	likeDislike="1";
//					    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
			    		 	if(Utills.haveNetworkConnection(getActivity()))
			    		 	{
			    		 		new asyncLikeDislike().execute();
			    		 	}
			    		 		
					    	
			    		 
		    		 }
				      System.out.println("drop ended"+Math.abs(endX)+Math.abs(positionX)+"");
				     
//		      View view = (View) event.getLocalState();
//		      ViewGroup owner = (ViewGroup) view.getParent();
//		      owner.removeView(view);
//		      LinearLayout container = (LinearLayout) v;
//		      container.addView(view);
//		      view.setVisibility(View.VISIBLE);
		    	} catch (Exception e) {
					e.printStackTrace();
				}
		      break;
		      
		    case DragEvent.ACTION_DRAG_LOCATION:
			   
		    	 System.out.println("loc drag");
 	
		    	positionX=event.getX();
		    	System.out.println("loc drag"+positionX+"end"+endX);
		    	 if(Math.abs(endX)-Math.abs(positionX)>150)
			      {
		    		if(toast!=null)
		    		{
	    				toast.cancel();
	    			}
		    			 System.out.println("leftp");
			    		 showCustomAlert(R.drawable.dislike_toast);
			    		 lk_dlk=false;
		    		
		    		
		    
			      }
	    		 else if(Math.abs(positionX)-Math.abs(endX)>150)
	    		 {
	    			if(toast!=null)
	    			{
	    				toast.cancel();
	    			}
	    				 System.out.println("rightp");
			    		 showCustomAlert(R.drawable.like_toast);
			    		 lk_dlk=true;
	    			
		    		
		    		
	    		 }
			      System.out.println("drop ended"+Math.abs(endX)+Math.abs(positionX)+"");
		    	
			      break;
		    
		    case DragEvent.ACTION_DRAG_ENDED:
		    	
		    	
		    	uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
//		      v.setBackgroundDrawable(normalShape);
		      
//		      if(Math.abs(endX)-Math.abs(positionX)>100)
//		      {
//	    		 Toast.makeText(con, "left", Toast.LENGTH_SHORT).show();
//	    		 System.out.println("leftp");
//	    		 
//	    		 	likeDislike="1";	 
//			    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
//			    	new asyncLikeDislike().execute();
//	    		 
//		      }
//		      else if(Math.abs(endX)<Math.abs(positionX))
//	    		 {
//	    			 Toast.makeText(con, "right", Toast.LENGTH_SHORT).show();
//		    		 System.out.println("rightp");
//		    		 
//		    		 	likeDislike="2";
//				    	uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
//				    	new asyncLikeDislike().execute();
//		    		 
//	    		 }
		    	if(toast!=null)
	    		{
    				toast.cancel();
    			}
		      System.out.println("drag ended"+Math.abs(endX)+Math.abs(positionX)+"");
		     
		      default:
		      break;
		      
		     
		    }
		   
		    return true;
		  }
		  
	}
	
	
	
	
	
	
	
	
	
	

	
	
	
	
//	PROGRESS DIALLOG
	ImageView iv;
	private class TransparentProgressDialog extends Dialog {
		
//		private ImageView iv;
			
		public TransparentProgressDialog(Context context, int resourceIdOfImage) {
			super(context, R.style.TransparentProgressDialog);
        	WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        	wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        	getWindow().setAttributes(wlmp);
        	
        
        	
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
//		layout.setBackgroundColor(Color.RED);

		LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(500,500);
		paramsLayout.gravity=Gravity.CENTER;

//LayoutInflater layoutInflater = (LayoutInflater) 
//   getSystemService(Context.LAYOUT_INFLATER_SERVICE);    
//layout.addView(layoutInflater.inflate(R.layout.activity_main,layout, false) ); 
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity=Gravity.CENTER_HORIZONTAL;
		
		iv = new ImageView(context);
		
		iv.setImageResource(resourceIdOfImage);
//		layout.setPadding(40,40, 40, 40);
	
		TextView tVloading=new TextView(getActivity());
		tVloading.setText("Finding items near you..");
		tVloading.setTextSize(22);
//		tVloading.setTextSize(27);
		
		
		layout.addView(iv, params);
		layout.addView(tVloading, params);
		addContentView(layout, paramsLayout);
//		setContentView(R.layout.dialogo_choose);
	}
			
		@Override
		public void show() {
			super.show();
//			RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			
				ani_0 = new TranslateAnimation (
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
				
				ani_1 = new TranslateAnimation (
					Animation.RELATIVE_TO_SELF,  -1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			ani_0.setInterpolator (new AccelerateDecelerateInterpolator ());
			ani_0.setDuration (1000);
			ani_0.setFillEnabled (true);
			ani_0.setFillAfter (true);
			ani_0.setAnimationListener (animationListener);
			ani_1.setInterpolator (new AccelerateDecelerateInterpolator ());
			ani_1.setDuration (1000);
			ani_1.setFillEnabled (true);
			ani_1.setFillAfter (true);
			ani_1.setAnimationListener (animationListener);
			
//			Animation anim= new MyAnimation(iv, 90);
			
//			anim.setInterpolator(new LinearInterpolator());
//			anim.setRepeatCount(Animation.INFINITE);
//			anim.setDuration(700);
//			iv.setAnimation(anim);
			iv.startAnimation(ani_0);
		}
	}
//	PROGRESS DIALLOG
	TranslateAnimation ani_0,ani_1;
	final AnimationListener animationListener = new AnimationListener () {
		@ Override   
		public   void onAnimationEnd (Animation animation) {
		if (animation == ani_0) {
		iv.startAnimation (ani_1);
		}
		if (animation == ani_1) {
		iv.startAnimation (ani_0);
		}
		}
		  
		@ Override   
		public    void onAnimationRepeat (Animation animation) {
		
		}
		  
		@ Override   
		public    void onAnimationStart (Animation animation) {
	
		}
		};

//	/
	public class MyAnimation extends Animation {

	    private View view;
	    private float cx, cy;           // center x,y position of circular path
	    private float prevX, prevY;     // previous x,y position of image during animation
	    private float r;                // radius of circle


	    /**
	     * @param view - View that will be animated
	     * @param r - radius of circular path
	     */
	    public MyAnimation(View view, float r){
	        this.view = view;
	        this.r = r;
	    }

	    @Override
	    public boolean willChangeBounds() {
	        return true;
	    }

	    @Override
	    public void initialize(int width, int height, int parentWidth, int parentHeight) {
	        // calculate position of image center
	        int cxImage = width / 2;
	        int cyImage = height / 2;
	        cx = view.getLeft() + cxImage;
	        cy = view.getTop() + cyImage;

	        // set previous position to center
	        prevX = cx;
	        prevY = cy;
	    }

	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        if(interpolatedTime == 0){
	            // I ran into some issue where interpolated would be
	            return;
	        }

	        float angleDeg = (interpolatedTime * 360f + 90) % 360;
	        float angleRad = (float) Math.toRadians(angleDeg);

	        // r = radius, cx and cy = center point, a = angle (radians)
	        float x = (float) (cx + r * Math.cos(angleRad));
	        float y = (float) (cy + r * Math.sin(angleRad));


	        float dx = prevX - x;
	        float dy = prevY - y;

	        prevX = x;
	        prevY = y;

	        t.getMatrix().setTranslate(dx, dy);
	    }
	}
//	/
	
	
	
	
	
	
	
	
	public void showCustomAlert(int res)
    {
         
        Context context = getActivity();
        
        
        
        
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getActivity().getLayoutInflater();
          
        // Call toast.xml file for toast layout 
        View toastRoot = inflater.inflate(R.layout.toast_like, null);
          
        
        
        ImageView imglike_dislike=(ImageView)toastRoot.findViewById(R.id.imgVL_D);
        imglike_dislike.bringToFront();
        imglike_dislike.setImageResource(res);
        toast = new Toast(context);
         
        // Set layout to toast 
        toast.setView(toastRoot);
        toast.setGravity(Gravity.TOP | Gravity.CENTER,
                0, 90);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
         
    }
	
	
	
	
	
}

