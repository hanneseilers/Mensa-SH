package de.hanneseilers.mensa_sh.loader;

import java.util.ArrayList;
import java.util.List;

import de.hanneseilers.mensa_sh.CacheManager;
import de.hanneseilers.mensa_sh.Main_Activity;
import de.hanneseilers.mensa_sh.enums.LoadingProgress;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

/**
 * Class for async loading of cities
 * @author hannes
 *
 */
public class AsyncCitiesLoader extends AsyncTask<Void, Integer, List<String>> {
	
	private Main_Activity ctx;
	
	public AsyncCitiesLoader(Main_Activity ctx){
		super();
		this.ctx = ctx;
	}
	
	/**
	 * Gets cities
	 */
	@Override
	protected List<String> doInBackground(Void... params) {		
		// check if data is cache
		String ret;
		List<String> retList = new ArrayList<String>();

		if( (ret = CacheManager.readCachedFile(ctx, "cache_cities")) != null ){
			String retArray[] = ret.split(";");
			for( String city : retArray ){
				retList.add(city);
			}
		}
		
		if( retList.size() == 0 ){			
			// cache file
			retList = Mensa.getCities();
			ret = "";
			for( String city : retList ){
				ret += city + ";";
			}
			CacheManager.writeChachedFile(ctx, "cache_cities", ret);
		}
		
		return retList;
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(List<String> result) {
		ctx.clearCitiesAdapter();
		for(String c : result){
			ctx.addCity(c);
		}
		ctx.notifyCityAdapter();
		ctx.setLoadingProgress(LoadingProgress.CITIES_LOADED);
	}

}
