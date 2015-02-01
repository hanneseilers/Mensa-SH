package de.hanneseilers.mensash.async;

import java.util.List;

import de.hanneseilers.mensash.MainActivity;
import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

/**
 * {@link AsyncTask} to load {@link Meal}s of a {@link Mensa}.
 * @author Hannes Eilers
 *
 */
public class AsyncMealsLoader extends AsyncTask<Mensa, Void, List<Meal>> {

	@Override
	protected List<Meal> doInBackground(Mensa... params) {
		if( params.length > 0 ){
			return params[0].getMeals();
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
