package project.swapstuff.model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import project.swapstuff.R;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.Toast;

public class Utills {
	
	
	

	public static String URL="http://www.swapstff.com/";
//	public static String URL="http://116.193.163.158:8083/";
	public static String FbID="test";
	public static String id,itemid,lati,longi;
	public static int km;
	
	public static String Imagebytee="";
	
	public static Double latitud;
	public static Double longitud;
	
	public static boolean chatActive=false;
	
	public static String NotificationEnabled="1";
	
	public static String MatchNotificationEnabled="1";
	
	public static boolean refreshMatch=true;
	
	
	public static void CreateDB(Activity actividad) {
		String[] querycreator = leerFileCreate(R.raw.tables, actividad);
		PresellDB.Create.clear();
		for (int i = 0; i < querycreator.length; i++) {
			PresellDB.Create.add(querycreator[i]);
			Log.i("database created", querycreator[i]+"");
		}

	}
	
	public static String[] leerFileCreate(int file, Activity actividad) {
		String[] inserts = null;
		try {
			String str = "";
			StringBuffer buf = new StringBuffer();
			InputStream is = actividad.getResources().openRawResource(file);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			if (is != null) {
				while ((str = reader.readLine()) != null) {
					buf.append(str);

				}
			}
			is.close();
			String filetext = buf.toString();
			inserts = filetext.split(";");
		} catch (Exception e) {
			return null;
		}
		return inserts;

	}
	
	
	
	
	
	
	public static void showToast(Context con,String msg) {
		
		Toast tmsg=Toast.makeText(con, msg, Toast.LENGTH_LONG);
		tmsg.setGravity(Gravity.CENTER,0, 0);
		tmsg.show();
		
		
		
	}
	
	
	
	
	
	
	public static  boolean haveNetworkConnection(Context con ) {

		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedMobile || haveConnectedWifi;
	}

	
	
	
	
	
	  public static  String BitMapToString(Bitmap bitmap){
		  
		  String temp = "";
		  try {
		  ByteArrayOutputStream baos=new  ByteArrayOutputStream();
          bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
          
          byte [] b=baos.toByteArray();
        	
				baos.close();
			
        	baos=null;
         temp=Base64.encodeToString(b, Base64.DEFAULT);
         
		  } catch (IOException e) {
				
				e.printStackTrace();
			}
		  catch (Exception e) {
			// TODO: handle exception
		}
		
		  return temp;
    }

	
	
	   public static Bitmap StringToBitMap(String encodedString){
		     try{
		       byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
		       Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		       return bitmap;
		     }catch(Exception e){
		       e.getMessage();
		       return null;
		     }
		      }
	   
	   
	   
	   
	   
	   
	   public static String parseDateToddMMyyyy(String time) {
		    String inputPattern = "yyyy-MM-dd";
		    String outputPattern = "dd-MMMM";
		    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		    Date date = null;
		    String str = null;

		    try {
		        date = inputFormat.parse(time);
		        str = outputFormat.format(date);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    } catch (java.text.ParseException e) {

				e.printStackTrace();
			}
		    return str;
		}
	   
	   
	   
	   
//	   =================
			   public static Dialog prepararDialog(Context contexto, int layout) {
			     final Dialog dialog = new Dialog(contexto, R.style.FullHeightDialog);
			     dialog.setContentView(layout);
			     dialog.setCancelable(false);

			     Window window = dialog.getWindow();
			     window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			     window.setGravity(Gravity.CENTER);
			     return dialog;
			    }
	   
	   

}
