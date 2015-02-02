package de.hanneseilers.mensash.async;

import de.hanneseilers.mensash.MainActivity;
import de.hanneseilers.mensash.R;
import de.mensa.sh.core.Meal;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;

public class AsyncRatingsLoader extends AsyncTask<Object, Void, Integer> {

	private LinearLayout mMealLayout;
	
	@Override
	protected Integer doInBackground(Object... arg0) {
		if( arg0.length > 1 ){
			mMealLayout = (LinearLayout) arg0[1];
			Meal vMeal = (Meal) arg0[0];
			return MainActivity.getInstance().getMenuTableFragment().getMensa()
					.getRating(vMeal);
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		
		// get widgets
		RatingBar vRating = (RatingBar) mMealLayout.findViewById(R.id.ratMealRating);
		ProgressBar vLoading = (ProgressBar) mMealLayout.findViewById(R.id.pgbMealLoading);
		
		// show result
		vLoading.setVisibility( View.GONE );
		if( result != null && result >= 0 && mMealLayout != null ){
			int rating = result.intValue();
			
			vRating.setRating( (float) rating );
			vRating.setVisibility( View.VISIBLE );
		}
		
	}

}
