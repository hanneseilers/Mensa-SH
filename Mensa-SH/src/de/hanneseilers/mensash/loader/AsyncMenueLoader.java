package de.hanneseilers.mensash.loader;

import de.hanneseilers.mensash.CacheManager;
import de.hanneseilers.mensash.activities.ActivityMain;
import de.hanneseilers.mensash.enums.LoadingProgress;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

public class AsyncMenueLoader extends AsyncTask<String, Integer, String> {

	private ActivityMain ctx;
	private Mensa mensa = null;
	
	public AsyncMenueLoader(ActivityMain ctx){
		super();
		this.ctx = ctx;
	}
	
	/**
	 * Gets cities
	 */
	@Override
	protected String doInBackground(String... params) {
		
		// find mensa with params name
		for( Mensa mensa : ctx.getLocations() ){
			if( mensa.getName().equals(params[0]) ){
				
				this.mensa = mensa;
				
				String html = "";
				if( (html = CacheManager.readCachedFile(ctx, "menue_"+mensa.getCity()+"_"+mensa.getName()))
						== null ){
					html = mensa.getMenueAsHtml();
					CacheManager.writeChachedFile( ctx, "menue_"+mensa.getCity()+"_"+mensa.getName(), html );
				}

				return html;				
			}
		}
		
		return "Kein Speiseplan gefunden!";
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(String result) {
		
		// check if result is empty
		if( result == "" ){
			result = "<b>Kein Speiseplan gefunden!</b>";
		}
		
		// load menue
		// set lunch time
		ctx.setLunchTime(mensa.getLunchTime());
		ctx.loadWebsiteHtml(result);
		
		// load ratings
		if( AsyncRatingsLoader.task != null ){
			AsyncRatingsLoader.task.cancel(true);
			long t1 = System.currentTimeMillis();
			
			// wait until task is finished or timeout
			while( !AsyncRatingsLoader.task.isCancelled()
					&& (System.currentTimeMillis()-t1) < AsyncRatingsLoader.taskTimeout );
			AsyncRatingsLoader.task = null;
		}
		ctx.setLoadingProgress( LoadingProgress.INIT, "loading ratings..." );
		new AsyncRatingsLoader(ctx).execute( new String[]{mensa.getName(), result} );
	}		
	
}
