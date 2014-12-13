package project.swapstuff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
import project.swapstuff.model.Profile_info;
import project.swapstuff.model.Utills;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.TransactionTooLargeException;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileCreate extends Activity implements OnClickListener {

	Button uiC_btndone, uiC_btneditProfile;
	EditText uiC_edTitle, uiC_edDesc;
	TextView uiC_uploadPick;
	static ImageView uiC_imgV_DP;
	static String imgbytes = "";
	boolean imgORnot=false;
	String itemID = "";
	SharedPreferences shared;

	String title = "";
	String desc = "";

	
	List<Intent> targetedShareIntents;
	
	
	Bitmap bitmap;
	
	Toast toast;
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
	    .setTitle("Exit")
	    .setMessage("Are you sure you want Exit?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        
	           finish();
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
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		outState.putString("img", imgbytes);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		
		shared = getSharedPreferences("", MODE_PRIVATE);
		Utills.id = shared.getString("id", "4");
		Utills.km = shared.getInt("km", 1);

		Utills.NotificationEnabled = shared.getString("noti",
				"1");
		Utills.MatchNotificationEnabled = shared.getString(
				"matchnoti", "1");

		Utills.FbID = shared.getString("fbid", "test");
		
	
		
		
		imgORnot=true;
		
		imgbytes = savedInstanceState.getString("img");

		uiC_imgV_DP.setImageBitmap(Utills.StringToBitMap(imgbytes));
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_create);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#0B90A8")));
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		getActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>Create Profile</font>")));
		
		

		uiC_imgV_DP = (ImageView) findViewById(R.id.uiC_imgVDPCreateProfile);
		uiC_btndone = (Button) findViewById(R.id.uiC_btnCreateProfile);
		uiC_edTitle = (EditText) findViewById(R.id.uiC_edTitleCreateProfile);
		uiC_edDesc = (EditText) findViewById(R.id.uiC_edDescCreateProfile);
		uiC_uploadPick = (TextView) findViewById(R.id.uiC_txtvuploaddCreateProfile);

		uiC_imgV_DP.setOnClickListener(this);
		uiC_btndone.setOnClickListener(this);

		StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(pol);
		
		
		
		CommonUtilities.FragmentToOpen=0;

	}

	public void DialogoChoose() {
		final Dialog dialogLoader = new Dialog(this, R.style.Theme_Dialog);
		dialogLoader.setTitle("Select a Image ");
		dialogLoader.setContentView(R.layout.dialogo_choose);
		dialogLoader.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		ImageView uiC_imgbtntake_gallery = (ImageView) dialogLoader
				.findViewById(R.id.uiC_imgbtntake_gallery);
		ImageView uiC_imgbtntake_camera = (ImageView) dialogLoader
				.findViewById(R.id.uiC_imgbtntake_camera);

		uiC_imgbtntake_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogLoader.dismiss();

				Intent pickPhoto = new Intent(Intent.ACTION_PICK );
//				pickPhoto.setPackage("com.android.gallery3d");
				pickPhoto.setType("image/*");
				pickPhoto.putExtra("crop", "true");
				pickPhoto.putExtra("return-data", false);
				pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
				pickPhoto.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				startActivityForResult(pickPhoto, 22);

				
				
			
			}
		});

		uiC_imgbtntake_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogLoader.dismiss();
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,getTempUri());

				cameraIntent.putExtra("return-data", true);
				// cameraIntent.putExtra(MediaStore.extra_, 1);
				startActivityForResult(cameraIntent, 1888);
				
				
				

			}
		});

		dialogLoader.show();
	}

	
	 static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";  

	private Uri getTempUri() {
	    return Uri.fromFile(getTempFile());
	}
	private File getTempFile() {

	    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

	        File file = new File(Environment.getExternalStorageDirectory(),TEMP_PHOTO_FILE);
	        try {
	            file.createNewFile();
	        } catch (IOException e) {}

	        return file;
	    } else {

	        return null;
	    }
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == uiC_imgV_DP) {
			DialogoChoose();
		} 
		else if (v == uiC_btndone) {
			
			
			
			
			InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(uiC_edDesc.getWindowToken(), 0);
			
			

			title = uiC_edTitle.getText().toString();
			desc = uiC_edDesc.getText().toString();
			if (!imgORnot || title.trim().equals("")
					|| desc.trim().equals("")) {
				if (!imgORnot) {
					if(toast!=null)
					{
						toast.cancel();
					}
					
					toast=Toast.makeText(ProfileCreate.this, "Please select a image", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER,0, 0);
					toast.show();
				} else if (title.trim().equals("")) {
					uiC_edTitle.setHint("*Enter a title");
					uiC_edTitle.setHintTextColor(Color.RED);
				} else if (desc.trim().equals("")) {
					uiC_edDesc.setHint("*Enter Description");
					uiC_edDesc.setHintTextColor(Color.RED);
				}

			}

			else {

				

				if(Utills.haveNetworkConnection(ProfileCreate.this))
				{
					new asyncCreateItem().execute();
				}
				else
				{
					if(toast!=null)
					{
						toast.cancel();
					}
					
					toast=Toast.makeText(ProfileCreate.this, "No network connection..!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER,0, 0);
					toast.show();
				}
				
			}

		
		}

	}
	public String getPath(Uri uri) {
		  String[] projection = { MediaStore.Images.Media.DATA };
		  Cursor cursor = managedQuery(uri, projection, null, null, null);
		  if (cursor != null) {
		   // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
		   // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
		   int column_index = cursor
		     .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		   cursor.moveToFirst();
		   return cursor.getString(column_index);
		  } else
		   return null;
		 }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 22:
			if (resultCode == Activity.RESULT_OK) {
				data=null;
				String finalPicturePath;
				String filePath = null;
//				Uri selectedImage = data.getData();
				
				  File tempFile = getTempFile();

                  filePath= Environment.getExternalStorageDirectory()
                      +"/"+TEMP_PHOTO_FILE;
				
                  if (filePath != null) {
                	  
                	  try {
                		  decodeFile(filePath);
					} catch (Exception e) {
						e.printStackTrace();
						Utills.showToast(ProfileCreate.this, "Please select some other Source..!");
						Intent pickPhoto = new Intent(Intent.ACTION_PICK );
						pickPhoto.setType("image/*");
						pickPhoto.putExtra("crop", "true");
						pickPhoto.putExtra("return-data", false);
						pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
						pickPhoto.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
						startActivityForResult(pickPhoto, 22);
//						startActivityForResult(Intent.createChooser(targetedShareIntents.remove(0), "Select a picture"), 22);
					}
			           
			           } 
                  
                  
                  if (tempFile.exists()) tempFile.delete();
                  


			}

			break;

		case 1888:

			try {

				imgORnot=true;
				

				String finalPicturePath;
				String filePath = null;
				File tempFile = getTempFile();

				filePath = Environment.getExternalStorageDirectory() + "/"
						+ TEMP_PHOTO_FILE;

				if (filePath != null) {

					
						decodeFile(filePath);
				}
				
				if (tempFile.exists()) {
					tempFile.delete();
				}
				
			} catch (RuntimeException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
//	-------------------------------------------
	
	
	public void decodeFile(String filePath) {
		
		  // Decode image size
		  BitmapFactory.Options o = new BitmapFactory.Options();
		  o.inJustDecodeBounds = true;
		  BitmapFactory.decodeFile(filePath, o);

		  // The new size we want to scale to
		  final int REQUIRED_SIZE = 1024;

		  // Find the correct scale value. It should be the power of 2.
		  int width_tmp = o.outWidth, height_tmp = o.outHeight;
		  int scale = 1;
		  while (true) {
		   if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
		    break;
		   width_tmp /= 2;
		   height_tmp /= 2;
		   scale *= 2;
		  }

		  // Decode with inSampleSize
		  BitmapFactory.Options o2 = new BitmapFactory.Options();
		  o2.inSampleSize = scale;
		  bitmap = BitmapFactory.decodeFile(filePath, o2);
		  
		  
		  
		  try {
	            ExifInterface ei = new ExifInterface(filePath);
	            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
	            Matrix matrix = new Matrix();
	            switch (orientation) {
	                case ExifInterface.ORIENTATION_ROTATE_90:
	                    matrix.postRotate(90);
	                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	                    break;
	                case ExifInterface.ORIENTATION_ROTATE_180:
	                    matrix.postRotate(180);
	                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	                    break;
	                case ExifInterface.ORIENTATION_ROTATE_270:
	                    matrix.postRotate(270);
	                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	                    break;
	                default:
	                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	                    break;
	            }
	        } catch (Throwable e) {
	            e.printStackTrace();
	        }
		  
		  
		  
		  
		  
		  
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			uiC_imgV_DP.setImageBitmap(bitmap);
//
			imgbytes = Utills.BitMapToString(bitmap);

//			 byte [] b=baos.toByteArray();
//			 imgbytes=Base64.encodeToString(b, Base64.DEFAULT);
		
			imgORnot=true;
		  System.out.println("bitmat image--->" + bitmap.toString());
		  uiC_imgV_DP.setImageBitmap(bitmap);

		 }
	
	// ----------------------------------------
	class asyncCreateItem extends AsyncTask<Void, Void, Void> {

		ProgressDialog pd;

		@Override
		protected void onPreExecute() {

			pd = ProgressDialog.show(ProfileCreate.this, "", "Please Wait..");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String result = "";

			String HostUrl = Utills.URL+"Items/SaveItem";
//			http://116.193.163.158:8083
//			String HostUrl = "http://116.193.163.156:8012/Items/SaveItem";
			

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();

				// public int ItemID { get; set; }
				// public int ProfileID { get; set; }
				// public string ItemTitle { get; set; }
				// public string ItemDescription { get; set; }
				// public byte[] ItemImage { get; set; }
				// public Nullable<System.DateTime> ItemDateTimeCreated { get;
				// set; }
				// public Nullable<bool> IsActive { get; set; }

				params1.add(new BasicNameValuePair("ItemID", "-1"));
				params1.add(new BasicNameValuePair("ProfileID", Utills.id.replace("\"", "")+""));
				params1.add(new BasicNameValuePair("ItemTitle", title));
				params1.add(new BasicNameValuePair("ItemDescription", desc));
				params1.add(new BasicNameValuePair("ItemImage", imgbytes+""));
				params1.add(new BasicNameValuePair("ItemDateTimeCreated", ""));
				params1.add(new BasicNameValuePair("IsActive", "true"));

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

//				Log.e("Response", params1.toString() + "");

				HttpEntity entity2 = new UrlEncodedFormEntity(params1, "UTF-8");
				httpPost.setEntity(entity2);

				ResponseHandler<String> handler2 = new BasicResponseHandler();
				result = httpClient.execute(httpPost, handler2);

				itemID = result;
				Log.e("Response", itemID + "");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("profile id"+ Utills.id.replace("\"", "")+"");
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			pd.dismiss();

			if (itemID.contains("-")) {
				Utills.showToast(ProfileCreate.this, "Error");
			} else {

				try {
					

				
				Utills.itemid = itemID;
				shared = getSharedPreferences("", MODE_PRIVATE);
				Editor ed = shared.edit();
				ed.putString("itemid", itemID);
				ed.putString("imgITEM", imgbytes);
				ed.putString("imgTITLE", title);
				ed.putString("imgDESC", desc);
				ed.commit();
				Intent gotoHome = new Intent(getApplicationContext(),
						MainActivitySwapStuff.class);
				startActivity(gotoHome);
				finish();
				
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			super.onPostExecute(result);
		}

	}

}
