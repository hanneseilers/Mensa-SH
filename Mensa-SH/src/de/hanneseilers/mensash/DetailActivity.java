package de.hanneseilers.mensash;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.Toast;

import com.google.gson.Gson;

import de.hanneseilers.mensash.activities.ActivityMain;
import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class DetailActivity extends Activity {

	private Mensa mensa;
	private Meal meal;
	
	private RatingBar rating;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Gson gson = new Gson();
		Bundle extras = getIntent().getExtras();
		mensa = gson.fromJson(extras.getString("Mensa"), Mensa.class);
		meal = gson.fromJson(extras.getString("Meal"), Meal.class);
		
		TextView txtName = (TextView) findViewById(R.id.txtName);
	    TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
	    ImageView imgAlcohol = (ImageView) findViewById(R.id.imgAlcohol);
	    ImageView imgBeef = (ImageView) findViewById(R.id.imgBeef);
	    ImageView imgPork = (ImageView) findViewById(R.id.imgPork);
	    ImageView imgVegetarian = (ImageView) findViewById(R.id.imgVegetarian);
	    ImageView imgVegan = (ImageView) findViewById(R.id.imgVegan);
	    rating = (RatingBar) findViewById(R.id.ratingBar);
	    Button sendButton = (Button) findViewById(R.id.sendButton);
	    
	    txtName.setText(meal.getDate() + meal.getMealName());
	    txtInfo.setText(meal.getPrice());

	    imgAlcohol.setVisibility(meal.isAlc() ? View.VISIBLE : View.GONE);
	    imgBeef.setVisibility(meal.isCow() ? View.VISIBLE : View.GONE);
	    imgPork.setVisibility(meal.isPig() ? View.VISIBLE : View.GONE);
	    imgVegetarian.setVisibility(meal.isVegetarian() ? View.VISIBLE : View.GONE);
	    imgVegan.setVisibility(meal.isVegan() ? View.VISIBLE : View.GONE);
	    
	    if(meal.getRating() >= 0) {
	    	rating.setRating(meal.getRating());
	    }
	    
	    sendButton.setOnClickListener(new addRatingListener());
	}

	/**
	 * Set up the {@link android.app.ActionBar}
	 */
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Listener class for adding rating
	 * @author Hannes Eilers
	 *
	 */
	private class addRatingListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			
			view.setEnabled(false);
			
    		int ratingInt = (int) rating.getRating();  
    		
    		(new sendRatingTask()).execute( mensa, meal, ratingInt, view );    		
    		
		}
		
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
				int ratingInt = (Integer) params[2];	
				view = (View) params[3];
				
				String hash = ActivityMain.getUniqueDeviceHash(view.getContext());
				if(mensa==null) Log.d("Mensa", "mensa==null");
				if(hash==null) Log.d("Mensa", "hash==null");
				if(meal==null) Log.d("Mensa", "meal==null");
				try{
				ret = mensa.addRating( meal, ratingInt, "", hash ); 
				}catch(NullPointerException e){
					
				}
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
    		
    		view.setEnabled(true);
		}
		
	}

}
