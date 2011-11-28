package de.hsrm.medieninf.mobcomp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import de.hsrm.medieninf.mobcomp.ueb02.R;

public class Lifecycle extends Activity {
    
	private static final String TAG = "LifecycleActivity";
	
	/** 
	 * Called when the activity is first created. 
	 * This is where you should do all of your normal 
	 * static set up: create views, bind data to lists, etc. 
	 * This method also provides you with a Bundle containing 
	 * the activity's previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.v(TAG, "onCreate() -> onStart()");
        setContentView(R.layout.main);
    }
    
    /** 
     * Called after your activity has been stopped, prior to it being started again.
     * Always followed by onStart() 
     */
    @Override
    public void onRestart() {
    	super.onRestart();
    	Log.v(TAG, "onRestart() -> onStart()");
    }
    
    /**
     * Called when the activity is becoming visible to the user.
     * 
     * Followed by onResume() if the activity comes to the foreground, 
     * or onStop() if it becomes hidden.
     */
    @Override
    public void onStart() {
    	super.onStart();
    	Log.v(TAG, "onStart() -> ( onResume() | onStop() )");
    }
    
    /**
     * Called when the activity will start interacting with the user. 
     * At this point your activity is at the top of the activity 
     * stack, with user input going to it.
     * 
     * Always followed by onPause().
     */
    @Override
    public void onResume() {
    	super.onResume();
    	Log.v(TAG, "onResume() -> onPause()");
    }
    
    /**
     * Called when the system is about to start resuming a previous activity. 
     * 
     * This is typically used to commit unsaved changes to persistent data, 
     * stop animations and other things that may be consuming CPU, etc. 
     * Implementations of this method must be very quick because the next 
     * activity will not be resumed until this method returns.
     * 
     * Followed by either onResume() if the activity returns back to the 
     * front, or onStop() if it becomes invisible to the user.
     */
    @Override
    public void onPause() {
    	super.onPause();
    	Log.v(TAG, "onPause() -> ( onResume() | onStop() )");    	
    }
    
    /**
     * Called when the activity is no longer visible to the user, 
     * because another activity has been resumed and is covering 
     * this one. This may happen either because a new activity is 
     * being started, an existing one is being brought in front 
     * of this one, or this one is being destroyed.
     * 
     * Followed by either onRestart() if this activity is coming 
     * back to interact with the user, or onDestroy() if this
     * activity is going away.
     */
    @Override
    public void onStop() {
    	super.onStop();
    	Log.v(TAG, "onStop() -> ( onRestart() | onDestroy() )");
    }
    
    /**
     * The final call you receive before your activity is destroyed. 
     * This can happen either because the activity is finishing 
     * (someone called finish() on it, or because the system is 
     * temporarily destroying this instance of the activity to save 
     * space. You can distinguish between these two scenarios 
     * with the isFinishing() method.
     */
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	Log.v(TAG, "onDestroy()");
    }
}