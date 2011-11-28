package de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.BullshitWord;

public class BullshitWordDbAdapter {
	private static final String TAG = "BullshitWordDbAdapter";

	private static final String DB_FILENAME = "db.db";
	private static final int DB_VERSION = 1;
	private static final String TABLE = "bullshit_word";

	public static final int COL_ID = 0;
	public static final int COL_SHEET = 1;
	public static final int COL_WORD = 2;
	public static final int COL_CREATED = 3;
	public static final int COL_HEARD = 4;
	public static final String KEY_ID = "_id";
	public static final String KEY_SHEET = "sheet";
	public static final String KEY_WORD = "word";
	public static final String KEY_CREATED = "created";
	public static final String KEY_HEARD = "heard";

	private SQLiteDatabase db;
	private BullshitWordDBHelper dbHelper;

	// SQL Anweisungen um DB zu erstellen.
	private static final String SQL_CREATE = "CREATE TABLE "
			+ TABLE + "(" 
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", "
			+ KEY_SHEET + " INTEGER " + ", "
			+ KEY_WORD + " STRING NOT NULL" + ", "
			+ KEY_CREATED + " STRING NOT NULL" + ", " 
			+ KEY_HEARD + " STRING NULL" + ", "
			+ ")";

	public BullshitWordDbAdapter(Context context) {
		dbHelper = new BullshitWordDBHelper(context, DB_FILENAME, null, DB_VERSION);
	}

	public BullshitWordDbAdapter open() throws SQLException {
		if (db == null) {
			Log.v(TAG, "Öffne Datenbank...");
			try {
				db = dbHelper.getWritableDatabase();
				Log.v(TAG, "Datenbank zum Schreiben geöffnet.");
			} catch (SQLException e) {
				db = dbHelper.getReadableDatabase();
				Log.v(TAG, "Datenbank zum Lesen geöffnet.");
			}
		} else {
			Log.v(TAG, "Datenbank bereits geöffnet.");
		}
		return this;
	}

	public BullshitWordDbAdapter close() {
		if (db != null) {
			Log.v(TAG, "Schließe Datenbank.");
			db.close();
		} else {
			Log.v(TAG, "Datenbank bereits geschlossen.");
		}
		db = null;
		return this;
	}

	protected SQLiteDatabase getDb() {
		open();
		return db;
	}

	public Cursor getBullshitsCursor() {
		String[] cols = new String[] { KEY_ID, KEY_CREATED };
		return getDb().query(TABLE, cols, null, null, null, null,
				null);
	}

	private static class BullshitWordDBHelper extends SQLiteOpenHelper {
		
		public BullshitWordDBHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE);
			onCreate(db);
		}

	}

	public BullshitWord persist(BullshitWord entry) {
		ContentValues entryValues = new ContentValues();
		entryValues.put(KEY_SHEET, entry.getSheet().getId());
		entryValues.put(KEY_WORD, entry.getWord());
		if (entry.getId() > 0) {
			entryValues.put(KEY_ID, entry.getId());
			db.update(TABLE, entryValues, KEY_ID + "=" + entry.getId(), null);
		} else {
			int id = (int) db.insert(TABLE, null, entryValues);
			entry.setId(id);
		}
		return entry;
	}
}
