package de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.R;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Entity;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Sheet;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Word;

/**
 * Datenbank-Adapter für Bullshit-Bingo
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class DbAdapter {
	private static final String DB_FILENAME = "db.db";
	private static final int DB_VERSION = 4;
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	private Context context;

	// Tabelle für Blätter
	private static final String SHEET_TABLE = "sheet";
	public static final int SHEET_COL_ID = 0;
	public static final int SHEET_COL_NWORDS = 1;
	public static final int SHEET_COL_NWORDS_HEARD = 2;
	public static final int SHEET_COL_CREATION_TIME = 3;
	public static final String SHEET_KEY_ID = "_id";
	public static final String SHEET_KEY_NWORDS = "nwords";
	public static final String SHEET_KEY_NWORDS_HEARD = "heard";
	public static final String SHEET_KEY_CREATION_TIME = "creationTime";
	private static final String SHEET_SQL_CREATE = "CREATE TABLE "
			+ SHEET_TABLE + "(" + SHEET_KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + SHEET_KEY_NWORDS
			+ " INTEGER NOT NULL" + ", " + SHEET_KEY_NWORDS_HEARD
			+ " INTEGER NOT NULL" + ", " + SHEET_KEY_CREATION_TIME
			+ " STRING NOT NULL" + ")";

	// Tabelle für Wörter auf Blättern
	public static final String WORD_TABLE = "word";
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
	public static final String WORD_SQL_CREATE = "CREATE TABLE " + WORD_TABLE
			+ "(" + WORD_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", "
			+ WORD_KEY_SHEET + " INTEGER " + ", " + WORD_KEY_WORD
			+ " STRING NOT NULL" + ", " + WORD_KEY_HEARD_TIME + " STRING NULL"
			+ ", " + WORD_KEY_CREATION_TIME + " STRING NOT NULL" + ")";

	// Felder für Info, keine Tabelle
	public static final int INFO_COL_NUMWORDS = 0;
	public static final String INFO_KEY_NUMWORDS = "numWords";

	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
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
		this.context = context;
		dbHelper = new DBHelper(context, DB_FILENAME, null, DB_VERSION);
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

	/**
	 * Liefert einen Cursor für ein Blatt
	 * @param id
	 */
	public Cursor getSheetCursor(int id) {
		String[] cols = new String[] { SHEET_KEY_ID, SHEET_KEY_NWORDS,
				SHEET_KEY_NWORDS_HEARD, SHEET_KEY_CREATION_TIME };
		Cursor result = getDb().query(SHEET_TABLE, cols,
				SHEET_KEY_ID + "=" + id, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return null;
		}
		return result;
	}
	
	/**
	 * Liefert eine Liste mit allen Blättern
	 */
	public Cursor getSheetsCursor() {
		return getSheetsCursor(new String[] { SHEET_KEY_ID, SHEET_KEY_NWORDS,
				SHEET_KEY_NWORDS_HEARD, SHEET_KEY_CREATION_TIME });
	}

	/**
	 * Liefert eine Liste mit allen Blättern, aber nur bestimmten Spalten
	 * @param cols
	 */
	public Cursor getSheetsCursor(String[] cols) {
		Cursor result = getDb().query(SHEET_TABLE, cols, null, null, null,
				null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return null;
		}
		return result;
	}



	/**
	 * Liefert ein Blatt
	 * @param id
	 */
	public Sheet getSheet(int id) {
		Cursor result = getSheetCursor(id);
		Sheet item = new Sheet();
		item.setId(id);
		item.setCreationTime(result.getString(SHEET_COL_CREATION_TIME));
		item.setNumberOfWords(result.getInt(SHEET_COL_NWORDS));
		item.setNumberOfHeardWords(result
				.getInt(SHEET_COL_NWORDS_HEARD));
		return item;
	}

	/**
	 * Liefert ein Wort
	 * @param id
	 */
	public Word getWord(int id) {

		String[] cols = new String[] { WORD_KEY_ID, WORD_KEY_SHEET,
				WORD_KEY_WORD, WORD_KEY_HEARD_TIME, WORD_KEY_CREATION_TIME };
		Cursor result = getDb().query(WORD_TABLE, cols,
				SHEET_KEY_ID + "=" + id, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return null;
		}
		Word item = new Word();
		item.setId(id);
		item.setSheetId(result.getInt(WORD_COL_SHEET));
		item.setCreationTime(result.getString(WORD_COL_CREATION_TIME));
		item.setHeardTime(result.getString(WORD_COL_HEARD_TIME));
		item.setWord(result.getString(WORD_COL_WORD));
		return item;
	}

	/**
	 * Liefert einen Cursor zu den Worten eines Blattes
	 * 
	 * @param id
	 */
	public Cursor getSheetWordsCursor(int id) {
		Cursor result = getDb().query(
				WORD_TABLE,
				new String[] { WORD_KEY_ID, WORD_KEY_SHEET, WORD_KEY_WORD,
						WORD_KEY_HEARD_TIME, WORD_KEY_CREATION_TIME },
				WORD_KEY_SHEET + "=" + id, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			return null;
		}
		return result;
	}

	/**
	 * Speichert ein Blatt
	 * @param item
	 */
	public Sheet persist(Sheet item) {
		ContentValues itemValues = new ContentValues();
		itemValues.put(SHEET_KEY_NWORDS, item.getNumberOfWords());
		itemValues.put(SHEET_KEY_NWORDS_HEARD,
				item.getNumberOfHeardWords());
		if (item.getId() > 0) {
			itemValues.put(SHEET_KEY_ID, item.getId());
			itemValues.put(SHEET_KEY_CREATION_TIME, item.getCreationTime());
		} else {
			itemValues.put(SHEET_KEY_CREATION_TIME, new Date().toGMTString());
		}
		return (Sheet) persist(item, itemValues, SHEET_TABLE, SHEET_KEY_ID);
	}

	/**
	 * Speichert ein Wort
	 * @param item
	 */
	public Word persist(Word item) {
		ContentValues itemValues = new ContentValues();
		itemValues.put(WORD_KEY_SHEET, item.getSheetId());
		itemValues.put(WORD_KEY_WORD, item.getWord());
		itemValues.put(WORD_KEY_HEARD_TIME, item.getHeardTime());
		if (item.getId() > 0) {
			itemValues.put(WORD_KEY_ID, item.getId());
			itemValues.put(WORD_KEY_CREATION_TIME, item.getCreationTime());
		} else {
			itemValues.put(WORD_KEY_CREATION_TIME, new Date().toGMTString());
		}
		item = (Word) persist(item, itemValues, WORD_TABLE, WORD_KEY_ID);

		// Anzahl der gehörten Wörter aktualisieren
		String countSql = "SELECT COUNT(" + WORD_KEY_ID
				+ ") FROM " + WORD_TABLE + " WHERE " + WORD_KEY_SHEET
				+ "= " + item.getSheetId() + " AND " + WORD_KEY_HEARD_TIME
				+ " IS NOT NULL";
		Cursor countResult = db.rawQuery(countSql, null);
		Integer numCompleted = 0;
		if (countResult.moveToFirst()) {
			numCompleted = countResult.getInt(0);
		}
		countResult.close();
		String sql = "UPDATE " + SHEET_TABLE + " SET "
				+ SHEET_KEY_NWORDS_HEARD + "= " + numCompleted + " WHERE " + SHEET_KEY_ID + " = "
				+ item.getSheetId();
		db.execSQL(sql);
		
		return item;
	}

	/**
	 * Allgemeine Methode zum speichern von Entities
	 * @param item
	 * @param itemValues
	 * @param table
	 * @param idKey
	 * @return
	 */
	private Entity persist(Entity item, ContentValues itemValues, String table,
			String idKey) {
		if (item.getId() > 0) {
			db.update(table, itemValues, idKey + "=" + item.getId(), null);
		} else {
			int id = (int) db.insert(table, null, itemValues);
			item.setId(id);
		}
		return item;
	}

	/**
	 * Löscht ein Blatt und dessen Worte
	 * @param item
	 */
	public void remove(Sheet item) {
		db.delete(WORD_TABLE, WORD_KEY_SHEET + "=" + item.getId(), null);
		db.delete(SHEET_TABLE, SHEET_KEY_ID + "=" + item.getId(), null);
	}

	/**
	 * Liefert allgemeine Infos zum Spiel - z.Z. die maximal Anzahl von Worten
	 * 
	 * @return Cursor
	 */
	public Cursor getInfoCursor() {
		MatrixCursor infoCursor = new MatrixCursor(
				new String[] { INFO_KEY_NUMWORDS }, 1);
		ArrayList<Integer> infoValues = new ArrayList<Integer>();
		infoValues
				.add(context.getResources().getStringArray(R.array.bullshit).length);
		infoCursor.addRow(infoValues);
		return infoCursor;
	}
}
