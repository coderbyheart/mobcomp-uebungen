package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CurrencyDbAdapter {

	private static final String DB_FILENAME = "currencies.db";
	private static final int DB_VERSION = 1;
	private static final String CURRENCY_TABLE = "currency";

	public static final int CURRENCY_COL_ID = 0;
	public static final int CURRENCY_COL_SYMBOL = 1;
	public static final int CURRENCY_COL_RATE = 2;
	public static final int CURRENCY_COL_NAME = 3;
	public static final String CURRENCY_KEY_ID = "_id";
	public static final String CURRENCY_KEY_SYMBOL = "symbol";
	public static final String CURRENCY_KEY_RATE = "rate";
	public static final String CURRENCY_KEY_NAME = "name";

	private SQLiteDatabase db;
	private final Context context;
	private CurrencyDBHelper dbHelper;

	// SQL Anweisungen um DB zu erstellen.
	private static final String CURRENCY_SQL_CREATE = "CREATE TABLE "
			+ CURRENCY_TABLE + "(" + CURRENCY_KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + CURRENCY_KEY_SYMBOL
			+ " TEXT NOT NULL" + ", " + CURRENCY_KEY_RATE + " DOUBLE NOT NULL"
			+ ", " + CURRENCY_KEY_NAME + " TEXT NOT NULL" + ")";

	public CurrencyDbAdapter(Context context) {
		this.context = context;
		dbHelper = new CurrencyDBHelper(context, DB_FILENAME, null, DB_VERSION);
	}

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}

	private static class CurrencyDBHelper extends SQLiteOpenHelper {
		private Context ctx;

		public CurrencyDBHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			ctx = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CURRENCY_SQL_CREATE);

			Rechner rechner = new Rechner(ctx);
			for (Waehrung waehrung : rechner.getDefaultCurrencies()) {
				ContentValues currencyValues = new ContentValues();
				currencyValues.put(CURRENCY_KEY_NAME, waehrung.getName());
				currencyValues.put(CURRENCY_KEY_RATE, waehrung.getRate());
				currencyValues.put(CURRENCY_KEY_SYMBOL, waehrung.getSymbol());
				db.insert(CURRENCY_TABLE, null, currencyValues);
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + CURRENCY_TABLE);
			onCreate(db);
		}

	}

	public Cursor getCurrencies() {
		String[] cols = new String[] { CURRENCY_KEY_ID, CURRENCY_KEY_SYMBOL,
				CURRENCY_KEY_RATE, CURRENCY_KEY_NAME };
		return db.query(CURRENCY_TABLE, cols, null, null, null, null, null);
	}

}
