package de.hanneseilers.mensash;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hanneseilers.mensash.async.AsyncLocationsLoader;
import de.mensa.sh.core.Cache;
import de.mensa.sh.core.Mensa;
import de.mensa.sh.core.Settings;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.view.Gravity;

public class MainActivity extends FragmentActivity {
	
	private static MainActivity INSTANCE = null;
	
	private DrawerLayout mDrawerLayout;	
	private NavigationDrawerFragment mNavigationDrawer;
	private MenuTableFragment mMenuTableFragment;
	
	private Map<String, List<Mensa>> mLocations = new HashMap<String, List<Mensa>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		INSTANCE = this;
		
		// set ratings api url
		Settings.sh_mensa_db_api_url = "http://mensash.private-factory.de/api.php";
		
		// clear cached file
		clearCachedFiles(false);

		// set caching settings
		Cache.setFileWriterClass( new AndroidFileWriter() );
		
		// get navigation drawer and background
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		FragmentManager vFragmentManager = getSupportFragmentManager();
		mNavigationDrawer = (NavigationDrawerFragment) vFragmentManager 
				.findFragmentById(R.id.navigation_drawer);
		mNavigationDrawer.setDrawerShadow( mDrawerLayout );
		
		// set main fragment
		mMenuTableFragment = new MenuTableFragment();
		vFragmentManager.beginTransaction()
			.replace(R.id.container, mMenuTableFragment)
			.commit();
		
		// load locations
		(new AsyncLocationsLoader()).execute();
	}
	
	/**
	 * Deletes cached files once every monday.
	 * @param aForce	Set {@code true} to force deletion of cached file.
	 */
	private void clearCachedFiles(boolean aForce){
		
		// set calendar to monday
		Calendar vCalendar = Calendar.getInstance();
		vCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		long vCacheClearedLast = getSettings().getLong(
				getString(R.string.settings_cache_cleared_last),
				System.currentTimeMillis() );
		
		// clear cache
		File vDirectory = getFilesDir();
		if( vDirectory.isDirectory()
				&& (aForce || vCacheClearedLast < vCalendar.getTimeInMillis()) ){
			
			for( File vFile : vDirectory.listFiles() ){
				vFile.delete();
			}
				
			// set time to settings
			getSettings().edit().putLong(
					getString(R.string.settings_cache_cleared_last),
					vCalendar.getTimeInMillis()).commit();
		}
		
		
	}
	
	public SharedPreferences getSettings(){
		return getPreferences(Context.MODE_PRIVATE);
	}
	
	/**
	 * Opens/shows navigation drawer.
	 */
	public void openDrawers(){
		mDrawerLayout.openDrawer(Gravity.START);
	}
	
	/**
	 * Closes navigation drawer.
	 */
	public void closeDrawer(){
		mDrawerLayout.closeDrawers();
	}
	
	/**
	 * @return	{@link NavigationDrawerFragment} of navigation drawer.
	 */
	public NavigationDrawerFragment getNavigationDrawerFragment(){
		return mNavigationDrawer;
	}
	
	/**
	 * @return	{@link MenuTableFragment} of menu fragment.
	 */
	public MenuTableFragment getMenuTableFragment(){
		return mMenuTableFragment;
	}
	
	/**
	 * Set available mensa locations.
	 * @param aLocations	{@link List} of {@link Mensa} locations.
	 */
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
				if( getMensaLocations() != null && getMensaLocations().size() > 0 ){
					// upodate locations
					getNavigationDrawerFragment().updateLocations( getMensaLocations() );
				} else {
					// load locations
					(new AsyncLocationsLoader()).execute();
				}
			}
		});
			
	}
	
	/**
	 * @return	{@link Map} of {@link List}s with {@link Mensa} locations, sorted by city names.
	 */
	public Map<String, List<Mensa>> getMensaLocations(){
		synchronized (mLocations) {
			return new HashMap<String, List<Mensa>>(mLocations);
		}
	}
	
	/**
	 * @return	{@link String} of device hash.
	 */
	public String getDeviceHash(){
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + Secure.getString(getContentResolver(),Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    
	    return deviceUuid.toString();
	}
	
	
	
	/**
	 * @return	{@link MainActivity} instance.
	 */
	public static MainActivity getInstance(){
		return INSTANCE;
	}

}
