package de.hanneseilers.mensash.versions.hints;

import android.app.Activity;
import android.app.DialogFragment;

public abstract class Hint extends DialogFragment {

	public static Activity activity;
	public boolean active = true;
	public String name = "hint";
	
	public Hint(Activity activity){}
	
	public Hint() {
		super();
	}
	
	protected void setInactive(){
		active = false;
	}
	
	
}
