package project.swapstuff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.loopj.android.image.SmartImageView;

import project.swapstuff.model.Utills;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import eu.janmuller.android.simplecropimage.CropImage;

public class EditProfileFragment extends Fragment implements OnClickListener {

	public EditProfileFragment(Context conn) {
		this.con = conn;
	}

	public EditProfileFragment() {
		// TODO Auto-generated constructor stub
	}

	Button uiC_btndone;
	// ImageButton uiC_imgbtneditProfile;
	EditText uiC_edTitle, uiC_edDesc;
	// TextView uiC_txtuploadPick;
	static SmartImageView uiC_imgDP;
	static Context con;
	boolean edit = true;
	static String imgbytes = "", itemTitleDB = "", itemDescDB = "";
	SharedPreferences preferences;
	SharedPreferences.Editor editorPreferences = null;
	Uri selectedImage;
	// FOTOS
	String URL_FOTO = "", tempImagePath = "";

	Bitmap bitmap;

	boolean ImageChanged = false;
	int DeleteFirst = 0;

	Toast toast;

	File capturedFile;

	@Override
	public void onSaveInstanceState(Bundle outState) {

		// outState.putParcelable("outputFileUri", selectedImage);
		outState.putString("byte", imgbytes);

		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_editprofile,
				container, false);

		if (savedInstanceState != null) {
			imgbytes = savedInstanceState.getString("byte");

			ImageChanged = true;
			

			System.out.println(Utills.itemid + "itemid");
			SharedPreferences shared = getActivity().getSharedPreferences("",
					getActivity().MODE_PRIVATE);
			Editor ed = shared.edit();

			ed.putString("imgITEM", imgbytes);
			ed.commit();

			Utills.id = shared.getString("id", "4");
			Utills.km = shared.getInt("km", 1);
			Utills.itemid = shared.getString("itemid", "2");
			Utills.NotificationEnabled = shared.getString("noti", "1");
			Utills.MatchNotificationEnabled = shared
					.getString("matchnoti", "1");

			Utills.FbID = shared.getString("fbid", "test");

			uiC_btndone = (Button) rootView.findViewById(R.id.uiC_btndoneEdit);
			// uiC_imgbtneditProfile = (ImageButton) rootView
			// .findViewById(R.id.uiC_imgbtneditprofile);
			uiC_edTitle = (EditText) rootView
					.findViewById(R.id.uiC_edEditTitle);
			uiC_edDesc = (EditText) rootView.findViewById(R.id.uiC_edEditDesc);
			// uiC_txtuploadPick = (TextView) rootView
			// .findViewById(R.id.uiC_txtvuploadd);
			uiC_imgDP = (SmartImageView) rootView.findViewById(R.id.uiC_imgVDP);
			setValuesDB();

			
		}

		uiC_imgDP = (SmartImageView) rootView.findViewById(R.id.uiC_imgVDP);
		uiC_btndone = (Button) rootView.findViewById(R.id.uiC_btndoneEdit);
		// uiC_imgbtneditProfile = (ImageButton) rootView
		// .findViewById(R.id.uiC_imgbtneditprofile);
		uiC_edTitle = (EditText) rootView.findViewById(R.id.uiC_edEditTitle);
		uiC_edDesc = (EditText) rootView.findViewById(R.id.uiC_edEditDesc);
		// uiC_txtuploadPick = (TextView) rootView
		// .findViewById(R.id.uiC_txtvuploadd);

		// uiC_edTitle.setEnabled(true);
		// uiC_edDesc.setEnabled(true);
		// edit = true;

		setValuesDB();

		preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		editorPreferences = preferences.edit();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	

		// uploadPick.getBackground().setAlpha(45);
		uiC_imgDP.setOnClickListener(this);
		uiC_btndone.setOnClickListener(this);
		// uiC_imgbtneditProfile.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == uiC_imgDP) {

			new AlertDialog.Builder(getActivity())
					.setTitle("Warning !")
					.setMessage(
							"Changing image will delete your matches , Are you sure you want to change image?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									DialogoChoose();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();

		}
	
