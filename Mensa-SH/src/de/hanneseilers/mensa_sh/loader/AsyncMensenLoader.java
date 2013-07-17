package de.hanneseilers.mensa_sh.loader;

import java.util.List;

import de.hanneseilers.mensa_sh.Menue;
import de.mensa.sh.core.Mensa;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class AsyncMensenLoader extends AsyncTask<String, Void, List<Mensa>> {

	private ProgressDialog progressBar;
	private Menue ctx;
	
	public AsyncMensenLoader(Menue ctx){
		super();
		this.ctx = ctx;
	}
	
	@Override
	protected void onPreExecute() {	
		progressBar = ProgressDialog.show(ctx, "loading", "Lade verfügbare Mensen");
		progressBar.setCancelable(true);
				
        super.onPreExecute();
	}
	
	/**
	 * Gets cities
	 */
	@Override
	protected List<Mensa> doInBackground(String... params) {
		System.out.println("load mensen of city " + params[0]);
		return Mensa.getLocations(params[0]);
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(List<Mensa> result) {
		Menue.locations = result;
		int count = Menue.adapterMensa.getCount();
		
		// get mensa names
		Menue.adapterMensa.clear();
		for(Mensa m : result){
			Menue.adapterMensa.add( m.getName() );
		}
		Menue.adapterMensa.notifyDataSetChanged();
		if(count > 0){
			ctx.loadFirstMensaMenue();
		}
		
		
		progressBar.dismiss();
	}
	
}
