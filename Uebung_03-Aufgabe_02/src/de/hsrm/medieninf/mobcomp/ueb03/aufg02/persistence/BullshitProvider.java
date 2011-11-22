package de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence;

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

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