		else if (v == uiC_btndone) {

			String title = uiC_edTitle.getText().toString();
			String desc = uiC_edDesc.getText().toString();

			if (ImageChanged || !itemTitleDB.equals(title)
					|| !itemDescDB.equals(desc)) {

				if (Utills.haveNetworkConnection(getActivity())) {

					if (imgbytes.equals("") || title.trim().equals("")
							|| desc.trim().equals("")) {
						if (imgbytes.equals("")) {
							Utills.showToast(con, "Please select an image");
						} else if (title.trim().equals("")) {
							uiC_edTitle.setHint("*Enter a title");
							uiC_edTitle.setHintTextColor(Color.RED);
						} else if (desc.trim().equals("")) {
							uiC_edDesc.setHint("*Enter Description");
							uiC_edDesc.setHintTextColor(Color.RED);
						}

					}

					else {

						// getActivity().getWindow().setSoftInputMode(
						// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
						new AlertDialog.Builder(getActivity())
								.setTitle("Warning !")
								.setMessage(
										"Are you sure you want to save changes..?")
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {

												new asyncIteminfoUpdate()
														.execute();
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										})
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();

					}

				} else {
					if (toast != null) {
						toast.cancel();
					}

					toast = Toast.makeText(getActivity(),
							"No network connection..!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}

			} else {

				if (toast != null) {
					toast.cancel();
				}

				toast = Toast.makeText(getActivity(),
						"Sorry ,there are no changes to save..!",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			}

		}

	}

	public void DialogoChoose() {
		final Dialog dialogLoader = new Dialog(con, R.style.Theme_Dialog);
		dialogLoader.setTitle("Upload  your item image");
		dialogLoader.setContentView(R.layout.dialogo_choose);
		dialogLoader.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		dialogLoader.setCanceledOnTouchOutside(true);

		ImageView uiC_imgbtngallery = (ImageView) dialogLoader
				.findViewById(R.id.uiC_imgbtntake_gallery);
		ImageView uiC_imgbtncamera = (ImageView) dialogLoader
				.findViewById(R.id.uiC_imgbtntake_camera);

		uiC_imgbtngallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				dialogLoader.dismiss();

				Intent pickPhoto = new Intent(Intent.ACTION_PICK);
				pickPhoto.setType("image/*");
				startActivityForResult(pickPhoto, 22);

			}
		});

		uiC_imgbtncamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialogLoader.dismiss();

				DeleteFirst = 1;

				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

				cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,getTempUri());

				cameraIntent.putExtra("return-data", true);

				startActivityForResult(cameraIntent, 1888);
			}
		});

		dialogLoader.show();
	}

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
		
		
		

