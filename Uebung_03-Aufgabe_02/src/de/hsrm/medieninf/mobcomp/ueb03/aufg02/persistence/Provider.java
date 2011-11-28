package de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence;

import java.util.Date;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.R;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Sheet;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Word;

public class Provider extends ContentProvider {

	private static final String AUTHORITY = "de.hsrm.medieninf.mobcomp.ueb03.aufg02";
	private static final String CONTENT_URI_STRING = "content://" + AUTHORITY;
	public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
	private static final int SHEET_LIST = 1;
	private static final int SHEET_ENTRY = 2;
	private static final int WORD_ENTRY = 3;
	private static final UriMatcher uriMatcher;
	private DbAdapter dbc;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "sheet", SHEET_LIST);
		uriMatcher.addURI(AUTHORITY, "sheet/#", SHEET_ENTRY);
		uriMatcher.addURI(AUTHORITY, "word/#", WORD_ENTRY);
	}

	/**
	 * Wird beim Erzeugen aufgerufen
	 */
	@Override
	public boolean onCreate() {
		dbc = new DbAdapter(getContext());
		dbc.open();
		return true;
	}

	/**
	 * Löscht Sheets. Worte können nicht gelöscht werden.
	 */
	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		switch (uriMatcher.match(uri)) {
		case SHEET_ENTRY:
			Sheet bss = dbc.getSheet(Integer.parseInt(uri.getFragment()));
			dbc.remove(bss);
			return 1;
		default:
			return 0;
		}
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Legt eine neues Bullshit-Bingo-Blatt an
	 */
	@Override
	public Uri insert(Uri uri, ContentValues cv) {
		switch (uriMatcher.match(uri)) {
		case SHEET_LIST:

			String[] bullshitWords = getContext().getResources()
					.getStringArray(R.array.bullshit);

			Sheet bss = new Sheet();
			int nWords = cv.getAsInteger(DbAdapter.SHEET_KEY_NWORDS);
			String now = new Date().toGMTString();
			bss.setNumberOfWords(nWords);
			bss.setCreationTime(now);
			dbc.persist(bss);

			for (int i = 0; i < nWords; i++) {
				Word bsw = new Word();
				bsw.setCreationTime(now);
				bsw.setSheet(bss);
				int randKey = Double.valueOf(
						Math.random() * bullshitWords.length).intValue();
				bsw.setWord(bullshitWords[randKey]);
				dbc.persist(bsw);
			}

			return CONTENT_URI.buildUpon().appendPath("sheet")
					.appendPath("" + bss.getId()).build();
		default:
			return null;
		}
	}

	@Override
	public Cursor query(Uri uri, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		switch (uriMatcher.match(uri)) {
		case SHEET_ENTRY:
			return dbc.getSheetCursor(Integer.parseInt(uri.getFragment()));
		case SHEET_LIST:
			return dbc.getSheetsCursor();
		default:
			return null;
		}
	}

	/**
	 * Aktualisiert Worte, hierbei wird nur das Wort auf "gehört" gesetzt.
	 * 
	 * Sheets können nicht aktualisiert werden
	 */
	@Override
	public int update(Uri uri, ContentValues cv, String arg2, String[] arg3) {
		switch (uriMatcher.match(uri)) {
		case WORD_ENTRY:
			Word bsw = dbc.getWord(Integer.parseInt(uri.getFragment()));
			bsw.setHeardTime(new Date().toGMTString());
			dbc.persist(bsw);
			return 1;
		default:
			return 0;
		}
	}
}
