package de.hanneseilers.mensash.versions;

import java.util.ArrayList;
import java.util.List;

import de.hanneseilers.mensash.versions.hints.Disclaimer;
import de.hanneseilers.mensash.versions.hints.Hint;
import de.hanneseilers.mensash.versions.hints.VersionHintDialog1;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

/**
 * Class for showing version hints
 * @author Hannes Eilers
 *
 */
public final class VersionHints{
	
	private static List<Hint> hintlist = new ArrayList<Hint>();
	
	/**
	 * Initates hintlist if empty
	 * @param activity
	 */
	private static void initHintList(Activity activity){
		if( hintlist.size() == 0 ){
			// add all hints to list
			hintlist.add( new VersionHintDialog1(activity) );
			hintlist.add( new Disclaimer(activity) );
		}
	}
	
	/**
	 * Shows all hints
	 * @param activity
	 */
	public static void showAllHints(Activity activity){	
		// init hintlist
		initHintList(activity);
		
		new AsyncTask<Activity, Void, Void>(){

			@Override
			protected Void doInBackground(Activity... params) {
				Activity activity = params[0];
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
				
				// generate all hints
				while( hintlist.size() > 0 ){
					// get next hint in list
					Hint hint = hintlist.get(0);
					
					// check for disclaimer
					if( !hint.name.equals("disclaimer")
							|| sharedPref.getBoolean("SHOW_RATING", false) ){
						showHint(hint, activity);
					}					
					
					// remove hint from list
					hintlist.remove(0);
				}
				
				return null;
			}
			
		}.execute(activity);
	}
	
	/**
	 * Shows a hint
	 * @param hint
	 * @param activity
	 */
	public static void showHint(Hint hint, Activity activity){		
		hint.show( activity.getFragmentManager(), hint.name );
		waitForHint(hint);
	}
	
	/**
	 * Waits until hint is closed
	 * @param hint
	 */
	public static void waitForHint(Hint hint){
		while( hint.active );
	}
	
	/**
	 * Shows disclaimer
	 * @param activity
	 */
	public static void showDisclaimer(Activity activity){
		new AsyncTask<Activity, Void, Void>(){

			@Override
			protected Void doInBackground(Activity... params) {
				Activity activity = params[0];
				showHint(new Disclaimer(activity), activity);				
				return null;
			}
			
		}.execute(activity);
			
	}
	
}
