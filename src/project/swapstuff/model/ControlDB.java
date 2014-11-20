package project.swapstuff.model;

import java.util.ArrayList;

import javax.crypto.NullCipher;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ControlDB {

	public static PresellDB presell;
	static String DBNAMEPRESELL = "PresellDB";

	// INSERT VALUES IN TABLE
	public static void insertUserdetails(Context actividad,
			ArrayList<Profile_info> info_user) {

		Log.d("SQLite", "insert userdetails ");
		presell = new PresellDB(actividad, DBNAMEPRESELL, null, 1);
		SQLiteDatabase db = presell.getWritableDatabase();
		if (db != null) {
			for (int g = 0; g < info_user.size(); g++) {
				Profile_info user_info = info_user.get(g);

				db.execSQL("INSERT INTO userdetails VALUES ('"
						+ user_info.getItem_id() + "', '"
						+ user_info.getUser_id() + "', '"
						+ user_info.getTitle() + "', '" + user_info.getDesc()
						+ "', '" + user_info.getImgDp() + "')");
			}

			db.close();
		}
	}

	// SELECT VALUES FROM TABLE
	public static ArrayList<Profile_info> SelectFromUserDetails(
			Context actividad) {

		ArrayList<Profile_info> userinfo = new ArrayList<Profile_info>();
		try
		{
		
		
		presell = new PresellDB(actividad, DBNAMEPRESELL, null, 1);
		SQLiteDatabase db = presell.getWritableDatabase();
		if (db != null) {
			String query = "SELECT * FROM userdetails";

			Cursor cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {

				do {
					Profile_info info = new Profile_info();
					info.setItem_id((cursor.getString(0)));
					info.setUser_id((cursor.getString(1)));
					info.setTitle(cursor.getString(2));
					info.setDesc(cursor.getString(3));
					info.setImgDp(cursor.getString(4));
					userinfo.add(info);
				} while (cursor.moveToNext());
				cursor.close();
			}
			db.close();
			
			
		}
		Log.i("SQLite", "User info table size: " + userinfo.size());
		return userinfo;
		
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return userinfo;
		
	}

	// DELETE VALUES FROM TABLE
	public static void deleteuserdetails(Context actividad) {
		try {
			Log.d("SQLite", "delete USERINFO");
			presell = new PresellDB(actividad, DBNAMEPRESELL, null, 1);
			SQLiteDatabase db = presell.getWritableDatabase();
			db.delete("userdetails", "1", new String[] {});
			db.close();
		} catch (Exception e) {
			Log.i("error-delete", e.getMessage()+"");
		}
	}
	
	
//	GET COUNT OF USERDETAILS
//	Cursor cur = db.rawQuery("SELECT COUNT(*) FROM CAT_BUD_TAB", null);	
	

	public static boolean getCountuserDetails(Context actividad) {
		
	boolean length=false;
	
	presell = new PresellDB(actividad, DBNAMEPRESELL, null, 1);
	SQLiteDatabase db = presell.getWritableDatabase();
	if (db != null) {
		String query = "SELECT COUNT(*) FROM userdetails";

		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null) {
		    cursor.moveToFirst();                       // Always one row returned.
		    if (cursor.getInt (0) == 0) 
		    {
		    	length=true;							//Table is empty
		    }
		}
				
		
	}
	return length;
	
	
	
	}
}
