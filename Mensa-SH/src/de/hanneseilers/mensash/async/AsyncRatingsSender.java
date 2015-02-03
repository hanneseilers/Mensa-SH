package de.hanneseilers.mensash.async;

import de.hanneseilers.mensash.MainActivity;
import de.hanneseilers.mensash.R;
import de.mensa.sh.core.Meal;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * {@link AsyncTask} for submitting meal rating.
 * @author H. Eilers
 *
 */
public class AsyncRatingsSender extends AsyncTask<Integer, Void, Boolean> {

	private Meal mMeal;
	private View mMealView;
	
	/**
	 * Constructor
	 * @param aMeal		{@link Meal} to submit rating for.
	 * @param aMealView	{@link View} of meal layout.
	 */
	public AsyncRatingsSender(Meal aMeal, View aMealView) {
		mMeal = aMeal;
		mMealView = aMealView;
	}
	
	@Override
	protected void onPreExecute() {
		// show loading
		ProgressBar vLoading = (ProgressBar) mMealView.findViewById(R.id.pgbMealLoading);		
		vLoading.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected Boolean doInBackground(Integer... arg0) {
		if( arg0.length > 0 && mMeal != null ){
			return MainActivity.getInstance().getMenuTableFragment().getMensa()
				.addRating(mMeal, arg0[0].intValue(), "",
						MainActivity.getInstance().getDeviceHash());
		}
		
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		ProgressBar vLoading = (ProgressBar) mMealView.findViewById(R.id.pgbMealLoading);		
		vLoading.setVisibility(View.GONE);
		
		if( result ){
			
			Toast.makeText( MainActivity.getInstance(),
					R.string.rating_submit_success, Toast.LENGTH_SHORT ).show();
			new AsyncRatingsLoader(mMealView, true).execute(new Meal[]{mMeal});
			
		} else {
			
			Toast.makeText( MainActivity.getInstance(),
					R.string.rating_submit_success, Toast.LENGTH_SHORT ).show();
			
		}
	}

}
