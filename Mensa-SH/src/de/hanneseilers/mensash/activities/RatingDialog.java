package de.hanneseilers.mensash.activities;

import de.hanneseilers.mensash.R;
import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

/**
 * Class for generating rating dialog
 * @author Hannes Eilers
 *
 */
public class RatingDialog extends DialogFragment {
	
	private Mensa mensa = null;
	private Meal meal = null;
	
	public RatingDialog(Mensa mensa, Meal meal){
		setMensa(mensa);
		setMeal(meal);
	}
	
	/**
	 * Create dialog
	 * @return
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view = inflater.inflate(R.layout.dialog_rating, null);
		TextView lblRating = (TextView) view.findViewById(R.id.lblRating);
        RatingBar pgbRating = (RatingBar) view.findViewById(R.id.pgbRating);
		
		builder.setView( view );
		builder.setTitle(meal.getMealName());
        builder.setPositiveButton( getString(R.string.txtSend), new addRatingListener(view) )
        .setNegativeButton( getString(R.string.txtCancel), new DialogInterface.OnClickListener() {
        	
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
            
        });
        
        lblRating.setText("Bewertung: 0/5");
        pgbRating.setOnRatingBarChangeListener( new ratingChangedListener(lblRating) );
		
		return builder.create();		
	}

	/**
	 * @return the mensa
	 */
	public Mensa getMensa() {
		return mensa;
	}

	/**
	 * @param mensa the mensa to set
	 */
	public void setMensa(Mensa mensa) {
		this.mensa = mensa;
	}

	/**
	 * @return the meal
	 */
	public Meal getMeal() {
		return meal;
	}

	/**
	 * @param meal the meal to set
	 */
	public void setMeal(Meal meal) {
		this.meal = meal;
	}
	
	/**
	 * AsyncTask to send rating to database
	 * @author Hannes Eilers
	 *
	 */
	private class sendRatingTask extends AsyncTask<Object, Void, Boolean>{

		private View view = null;
		
		@Override
		protected Boolean doInBackground(Object... params) {			
			boolean ret = false;
			
			if( params.length > 3 ){
				Mensa mensa = (Mensa) params[0];
				Meal meal = (Meal) params[1];
				int rating = (Integer) params[2];	
				view = (View) params[3];
				
				ret = mensa.addRating( meal, rating, "", ActivityMain.getUniqueDeviceHash(view.getContext()) ); 				
			}
			
			return ret;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			String msg = "Bewertung nicht möglich!";
			if(result){
				msg  = "Bewertung gesendet.";
			}
			
			// Send rating
    		Toast toast = Toast.makeText( view.getContext(), msg, Toast.LENGTH_SHORT );
    		toast.show();
		}
		
	}
	
	/**
	 * Listener class for adding rating
	 * @author Hannes Eilers
	 *
	 */
	private class addRatingListener implements OnClickListener{

		private View view = null;
		
		public addRatingListener(View view){
			super();
			this.view = view;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			RatingBar pgbRating = (RatingBar) view.findViewById(R.id.pgbRating);
    		int rating = (int) pgbRating.getRating();  
    		
    		(new sendRatingTask()).execute( new Object[]{mensa, meal, rating, view} );    		
    		
		}
		
	}
	
	/**
	 * Listener class for rating bar changes
	 * @author Hannes Eilers
	 *
	 */
	private class ratingChangedListener implements OnRatingBarChangeListener{

		private TextView label = null;
		
		public ratingChangedListener(TextView label) {
			super();
			this.label = label;
		}
		
		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
    		String iRating = Integer.toString((int) rating);
			label.setText("Bewertung: " + iRating + "/5");
		}
		
	}

}
