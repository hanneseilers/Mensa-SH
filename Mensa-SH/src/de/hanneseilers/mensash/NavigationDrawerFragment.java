package de.hanneseilers.mensash;

import java.util.List;
import java.util.Map;

import de.mensa.sh.core.Mensa;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements
	OnClickListener{
	
	private TextView lblNavigationCity;
	private TextView lblNavigationMensa;
	private LinearLayout divNavigationCity;
	private LinearLayout divNavigationMensa;
	private ProgressBar pgbNavigationLoading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		// get layout
		ScrollView vView = (ScrollView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);	
		
		// get widgets
		lblNavigationCity = (TextView) vView.findViewById(R.id.lblNavigationCity);
		lblNavigationMensa = (TextView) vView.findViewById(R.id.lblNavigationMensa);
		divNavigationCity = (LinearLayout) vView.findViewById(R.id.divNavigationCity);
		divNavigationMensa = (LinearLayout) vView.findViewById(R.id.divNavigationMensa);
		pgbNavigationLoading = (ProgressBar) vView.findViewById(R.id.pgbNavigationLoading);
		
		// set listener
		lblNavigationCity.setOnClickListener(this);
		lblNavigationMensa.setOnClickListener(this);
		
		// set drawer toggle
		
		
		return vView;
	}
	
	/**
	 * Updates locations.
	 * @param aLocations	{@link Map} of {@link List}s of {@link Mensa} locations, sorted by city names.
	 */
	@SuppressLint("InflateParams")
	public synchronized void updateLocations(Map<String, List<Mensa>> aLocations){
		divNavigationCity.removeAllViews();
		
		// add all cities			
		for( String vCity : aLocations.keySet() ){
			TextView vTextView	 = (TextView) getActivity().getLayoutInflater()
					.inflate(R.layout.navigation_city, null);
			vTextView.setText(vCity);
			vTextView.setOnClickListener(this);
			divNavigationCity.addView(vTextView);
		}
		
		// show elements
		lblNavigationCity.setVisibility(View.VISIBLE);
		lblNavigationMensa.setVisibility(View.VISIBLE);
		pgbNavigationLoading.setVisibility(View.GONE);
		
		// select first added mensa
		TextView vTextView = (TextView) divNavigationCity.getChildAt(0);
		if( vTextView != null ){
			showCityList(true);
			onClick(vTextView);
		}
		
	}
	
	/**
	 * Sets shadow of a {@link DrawerLayout}.
	 * @param aDrawerLayout	{@link DrawerLayout} to set shadow.
	 */
	public void setDrawerShadow(DrawerLayout aDrawerLayout){
		// set drawer shadow
		aDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	}
	
	/**
	 * Loads mensa locatios of a city.
	 * @param aCity	{@link String} of city name.
	 */
	private void loadCityMensas(String aCity){
		divNavigationMensa.removeAllViews();
		Map<String, List<Mensa>> vLocations = MainActivity.getInstance().getMensaLocations();
		if( vLocations.containsKey(aCity) ){
			for( Mensa vMensa : vLocations.get(aCity) ){
				NavigationMensa vView = new NavigationMensa(getActivity());
				vView.setMensa(vMensa);
				divNavigationMensa.addView(vView);
			}
		}
		
		// open mensa list
		showMensaList(true);
		showCityList(false);
	}
	
	/**
	 * Shows or hides list of cities.
	 * @param aShow	Set {@code true} to show list of cities, {@code false} otherwise.
	 */
	private void showCityList(boolean aShow){
		if( aShow ){
			lblNavigationCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
			divNavigationCity.setVisibility(View.VISIBLE);
		} else {
			lblNavigationCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
			divNavigationCity.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Shows or hides list of mensas.
	 * @param aShow	Set {@code true} to show mensas, {@code false} otherwise.
	 */
	private void showMensaList(boolean aShow){
		if( aShow ){
			lblNavigationMensa.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
			divNavigationMensa.setVisibility(View.VISIBLE);
		} else {
			lblNavigationMensa.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
			divNavigationMensa.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Set selected {@link NavigationMensa}.
	 * @param aNavigationMensa	Selected {@link NavigationMensa}.
	 */
	public void selectMensa(NavigationMensa aNavigationMensa){		
		// select mensa
		for( int i=0; i < divNavigationMensa.getChildCount(); i++ ){			
			NavigationMensa vNavigationMensa = (NavigationMensa) divNavigationMensa.getChildAt(i);
			
			if( vNavigationMensa == aNavigationMensa ){
				vNavigationMensa.setSelected(true);
			} else {
				vNavigationMensa.setSelected(false);
			}
		}
		
		// set mensa for menu
		MainActivity.getInstance().getMenuTableFragment().setMensa( aNavigationMensa.getMensa() );
		MainActivity.getInstance().closeDrawer();
	}

	@Override
	public void onClick(View v) {
		
		if( v == lblNavigationCity ){
			
			if( divNavigationCity.getVisibility() == View.GONE ){
				showCityList(true);
			} else {
				showCityList(false);
			}
			
		} else if( v == lblNavigationMensa ){
			
			if( divNavigationMensa.getVisibility() == View.GONE ){
				showMensaList(true);
			} else {
				showMensaList(false);
			}
			
		} else if(v != null  && v instanceof TextView ) {
			
			// get text view and load mensas of city
			TextView vTextView = (TextView) v;
			loadCityMensas( vTextView.getText().toString() );
			
			// select city
			for( int i=0; i < divNavigationCity.getChildCount(); i++ ){
				
				TextView vChild = (TextView) divNavigationCity.getChildAt(i);
				if( vChild == vTextView ){
					vChild.setSelected(true);
				} else {
					vChild.setSelected(false);
				}
				
			}
			
		}
		
	}
	
}
