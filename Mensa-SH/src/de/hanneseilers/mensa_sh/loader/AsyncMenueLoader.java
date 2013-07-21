package de.hanneseilers.mensa_sh.loader;

import de.hanneseilers.mensa_sh.CacheManager;
import de.hanneseilers.mensa_sh.activities.ActivityMain;
import de.hanneseilers.mensa_sh.enums.LoadingProgress;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

public class AsyncMenueLoader extends AsyncTask<String, Integer, String> {

	private ActivityMain ctx;
	
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
		for( Mensa m : ctx.getLocations() ){
			if( m.getName().equals(params[0]) ){
				
				String ret = "";
				if( (ret = CacheManager.readCachedFile(ctx, "menue_"+m.getCity()+"_"+m.getName()))
						== null ){
					ret = m.getMenueAsHtml();
					CacheManager.writeChachedFile( ctx, "menue_"+m.getCity()+"_"+m.getName(), ret );
				}
				
				// set lunch time
				ctx.setLunchTime(m.getLunchTime());
				return ret;				
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
		ctx.loadWebsiteHtml(result);
		ctx.setLoadingProgress(LoadingProgress.MENUE_LOADED);
	}
	
}
