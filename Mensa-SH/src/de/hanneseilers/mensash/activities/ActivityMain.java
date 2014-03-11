package de.hanneseilers.mensash.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.ArrayAdapter;
import org.holoeverywhere.widget.DrawerLayout;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;

import de.hanneseilers.mensash.MenuAdapter;
import de.hanneseilers.mensash.MenuFragment;
import de.hanneseilers.mensash.DetailActivity;
import de.hanneseilers.mensash.MenueFragmentPagerAdapter;
import de.hanneseilers.mensash.R;
import de.hanneseilers.mensash.CacheManager;
import de.hanneseilers.mensash.enums.LoadingProgress;
import de.hanneseilers.mensash.loader.AsyncCitiesLoader;
import de.hanneseilers.mensash.loader.AsyncMensenLoader;
import de.hanneseilers.mensash.loader.AsyncMenueLoader;
import de.hanneseilers.mensash.versions.VersionHints;
import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;
import de.mensa.sh.core.Settings;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;

import android.support.v4.app.ActionBarDrawerToggle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.SimpleAdapter;


public class ActivityMain extends Activity implements MenuFragment.Callback {
	
	private ArrayAdapter<String> adapterCity;
	private SimpleAdapter adapterMensa;
	
	public List<Mensa> locations = new ArrayList<Mensa>();
	
	private boolean firstSelection = true;
	
	private ArrayList<MenuFragment> menuFragments;
	
	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawer;
    public ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<HashMap<String,String>> drawerItemsList = new ArrayList<HashMap<String,String>>();
    private Mensa selectedMensa;
	
	// When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
	MenueFragmentPagerAdapter mMenueFragmentPagerAdapter;
    ViewPager mViewPager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set url for mensa-sh-parser
		Settings.sh_mensa_db_api_url = "http://mensash.private-factory.de/api.php";
		
