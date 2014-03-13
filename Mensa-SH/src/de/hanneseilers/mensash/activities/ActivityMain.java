package de.hanneseilers.mensash.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.DrawerLayout;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;

import de.hanneseilers.mensash.MenuAdapter;
import de.hanneseilers.mensash.MenuFragment;
import de.hanneseilers.mensash.MenueFragmentPagerAdapter;
import de.hanneseilers.mensash.R;
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

import android.widget.ImageView;
import android.widget.SimpleAdapter;


public class ActivityMain extends Activity implements MenuFragment.Callback {
	
	private static final int RATING_RESULT_CODE = 0;
	public static final String preferenceSaveLastMensa = "SAVE_LAST_MENSA";
	public static final String preferenceLastMensa = "LAST_MENSA";
	public static final String preferenceLastCity = "LAST_CITY";
	
	private SimpleAdapter adapterCity;
	private SimpleAdapter adapterMensa;
	
	private List<String> cities = new ArrayList<String>();
	private List<Mensa> mensen = new ArrayList<Mensa>();
	
	private boolean firstSelection = true;
	
	private ArrayList<MenuFragment> menuFragments;	
	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawer;
    private ListView mDrawerMensaList;
    private ListView mDrawerCitiesList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout layoutMenuCity;
    private LinearLayout layoutMenuMensa;
    private ImageView imgExpandCity;
    private ImageView imgExpandMensa;
    
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    private ArrayList<HashMap<String,String>> drawerMensaItemsList = new ArrayList<HashMap<String,String>>();
    private ArrayList<HashMap<String,String>> drawerCitiesItemsList = new ArrayList<HashMap<String,String>>();
    
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
		updateAppRevision();
		
		setContentView(R.layout.activity_main);
		
		// get drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerCitiesList = (ListView) findViewById(R.id.left_city_drawer_list);
        mDrawerMensaList = (ListView) findViewById(R.id.left_mensa_drawer_list);  
        
        // get menu heads
 		layoutMenuCity = (LinearLayout) findViewById(R.id.layoutMenuCity);
 		layoutMenuMensa = (LinearLayout) findViewById(R.id.layoutMenuMensa);
 		imgExpandCity = (ImageView) layoutMenuCity.findViewById(R.id.imgExpandCity);
 		imgExpandMensa = (ImageView) layoutMenuMensa.findViewById(R.id.imgExpandMensa);
 		
 		// add listeners to menu headers
 		layoutMenuCity.setOnClickListener( new View.OnClickListener() {			
 			@Override
 			public void onClick(View v) {
 				if( mDrawerCitiesList.getVisibility() == View.VISIBLE ){
 					mDrawerCitiesList.setVisibility(View.GONE);
 					imgExpandCity.setImageResource(R.drawable.ic_close);
 				}
 				else{
 					mDrawerCitiesList.setVisibility(View.VISIBLE);
 					imgExpandCity.setImageResource(R.drawable.ic_expand);
 				}
 			}
 		} );
 		
