package de.hanneseilers.mensash.async;

import de.hanneseilers.mensash.MainActivity;
import de.hanneseilers.mensash.R;
import de.mensa.sh.core.Meal;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * {@link AsyncTask} for loading meal rating.
 * @author H. Eilers
 *
 */
public class AsyncRatingsLoader extends AsyncTask<Meal, Void, Integer> {

	private View mMealView;
	private boolean mForceUpdate;
	
	/**
	 * Constructor
	 * @param aMealView	{@link View} of meals layout
	 * @param aForceUpdate	Set {@code true} to force online update, {@code false} otherwise.
	 */
	public AsyncRatingsLoader(View aMealView, boolean aForceUpdate) {
		mMealView = aMealView;
		mForceUpdate = aForceUpdate;
	}
	
	@Override
	protected void onPreExecute() {
		// get widgets
		RatingBar vRating = (RatingBar) mMealView.findViewById(R.id.ratMealRating);
		ProgressBar vLoading = (ProgressBar) mMealView.findViewById(R.id.pgbMealLoading);
		TextView vText = (TextView) mMealView.findViewById(R.id.txtMealClickToRate);
		
		// show loading
		vRating.setVisibility(View.GONE);
		vLoading.setVisibility(View.VISIBLE);
		vText.setVisibility(View.GONE);
	}
	
	@Override
	protected Integer doInBackground(Meal... arg0) {
		if( arg0.length > 0 && mMealView != null ){
			Meal vMeal = (Meal) arg0[0];
			return MainActivity.getInstance().getMenuTableFragment().getMensa()
					.getRating(vMeal, mForceUpdate);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		
		// get widgets
		RatingBar vRating = (RatingBar) mMealView.findViewById(R.id.ratMealRating);
		ProgressBar vLoading = (ProgressBar) mMealView.findViewById(R.id.pgbMealLoading);
		TextView vText = (TextView) mMealView.findViewById(R.id.txtMealClickToRate);
		
		// show result
		vLoading.setVisibility( View.GONE );
		if( result != null && result >= 0 && mMealView != null ){
			int rating = result.intValue();
			
			vRating.setRating( (float) rating );
			vRating.setVisibility( View.VISIBLE );
		} else {
			vText.setVisibility(View.VISIBLE);
		}
		
	}

}
