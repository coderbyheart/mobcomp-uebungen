package de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence;

import java.util.ArrayList;
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

	private static final String AUTHORITY = "de.hsrm.medieninf.mobcomp.ueb03.aufg02.provider.content";
	private static final String CONTENT_URI_STRING = "content://" + AUTHORITY;
	public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
	private static final int SHEET_LIST = 1;
	private static final int SHEET_ITEM = 2;
	private static final int SHEET_WORDS = 3;
	private static final int WORD_ITEM = 4;
	private static final int INFO_LIST = 5;
	private static final UriMatcher uriMatcher; 
	private DbAdapter dbc;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "sheet", SHEET_LIST);
		uriMatcher.addURI(AUTHORITY, "sheet/#", SHEET_ITEM);
		uriMatcher.addURI(AUTHORITY, "sheet/#/word", SHEET_WORDS);
		uriMatcher.addURI(AUTHORITY, "word/#", WORD_ITEM);
		uriMatcher.addURI(AUTHORITY, "info", INFO_LIST);
	}
	private static final String VND_SHEET_ITEM = "vnd.android.cursor.item/vnd.de.hsrm.medieninf.mobcomp.ueb03.aufg02.sheet";
	private static final String VND_SHEET_DIR = "vnd.android.cursor.dir/vnc.de.hsrm.medieninf.mobcomp.ueb03.aufg02.sheet";
	private static final String VND_WORD_ITEM = "vnd.android.cursor.item/vnd.de.hsrm.medieninf.mobcomp.ueb03.aufg02.word";
	private static final String VND_WORD_DIR = "vnd.android.cursor.dir/vnc.de.hsrm.medieninf.mobcomp.ueb03.aufg02.word";
	private static final String VND_INFO_ITEM = "vnd.android.cursor.item/vnc.de.hsrm.medieninf.mobcomp.ueb03.aufg02.info";

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
	public int delete(Uri uri, String where, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case SHEET_ITEM:
			Sheet bss = dbc.getSheet(Integer.parseInt(uri.getFragment()));
			dbc.remove(bss);
			return 1;
		default:
			return 0;
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case SHEET_LIST:
			return VND_SHEET_DIR;
		case SHEET_ITEM:
			return VND_SHEET_ITEM;
		case WORD_ITEM:
			return VND_WORD_ITEM;
		case SHEET_WORDS:
			return VND_WORD_DIR;
		case INFO_LIST:
			return VND_INFO_ITEM;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
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
			if (nWords > bullshitWords.length) return null;
			
			String now = new Date().toGMTString();
			bss.setNumberOfWords(nWords);
			bss.setCreationTime(now);
			dbc.persist(bss);

			ArrayList<Integer> usedWords = new ArrayList<Integer>();
			for (int i = 0; i < nWords; i++) {
				Word bsw = new Word();
				bsw.setCreationTime(now);
				bsw.setSheet(bss);
				Integer randKey;
				do {
					randKey = Double.valueOf(Math.random() * bullshitWords.length).intValue();
				} while(usedWords.contains(randKey));
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
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (uriMatcher.match(uri)) {
		case SHEET_ITEM:
			return dbc.getSheetCursor(Integer.parseInt(uri.getPathSegments().get(1)));
		case SHEET_LIST:
			return dbc.getSheetsCursor();
		case SHEET_WORDS:
			return dbc.getSheetWordsCursor(Integer.parseInt(uri.getPathSegments().get(1)));
		case INFO_LIST:
			return dbc.getInfoCursor();
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
	public int update(Uri uri, ContentValues values, String where,
			String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case WORD_ITEM:
			Word bsw = dbc.getWord(Integer.parseInt(uri.getPathSegments().get(1)));
			bsw.setHeardTime(new Date().toGMTString());
			dbc.persist(bsw);
			return 1;
		default:
			return 0;
		}
	}
}
