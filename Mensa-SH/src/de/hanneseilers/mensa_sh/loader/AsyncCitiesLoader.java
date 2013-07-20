package de.hanneseilers.mensa_sh.loader;

import java.util.List;

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
		return Mensa.getCities();
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
