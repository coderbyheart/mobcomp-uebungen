package de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.BullshitSheet;

public class BullshitSheetDbAdapter {
	private static final String DB_FILENAME = "db.db";
	private static final int DB_VERSION = 1;
	private static final String TABLE = "bullshit_sheet";

	public static final int COL_ID = 0;
	public static final int COL_NWORDS = 1;
	public static final int COL_CREATED = 2;
	public static final String KEY_ID = "_id";
	public static final String KEY_NWORDS = "nwords";
	public static final String KEY_CREATED = "created";

	private SQLiteDatabase db;
	private BullshitSheetDBHelper dbHelper;

	// SQL Anweisungen um DB zu erstellen.
	private static final String SQL_CREATE = "CREATE TABLE " + TABLE + "("
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + KEY_NWORDS
			+ " INTEGER NOT NULL" + ", " + KEY_CREATED + " STRING NOT NULL"
			+ ", " + ")";

	public BullshitSheetDbAdapter(Context context) {
		dbHelper = new BullshitSheetDBHelper(context, DB_FILENAME, null,
				DB_VERSION);
	}

	public BullshitSheetDbAdapter open() throws SQLException {
		if (db == null) {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (SQLException e) {
				db = dbHelper.getReadableDatabase();
			}
		}
		return this;
	}

	public BullshitSheetDbAdapter close() {
		if (db != null) {
			db.close();
			db = null;
		}
		return this;
	}

	protected SQLiteDatabase getDb() {
		open();
		return db;
	}

	private static class BullshitSheetDBHelper extends SQLiteOpenHelper {

		public BullshitSheetDBHelper(Context context, String name,
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

	public BullshitSheet getBullshit(int bullshitId) {
		String[] cols = new String[] { KEY_ID, KEY_NWORDS, KEY_CREATED };
		Cursor result = getDb().query(TABLE, cols, KEY_ID + "=" + bullshitId,
				null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return null;
		}
		BullshitSheet entry = new BullshitSheet();
		entry.setId(bullshitId);
		entry.setTime(result.getString(COL_CREATED));
		entry.setNwords(result.getInt(COL_NWORDS));
		return entry;
	}

	public List<BullshitSheet> getBullshits() {
		ArrayList<BullshitSheet> Bullshits = new ArrayList<BullshitSheet>();
		String[] cols = new String[] { KEY_ID, KEY_NWORDS, KEY_CREATED };
		Cursor result = getDb()
				.query(TABLE, cols, null, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return Bullshits;
		}
		do {
			BullshitSheet entry = new BullshitSheet();
			entry.setId(result.getInt(COL_ID));
			Bullshits.add(entry);
		} while (result.moveToNext() && Bullshits.size() < 10);
		return Bullshits;
	}

	public BullshitSheet persist(BullshitSheet entry) {
		ContentValues entryValues = new ContentValues();
		if (entry.getId() > 0) {
			entryValues.put(KEY_ID, entry.getId());
			db.update(TABLE, entryValues, KEY_ID + "=" + entry.getId(), null);
		} else {
			int id = (int) db.insert(TABLE, null, entryValues);
			entry.setId(id);
		}
		return entry;
	}

	public void remove(BullshitSheet bullshit) {
		db.delete(TABLE, KEY_ID + "=" + bullshit.getId(), null);
	}
}
