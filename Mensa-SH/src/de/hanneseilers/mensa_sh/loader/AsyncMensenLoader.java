package de.hanneseilers.mensa_sh.loader;

import java.util.List;

import de.hanneseilers.mensa_sh.Menue;
import de.hanneseilers.mensa_sh.enums.LoadingProgress;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

public class AsyncMensenLoader extends AsyncTask<String, Integer, List<Mensa>> {

	private Menue ctx;
	
	public AsyncMensenLoader(Menue ctx){
		super();
		this.ctx = ctx;
	}
	
	/**
	 * Gets cities
	 */
	@Override
	protected List<Mensa> doInBackground(String... params) {
		return Mensa.getLocations(params[0]);
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(List<Mensa> result) {
		ctx.setLocations(result);
		int count = ctx.countMensenInList();
		
		// get mensa names
		ctx.clearMensaAdapter();
		for(Mensa m : result){
			ctx.addMensa( m.getName() );
		}
		ctx.notifyMensaAdapter();
		ctx.setLoadingProgress(LoadingProgress.MENSEN_LOADED);
		
		// check if to programmaticaly load menue
		if(count > 0){
			ctx.loadFirstMensaMenue();
		}
	}
	
}
