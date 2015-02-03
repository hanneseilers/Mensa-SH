package de.hanneseilers.mensash;

import java.util.List;

import de.hanneseilers.mensash.async.AsyncRatingsLoader;
import de.hanneseilers.mensash.async.AsyncRatingsSender;
import de.mensa.sh.core.Meal;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class MenuFragment extends Fragment implements
	OnClickListener{
	
	private static View sMealView;
	private static Meal sMeal;
	private static RatingBar sRatRating;
	
	private LinearLayout mRootView;
	
	private List<Meal> mMeals;
	private int mDay;
	
	public MenuFragment(){}
	
	public MenuFragment(int position, List<Meal> aMeals) {
		mDay = position;
		mMeals = aMeals;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {		
		// create root view
		mRootView = new LinearLayout( getActivity() );
		mRootView.setOrientation(LinearLayout.VERTICAL);
		updateMeals();
		return mRootView;
	}
	
	/**
	 * Update meals of this day.
	 */
	private synchronized void updateMeals(){
		// clear meals		
		mRootView.removeAllViews();
		
		// create content
		if( mMeals != null ){
			
			boolean even = false;
			for( Meal vMeal : mMeals ){
				if( vMeal.getDay() == mDay ){
					
					// get widgets
					LinearLayout vMealView = (LinearLayout) getActivity().getLayoutInflater()
							.inflate(R.layout.fragment_menu_meal, mRootView, false);
					TextView vMealName = (TextView) vMealView.findViewById(R.id.txtMealName);
					TextView vMealPrice = (TextView) vMealView.findViewById(R.id.txtMealPrice);
					LinearLayout vDivMealInfo = (LinearLayout) vMealView.findViewById(R.id.divMealInfo);
					
					// set content
					vMealName.setText( vMeal.getMealName() );
					vMealPrice.setText( vMeal.getPrice() );
					
					// add icons
					ImageView vImage;
					if( vMeal.isAlc() ){
						vImage = new ImageView(getActivity());
						vImage.setImageDrawable( getResources().getDrawable(R.drawable.ic_alkohol) );
						vDivMealInfo.addView( vImage );						
					}
					
					if( vMeal.isCow() ){
						vImage = new ImageView(getActivity());
						vImage.setImageDrawable( getResources().getDrawable(R.drawable.ic_rind) );
						vDivMealInfo.addView( vImage );						
					}
					
					if( vMeal.isPig() ){
						vImage = new ImageView(getActivity());
						vImage.setImageDrawable( getResources().getDrawable(R.drawable.ic_schwein) );
						vDivMealInfo.addView( vImage );						
					}
					
					if( vMeal.isVegetarian() ){
						vImage = new ImageView(getActivity());
						vImage.setImageDrawable( getResources().getDrawable(R.drawable.ic_vegetarisch) );
						vDivMealInfo.addView( vImage );						
					}
					
					if( vMeal.isVegan() ){
						vImage = new ImageView(getActivity());
						vImage.setImageDrawable( getResources().getDrawable(R.drawable.ic_vegan) );
						vDivMealInfo.addView( vImage );						
					}
					
					// set background color
					if( !even ){
						vMealView.setBackgroundColor( getResources().getColor(R.color.highlight_gray_light) );
					}					
					even = !even;
					
					// add listener
					vMealView.setOnClickListener(this);
					
					// add to root view
					mRootView.addView(vMealView);
					
					// start ratings loader
					new AsyncRatingsLoader(vMealView, false).execute(new Meal[]{vMeal});
					
				}
			}
		}
	}
	
	/**
	 * Set available meals.
	 * @param aMeals	{@link List} of {@link Meal}s.
	 */
	public void setMeals(List<Meal> aMeals){
		mMeals = aMeals;
		updateMeals();
	}
	
	/**
	 * Gets meal by it's name.
	 * @param aMealName	{@link String} of meals name.
	 * @return			{@link Meal}, or {@code null if not found}.
	 */
	public Meal getMeal(String aMealName){
		for( Meal vMeal : mMeals ){
			if( vMeal.getMealName().equals(aMealName) ){
				return vMeal;
			}
		}
		
		return null;
	}
	
	@SuppressLint("InflateParams")
	private void showRatingDialog(View aMealView, Meal aMeal){
		// set static variables
		sMealView = aMealView;
		sMeal = aMeal;
		
		// get widgets
		LinearLayout vLayout = (LinearLayout) MainActivity.getInstance().getLayoutInflater()
				.inflate(R.layout.dialog_rating, null);
		
		RatingBar ratMealRating = (RatingBar) sMealView.findViewById(R.id.ratMealRating);
		TextView txtRatingMeal = (TextView) vLayout.findViewById(R.id.txtRatingMeal);
		TextView txtRatingDisclaimer = (TextView) vLayout.findViewById(R.id.txtRatingDisclaimer);
		sRatRating = (RatingBar) vLayout.findViewById(R.id.ratRating);
		
		// set content
		txtRatingMeal.setText( sMeal.getMealName() );
		sRatRating.setRating( ratMealRating.getRating() );
		
		// set disclaimer listener
		txtRatingDisclaimer.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				showDisclaimer();
			}
		});
		
		// create dialog
		AlertDialog.Builder vBuilder = new AlertDialog.Builder( MainActivity.getInstance() );
		vBuilder.setTitle( getString(R.string.rating) )
			.setView(vLayout)
			.setPositiveButton( R.string.submit, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new AsyncRatingsSender(sMeal, sMealView)
						.execute( new Integer[]{(int) sRatRating.getRating()} );
				}
			})
			.setNegativeButton( R.string.cancel, null);
		
		vBuilder.create().show();
	}
	
	/**
	 * Shows disclaimer dialog
	 */
	private void showDisclaimer(){
		// create dialog
		AlertDialog.Builder vBuilder = new AlertDialog.Builder( MainActivity.getInstance() );
		vBuilder.setTitle( getString(R.string.disclaimer) )
			.setMessage( getString(R.string.disclaimer_text) )
			.setPositiveButton( R.string.close, null );		
		vBuilder.create().show();
	}

	@Override
	public void onClick(View vMealView) {
		TextView vMealName = (TextView) vMealView.findViewById(R.id.txtMealName);
		Meal vMeal = getMeal( vMealName.getText().toString() );
		showRatingDialog(vMealView, vMeal);
	}
	
}
