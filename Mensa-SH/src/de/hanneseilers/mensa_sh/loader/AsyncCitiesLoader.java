package de.hanneseilers.mensa_sh.loader;

import java.util.ArrayList;
import java.util.List;

import de.hanneseilers.mensa_sh.CacheManager;
import de.hanneseilers.mensa_sh.Menue;
import de.hanneseilers.mensa_sh.enums.LoadingProgress;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

/**
 * Class for async loading of cities
 * @author hannes
 *
 */
public class AsyncCitiesLoader extends AsyncTask<Void, Integer, List<String>> {
	
	private Menue ctx;
	
	public AsyncCitiesLoader(Menue ctx){
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

		System.out.println("> load cities");
		if( (ret = CacheManager.readCachedFile(ctx, "cache_cities")) != null ){
			System.out.println("> cached");
			String retArray[] = ret.split("\n");
			for( String city : retArray ){
				retList.add(city);
			}
			if( retList.size() == 0 ){
				System.out.println("> no cities found");
				retList = Mensa.getCities();
			}
		}
		else{			
			// cache file
			System.out.println("> not cached");
			retList = Mensa.getCities();
			ret = "";
			for( String city : retList ){
				ret += city + "\n";
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
