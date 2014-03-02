package de.hanneseilers.mensash.loader;

import java.util.Hashtable;
import java.util.List;

import de.hanneseilers.mensash.CacheManager;
import de.hanneseilers.mensash.activities.ActivityMain;
import de.hanneseilers.mensash.enums.LoadingProgress;
import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;
import android.os.AsyncTask;

public class AsyncMenueLoader extends AsyncTask<Mensa, Integer, List<Meal>> {

	private ActivityMain ctx;
	private Mensa selectedMensa;
	
	public AsyncMenueLoader(ActivityMain ctx){
		super();
		this.ctx = ctx;
	}
	
	@Override
	protected void onPreExecute() {
		ctx.setLoadingProgress(LoadingProgress.INIT);
	}
	
	/**
	 * Gets cities
	 */
	@Override
	protected List<Meal> doInBackground(Mensa... params) {
		
		List<Meal> meals = params[0].getMeals();
		Hashtable<String, Integer> ratings = params[0].getRatings(meals);
		for(Meal meal:meals) {
			String key = meal.getKey();
			int rating = -1;
			if( ratings.containsKey(key) ){
				rating = ratings.get(key);						
			}
			meal.setRating(rating);
		}
		return meals;				
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(List<Meal> result) {
		
		// check if result is empty
		if( result == null ){
			ctx.setErrorMealList();
		}
		
		// load menue
		//ctx.selectDrawerItem(ctx.locations.indexOf(selectedMensa));
		ctx.setMealList(result);
		ctx.setLoadingProgress(LoadingProgress.MENUE_LOADED);

	}
	
}
