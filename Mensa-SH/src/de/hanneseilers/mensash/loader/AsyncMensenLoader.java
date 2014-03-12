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
		ctx.setMensen(result);
		
		// get mensa names
		ctx.clearMensaAdapter();
		for(Mensa m : result){
			ctx.addMensa( m.getName(), m.getLunchTime() );
		}
		ctx.notifyMensaAdapter();
		ctx.setLoadingProgress(LoadingProgress.MENSEN_LOADED);
		
		// check if to programmaticaly load menue
		if(ctx.countMensenInList() > 0){
			if( result.size() == 1 ){
				ctx.selectMensaDrawerItem(0);
				ctx.closeMenu();
			}
			else{
				ctx.selectMensaDrawerItem(getSelection(result));
			}
		}
	}
	
	/**
	 * Returns the position to select
	 * @return
	 */
	private int getSelection(List<Mensa> result){		
		// get settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		String city = ctx.getSelectedCity();
		String savedCity = sharedPref.getString(ActivityMain.preferenceLastCity, null);
		boolean savelastMensa = sharedPref.getBoolean(ActivityMain.preferenceSaveLastMensa, false);
		
		// load saved mensa on first load
		if( (ctx.isFirstSelection() && savelastMensa  )
				|| (savelastMensa && city != null && savedCity != null && city.equals(savedCity)) ){
			
			// load last saved mensa
			String mensa = sharedPref.getString(ActivityMain.preferenceLastMensa, null);
			if( mensa != null ){				
				for( int i=0; i<result.size(); i++ ){
					if( result.get(i).getName().equals(mensa) ){
						return i;
					}
				}				
			}
			
		}
		
		return 0;
	}
	
}
