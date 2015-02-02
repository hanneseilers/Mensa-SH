package de.hanneseilers.mensash;

import java.util.List;

import de.hanneseilers.mensash.async.AsyncRatingsLoader;
import de.mensa.sh.core.Meal;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuFragment extends Fragment {
	
	private LinearLayout vRootView;
	
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
		vRootView = new LinearLayout( getActivity() );
		vRootView.setOrientation(LinearLayout.VERTICAL);
		updateMeals();
		return vRootView;
	}
	
	/**
	 * Update meals of this day.
	 */
	private synchronized void updateMeals(){
		// clear meals
		vRootView.removeAllViews();
		
		// create content
		if( mMeals != null ){
			
			boolean even = false;
			for( Meal vMeal : mMeals ){
				if( vMeal.getDay() == mDay ){
					
					// get widgets
					LinearLayout vMealView = (LinearLayout) getActivity().getLayoutInflater()
							.inflate(R.layout.fragment_menu_meal, vRootView, false);
					TextView mMealName = (TextView) vMealView.findViewById(R.id.txtMealName);
					TextView mMealPrice = (TextView) vMealView.findViewById(R.id.txtMealPrice);
					
					// set content
					mMealName.setText( vMeal.getMealName() );
					mMealPrice.setText( vMeal.getPrice() );
					
					if( !even ){
						vMealView.setBackgroundColor( getResources().getColor(R.color.highlight_gray_light) );
					}					
					even = !even;
					
					// add to root view
					vRootView.addView(vMealView);
					
					// start ratings loader
					new AsyncRatingsLoader().execute(new Object[]{vMeal, vMealView});
					
				}
			}
		}
	}
	
	public void setMeals(List<Meal> aMeals){
		mMeals = aMeals;
		updateMeals();
	}
	
}
