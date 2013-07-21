package de.hanneseilers.mensa_sh.activities;

import java.util.ArrayList;
import java.util.List;

import de.hanneseilers.mensa_sh.R;
import de.hanneseilers.mensa_sh.R.id;
import de.hanneseilers.mensa_sh.R.layout;
import de.hanneseilers.mensa_sh.R.menu;
import de.hanneseilers.mensa_sh.enums.LoadingProgress;
import de.hanneseilers.mensa_sh.loader.AsyncCitiesLoader;
import de.hanneseilers.mensa_sh.loader.AsyncMensenLoader;
import de.hanneseilers.mensa_sh.loader.AsyncMenueLoader;
import de.mensa.sh.core.Mensa;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ActivityMain extends Activity implements OnItemSelectedListener {
	
	private ArrayAdapter<String> adapterCity;
	private ArrayAdapter<String> adapterMensa;
	private WebView webView;
	private LinearLayout layoutLoading;
	
	private List<Mensa> locations = new ArrayList<Mensa>();
	
	private Spinner spinnerCity;
	private Spinner spinnerMensa;
	
	private LoadingProgress progress = LoadingProgress.INIT;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// get spinners and webview
		spinnerCity = (Spinner) findViewById(R.id.lstCity);
		spinnerMensa = (Spinner) findViewById(R.id.lstMensa);
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		layoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
		
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
		progress = aProgress;
		if( progress == LoadingProgress.MENUE_LOADED ){
			layoutLoading.setVisibility( View.GONE );
		}
		else {
			layoutLoading.setVisibility( View.VISIBLE );
		}
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
	private void loadMenue(String mensa){
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
			System.out.println(">selected city " + parent.getItemAtPosition(pos).toString());
			addMensen( parent.getItemAtPosition(pos).toString() );
			break;
		case R.id.lstMensa:
			System.out.println(">selected mensa " + parent.getItemAtPosition(pos).toString());
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

}
