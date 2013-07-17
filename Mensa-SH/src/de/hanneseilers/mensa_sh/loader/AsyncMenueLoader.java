package de.hanneseilers.mensa_sh.loader;

import de.hanneseilers.mensa_sh.Menue;
import de.mensa.sh.core.Mensa;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncMenueLoader extends AsyncTask<String, Void, String> {

	private ProgressDialog progressBar;
	private Context ctx;
	
	public AsyncMenueLoader(Context ctx){
		super();
		this.ctx = ctx;
	}
	
	@Override
	protected void onPreExecute() {	
		progressBar = ProgressDialog.show(ctx, "loading", "Lade Speiseplan");
		progressBar.setCancelable(true);
				
        super.onPreExecute();
	}
	
	/**
	 * Gets cities
	 */
	@Override
	protected String doInBackground(String... params) {
		
		System.out.println("load menue of mensa " + params[0]);
		
		// find mensa with params name
		for( Mensa m : Menue.locations ){
			if( m.getName().equals(params[0]) ){
				return m.getMenueAsHtml();
			}
		}
		
		return "Kein Speiseplan gefunden!";
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(String result) {
		
		// check if result is empty
		if( result == "" ){
			result = "<b>Kein Speiseplan gefunden!</b>";
		}
		
		// load menue
		Menue.webView.loadData(result, "text/html", "UTF-8");
		Menue.webView.reload();
		
		progressBar.dismiss();
	}
	
}
