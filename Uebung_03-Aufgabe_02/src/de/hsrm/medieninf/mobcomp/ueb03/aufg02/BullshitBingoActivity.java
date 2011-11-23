package de.hsrm.medieninf.mobcomp.ueb03.aufg02;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Sheet;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence.DbAdapter;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence.Provider;

public class BullshitBingoActivity extends Activity {
	
	ArrayList<TextView> wordViews;
	int numberOfWords = 9;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        wordViews = new ArrayList<TextView>();
        TableLayout grid = (TableLayout)findViewById(R.id.grid); 
        
        int rows = Double.valueOf(Math.ceil(Math.sqrt(numberOfWords))).intValue();
        for(int i = 0; i < rows; i++) {
        	TableRow row = (TableRow)getLayoutInflater().inflate(R.layout.grid_row, grid, false);
        	grid.addView(row);
        	for(int j = 0; j < rows; j++) {
        		TextView cell = (TextView)getLayoutInflater().inflate(R.layout.grid_cell, row, false);       		
        		row.addView(cell);
        		wordViews.add(cell);
        	}
        }
        
        // Neue Folie anlegen
        ContentResolver cr = getContentResolver();
        Uri uri = Provider.CONTENT_URI.buildUpon().appendPath("sheet").build();
        ContentValues cv = new ContentValues();
        cv.put(DbAdapter.SHEET_KEY_NWORDS, numberOfWords);
        Uri sheetUrl = cr.insert(uri, cv);
        
        // Folie holen
        Cursor sheetCursor = cr.query(sheetUrl, null, null, null, null);
        Sheet sheet = new Sheet();
        sheet.setId(sheetCursor.getInt(DbAdapter.SHEET_COL_ID));
        sheet.setNumberOfWords(sheetCursor.getInt(DbAdapter.SHEET_COL_NWORDS));
        sheet.setCreationTime(sheetCursor.getString(DbAdapter.SHEET_COL_CREATION_TIME));
        Log.v(getClass().getCanonicalName(), "Got sheet #" + sheet.getId());
        
        // Wörter der Folie holen
        Cursor wordCursor = cr.query(wordUrl, null, null, null, null);
        
    }
}