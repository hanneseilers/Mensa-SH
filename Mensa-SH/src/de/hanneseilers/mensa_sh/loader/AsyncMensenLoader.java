package de.hanneseilers.mensa_sh.loader;

import java.util.ArrayList;
import java.util.List;

import de.hanneseilers.mensa_sh.CacheManager;
import de.hanneseilers.mensa_sh.Main_Activity;
import de.hanneseilers.mensa_sh.enums.LoadingProgress;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

public class AsyncMensenLoader extends AsyncTask<String, Integer, List<Mensa>> {

	private Main_Activity ctx;
	
	public AsyncMensenLoader(Main_Activity ctx){
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
