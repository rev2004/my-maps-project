package tam.Android.Database;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.icons.draw.view.MapLocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MapDatabase {
	public static final String KEY_ROWID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LNG = "lng";
	private static final String DATABASE_NAME = "mymaps";
	private static final String DATABASE_TABLE = "place";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table place(id integer primary key autoincrement,"
			+ "name text not null, lat text not null, lng text not null);";

	private MapOpenHelper helper;
	private SQLiteDatabase db;

	public MapDatabase(Context context) {
		helper = new MapOpenHelper(context);
	}

	public MapDatabase open() {
		db = helper.getWritableDatabase();
		return this;
	}

	public void close() {
		helper.close();
	}

	public long insertPlace(String name, String lat, String lng) {
		open();
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_LAT, lat);
		initialValues.put(KEY_LNG, lng);
		long val = db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return val;
	}

	public boolean deleteAll() {
		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	public ArrayList<String> getAllLocations() throws SQLException {
		open();
		Cursor cur = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_NAME, KEY_LAT, KEY_LNG }, null, null, null, null, null,
				null);
		cur.moveToFirst();
		ArrayList<String> list = new ArrayList<String>();
		while (cur.isAfterLast() == false) {
			list.add(cur.getString(1));
			cur.moveToNext();
		}
		cur.close();
		close();
		return list;
	}
	
	public ArrayList<MapLocation> getAllGeoPoints() throws SQLException {
		open();
		Cursor cur = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_NAME, KEY_LAT, KEY_LNG }, null, null, null, null, null,
				null);
		cur.moveToFirst();
		ArrayList<MapLocation> list = new ArrayList<MapLocation>();
		while (cur.isAfterLast() == false) {
			Double lat = cur.getDouble(2);
			Double lng = cur.getDouble(3);

			list.add(new MapLocation(new GeoPoint(lat.intValue(), lng
					.intValue()), cur.getString(1)));
			cur.moveToNext();
		}
		cur.close();
		close();
		return list;
	}

	public GeoPoint getLocation(String place) {
		open();
		GeoPoint gp = null;
		String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + " like '%" + place + "%'";
		Cursor cursor = db.rawQuery(query , null);
		// 2 cai y chang
		/*
		Cursor cursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_NAME, KEY_LAT, KEY_LNG }, 
                KEY_NAME + " = ?", new String[] { place }, null, null, null, null);
        */
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false)
		{
			String lat = cursor.getString(cursor.getColumnIndex(KEY_LAT));
			String lng = cursor.getString(cursor.getColumnIndex(KEY_LNG));
			gp = new GeoPoint(Integer.parseInt(lat), Integer.parseInt(lng));
			cursor.moveToNext();
		}
		cursor.close();
		close();
		return gp;
	}

	private static class MapOpenHelper extends SQLiteOpenHelper {
		@SuppressWarnings("unused")
		private final Context context;
		private SQLiteDatabase database;

		public MapOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			database = db;
			database.execSQL(DATABASE_CREATE);
			Log.v("a", "table created");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
}
