package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import android.app.Activity;

abstract public class DbActivity extends Activity {

	CurrencyDbAdapter dba;

	/** Called when the activity is first created. */
	@Override
	public void onResume() {
		dba = new CurrencyDbAdapter(this).open();
		super.onResume();
	}

	@Override
	public void onPause() {
		dba.close();
		super.onPause();
	}
}