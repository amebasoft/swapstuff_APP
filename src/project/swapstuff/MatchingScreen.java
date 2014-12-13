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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import project.swapstuff.adapter.ViewPagerAdapter;
import project.swapstuff.model.NearbyItems;
import project.swapstuff.model.Utills;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class MatchingScreen extends Fragment {

	float positionX, endX;

	public MatchingScreen() {
	}

	public MatchingScreen(Context conn) {

		this.con = conn;
	}

	Context con;

//	SmartImageView  uiC_imgVMatchingDP;
	TextView uiC_txtvTitle, uiC_txtvKM, uiC_txtvDesc, uiC_txtvSearch;
	ImageButton uiC_imgbtnLike, uiC_imgbtnDislike, uiC_ReportAbuse;

	LinearLayout uiC_layoutDetailsContaoner;

	String likeDislike = "";

	String htmlText;

//	static String[] itmID, Distance, DateTimeCreated, itemName,
//			ItemDescription, ProfileID, imgs;
	
	ArrayList<NearbyItems> NearbyitemsList=new ArrayList<NearbyItems>();

	static int itemposition=0;
	static int itempositionTEMP=0;

	JSONArray jsnArray;

	boolean lk_dlk;

	FrameLayout uiClayout_frame;
	Toast toast;

	ProgressBar progressBarImageShow;
	
	 int  lastPage,  mCurrentSelectedScreen,mNextSelectedScreen=0; 
	ViewPagerAdapter adapter;
	 ViewPager myPager;
	 
	 @Override
	public void onResume() {
		   lastPage= mCurrentSelectedScreen=mNextSelectedScreen=itemposition=itempositionTEMP=0; 
		super.onResume();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_matchingscreen,
				container, false);
//		uiC_imgVMatchingDP = (SmartImageView ) rootView
//				.findViewById(R.id.uiC_imgVMatchingScreen);
		
		
		   myPager = (ViewPager) rootView.findViewById(R.id.myfivepanelpager);
		 
		progressBarImageShow =(ProgressBar)rootView.findViewById(R.id.progressBarImageShow);
		uiC_imgbtnLike = (ImageButton) rootView
				.findViewById(R.id.uiC_imgbtn_like);
		uiC_imgbtnDislike = (ImageButton) rootView
				.findViewById(R.id.uiC_imgbtn_close);
		uiC_ReportAbuse = (ImageButton) rootView
				.findViewById(R.id.uiC_imgbtnReportAbuse);
		uiC_txtvTitle = (TextView) rootView
				.findViewById(R.id.uiC_txtVtitlematching);
		uiC_txtvDesc = (TextView) rootView
				.findViewById(R.id.uiC_txtvdescMatching);
		uiC_txtvKM = (TextView) rootView.findViewById(R.id.uiC_txtvKMmatching);
		uiC_txtvSearch = (TextView) rootView.findViewById(R.id.uiC_txtVSearch);
		uiC_layoutDetailsContaoner = (LinearLayout) rootView
				.findViewById(R.id.uiClayoutDetailContainer);
		uiClayout_frame = (FrameLayout) rootView
				.findViewById(R.id.Layout_frame);

		if (Utills.haveNetworkConnection(getActivity())) {
			new asyncNearbyUsers().execute();
		} else {
			if (toast != null) {
				toast.cancel();
			}
			toast = Toast.makeText(getActivity(), "No network connection..!",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

			uiC_ReportAbuse.setVisibility(View.GONE);
			uiC_imgbtnLike.setVisibility(View.GONE);
			uiC_imgbtnDislike.setVisibility(View.GONE);
			uiC_txtvTitle.setVisibility(View.GONE);
			uiC_txtvDesc.setVisibility(View.GONE);
			uiC_txtvKM.setVisibility(View.GONE);
//			uiC_imgVMatchingDP.setVisibility(View.GONE);
			uiC_txtvSearch.setVisibility(View.VISIBLE);

			uiC_layoutDetailsContaoner.setBackgroundColor(Color.TRANSPARENT);

		}

		uiC_imgbtnDislike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (Utills.haveNetworkConnection(getActivity())) {
					likeDislike = "2";
					Animation myRotation = AnimationUtils.loadAnimation(
							getActivity(), R.anim.right_to_left);
//					uiC_imgVMatchingDP.startAnimation(myRotation);
//					uiC_imgVMatchingDP.setImageBitmap(Utills
//							.StringToBitMap(imgs[itemposition]));
					
//					uiC_imgVMatchingDP.setImageUrl(imgs[0]);
					
					new asyncLikeDislike().execute();
				} else {
					if (toast != null) {
						toast.cancel();
					}
					toast = Toast.makeText(getActivity(),
							"No network connection..!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}

			}
		});

		uiC_imgbtnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Utills.haveNetworkConnection(getActivity())) {
					likeDislike = "1";
					Animation myRotation = AnimationUtils.loadAnimation(
							getActivity(), R.anim.left_to_right_in);
//					uiC_imgVMatchingDP.startAnimation(myRotation);
//					uiC_imgVMatchingDP.setImageBitmap(Utills
//							.StringToBitMap(imgs[itemposition]));

//					uiC_imgVMatchingDP.setImageUrl(imgs[0]);
					
					new asyncLikeDislike().execute();
				} else {
					if (toast != null) {
						toast.cancel();
					}
					toast = Toast.makeText(getActivity(),
							"No network connection..!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}

			}
		});

		uiC_ReportAbuse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(con, "Reported Abuse ! ",
				// Toast.LENGTH_SHORT).show();

				new AlertDialog.Builder(getActivity())
						.setTitle("Warning !")
						.setMessage("Report item ?")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										
										Intent gotoContactscreen = new Intent(
												getActivity(),
												Contact_For_Report.class);
										gotoContactscreen.putExtra("id",
												NearbyitemsList.get(itemposition).getItmID().replace(
														"\"", ""));
										gotoContactscreen.putExtra("finish",
												"y");
										startActivity(gotoContactscreen);
										// if(itemposition==0)
										// {
										// ShowAnim(1);
										// itemposition=2;
										// }
										// else
										// {
										// ShowAnim(itemposition);
										// itemposition++;
										// }

									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();

			}
		});


