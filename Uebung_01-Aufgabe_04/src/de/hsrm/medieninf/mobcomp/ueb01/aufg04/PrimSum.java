package de.hsrm.medieninf.mobcomp.ueb01.aufg04;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

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
        
        CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
        	public void run() {
        		Log.v(TAG, "Consumer ist fertig.");
        		Log.v(TAG, "Stoppe Producer ...");
        		p.stop();
        		Log.v(TAG, "Letzte Primzahl: " + c.getLastPrime());
        		try {
					t1.join();
				} catch (InterruptedException e) {
					Log.v(TAG, "Fehler beim Joinen von Thread #1");
				}
        		try {
					t2.join();
				} catch (InterruptedException e) {
					Log.v(TAG, "Fehler beim Joinen von Thread #2");
				}
        		finish();
        	}
        });
        c = new ProdCon(queue, true, barrier);

        t1 = new Thread(p);
        t2 = new Thread(c);
        
        // Wenn Consumer genug hat, Producer beenden
        // Wenn alle Threads beendet wurden, dann rufen
        // Sie die Methode finish um die Anwendung zu beenden
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
        if (p.isRunning()) {
        	Log.v(TAG, "Pausiere Producer");
        	p.onPause();
        }
        if (c.isRunning()) {
        	Log.v(TAG, "Pausiere Consumer");
        	c.onPause();
        }
    }
}