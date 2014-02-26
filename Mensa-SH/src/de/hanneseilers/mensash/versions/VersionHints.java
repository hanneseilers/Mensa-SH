package de.hanneseilers.mensash.versions;

import de.hanneseilers.mensash.versions.hints.Disclaimer;
import de.hanneseilers.mensash.versions.hints.Hint;
import de.hanneseilers.mensash.versions.hints.VersionHintDialog1;
import android.app.Activity;
import android.os.AsyncTask;

/**
 * Class for showing version hints
 * @author Hannes Eilers
 *
 */
public final class VersionHints{
	
	/**
	 * Shows all hints
	 * @param activity
	 */
	public static void showAllHints(Activity activity){		
		new AsyncTask<Activity, Void, Void>(){

			@Override
			protected Void doInBackground(Activity... params) {
				Activity activity = params[0];
				
				// generate all hints
				Hint hint1 = new VersionHintDialog1(activity);
				Hint disclaimer = new Disclaimer(activity);
				
				// show hints
				showHint(hint1, activity);
				
				showHint(disclaimer, activity);
				
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
		
		// show hint using async task
		new AsyncTask<Object, Void, Void>(){

			@Override
			protected Void doInBackground(Object... params) {
				if( params.length > 1 ){
					Hint hint = (Hint) params[0];
					Activity activity = (Activity) params[1];
					
					// show hint
					hint.show( activity.getFragmentManager(), hint.name );
					waitForHint(hint);
				}
				return null;
			}
			
		}.execute( new Object[]{hint, activity} );
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
				Hint disclaimer = new Disclaimer(activity);
				showHint(disclaimer, activity);				
				return null;
			}
			
		}.execute(activity);		
	}
	
}
