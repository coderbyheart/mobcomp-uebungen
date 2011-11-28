package de.hsrm.medieninf.mobcomp.ueb03.aufg01.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Highscore;

public class HighscoreDbAdapter {
	private static final String TAG = "HighscoreDbAdapter";

	private static final String DB_FILENAME = "db.db";
	private static final int DB_VERSION = 2;
	private static final String TABLE = "highscore";

	public static final int COL_ID = 0;
	public static final int COL_TRIES = 1;
	public static final int COL_TIME = 2;
	public static final int COL_NAME = 3;
	public static final String KEY_ID = "_id";
	public static final String KEY_TRIES = "tries";
	public static final String KEY_TIME = "" + "time";
	public static final String KEY_NAME = "name";

	private SQLiteDatabase db;
	private HighscoreDBHelper dbHelper;

	// SQL Anweisungen um DB zu erstellen.
	private static final String SQL_CREATE = "CREATE TABLE "
			+ TABLE + "(" 
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " 
			+ KEY_TRIES + " INT NOT NULL" + ", " 
			+ KEY_TIME + " INTEGER NOT NULL" + ", " 
			+ KEY_NAME + " TEXT NOT NULL" 
			+ ")";

	public HighscoreDbAdapter(Context context) {
		dbHelper = new HighscoreDBHelper(context, DB_FILENAME, null, DB_VERSION);
	}

	public HighscoreDbAdapter open() throws SQLException {
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

	public HighscoreDbAdapter close() {
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

	public Cursor getHighscoresCursor() {
		String[] cols = new String[] { KEY_ID, KEY_TRIES, KEY_TIME,
				KEY_NAME };
		return getDb().query(TABLE, cols, null, null, null, null,
				null);
	}

	private static class HighscoreDBHelper extends SQLiteOpenHelper {
		
		public HighscoreDBHelper(Context context, String name,
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

	public Highscore getHighscore(int highscoreId) {
		String[] cols = new String[] { KEY_ID, KEY_TRIES, KEY_TIME, KEY_NAME };
		Cursor result = getDb().query(TABLE, cols,
				KEY_ID + "=" + highscoreId, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			Log.v(TAG, "Highscore #" + highscoreId + " nicht gefunden.");
			return null;
		}
		Highscore hs = new Highscore();
		hs.setId(highscoreId);
		hs.setName(result.getString(COL_NAME));
		hs.setTries(result.getInt(COL_TRIES));
		hs.setTime(result.getInt(COL_TIME));
		return hs;
	}

	public List<Highscore> getHighscores() {
		ArrayList<Highscore> highscores = new ArrayList<Highscore>();
		String[] cols = new String[] { KEY_ID, KEY_TRIES, KEY_TIME,
				KEY_NAME };
		Cursor result = getDb().query(TABLE, cols, null, null, null,
				null, KEY_TRIES + " ASC, " + KEY_TIME + " ASC");
		result.moveToFirst();
		if (result.isAfterLast()) {
			Log.v(TAG, "Keine Highscore gefunden.");
			return highscores;
		}
		do {
			Highscore hs = new Highscore();
			hs.setId(result.getInt(COL_ID));
			hs.setName(result.getString(COL_NAME));
			hs.setTries(result.getInt(COL_TRIES));
			hs.setTime(result.getInt(COL_TIME));
			highscores.add(hs);
		} while (result.moveToNext() && highscores.size() < 10);
		return highscores;
	}

	public Highscore persist(Highscore hs) {
		ContentValues hsValues = new ContentValues();
		hsValues.put(KEY_NAME, hs.getName());
		hsValues.put(KEY_TRIES, hs.getTries());
		hsValues.put(KEY_TIME, hs.getTime());
		if (hs.getId() > 0) {
			hsValues.put(KEY_ID, hs.getId());
			db.update(TABLE, hsValues, KEY_ID + "=" + hs.getId(), null);
		} else {
			int id = (int) db.insert(TABLE, null, hsValues);
			hs.setId(id);
		}
		return hs;
	}

	public void remove(Highscore Highscore) {
		db.delete(TABLE, KEY_ID + "=" + Highscore.getId(), null);
	}
}