		// check if to show version hints
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);	
		boolean showHints = sharedPref.getBoolean("SHOW_VERSION_HINTS", false);

		if( showHints || checkIfUpdated() ){
			VersionHints.showAllHints(this);
		}
		
		setContentView(R.layout.activity_main);
		
		// get drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);
		
		// add resources to spinners
		adapterCity = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				new ArrayList<String>());
		adapterMensa = new SimpleAdapter(
				this, drawerItemsList, R.layout.drawer_list_item,
				new String[] { "txtName","txtInfo" },
				new int[] { R.id.txtName, R.id.txtInfo } );
		
		// Set the adapter for the drawer list view
        mDrawerList.setAdapter(adapterMensa);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
		
		// setup tabs
		// ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mMenueFragmentPagerAdapter =
                new MenueFragmentPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMenueFragmentPagerAdapter);
        
        menuFragments = new ArrayList<MenuFragment>();
        
        int i;
        for (i = 0; i <= 4; i++) {
        	menuFragments.add((MenuFragment) mMenueFragmentPagerAdapter.addItem());
    		menuFragments.get(i).adapterMeals = new MenuAdapter(this, menuFragments.get(i).listMeals );
        }
        
        // Bind the widget to the adapter
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
		
		for (MenuFragment frag:menuFragments) {
			frag.adapterMeals.notifyDataSetChanged();
		}
		
		if(savedInstanceState != null) {
			mTitle = savedInstanceState.getCharSequence("title");
			getSupportActionBar().setTitle(mTitle);
		}
		
		addMensen(sharedPref.getString("city", "Kiel"));
		
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menue, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }		
		// Handle item selection		
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, ActivitySettingsPreference.class));
			break;
		case R.id.action_info:
			startActivity( new Intent(this, ActivityInfo.class) );
			break;
		case R.id.disclaimer:
			VersionHints.showDisclaimer(this);
			break;
        default:
            return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	/**
	 * Addas a city to list
	 * @param aName
	 */
	public void addCity(String aName){
		adapterCity.add(aName);
	}
	
	/**
	 * Clears city adapter
	 */
	public void clearCitiesAdapter(){
		adapterCity.clear();
	}
	
	/**
	 * Notifies city adapter about change
	 */
	public void notifyCityAdapter(){
		adapterCity.notifyDataSetChanged();
	}
	
	
	/**
	 * Adds a mensa to list
	 * @param aName
	 */
	public void addMensa(String aName, String info){
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("txtName", aName);
		hm.put("txtInfo", info);
		drawerItemsList.add(hm);
		adapterMensa.notifyDataSetChanged();
	}
	
	/**
	 * Clears mensa adapter
	 */
	public void clearMensaAdapter(){
		drawerItemsList.clear();
		adapterMensa.notifyDataSetChanged();
	}
	
	/**
	 * Notifies mensa adapter about a change
	 */
	public void notifyMensaAdapter(){
		adapterMensa.notifyDataSetChanged();
	}
	
	/**
	 * @return Number of mensen in list
	 */
	public int countMensenInList(){
		return adapterMensa.getCount();
	}
	
	public void setErrorMealList() {
		for (MenuFragment fragment:menuFragments) {
			fragment.listMeals.clear();
			Meal error = new Meal();
			error.setMealName("Fehler");
			fragment.listMeals.add(error);
			fragment.adapterMeals.notifyDataSetChanged();
		}
	}
	
	public void setMealList(List<Meal> meals) {
		for(MenuFragment fragment:menuFragments) {
			fragment.listMeals.clear();
		}

		int day = -1;
		for (Meal meal:meals) {
			day = meal.getDay();
			menuFragments.get(day).listMeals.add(meal);
		}
		for(MenuFragment fragment:menuFragments) {
			fragment.adapterMeals.notifyDataSetChanged();
		}
		mMenueFragmentPagerAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Adds the available mensen for that city
	 * @param city
	 */
	private void addMensen(String city){
		//setLoadingProgress(LoadingProgress.INIT);
		new AsyncMensenLoader(this).execute(city);
	}
	
	/**
	 * Loads a menue of a mensa
	 * @param mensa
	 */
	private void loadMenue(Mensa mensa){
		selectedMensa = mensa;
		Calendar c = Calendar.getInstance(); 
		int today = c.get(Calendar.DAY_OF_WEEK)-2;
		if (today < 0 || today > 4) { //Weekend
			today = 0;
		}
		this.mViewPager.setCurrentItem(today, true);
		new AsyncMenueLoader(this).execute(mensa);
	}
	
	/**
	 * Loads the menue of the first mensa
	 */
	public void loadFirstMensaMenue(){
		if( mDrawerList.getCount() > 0 ){
			selectDrawerItem(0);
			loadMenue( locations.get(0) );
		}
	}


	/**
	 * @return the locations
	 */
	public List<Mensa> getLocations() {
		return locations;
	}


	/**
	 * @param locations the locations to set
	 */
	public void setLocations(List<Mensa> locations) {
		this.locations = locations;
	}


	/**
	 * @return the spinnerMensa
	 */
	public ListView getSpinnerMensa() {
		return mDrawerList;
	}


	/**
	 * @return the firstSelection
	 */
	public boolean isFirstSelection() {
		return firstSelection;
	}


	/**
	 * @param firstSelection the firstSelection to set
	 */
	public void setFirstSelection(boolean firstSelection) {
		this.firstSelection = firstSelection;
	}
	
	public static String getWeekdayString(int dayOfWeek) {
		String weekDay = "";
		if (0 == dayOfWeek) weekDay = "Montag";
	    else if (1 == dayOfWeek) weekDay = "Dienstag";
	    else if (2 == dayOfWeek) weekDay = "Mittwoch";
	    else if (3 == dayOfWeek) weekDay = "Donnerstag";
	    else if (4 == dayOfWeek) weekDay = "Freitag";
	    else if (5 == dayOfWeek) weekDay = "Samstag";
	    else if (6 == dayOfWeek) weekDay = "Sonntag";
		return weekDay;
	}
	
	/**
	 * Cleanup before activity is destroyed
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// get settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		// save selected city and mensa
		if( sharedPref.getBoolean("SAVE_LAST_MENSA", false) ){      
			String city = sharedPref.getString("city", "Kiel");	
			CacheManager.writeChachedFile(this, AsyncCitiesLoader.cachedFileName, city);
			
			if(mDrawerList.getCheckedItemPosition() < mDrawerList.getCount()) {
				String mensa = locations.get(mDrawerList.getCheckedItemPosition()).getName();
				CacheManager.writeChachedFile(this, AsyncMensenLoader.cachedFileName, mensa);
			}	
		}
	}


	@Override
	public void showDetails(Meal meal) {
		Gson gson = new Gson();
		Intent i = new Intent(this,DetailActivity.class);
		i.putExtra("Meal", gson.toJson(meal));
		i.putExtra("Mensa", gson.toJson(selectedMensa));
		startActivity(i);
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
	    	selectDrawerItem(position);
		    mDrawerLayout.closeDrawer(mDrawer);
	    }
	}

	public void selectDrawerItem(int position) {
	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(drawerItemsList.get(position).get("txtName"));
	    loadMenue( locations.get(position) );
	}

	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    getSupportActionBar().setTitle(mTitle);
	}

	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawer);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
    	savedInstanceState.putCharSequence("title", mTitle);
        
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

	public void setLoadingProgress(LoadingProgress mensenLoaded) {
		if (mensenLoaded == LoadingProgress.MENUE_LOADED) {
			for (MenuFragment fragment:menuFragments) {
				fragment.setProgressBar(false);
			}
		} else if (mensenLoaded == LoadingProgress.INIT) {
			for (MenuFragment fragment:menuFragments) {
				fragment.setProgressBar(true);
			}
		}
	}
	
	/**
     * @return Unique id for device
     */
    public static String getUniqueDeviceHash(Context context){
    	/*
    	 * Getting device ID is not used due to using android id.
    	 * But still available for fallbacks.
    	 */
//    	String hash = "";
//    	TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//    	return telephonyManager.getDeviceId();
    	
    	return android.provider.Settings.Secure.getString( context.getContentResolver(), 
    			android.provider.Settings.Secure.ANDROID_ID); 

    }
    
    /**
	 * @return True if app was updated
	 */
	private boolean checkIfUpdated(){		

		try {

			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			// get manifest version code and preferences version code
			int prefVersion = sharedPref.getInt("APP_VERSION_CODE", -1);		
			int version = pInfo.versionCode;

			if( version != prefVersion ){
				// update version code in preferences
				sharedPref.edit().putInt( "APP_VERSION_CODE", version ).commit();
				return true;
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}



		return false;
	}
}