 		layoutMenuMensa.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if( mDrawerMensaList.getVisibility() == View.VISIBLE ){
					mDrawerMensaList.setVisibility(View.GONE);
					imgExpandMensa.setImageResource(R.drawable.ic_close);
 				}
 				else{
 					mDrawerMensaList.setVisibility(View.VISIBLE);
 					imgExpandMensa.setImageResource(R.drawable.ic_expand);
 				}
			}
		} );
		
		// add resources to adapters
		adapterCity = new SimpleAdapter(
				this, drawerCitiesItemsList, R.layout.drawer_list_city_item,
				new String[] { "txtName" },
				new int[] { R.id.txtName } );
				
		adapterMensa = new SimpleAdapter(
				this, drawerMensaItemsList, R.layout.drawer_list_mensa_item,
				new String[] { "txtName","txtInfo" },
				new int[] { R.id.txtName, R.id.txtInfo } );
		
		// Set the adapter for the drawer list views
		mDrawerCitiesList.setAdapter(adapterCity);
        mDrawerMensaList.setAdapter(adapterMensa);
        
        // Set the list's click listeners
        mDrawerCitiesList.setOnItemClickListener( new DrawerCityItemClickListener() );
        mDrawerMensaList.setOnItemClickListener( new DrawerMensaItemClickListener() );
        
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
		
		// Setup tabs
		// ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mMenueFragmentPagerAdapter =
                new MenueFragmentPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMenueFragmentPagerAdapter);
        
        menuFragments = new ArrayList<MenuFragment>();
        
        // adding menu fragments
        for( int i = 0; i <= 4; i++) {
        	menuFragments.add( (MenuFragment) mMenueFragmentPagerAdapter.addItem() );
    		menuFragments.get(i).adapterMeals = new MenuAdapter(this, menuFragments.get(i).listMeals );
        }
        
        // Bind the widget to the adapter
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setIndicatorColorResource(R.color.orange);
        tabs.setViewPager(mViewPager);
		
		for (MenuFragment frag : menuFragments) {
			frag.adapterMeals.notifyDataSetChanged();
		}
		
		if(savedInstanceState != null) {
			mTitle = savedInstanceState.getCharSequence("title");
			getSupportActionBar().setTitle(mTitle);
		}
		
		// load cities	
		new AsyncCitiesLoader(this).execute();
