package de.hanneseilers.mensash;

import java.util.List;
import java.util.Map;

import de.mensa.sh.core.Mensa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
		
		// set listener
		lblNavigationCity.setOnClickListener(this);
		lblNavigationMensa.setOnClickListener(this);
		
		return vView;
	}
	
	public synchronized void updateMenu(Map<String, List<Mensa>> aLocations){
		for( String vCity : aLocations.keySet() ){
			System.out.println("------------");
			System.out.println(vCity);
			
			for( Mensa vMensa : aLocations.get(vCity) ){
				System.out.println( vMensa.getName() );
			}
		}
	}
	
	public void setDrawerShadow(DrawerLayout aDrawerLayout){
		// set drawer shadow
		aDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	}

	@Override
	public void onClick(View v) {
		LinearLayout vContainer = null;
		if( v == lblNavigationCity ){
			vContainer = divNavigationCity;
		} else if( v == lblNavigationMensa ){
			vContainer = divNavigationMensa;
		}
		
		if( vContainer != null ){
			if( vContainer.getVisibility() == View.GONE ){
				vContainer.setVisibility(View.VISIBLE);
				((TextView) v).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_collapse, 0);
			} else {
				vContainer.setVisibility(View.GONE);
				((TextView) v).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_expand, 0);
			}
		}
	}
	
}
