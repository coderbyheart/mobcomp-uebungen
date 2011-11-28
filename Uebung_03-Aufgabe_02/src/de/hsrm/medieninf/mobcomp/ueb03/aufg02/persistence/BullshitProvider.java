package de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence;

import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.BullshitSheet;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class BullshitProvider extends ContentProvider {

	private static final String AUTHORITY = "de.hsrm.medieninf.mobcomp.ueb03.aufg02";
	private static final String CONTENT_URI_STRING = "content://" + AUTHORITY;
	public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
	private static final int BULLSHIT_SHEET_LIST = 1;
	private static final int BULLSHIT_SHEET_ENTRY = 2;
	private static final int BULLSHIT_WORD_LIST = 3;
	private static final int BULLSHIT_WORD_ENTRY = 4;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "sheet", BULLSHIT_SHEET_LIST);
		uriMatcher.addURI(AUTHORITY, "sheet/#", BULLSHIT_SHEET_ENTRY);
		uriMatcher.addURI(AUTHORITY, "word", BULLSHIT_WORD_LIST);
		uriMatcher.addURI(AUTHORITY, "word/#", BULLSHIT_WORD_ENTRY);
	}

	private BullshitSheetDbAdapter dbc;
	
	/**
	 * Wird beim Erzeugen aufgerufen
	 */
	@Override
	public boolean onCreate() {
		dbc = new BullshitSheetDbAdapter(getContext());
		dbc.open();
		return true;
	}

	/**
	 * Löschen wird nicht unterstützt
	 */
	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		return 0;
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
		switch(uriMatcher.match(uri)) {
		case BULLSHIT_SHEET_LIST:
			BullshitSheet bss = new BullshitSheet();
			bss.setNwords(cv.getAsInteger(BullshitSheetDbAdapter.KEY_NWORDS));
			dbc.persist(bss);
			return null;
		default:
			return null;
		}
	}

	@Override
	public Cursor query(Uri uri, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
