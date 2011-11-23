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
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Sheet;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Word;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Entity;

public class DbAdapter {
	private static final String DB_FILENAME = "db.db";
	private static final int DB_VERSION = 1;
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	
	// Tabelle für Blätter
	private static final String SHEET_TABLE = "bullshit_sheet";
	public static final int SHEET_COL_ID = 0;
	public static final int SHEET_COL_NWORDS = 1;
	public static final int SHEET_COL_CREATION_TIME = 2;
	public static final String SHEET_KEY_ID = "_id";
	public static final String SHEET_KEY_NWORDS = "nwords";
	public static final String SHEET_KEY_CREATION_TIME = "creationTime";
	private static final String SHEET_SQL_CREATE = "CREATE TABLE " + SHEET_TABLE + "("
			+ SHEET_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + SHEET_KEY_NWORDS
			+ " INTEGER NOT NULL" + ", " + SHEET_KEY_CREATION_TIME + " STRING NOT NULL"
			+ ", " + ")";
	
	// Tabelle für Wörter auf Blättern
	public static final String WORD_TABLE = "bullshit_word";
	public static final int WORD_COL_ID = 0;
	public static final int WORD_COL_SHEET = 1;
	public static final int WORD_COL_WORD = 2;
	public static final int WORD_COL_HEARD_TIME = 3;
	public static final int WORD_COL_CREATION_TIME = 4;
	public static final String WORD_KEY_ID = "_id";
	public static final String WORD_KEY_SHEET = "sheet";
	public static final String WORD_KEY_WORD = "word";
	public static final String WORD_KEY_HEARD_TIME = "heardTime";
	public static final String WORD_KEY_CREATION_TIME = "creationTime";
	public static final String WORD_SQL_CREATE = "CREATE TABLE "
			+ WORD_TABLE + "(" 
			+ WORD_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", "
			+ WORD_KEY_SHEET + " INTEGER " + ", "
			+ WORD_KEY_WORD + " STRING NOT NULL" + ", "
			+ WORD_KEY_HEARD_TIME + " STRING NULL" + ", "
			+ WORD_KEY_CREATION_TIME + " STRING NOT NULL" + ", "
			+ ")";
	
	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SHEET_SQL_CREATE);
			db.execSQL(WORD_SQL_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + SHEET_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + WORD_TABLE);
			onCreate(db);
		}

	}

	public DbAdapter(Context context) {
		dbHelper = new DBHelper(context, DB_FILENAME, null,
				DB_VERSION);
	}

	public DbAdapter open() throws SQLException {
		if (db == null) {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (SQLException e) {
				db = dbHelper.getReadableDatabase();
			}
		}
		return this;
	}

	public DbAdapter close() {
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
	
	public Cursor getSheetCursor(int id) {
		String[] cols = new String[] { SHEET_KEY_ID, SHEET_KEY_NWORDS, SHEET_KEY_CREATION_TIME };
		Cursor result = getDb().query(SHEET_TABLE, cols, SHEET_KEY_ID + "=" + id,
				null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return null;
		}
		return result;
	}

	public Sheet getSheet(int id) {
		Cursor result = getSheetCursor(id);
		Sheet entry = new Sheet();
		entry.setId(id);
		entry.setCreationTime(result.getString(SHEET_COL_CREATION_TIME));
		entry.setNumberOfWords(result.getInt(SHEET_COL_NWORDS));
		return entry;
	}

	public Word getWord(int id) {

		String[] cols = new String[] { WORD_KEY_ID,
				WORD_KEY_SHEET,
				WORD_KEY_WORD,
				WORD_KEY_HEARD_TIME,
				WORD_KEY_CREATION_TIME };
		Cursor result = getDb().query(WORD_TABLE, cols,
				SHEET_KEY_ID + "=" + id, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return null;
		}
		Entity sheet = getSheet(result
				.getInt(WORD_COL_SHEET));
		Word entry = new Word();
		entry.setId(id);
		entry.setSheet(sheet);
		return entry;
	}
	
	public Cursor getSheetsCursor() {
		return getSheetsCursor(new String[] { SHEET_KEY_ID, SHEET_KEY_NWORDS, SHEET_KEY_CREATION_TIME });
	}

	public Cursor getSheetsCursor(String[] cols) {
		Cursor result = getDb()
				.query(SHEET_TABLE, cols, null, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return null;
		}
		return result;
	}

	public List<Sheet> getSheets() {
		ArrayList<Sheet> sheets = new ArrayList<Sheet>();
		Cursor result = getSheetsCursor();
		do {
			Sheet entry = new Sheet();
			entry.setId(result.getInt(SHEET_COL_ID));
			sheets.add(entry);
		} while (result.moveToNext() && sheets.size() < 10);
		return sheets;
	}

	public Entity persist(Entity entry) {
		ContentValues entryValues = new ContentValues();
		if (entry.getId() > 0) {
			entryValues.put(SHEET_KEY_ID, entry.getId());
			db.update(SHEET_TABLE, entryValues, SHEET_KEY_ID + "=" + entry.getId(), null);
		} else {
			int id = (int) db.insert(SHEET_TABLE, null, entryValues);
			entry.setId(id);
		}
		return entry;
	}

	public void remove(Sheet entry) {
		db.delete(WORD_TABLE,
				WORD_KEY_SHEET + "=" + entry.getId(), null);
		db.delete(SHEET_TABLE, SHEET_KEY_ID + "=" + entry.getId(), null);
	}

	public void remove(Word entry) {
		db.delete(WORD_TABLE, WORD_KEY_ID
				+ "=" + entry.getId(), null);
	}
}