//		uiC_imgVMatchingDP.setOnTouchListener(new OnSwipeTouchListener() {
//
//			public void onSwipeLeft() {
//				Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT)
//						.show();
//				System.out.println("left");
//				super.onSwipeLeft();
//			}
//
//			public void onSwipeRight() {
//				Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT)
//						.show();
//				System.out.println("right");
//				super.onSwipeRight();
//			}
//
//			public boolean onTouch(View view, MotionEvent event) {
//				
//				ClipData data = ClipData.newPlainText("", "");
//				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
//						view);
//				view.startDrag(data, shadowBuilder, view, 0);
//
//				// return gestureDetector.onTouchEvent(event);
//				return false;
//			}
//
//		});

		uiClayout_frame.setOnDragListener(new MyDragListener());
		
		
		
		myPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				 if(lastPage>arg0)
			      {
					//User Move to left
					 itempositionTEMP++;
					 if(itempositionTEMP<NearbyitemsList.size())
					 {
						 likeDislike = "1";
						 new asyncLikeDislike().execute();
					 }
					 
					
			      }
			    else if(lastPage<arg0) 
			     {
			    	//User Move to right
			    	itempositionTEMP--;
			    	
			    	
			    	likeDislike = "2";
			    	new asyncLikeDislike().execute();
			     }
			      lastPage=arg0;
			
			     
			      mCurrentSelectedScreen = arg0;
			      mNextSelectedScreen = arg0;
			}
			
			@Override
			public void onPageScrolled( int position , float positionOffset , int positionOffsetPixels ) {
				
				
				
				 if ( position == lastPage )
				    {
				        // We are moving to next screen on right side
				        if ( positionOffset > 0.2 )
				        {   
				        	
				            // Closer to next screen than to current
				            if ( position + 1 != mNextSelectedScreen )
				            {
				                mNextSelectedScreen = position + 1;

				                showCustomAlert(R.drawable.dislike_toast);
				            }
				        }
				        else
				        {
				        	
				            // Closer to current screen than to next
				            if ( position != mNextSelectedScreen )
				            {
				                mNextSelectedScreen = position;
				                
				                if(toast!=null)
				                {
				                	toast.cancel();
				                }
				            }
				        }
				    }
				    else
				    {
				        // We are moving to next screen left side
				        if ( positionOffset > 0.8 )
				        {   
				        	
				            // Closer to current screen than to next
				            if ( position + 1 != mNextSelectedScreen )
				            {
				                mNextSelectedScreen = position + 1;
				               
				                if(toast!=null)
				                {
				                	toast.cancel();
				                }
				            }
				        }
				        else
				        {
				        	
				            // Closer to next screen than to current
				            if ( position != mNextSelectedScreen )
				            {
				                mNextSelectedScreen = position;
				                showCustomAlert(R.drawable.like_toast);
				                
				               
				            }
				        }
				    }
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				progressBarImageShow.setVisibility(View.INVISIBLE);
			}
		});

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

			

			//
			return false;
			
		}
	}

	// ----------------------Class to get list of near by users
	class asyncNearbyUsers extends AsyncTask<Void, Void, Void> {

		

		TransparentProgressDialog progressdialog = new TransparentProgressDialog(
				getActivity(), R.drawable.ic_search);

		@Override
		protected void onPreExecute() {

			// pd=ProgressDialog.show(getActivity(), "", "Loading..");
			progressdialog.show();

			uiC_imgbtnLike.setVisibility(View.GONE);
			uiC_imgbtnDislike.setVisibility(View.GONE);
			uiC_txtvTitle.setVisibility(View.GONE);
			uiC_txtvDesc.setVisibility(View.GONE);
			uiC_txtvKM.setVisibility(View.GONE);
//			uiC_imgVMatchingDP.setVisibility(View.GONE);
			uiC_ReportAbuse.setVisibility(View.GONE);

			uiC_layoutDetailsContaoner.setBackgroundColor(Color.TRANSPARENT);

			uiC_txtvSearch.setVisibility(View.INVISIBLE);

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String result = "";

			String HostUrl = Utills.URL+"Items/GetItemsNearBy";
			// String HostUrl =
			// "http://116.193.163.156:8012/Items/GetItemsNearBy";

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();

				params1.add(new BasicNameValuePair("ItemID", Utills.itemid
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileID", Utills.id
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ItemTitle", ""));
				params1.add(new BasicNameValuePair("ItemDescription", ""));
				params1.add(new BasicNameValuePair("ItemImage", ""));
				params1.add(new BasicNameValuePair("ItemDateTimeCreated", ""));
				params1.add(new BasicNameValuePair("IsActive", "true"));

				
				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				HttpEntity entitymatch = new UrlEncodedFormEntity(params1,
						"UTF-8");
				httpPost.setEntity(entitymatch);

				ResponseHandler<String> handlermatch = new BasicResponseHandler();
				result = httpClient.execute(httpPost, handlermatch);

			

				jsnArray = new JSONArray(result);

//				itmID = new String[jsnArray.length()];
//				Distance = new String[jsnArray.length()];
//				// DateTimeCreated = new String[jsnArray.length()];
//				itemName = new String[jsnArray.length()];
//				ItemDescription = new String[jsnArray.length()];
//				ProfileID = new String[jsnArray.length()];
//				imgs = new String[jsnArray.length()];
				NearbyItems nearByitemsFirst=new NearbyItems();
				nearByitemsFirst.setItmID("");
				nearByitemsFirst.setDistance("");
				nearByitemsFirst.setImgs("");
				nearByitemsFirst.setItemDescription("");
				nearByitemsFirst.setItemName("");
				nearByitemsFirst.setProfileID("");
				
				NearbyitemsList.add(nearByitemsFirst);
				
				for (int g = 0; g < jsnArray.length(); g++) {
					
					NearbyItems nearByitems=new NearbyItems();

					JSONObject oj = jsnArray.getJSONObject(g);

//					itmID[g] = oj.getString("ItemID");
					nearByitems.setItmID(oj.getString("ItemID"));

					String dis = oj.getString("Distance");

//					Distance[g] = dis.substring(0, dis.indexOf(".") + 2);
					nearByitems.setDistance(dis.substring(0, dis.indexOf(".") + 2));
					// DateTimeCreated[g] = oj.getString("DateTimeCreated");
//					nearByitems.setDateTimeCreated(oj.getString("DateTimeCreated"));
					
//					itemName[g] = oj.getString("ItemTitle");
					nearByitems.setItemName(oj.getString("ItemTitle"));
					
//					ItemDescription[g] = oj.getString("ItemDescription");
					nearByitems.setItemDescription(oj.getString("ItemDescription"));
					
//					ProfileID[g] = oj.getString("ProfileID");
					nearByitems.setProfileID(oj.getString("ProfileID"));
					
//					imgs[g] = oj.getString("ItemImage");
					nearByitems.setImgs(oj.getString("ItemImage"));

					
					
					NearbyitemsList.add(nearByitems);

				}
			

								
				NearbyitemsList.add(nearByitemsFirst);
				 lastPage= mCurrentSelectedScreen=mNextSelectedScreen=itemposition=itempositionTEMP =NearbyitemsList.size()/2;

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
			
			} catch (JSONException e) {
			
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();

			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				// pd.dismiss();

				uiC_imgbtnLike.setVisibility(View.VISIBLE);
				uiC_imgbtnDislike.setVisibility(View.VISIBLE);
				uiC_txtvTitle.setVisibility(View.VISIBLE);
				uiC_txtvDesc.setVisibility(View.VISIBLE);
				uiC_txtvKM.setVisibility(View.VISIBLE);
//				uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
				uiC_ReportAbuse.setVisibility(View.VISIBLE);

				progressdialog.dismiss();

				// uiC_txtvSearch.setVisibility(View.VISIBLE);

				if (jsnArray.length() <= 0) {

					uiC_imgbtnLike.setVisibility(View.GONE);
					uiC_imgbtnDislike.setVisibility(View.GONE);
					uiC_txtvTitle.setVisibility(View.GONE);
					uiC_txtvDesc.setVisibility(View.GONE);
					uiC_txtvKM.setVisibility(View.GONE);
//					uiC_imgVMatchingDP.setVisibility(View.GONE);
					myPager.setVisibility(View.GONE);
					uiC_ReportAbuse.setVisibility(View.GONE);
					uiC_txtvSearch.setVisibility(View.VISIBLE);

					uiC_layoutDetailsContaoner
							.setBackgroundColor(Color.TRANSPARENT);

				} else {
					
					 adapter = new ViewPagerAdapter(getActivity(), NearbyitemsList);
					 myPager.setAdapter(adapter);
					  myPager.setCurrentItem(itemposition);
					  progressBarImageShow.setVisibility(View.VISIBLE);
					  myPager.bringToFront();
					  
					
					String DescText =NearbyitemsList.get(itemposition).getItemDescription();
					htmlText = "<body><b>Description:</b><br><i>" + DescText
							+ "</i></body>";
					uiC_txtvTitle.setText(NearbyitemsList.get(itemposition).getItemName());
					uiC_txtvKM.setText(NearbyitemsList.get(itemposition).getDistance() + "-km away.");
					uiC_txtvDesc.setText(Html.fromHtml(htmlText));

					// uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[0]));
//					Picasso.with(getActivity())
//							.load("https://swapstffstorage123.blob.core.windows.net/productimages/image_473bd9f1-60e8-49be-a866-6f9b28e5023b.jpg")
//							.error(R.drawable.ic_photos)
//							.into(uiC_imgVMatchingDP);
					
//					uiC_imgVMatchingDP.setImageUrl(imgs[0]);

				}
			} catch (Exception e) {
				e.printStackTrace();
				progressdialog.dismiss();

				uiC_ReportAbuse.setVisibility(View.GONE);
				uiC_imgbtnLike.setVisibility(View.GONE);
				uiC_imgbtnDislike.setVisibility(View.GONE);
				uiC_txtvTitle.setVisibility(View.GONE);
				uiC_txtvDesc.setVisibility(View.GONE);
				uiC_txtvKM.setVisibility(View.GONE);
				myPager.setVisibility(View.GONE);
//				uiC_imgVMatchingDP.setVisibility(View.GONE);
				uiC_txtvSearch.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}

	}

	// -----------------------------------------------------------

	// -------------Like dislike or report abuse
	class asyncLikeDislike extends AsyncTask<Void, Void, Void> {

		ProgressDialog pd;
		// TransparentProgressDialog progressdialog=new
		// TransparentProgressDialog(getActivity(), R.drawable.arrow);

		String result = "";

		String resuluresponse = "";

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getActivity(), "", "Loading..");
			// progressdialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			String HostUrl = Utills.URL+"ItemMatchs/SaveItemMatch";
			// String
			// HostUrl="http://116.193.163.156:8012/ItemMatchs/SaveItemMatch";

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();
				//
				// "ItemMatchID": 1,
				// "ItemID": 1,
				// "ProfileIdBy": 13,
				// "Distance": 0,
				// "IsLikeDislikeAbuseBy": 1,
				// "DateTimeCreated": "2014-09-23T17:30:35.8"
				
				params1.add(new BasicNameValuePair("ItemMatchID", "-1"));
				params1.add(new BasicNameValuePair("ItemID",
						NearbyitemsList.get(itemposition).getItmID().replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileIdBy", (Utills.id)
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("Distance", "0"));
				params1.add(new BasicNameValuePair("IsLikeDislikeAbuseBy",
						likeDislike));
				params1.add(new BasicNameValuePair("DateTimeCreated", ""));

				

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				HttpEntity entityLike = new UrlEncodedFormEntity(params1,
						"UTF-8");
				httpPost.setEntity(entityLike);

				ResponseHandler<String> handlerlike = new BasicResponseHandler();
				result = httpClient.execute(httpPost, handlerlike);

			

				resuluresponse = result.toString().replace("\"", "");

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			pd.dismiss();

			if (resuluresponse.equalsIgnoreCase("Matched")) {
				
				Utills.Imagebytee=NearbyitemsList.get(itemposition).getImgs();
				ShowDialog_matched();
				
			} else {
				
				
				if (itemposition == 0) {
					ShowAnim(1);
					itemposition = 1;
				} else {
					
					
					if(NearbyitemsList.size()-1==2)
					{
						ShowAnim(0);
					}
					else
					{
						NearbyitemsList.remove(itemposition);
						 adapter = new ViewPagerAdapter(getActivity(), NearbyitemsList);
						 myPager.setAdapter(adapter);
						 lastPage= mCurrentSelectedScreen=mNextSelectedScreen=itemposition=itempositionTEMP =NearbyitemsList.size()/2;
						myPager.setCurrentItem(itemposition, true);
						ShowAnim(itemposition);
					}
					
					
				}
			}

			// progressdialog.dismiss();

			super.onPostExecute(result);
		}

	}

	// popup matched
	public void ShowDialog_matched() {

		SharedPreferences shared = getActivity().getSharedPreferences("",
				getActivity().MODE_PRIVATE);

		String imgbytes = shared.getString("imgITEM", "");

		final Dialog dialog = Utills.prepararDialog(getActivity(),
				R.layout.there_is_match);

		ImageView uiC_imgVmyItem = (ImageView) dialog
				.findViewById(R.id.uiCimgVitem1);
		SmartImageView uiC_imgVotherItem = (SmartImageView) dialog
				.findViewById(R.id.uiCimgVitem2);
		Button uiC_btnChat = (Button) dialog
				.findViewById(R.id.uiCbtnsendMsgMatch);
		Button uiC_btnCancel = (Button) dialog.findViewById(R.id.uiCbtnCancel);

		uiC_imgVmyItem.setImageBitmap(Utills.StringToBitMap(imgbytes));
		
		
		uiC_imgVotherItem.setImageUrl(Utills.Imagebytee);

		uiC_btnChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Utills.haveNetworkConnection(getActivity())) {
					Intent gotochat = new Intent(getActivity(),
							ChatScreen_.class);
					gotochat.putExtra("itemid", NearbyitemsList.get(itemposition).getItmID());
					gotochat.putExtra("Title", NearbyitemsList.get(itemposition).getItemName());
					gotochat.putExtra("pid", NearbyitemsList.get(itemposition).getProfileID());
					gotochat.putExtra("km", NearbyitemsList.get(itemposition).getDistance());
//					Utills.Imagebytee = imgs[itemposition];
					// gotochat.putExtra("img", imgs[position]);

					if (itemposition == 0) {
						ShowAnim(1);
						itemposition = 1;
					} else {
						
						
						if(NearbyitemsList.size()-1==2)
						{
							ShowAnim(0);
						}
						else
						{
							NearbyitemsList.remove(itemposition);
							 adapter = new ViewPagerAdapter(getActivity(), NearbyitemsList);
							 myPager.setAdapter(adapter);
							 lastPage= mCurrentSelectedScreen=mNextSelectedScreen=itemposition=itempositionTEMP =NearbyitemsList.size()/2;
							myPager.setCurrentItem(itemposition, true);
							ShowAnim(itemposition);
						}
						
						
					}
					dialog.dismiss();
					startActivity(gotochat);

				} else {
					if (toast != null) {
						toast.cancel();
					}
					toast = Toast.makeText(getActivity(),
							"No network connection..!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}

			}
		});
		uiC_btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (itemposition == 0) {
					ShowAnim(1);
					itemposition = 1;
				} else {
					
					
					if(NearbyitemsList.size()-1==2)
					{
						ShowAnim(0);
					}
					else
					{
						NearbyitemsList.remove(itemposition);
						 adapter = new ViewPagerAdapter(getActivity(), NearbyitemsList);
						 myPager.setAdapter(adapter);
						 lastPage= mCurrentSelectedScreen=mNextSelectedScreen=itemposition=itempositionTEMP =NearbyitemsList.size()/2;
						myPager.setCurrentItem(itemposition, true);
						ShowAnim(itemposition);
					}
					
					
				}

				dialog.dismiss();

			}
		});

		dialog.show();

	}

	public void ShowAnim(int position) {

		if ((NearbyitemsList.size()) > position && position!=0 && position!=NearbyitemsList.size()) {

//			uiC_imgVMatchingDP.setImageBitmap(Utills
//					.StringToBitMap(imgs[position]));
//			uiC_imgVMatchingDP.setImageResource(R.drawable.loding_img);
//			uiC_imgVMatchingDP.setImageUrl(imgs[position]);

			Animation animupdown = AnimationUtils.loadAnimation(getActivity(),
					R.anim.top_to_bottomin);
			uiC_txtvTitle.startAnimation(animupdown);
			uiC_txtvKM.startAnimation(animupdown);
			uiC_txtvDesc.startAnimation(animupdown);

			String DescText = NearbyitemsList.get(position).getItemDescription();
			htmlText = "<body><h1>Description:</h1><br><i>" + DescText
					+ "</i></body>";
			uiC_txtvTitle.setText(NearbyitemsList.get(position).getItemName());
			uiC_txtvKM.setText(NearbyitemsList.get(position).getDistance() + "-km away.");
			uiC_txtvDesc.setText(Html.fromHtml(htmlText));

			// uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));

			// itemposition++;

		} else {

			uiC_imgbtnLike.setVisibility(View.GONE);
			uiC_imgbtnDislike.setVisibility(View.GONE);
			uiC_txtvTitle.setVisibility(View.GONE);
			uiC_txtvDesc.setVisibility(View.GONE);
			uiC_txtvKM.setVisibility(View.GONE);
//			uiC_imgVMatchingDP.setVisibility(View.GONE);
			uiC_ReportAbuse.setVisibility(View.GONE);
			uiC_txtvSearch.setVisibility(View.VISIBLE);
			myPager.setVisibility(View.INVISIBLE);
			uiC_layoutDetailsContaoner.setBackgroundColor(Color.TRANSPARENT);
		}

	}

	// DRAG LISTNER
	class MyDragListener implements OnDragListener {
		Drawable enterShape = getResources().getDrawable(R.drawable.like_btn);
		Drawable normalShape = getResources().getDrawable(R.drawable.dislike);

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
//				uiC_imgVMatchingDP.setVisibility(View.INVISIBLE);

				endX = event.getX();

				positionX = event.getX();

				System.out.println("start" + endX);
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				// v.setBackgroundDrawable(enterShape);
				System.out.println("enter");
				break;

			case DragEvent.ACTION_DRAG_EXITED:
				// v.setBackgroundDrawable(normalShape);
				if (toast != null) {
					toast.cancel();
				}
				System.out.println("exited");

//				uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
				
				System.out.println("exited ended" + Math.abs(endX)
						+ Math.abs(positionX) + "");
				break;

			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
//				uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
				if (toast != null) {
					toast.cancel();
				}
				System.out.println("drop");
				try {
					if (Math.abs(endX) - Math.abs(positionX) > 150) {
						// Toast.makeText(con, "Dislike",
						// Toast.LENGTH_SHORT).show();
						System.out.println("leftp");

						likeDislike = "2";
						// uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
						if (Utills.haveNetworkConnection(getActivity())) {
							new asyncLikeDislike().execute();
						}

					} else if (Math.abs(positionX) - Math.abs(endX) > 150) {
						// Toast.makeText(con, "Like",
						// Toast.LENGTH_SHORT).show();
						System.out.println("rightp");

						likeDislike = "1";
						// uiC_imgVMatchingDP.setImageBitmap(Utills.StringToBitMap(imgs[itemposition]));
						if (Utills.haveNetworkConnection(getActivity())) {
							new asyncLikeDislike().execute();
						}

					}
					

				
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DragEvent.ACTION_DRAG_LOCATION:

				

				positionX = event.getX();
//				System.out.println("loc drag" + positionX + "end" + endX);
				if (Math.abs(endX) - Math.abs(positionX) > 150) {
					if (toast != null) {
						toast.cancel();
					}
					System.out.println("leftp");
					showCustomAlert(R.drawable.dislike_toast);
					lk_dlk = false;

				} else if (Math.abs(positionX) - Math.abs(endX) > 150) {
					if (toast != null) {
						toast.cancel();
					}
					
					showCustomAlert(R.drawable.like_toast);
					lk_dlk = true;

				} else {
					if (toast != null) {
						toast.cancel();
					}
				}
				

				break;

			case DragEvent.ACTION_DRAG_ENDED:

//				uiC_imgVMatchingDP.setVisibility(View.VISIBLE);
			
				if (toast != null) {
					toast.cancel();
				}
				

			default:
				break;

			}

			return true;
		}

	}

	// PROGRESS DIALLOG
	ImageView iv;

	private class TransparentProgressDialog extends Dialog {

		// private ImageView iv;

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
			// layout.setBackgroundColor(Color.RED);

			LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(
					500, 500);
			paramsLayout.gravity = Gravity.CENTER;

			// LayoutInflater layoutInflater = (LayoutInflater)
			// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// layout.addView(layoutInflater.inflate(R.layout.activity_main,layout,
			// false) );

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;

			iv = new ImageView(context);

			iv.setImageResource(resourceIdOfImage);
			// layout.setPadding(40,40, 40, 40);

			TextView tVloading = new TextView(getActivity());
			tVloading.setText("Finding items near you..");
			tVloading.setTextSize(16);
			// tVloading.setTextSize(27);

			layout.addView(iv, params);
			layout.addView(tVloading, params);
			addContentView(layout, paramsLayout);
			// setContentView(R.layout.dialogo_choose);
		}

		@Override
		public void show() {
			super.show();
			// RotateAnimation anim = new RotateAnimation(0.0f, 360.0f ,
			// Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
			// .5f);

			ani_0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -0.2f,
					Animation.RELATIVE_TO_SELF, 0.3f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);

			ani_1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.3f,
					Animation.RELATIVE_TO_SELF, -0.2f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			ani_0.setInterpolator(new AccelerateDecelerateInterpolator());
			ani_0.setDuration(700);
			ani_0.setFillEnabled(true);
			ani_0.setFillAfter(true);
			ani_0.setAnimationListener(animationListener);
			ani_1.setInterpolator(new AccelerateDecelerateInterpolator());
			ani_1.setDuration(700);
			ani_1.setFillEnabled(true);
			ani_1.setFillAfter(true);
			ani_1.setAnimationListener(animationListener);

			
			iv.startAnimation(ani_0);
		}
	}

	// PROGRESS DIALLOG
	TranslateAnimation ani_0, ani_1;
	final AnimationListener animationListener = new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			if (animation == ani_0) {
				iv.startAnimation(ani_1);
			}
			if (animation == ani_1) {
				iv.startAnimation(ani_0);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}
	};

	// /
	public class MyAnimation extends Animation {

		private View view;
		private float cx, cy; // center x,y position of circular path
		private float prevX, prevY; // previous x,y position of image during
									// animation
		private float r; // radius of circle

		/**
		 * @param view
		 *            - View that will be animated
		 * @param r
		 *            - radius of circular path
		 */
		public MyAnimation(View view, float r) {
			this.view = view;
			this.r = r;
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
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
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			if (interpolatedTime == 0) {
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

	// /

	public void showCustomAlert(int res) {

		Context context = getActivity();

		
		// Create layout inflator object to inflate toast.xml file
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Call toast.xml file for toast layout
		View toastRoot = inflater.inflate(R.layout.toast_like, null);

		
		ImageView imglike_dislike = (ImageView) toastRoot
				.findViewById(R.id.imgVL_D);
		imglike_dislike.bringToFront();
		imglike_dislike.setImageResource(res);
		toast = new Toast(context);

		// Set layout to toast
		toast.setView(toastRoot);
		
		if(res==R.drawable.like_toast)
		{
			toast.setGravity(Gravity.TOP | Gravity.RIGHT, 90, 50);
		}
		else
		{
			toast.setGravity(Gravity.TOP | Gravity.LEFT, 90, 50);
		}
		
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();

	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	

}
