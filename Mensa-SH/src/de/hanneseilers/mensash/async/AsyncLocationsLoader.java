package de.hanneseilers.mensash.async;

import java.util.List;

import de.hanneseilers.mensash.MainActivity;
import de.mensa.sh.core.Mensa;

import android.os.AsyncTask;

public class AsyncLocationsLoader extends AsyncTask<Void, Void, List<Mensa>> {

	@Override
	protected List<Mensa> doInBackground(Void... params) {
		return Mensa.getLocations();
	}
	
	@Override
	protected void onPostExecute(List<Mensa> result) {
		if( result != null ){
			MainActivity.getInstance().setMensaLocations(result);
		}
	}
	
}
