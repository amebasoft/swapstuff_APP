package project.swapstuff.model;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class PresellDB extends SQLiteOpenHelper { 

	 
	public static ArrayList<String> Create = new ArrayList<String>();

	
	public PresellDB(Context con, String name, CursorFactory factory,
			int version) {
		super(con, name, factory, 1);
	}
   

	@Override 
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < Create.size(); i++) {
			Log.i("EXEC", Create.get(i));
			db.execSQL(Create.get(i));
		}
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {

		
	}

}