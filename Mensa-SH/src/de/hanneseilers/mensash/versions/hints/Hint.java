package de.hanneseilers.mensash.versions.hints;

import android.app.Activity;
import android.app.DialogFragment;

public abstract class Hint extends DialogFragment {

	public static Activity activity;
	public boolean active = true;
	public String name = "hint";
	
	/**
	 * Set {@code revision} to specifiy the revision the hint was added to application
	 * Use {@code -1} to show hint on every revesion
	 */
	public int revision = 0;
	
	public Hint(Activity activity){}
	
	public Hint() {
		super();
	}
	
	protected void setInactive(){
		active = false;
	}
	
	
}
