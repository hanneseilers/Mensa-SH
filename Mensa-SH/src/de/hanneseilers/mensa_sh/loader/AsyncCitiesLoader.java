package de.hanneseilers.mensa_sh.loader;

import java.util.List;

import de.hanneseilers.mensa_sh.Menue;
import de.mensa.sh.core.Mensa;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Class for async loading of cities
 * @author hannes
 *
 */
public class AsyncCitiesLoader extends AsyncTask<Void, Void, List<String>> {
	
	private ProgressDialog progressBar;
	private Context ctx;
	
	public AsyncCitiesLoader(Context ctx){
		super();
		this.ctx = ctx;
	}
	
	@Override
	protected void onPreExecute() {	
		progressBar = ProgressDialog.show(ctx, "loading", "Lade verfügbare Städe");
		progressBar.setCancelable(true);
				
        super.onPreExecute();
	}
	
	/**
	 * Gets cities
	 */
	@Override
	protected List<String> doInBackground(Void... params) {
		System.out.println("load cities");
		return Mensa.getCities();
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(List<String> result) {
		Menue.adapterCity.clear();
		for(String c : result){
			Menue.adapterCity.add( c );
		}
		Menue.adapterCity.notifyDataSetChanged();
		
		progressBar.dismiss();
	}

}
