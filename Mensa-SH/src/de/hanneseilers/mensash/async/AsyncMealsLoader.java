package de.hanneseilers.mensash.async;

import java.util.List;

import de.hanneseilers.mensash.MainActivity;
import de.hanneseilers.mensash.R;
import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

/**
 * {@link AsyncTask} to load {@link Meal}s of a {@link Mensa}.
 * @author Hannes Eilers
 *
 */
public class AsyncMealsLoader extends AsyncTask<Mensa, Void, List<Meal>> {

	private Mensa mMensa;
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<Meal> doInBackground(Mensa... params) {
		if( params.length > 0 ){
			mMensa = params[0];
			
			// get stored data
			Mensa vMensa = (Mensa) MainActivity.getInstance().getDataStorage()
					.getData( MainActivity.getInstance().getResources().getString(R.string.storage_mensa) );
			List<Meal> vMeals = (List<Meal>) MainActivity.getInstance().getDataStorage()
					.getData( MainActivity.getInstance().getResources().getString(R.string.storage_meals) );
			
			// check if to restore saved data
			if( vMensa != null && vMeals != null && vMensa.getMensaURL().equals(mMensa.getMensaURL()) ){
				System.out.println("restored meals");
				return vMeals;
			}
			
			vMeals = mMensa.getMeals();
			
			// save mensa and meals
			MainActivity.getInstance().getDataStorage()
				.addData( MainActivity.getInstance().getResources().getString(R.string.storage_mensa) , mMensa);
			MainActivity.getInstance().getDataStorage()
				.addData( MainActivity.getInstance().getResources().getString(R.string.storage_meals) , vMeals);
			
			return vMeals;
		}
			
		return null;
	}
	
	@Override
	protected void onPostExecute(List<Meal> result) {
		if( result != null ){
			MainActivity.getInstance().getMenuTableFragment().setMeals(result);	
		}
	}

}
