package de.hanneseilers.mensash.activities;

import java.util.ArrayList;
import java.util.List;

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
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityMain extends Activity implements OnItemSelectedListener {
	
	private ArrayAdapter<String> adapterCity;
	private ArrayAdapter<String> adapterMensa;
	private WebView webView;
	private WebSettings webSettings;
	private TextView txtLunchTime;
	private LinearLayout layoutLoading;
	private TextView txtInfo;
	
	private List<Mensa> locations = new ArrayList<Mensa>();
	
	private Spinner spinnerCity;
	private Spinner spinnerMensa;
	
	private LoadingProgress progress = LoadingProgress.INIT;
	private boolean firstSelection = true;
	
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// check if to show version hints
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean showRating = sharedPref.getBoolean("SHOW_VERSION_HINTS", false);
		if( showRating ){
			VersionHints.showAllHints(this);
		}
		
		// set urls for mensa-sh-parser
		Settings.sh_mensa_db_api_url = "http://mensash.private-factory.de/api.php";
		Settings.sh_mensa_rating_ico_full_url = "http://mensash.private-factory.de/img/star.png";
		Settings.sh_mensa_rating_ico_half_url = "http://mensash.private-factory.de/img/star_half.png";
		Settings.sh_mensa_rating_ico_empty_url = "http://mensash.private-factory.de/img/star_empty.png";
		Settings.sh_mensa_rating_ico_size = "16px";
		
		setContentView(R.layout.activity_main);
		
		// get spinners and webview
		spinnerCity = (Spinner) findViewById(R.id.lstCity);
		spinnerMensa = (Spinner) findViewById(R.id.lstMensa);
		txtLunchTime = (TextView) findViewById(R.id.txtLunchTime);
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(this, "Android");
		layoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
		txtInfo = (TextView) findViewById(R.id.txtInfo);
		
		// add resources to spinnes
		adapterCity = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				new ArrayList<String>());
		adapterMensa = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				new ArrayList<String>());
		
		spinnerCity.setAdapter(adapterCity);
		spinnerMensa.setAdapter(adapterMensa);
		
		addCities();
		
		spinnerCity.setOnItemSelectedListener(this);
		spinnerMensa.setOnItemSelectedListener(this);
		
	}
	
	
	/**
	 * Adds rating for a meal
	 * @param aMensa
	 * @param aMeal
	 */
    @JavascriptInterface
    public void addMealRating(String aMensa, String aMeal) { 
    	
    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean showRating = sharedPref.getBoolean("SHOW_RATING", false);
		
		if( showRating ){
	    	// get mensa and meal from serialzed objects
			Mensa mensa = Mensa.unserialize(aMensa);
			Meal meal = Meal.unserialize(aMeal);
			
			// show rating dialog	
			RatingDialog dialog = new RatingDialog(mensa, meal, this);
			dialog.show( getFragmentManager(), "Test" ); 
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
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menue, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
	public void addMensa(String aName){
		adapterMensa.add( aName );
	}
	
	/**
	 * Clears mensa adapter
	 */
	public void clearMensaAdapter(){
		adapterMensa.clear();
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
	 * Loads a website as html plain text string
	 * @param html
	 */
	public void loadWebsiteHtml(String html){
		webView.loadData(html, "text/html", "UTF-8");
		webView.reload();
	}
	
	/**
	 * Sets the loading progress
	 * @param aProgress
	 */
	public void setLoadingProgress(LoadingProgress aProgress){
		setLoadingProgress(aProgress, "loading...");
	}
	
	/**
	 * Sets the loading progress
	 * @param aProgress
	 * @param message
	 */
	public void setLoadingProgress(LoadingProgress aProgress, String message){
		progress = aProgress;
		txtInfo.setText(message);
		if( progress == LoadingProgress.MENUE_LOADED ){
			layoutLoading.setVisibility( View.GONE );
		}
		else {
			layoutLoading.setVisibility( View.VISIBLE );
		}
	}
	
	/**
	 * Sets the lunchtime
	 * @param lunchTime
	 */
	public void setLunchTime(String aLunchTime){
		final String lunchTime = aLunchTime;
		txtLunchTime.post( new Runnable() {			
			@Override
			public void run() {
				txtLunchTime.setText( "Mittagessen: " + lunchTime );				
			}
		} );
	}
	
	
	/**
	 * Adds cities to spinner list
	 */
	private void addCities(){
		setLoadingProgress(LoadingProgress.INIT);
		new AsyncCitiesLoader(this).execute();
	}
	
	/**
	 * Adds the available mensen for that city
	 * @param city
	 */
	private void addMensen(String city){
		setLoadingProgress(LoadingProgress.INIT);
		new AsyncMensenLoader(this).execute(city);
	}
	
	/**
	 * Loads a menue of a mensa
	 * @param mensa
	 */
	public void loadMenue(String mensa){
		setLoadingProgress(LoadingProgress.INIT);
		new AsyncMenueLoader(this).execute(mensa);
	}
	
	/**
	 * Loads the menue of the first mensa
	 */
	public void loadFirstMensaMenue(){
		if( spinnerMensa.getCount() > 0 ){
			setLoadingProgress(LoadingProgress.INIT);
			spinnerMensa.setSelection(0);
			loadMenue( spinnerMensa.getItemAtPosition(0).toString() );
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		// check which list was selected
		switch( parent.getId() ){
		case R.id.lstCity:
			addMensen( parent.getItemAtPosition(pos).toString() );
			break;
		case R.id.lstMensa:
			loadMenue( parent.getItemAtPosition(pos).toString() );
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}


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
	 * @return the spinnerCity
	 */
	public Spinner getSpinnerCity() {
		return spinnerCity;
	}


	/**
	 * @return the spinnerMensa
	 */
	public Spinner getSpinnerMensa() {
		return spinnerMensa;
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
			String city = spinnerCity.getSelectedItem().toString();
			String mensa = spinnerMensa.getSelectedItem().toString();
			
			CacheManager.writeChachedFile(this, AsyncCitiesLoader.cachedFileName, city);
			CacheManager.writeChachedFile(this, AsyncMensenLoader.cachedFileName, mensa);
		}
	}

}
