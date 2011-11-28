package de.hsrm.medieninf.mobcomp.ueb01.aufg04;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Laufzeit HTC Desire 10533ms
 * Laufzeit Emulator 50124ms
 */
public class PrimSum extends Activity {
	
	public static final String TAG = "PrimSumActivity";
	
	private Thread t1, t2;
	private ProdCon p, c;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.v(TAG, "onCreate()");
        
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();

        p = new ProdCon(queue);
        c = new ProdCon(queue, true);

        t1 = new Thread(p);
        t2 = new Thread(c);
        
        Log.v(TAG, "Starte Thread #1");
        t1.start();
        Log.v(TAG, "Starte Thread #2");
        t2.start();
        Log.v(TAG, "Fertig");
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.v(TAG, "Nehme wieder auf ...");
        p.onResume();
        c.onResume();
    }
    
    @Override
    public void onStop() {
    	super.onStop();
        Log.v(TAG, "Pausiere ...");
        p.onPause();
        c.onPause();
    }
}