//		addMensen( sharedPref.getString("city", "Kiel") );
		
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
	 * Closes menu
	 */
	public void closeMenu(){
		mDrawerLayout.closeDrawer(mDrawer);
	}
	
	/**
	 * Addas a city to list
	 * @param aName
	 */
	public void addCity(String aName){
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("txtName", aName);
		drawerCitiesItemsList.add(hm);
		adapterCity.notifyDataSetChanged();
	}
	
	/**
	 * Clears city adapter
	 */
	public void clearCitiesAdapter(){
		drawerCitiesItemsList.clear();
		notifyCityAdapter();
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
		drawerMensaItemsList.add(hm);
		notifyMensaAdapter();
	}
	
	/**
	 * Clears mensa adapter
	 */
	public void clearMensaAdapter(){
		drawerMensaItemsList.clear();
		notifyMensaAdapter();
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
	
	/**
	 * @return Number of cities in list
	 */
	public int countCitiesInList(){
		return adapterCity.getCount();
	}
	
	/**
	 * Create meal with error message
	 */
	public void setErrorMealList() {
		for (MenuFragment fragment:menuFragments) {
			fragment.listMeals.clear();
			fragment.adapterMeals.notifyDataSetChanged();
			fragment.setMessage( getResources().getString(R.string.info_error_meals) );
		}
	}
	
	public void setNoMealList() {
		for (MenuFragment fragment:menuFragments) {
			fragment.listMeals.clear();
			fragment.adapterMeals.notifyDataSetChanged();
			fragment.setMessage( getResources().getString(R.string.info_no_meals) );
		}
	}
	
	/**
	 * Add meals
	 * @param meals
	 */
	public void setMealList(List<Meal> meals) {
		// clear menu
		for(MenuFragment fragment:menuFragments) {
			fragment.listMeals.clear();
		}

		// add meals
		int day = -1;
		for (Meal meal:meals) {
			day = meal.getDay();
			menuFragments.get(day).setMessage(null);
			menuFragments.get(day).listMeals.add(meal);
		}
		
		// notify adapters about changes
		for(MenuFragment fragment:menuFragments) {
			fragment.adapterMeals.notifyDataSetChanged();
		}
		
		try{
			mMenueFragmentPagerAdapter.notifyDataSetChanged();
		}catch (IllegalStateException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the available mensen for that city
	 * @param city
	 */
	private void addMensen(String city){
		new AsyncMensenLoader(this).execute(city);
	}
	
	/**
	 * Loads a menue of a mensa
	 * @param mensa
	 */
	private void loadMenue(Mensa mensa){
		setSelectedMensa(mensa);
		
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
		if( mDrawerMensaList.getCount() > 0 ){
			selectMensaDrawerItem(0);
			loadMenue( mensen.get(0) );
		}
	}


	/**
	 * @return the mensen
	 */
	public List<Mensa> getMensen() {
		return mensen;
	}


	/**
	 * @param mensen the mensen to set
	 */
	public void setMensen(List<Mensa> mensen) {
		this.mensen = mensen;
	}
	
	/**
	 * @return the cities
	 */
	public List<String> getCities(){
		return cities;
	}
	
	/**
	 * @param cities the cities to set
	 */
	public void setCities(List<String> cities){
		this.cities = cities;
	}


	/**
	 * @return the spinnerMensa
	 */
	public ListView getSpinnerMensa() {
		return mDrawerMensaList;
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
	protected void onPause() {
		super.onPause();
		// get settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		// save last mensa
		if( sharedPref.getBoolean(ActivityMain.preferenceSaveLastMensa, true) ){
			
			Mensa mensa = getSelectedMensa();
			String city = getSelectedCity();
			
			if( city != null && mensa != null ){
				sharedPref.edit().putString(preferenceLastMensa, mensa.getName()).commit();
				sharedPref.edit().putString(preferenceLastCity, city).commit();
			}
			
		}
	}
	
	/**
	 * @return Currently selected city or {@code null} if no city selected
	 */
	public String getSelectedCity(){
		if( getSelectedMensa() != null ){
			return getSelectedMensa().getCity();
		}
		
		return null;
	}
	
	/**
	 * @return Currently selected mensa or {@code null} if no mensa selected
	 */
	public Mensa getSelectedMensa(){
		return selectedMensa;
	}
	
	/**
	 * Sets the selected mensa
	 * @param mensa
	 */
	private void setSelectedMensa(Mensa mensa){
		selectedMensa = mensa;
	}

	@Override
	public void showMealDetails(Meal meal) {
		Gson gson = new Gson();
		Intent i = new Intent(this,DetailActivity.class);
		i.putExtra("Meal", gson.toJson(meal));
		i.putExtra("Mensa", gson.toJson(selectedMensa));
		startActivityForResult(i, RATING_RESULT_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == RATING_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
            	Mensa mensa = new Gson().fromJson(data.getExtras().getString("Mensa"), Mensa.class);
            	new AsyncMenueLoader(this).execute(mensa);
            }
        }
    }
	
	/**
	 * Class for handling click on mensa menu item
	 */
	private class DrawerMensaItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
	    	selectMensaDrawerItem(position);
	    	closeMenu();
	    }
	}
	
	/**
	 * Class for handling click on city menu item
	 */
	private class DrawerCityItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
	    	selectCitiyDrawerItem(position);
	    	mDrawerCitiesList.setVisibility(View.GONE);
	    }
	}

	/**
	 * Selects mensa menu item
	 * @param position
	 */
	public void selectMensaDrawerItem(int position) {				
	    // check if position is out of bounds
		if( drawerMensaItemsList.size()-1 < position ){
			position = drawerMensaItemsList.size()-1;
		}
		
		// Highlight the selected item, update the title, and close the drawer
	    mDrawerMensaList.setItemChecked(position, true);
	    setTitle(drawerMensaItemsList.get(position).get("txtName"));
	    loadMenue( mensen.get(position) );
	}
	
	/**
	 * Selects city menu iem
	 * @param position
	 */
	public void selectCitiyDrawerItem(int position){
		if( drawerCitiesItemsList.size()-1 < position ){
			position = drawerCitiesItemsList.size()-1;
		}
		
		// Highlight the selected item, update the title, and close the drawer
	    mDrawerCitiesList.setItemChecked(position, true);
	    setTitle(drawerCitiesItemsList.get(position).get("txtName"));
	    
	    // show mensen of city
	    clearMensaAdapter();
	    addMensen( cities.get(position) );
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
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawer);
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

			return( version != prefVersion );
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Updates saved app revision in prefrences to revision of app package
	 */
	private void updateAppRevision(){
		try{
			
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
	
			// get manifest version code and preferences version code
			int version = pInfo.versionCode;		
			
			// update version code in preferences
			sharedPref.edit().putInt( "APP_VERSION_CODE", version ).commit();
			
		} catch (NameNotFoundException e){
			e.printStackTrace();
		}
	}
	
}
