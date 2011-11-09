package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

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

public class CurrencyDbAdapter {
	private static final String TAG = "CurrencyDbAdapter";

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
	private CurrencyDBHelper dbHelper;

	// SQL Anweisungen um DB zu erstellen.
	private static final String CURRENCY_SQL_CREATE = "CREATE TABLE "
			+ CURRENCY_TABLE + "(" + CURRENCY_KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + CURRENCY_KEY_SYMBOL
			+ " TEXT NOT NULL" + ", " + CURRENCY_KEY_RATE + " DOUBLE NOT NULL"
			+ ", " + CURRENCY_KEY_NAME + " TEXT NOT NULL" + ")";

	public CurrencyDbAdapter(Context context) {
		dbHelper = new CurrencyDBHelper(context, DB_FILENAME, null, DB_VERSION);
	}

	public CurrencyDbAdapter open() throws SQLException {
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

	public CurrencyDbAdapter close() {
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

	public Cursor getCurrenciesCursor() {
		String[] cols = new String[] { CURRENCY_KEY_ID, CURRENCY_KEY_SYMBOL,
				CURRENCY_KEY_RATE, CURRENCY_KEY_NAME };
		return getDb()
				.query(CURRENCY_TABLE, cols, null, null, null, null, null);
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
			for (Currency waehrung : rechner.getDefaultCurrencies()) {
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

	public Currency getCurrency(int currencyId) {
		String[] cols = new String[] { CURRENCY_KEY_ID, CURRENCY_KEY_SYMBOL,
				CURRENCY_KEY_RATE, CURRENCY_KEY_NAME };
		Cursor result = getDb().query(CURRENCY_TABLE, cols,
				CURRENCY_KEY_ID + "=" + currencyId, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			Log.v(TAG, "Währung #" + currencyId + " nicht gefunden.");
			return null;
		}
		Currency waehrung = new Currency();
		waehrung.setId(currencyId);
		waehrung.setName(result.getString(CURRENCY_COL_NAME));
		waehrung.setRate(result.getDouble(CURRENCY_COL_RATE));
		waehrung.setSymbol(result.getString(CURRENCY_COL_SYMBOL));
		return waehrung;
	}
	
	public List<Currency> getCurrencies() {
		String[] cols = new String[] { CURRENCY_KEY_ID, CURRENCY_KEY_SYMBOL,
				CURRENCY_KEY_RATE, CURRENCY_KEY_NAME };
		Cursor result = getDb().query(CURRENCY_TABLE, cols, null, null, null, null, null);
		result.moveToFirst();
		if (result.isAfterLast()) {
			Log.v(TAG, "Keine Währungen gefunden.");
			return null;
		}
		ArrayList<Currency> currencies = new ArrayList<Currency>();
		do {
			Currency waehrung = new Currency();
			waehrung.setId(result.getInt(CURRENCY_COL_ID));
			waehrung.setName(result.getString(CURRENCY_COL_NAME));
			waehrung.setRate(result.getDouble(CURRENCY_COL_RATE));
			waehrung.setSymbol(result.getString(CURRENCY_COL_SYMBOL));
			currencies.add(waehrung);
		} while(result.moveToNext());
		return currencies;
	}

	public Currency persist(Currency c) {
		ContentValues cValues = new ContentValues();
		cValues.put(CURRENCY_KEY_NAME, c.getName());
		cValues.put(CURRENCY_KEY_SYMBOL, c.getSymbol());
		cValues.put(CURRENCY_KEY_RATE, c.getRate());
		if (c.getId() > 0) {
			cValues.put(CURRENCY_KEY_ID, c.getId());
			db.update(CURRENCY_TABLE, cValues,
					CURRENCY_KEY_ID + "=" + c.getId(), null);
		} else {
			int id = (int) db.insert(CURRENCY_TABLE, null, cValues);
			c.setId(id);
		}
		return c;
	}

	public void remove(Currency currency) {
		db.delete(CURRENCY_TABLE, CURRENCY_KEY_ID + "=" + currency.getId(), null);
	}
}
