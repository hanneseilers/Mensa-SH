package de.hanneseilers.mensash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hanneseilers.mensash.async.AsyncLocationsLoader;
import de.mensa.sh.core.Cache;
import de.mensa.sh.core.Mensa;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends FragmentActivity {
	
	private static MainActivity INSTANCE = null;
	
	private NavigationDrawerFragment mNavigationDrawer;
	private MenuTableFragment mMenuTableFragment;
	
	private Map<String, List<Mensa>> mLocations = new HashMap<String, List<Mensa>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		INSTANCE = this;

		// set caching settings
		Cache.setFileWriterClass( new AndroidFileWriter() );
		
		// set navigation background
		FragmentManager vFragmentManager = getSupportFragmentManager();
		mNavigationDrawer = (NavigationDrawerFragment) vFragmentManager 
				.findFragmentById(R.id.navigation_drawer);
		mNavigationDrawer.setDrawerShadow( (DrawerLayout) findViewById(R.id.drawer_layout) );
		
		// set main fragment
		mMenuTableFragment = new MenuTableFragment();
		vFragmentManager.beginTransaction()
			.replace(R.id.container, mMenuTableFragment)
			.commit();
		
		// load locations
		(new AsyncLocationsLoader()).execute();
	}
	
	private NavigationDrawerFragment getNavigationDrawerFragment(){
		return mNavigationDrawer;
	}
	
	
	public MenuTableFragment getMenuTableFragment(){
		return mMenuTableFragment;
	}
	
	
	public void setMensaLocations(List<Mensa> aLocations){		
		// set new mensa locations
		synchronized (mLocations) {
			mLocations.clear();
			if( aLocations != null ){
				
				// add locations to map
				for( Mensa vMensa : aLocations ){
					if( !mLocations.containsKey(vMensa.getCity()) ){
						mLocations.put(vMensa.getCity(), new ArrayList<Mensa>());
					}
					
					mLocations.get(vMensa.getCity()).add(vMensa);
				}
				
			}
		}
		
		// update navigation
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				getNavigationDrawerFragment().updateMenu( getMensaLocations() );	
			}
		});
			
	}
	
	public Map<String, List<Mensa>> getMensaLocations(){
		synchronized (mLocations) {
			return new HashMap<String, List<Mensa>>(mLocations);
		}
	}
	
	
	
	
	public static MainActivity getInstance(){
		return INSTANCE;
	}

}
