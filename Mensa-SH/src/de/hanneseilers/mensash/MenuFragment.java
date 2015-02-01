package de.hanneseilers.mensash;

import java.util.List;

import de.mensa.sh.core.Meal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class MenuFragment extends Fragment {
	
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
		LinearLayout vRootView = new LinearLayout( getActivity() );	
		
		// create content
		if( mMeals != null ){
			
			boolean even = false;
			for( Meal vMeal : mMeals ){
				if( vMeal.getDay() == mDay ){
					
					// get widgets
					LinearLayout vMealView = (LinearLayout) inflater.inflate(R.layout.fragment_menu_meal, container,
							false);
					TextView mMealName = (TextView) vMealView.findViewById(R.id.txtMealName);
					TextView mMealPrice = (TextView) vMealView.findViewById(R.id.txtMealPrice);
					RatingBar mRating = (RatingBar) vMealView.findViewById(R.id.ratMealRating);
					ProgressBar mLoading = (ProgressBar) vMealView.findViewById(R.id.pgbMealLoading);
					
					// set content
					mMealName.setText( vMeal.getMealName() );
					mMealPrice.setText( vMeal.getPrice() );
					
					if( even ){
						vMealView.setBackgroundColor( getResources().getColor(R.color.highlight_gray_light) );
					}					
					even = !even;
					
					// add to root view
					vRootView.addView(vMealView);
					
				}
			}
		}
		
		System.out.println("count: " + vRootView.getChildCount());
		
		return vRootView;
	}
	
}
