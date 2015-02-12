package de.hanneseilers.mensash.async;

import java.util.ArrayList;
import java.util.List;

import de.hanneseilers.mensash.MainActivity;
import de.hanneseilers.mensash.R;
import de.mensa.sh.core.Mensa;

import android.os.AsyncTask;

/**
 * {@link AsyncTask} to load available locations.
 * @author Hannes Eilers
 *
 */
public class AsyncLocationsLoader extends AsyncTask<Void, Void, List<Mensa>> {
	
	@Override
	@SuppressWarnings("unchecked")
	protected List<Mensa> doInBackground(Void... params) {
		// get stored data
		List<Mensa> vLocations = (ArrayList<Mensa>) MainActivity.getInstance().getDataStorage()
				.getData( MainActivity.getInstance().getString(R.string.storage_locations) );
		
		// check if to restore data
		if( vLocations != null ){
			return vLocations;
		}
		
		vLocations = Mensa.getLocations();
		
		// save locations
		MainActivity.getInstance().getDataStorage()
			.addData( MainActivity.getInstance().getString(R.string.storage_locations), vLocations );
		
		return vLocations;
	}
	
	@Override
	protected void onPostExecute(List<Mensa> result) {
		if( result != null ){
			MainActivity.getInstance().setMensaLocations(result);
			MainActivity.getInstance().getMenuTableFragment().setLoading(false);
		}
	}
	
}
