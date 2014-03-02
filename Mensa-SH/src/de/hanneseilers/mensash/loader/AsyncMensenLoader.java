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
import android.util.Log;

public class AsyncMensenLoader extends AsyncTask<String, Integer, List<Mensa>> {

	private ActivityMain ctx;
	public static String cachedFileName = "saved_mensa";
	
	public AsyncMensenLoader(ActivityMain ctx){
		super();
		this.ctx = ctx;
	}
	
	/**
	 * Gets cities
	 */
	@Override
	protected List<Mensa> doInBackground(String... params) {
		// check if data is cache
		String ret;
		List<Mensa> retList = new ArrayList<Mensa>();

				
		if( (ret = CacheManager.readCachedFile(ctx, "mensen_"+params[0])) != null ){
			String retArray[] = ret.split(";");
			for( String mensa : retArray ){
				
				// get mensa data
				String mensaData[] = mensa.split(",");
				if(mensaData.length == 4){					
					// get offers
					List<String> offers = new ArrayList<String>();
					for( String o : mensaData[2].split("#") ){
						offers.add(o);
					}
					
					retList.add(
							new Mensa( params[0],
							mensaData[0],
							mensaData[1],
							offers,
							mensaData[3].trim()) );
				}
			}
		}
		
		if( retList.size() == 0 ){
			// cache file
			retList = Mensa.getLocations(params[0]);
			ret = "";
			for( Mensa m : retList ){
				ret += m.getName() + ","
						+ m.getLunchTime() + ",";
				for( String o : m.getOffers() ){
					ret += o + "#";
				}
				ret += "," + m.getMenueURL() + " ;";
			}
			CacheManager.writeChachedFile(ctx, "mensen_"+params[0], ret);
		}
		
		return retList;
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(List<Mensa> result) {
		ctx.setLocations(result);
		
		// get mensa names
		ctx.clearMensaAdapter();
		for(Mensa m : result){
			ctx.addMensa( m.getName(), m.getLunchTime() );
		}
		ctx.notifyMensaAdapter();
		int count = ctx.countMensenInList();
		ctx.getSpinnerMensa().setItemChecked(getSelection(result), true);
		ctx.setLoadingProgress(LoadingProgress.MENSEN_LOADED);
		
		// check if to programmaticaly load menue
		Log.d("Mensa", "count: " + count);
		if(count > 0){
			ctx.loadFirstMensaMenue();
		}
	}
	
	/**
	 * Returns the position to select
	 * @return
	 */
	private int getSelection(List<Mensa> result){
		// get settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		// check if to load position
		if( sharedPref.getBoolean("SAVE_LAST_MENSA", false) && ctx.isFirstSelection() ){
			String mensa;
			if( (mensa = CacheManager.readCachedFile(ctx, cachedFileName, false)) != null ){
				
				int i;
				for( i=0; i<result.size(); i++ ){
					if( result.get(i).getName().equals(mensa) ){
						break;
					}
				}				
				
				ctx.setFirstSelection(false);
				return i;
				
			}
		}
		
		return 0;
	}
	
}