//		  try {
//            ExifInterface ei = new ExifInterface(filePath);
//            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            Matrix matrix = new Matrix();
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    matrix.postRotate(90);
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    matrix.postRotate(180);
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    matrix.postRotate(270);
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                    break;
//                default:
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                    break;
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		
		
		uiC_imgDP.setImageBitmap(bitmap);
		//
		imgbytes = Utills.BitMapToString(bitmap);
		System.out.println("bitmat image--->" + bitmap.toString());
		uiC_imgDP.setImageBitmap(bitmap);
		
		File tempFile = getTempFile();		
		if (tempFile.exists()) {
		tempFile.delete();
	}

	}

	
	
	

	
	
	
	
	
	
	
	
	
	
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(uri, projection, null, null,
				null);
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

	static final String TEMP_PHOTO_FILE = "temporary_img.jpg";

	private Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}

	private File getTempFile() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			File file = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE);
			try {
				file.createNewFile();
			} catch (IOException e) {
			}

			return file;
		} else {

			return null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 22:
			

				ImageChanged = true;
				DeleteFirst = 1;
				try {
					String filePathG = Environment.getExternalStorageDirectory() + "/"
							+ TEMP_PHOTO_FILE;
					
					  InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
	                    FileOutputStream fileOutputStream = new FileOutputStream(filePathG);
	                    Utills.copyStream(inputStream, fileOutputStream);
	                    fileOutputStream.close();
	                    inputStream.close();

	                    performCrop(filePathG);
						
					
				
				} catch (Exception e) {
					e.printStackTrace();}

//				if (tempFile.exists()) {
//					tempFile.delete();
//				}
				

		

			break;
			
		case 77:
			
			try {
		
			if (resultCode == Activity.RESULT_OK) {
				 String path = data.getStringExtra(CropImage.IMAGE_PATH);

				
					if (path != null) {
						
							decodeFile(path);
					}
			}
			else
			{
				String filePathC = Environment.getExternalStorageDirectory() + "/"
						+ TEMP_PHOTO_FILE;
				decodeFile(filePathC);
			}
			
			
		
			} catch (Exception e) {
				e.printStackTrace();
			}
						
			break;

		case 1888:

			try {
				ImageChanged = true;
				DeleteFirst = 1;

				
				
				String filePath = null;
				filePath = Environment.getExternalStorageDirectory() + "/"
						+ TEMP_PHOTO_FILE;

				if (filePath != null) {

					performCrop(filePath);
//						decodeFile(filePath);
				}
				
//				if (tempFile.exists()) {
//					tempFile.delete();
//				}
				
			} catch (RuntimeException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			

		}
		DeleteFirst = 1;
		super.onActivityResult(requestCode, resultCode, data);
	}

	public String BitMapToString(Bitmap bitmap) {

		String temp = "";
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

			byte[] b = baos.toByteArray();

			baos.close();

			baos = null;
			temp = Base64.encodeToString(b, Base64.DEFAULT);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return temp;
	}

	public void setValuesDB() {

		try {
			
		
		
		SharedPreferences shared = getActivity().getSharedPreferences("",
				getActivity().MODE_PRIVATE);

		imgbytes = shared.getString("imgITEM", "");
		itemTitleDB = shared.getString("imgTITLE", "");
		itemDescDB = shared.getString("imgDESC", "");

		uiC_edTitle.setText(itemTitleDB + "");
		uiC_edDesc.setText(itemDescDB + "");
		
		
		if(!imgbytes.contains("."))
		{
			uiC_imgDP.setImageBitmap(Utills.StringToBitMap(imgbytes));
	
		}
		else
		{
			uiC_imgDP.setImageUrl(imgbytes);
		}
		
		} catch (Exception e) {
			SharedPreferences shared = getActivity().getSharedPreferences("",
					getActivity().MODE_PRIVATE);

			uiC_imgDP.setImageUrl(shared.getString("imgITEM", ""));
		}
		

	}

	// class to Edit item info

	class asyncIteminfoUpdate extends AsyncTask<Void, Void, Void>

	{
		String result = "";

		ProgressDialog pd;

		@Override
		protected void onPreExecute() {

			pd = ProgressDialog.show(getActivity(), "", "Please Wait..");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			String HostUrl = Utills.URL+"Items/SaveItem";

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HostUrl);

			try {
				List<NameValuePair> params1 = new LinkedList<NameValuePair>();

				params1.add(new BasicNameValuePair("ItemID", Utills.itemid
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ProfileID", Utills.id
						.replace("\"", "")));
				params1.add(new BasicNameValuePair("ItemTitle", uiC_edTitle
						.getText().toString()));
				params1.add(new BasicNameValuePair("ItemDescription",
						uiC_edDesc.getText().toString()));
				params1.add(new BasicNameValuePair("ItemImage", imgbytes));
				params1.add(new BasicNameValuePair("ItemDateTimeCreated", ""));
				params1.add(new BasicNameValuePair("IsActive", "true"));
				params1.add(new BasicNameValuePair("DeleteFirst", DeleteFirst
						+ ""));

				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");

				Log.e("Response", params1.toString() + "");

				HttpEntity entityEdit = new UrlEncodedFormEntity(params1,
						"UTF-8");
				httpPost.setEntity(entityEdit);

				ResponseHandler<String> handlerEdit = new BasicResponseHandler();
				result = httpClient.execute(httpPost, handlerEdit);

				// itemID=result;
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
		protected void onPostExecute(Void result1) {

			pd.dismiss();

			if (result.contains("-")) {
				Utills.showToast(con, "Error");
			} else {

				if(result.equals(""))
				{
					
				}
				else
				{
					Utills.itemid = result;
				}
				
				System.out.println(Utills.itemid + "itemid");
				SharedPreferences shared = getActivity().getSharedPreferences(
						"", getActivity().MODE_PRIVATE);
				Editor ed = shared.edit();
				ed.putString("itemid", Utills.itemid);
				ed.putString("imgITEM", imgbytes);
				ed.putString("imgTITLE", uiC_edTitle.getText().toString() + "");
				ed.putString("imgDESC", uiC_edDesc.getText().toString() + "");
				ed.commit();

			

				setValuesDB();

				ImageChanged = false;
				

				Toast.makeText(getActivity(), "Changes Saved !",
						Toast.LENGTH_LONG).show();

				
			}

			super.onPostExecute(result1);
		}

	}
	
	
	
	
	
	
	
	private void performCrop(String filePath){
		
		
		 Intent intent = new Intent(getActivity(), CropImage.class);

		    // tell CropImage activity to look for image to crop 
		  
		    intent.putExtra(CropImage.IMAGE_PATH, filePath);

		    intent.putExtra(CropImage.SCALE, false);

		    // if the aspect ratio is fixed to ratio 3/2
		    intent.putExtra(CropImage.ASPECT_X, 1);
		    intent.putExtra(CropImage.ASPECT_Y, 1);

		    startActivityForResult(intent, 77);
		

	}
	
	
	

}
