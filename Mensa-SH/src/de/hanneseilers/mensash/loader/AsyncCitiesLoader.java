package de.hanneseilers.mensash.loader;

import java.util.ArrayList;
import java.util.List;

import de.hanneseilers.mensash.CacheManager;
import de.hanneseilers.mensash.activities.ActivityMain;
import de.hanneseilers.mensash.enums.LoadingProgress;
import de.mensa.sh.core.Mensa;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

/**
 * Class for async loading of cities
 * @author hannes
 *
 */
public class AsyncCitiesLoader extends AsyncTask<Void, Integer, List<String>> {
	
	private ActivityMain ctx;
	public static String cachedFileName = "saved_city";
	
	public AsyncCitiesLoader(ActivityMain ctx){
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

		if( (ret = CacheManager.readCachedFile(ctx, "cities")) != null ){
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
			CacheManager.writeChachedFile(ctx, "cities", ret);
		}
		
		return retList;
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(List<String> result) {
		ctx.setCities(result);
		
		// get cities names
		ctx.clearCitiesAdapter();
		for( String city : result ){
			ctx.addCity(city);
		}
		ctx.notifyCityAdapter();
		ctx.setLoadingProgress(LoadingProgress.CITIES_LOADED);
		
		// check if to programmaticaly load menue
		if(ctx.countCitiesInList() > 0){
			ctx.selectCitiyDrawerItem(getSelection(result));
		}
	}
	
	/**
	 * Returns the position to select
	 * @return
	 */
	private int getSelection(List<String> result){
		// get settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		// load preset city on first loading
		if( ctx.isFirstSelection() && sharedPref.getBoolean(ActivityMain.preferenceSaveLastMensa, true)  ){
			String city = sharedPref.getString(ActivityMain.preferenceLastCity, null);
			if( city != null ){
				return result.indexOf(city);
			}
		}
		
		return 0;
	}

}
