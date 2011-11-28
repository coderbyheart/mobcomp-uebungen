package de.hsrm.medieninf.mobcomp.ueb01.aufg04;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class PrimSum extends Activity {
	
	public static final String TAG = "PrimSumActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	Log.v(TAG, "onCreate()");
    }
    
    /*
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        Log.v(TAG, "onCreate()");
        
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();

        ProdCon p = new ProdCon(queue, false);
        ProdCon c = new ProdCon(queue);

        Thread t1 = new Thread(p);
        Thread t2 = new Thread(c);

        Log.v(TAG, "Starte Thread #1");
        t1.start();
        Log.v(TAG, "Starte Thread #2");
        t2.start();
        Log.v(TAG, "Fertig");
    }
    */
}