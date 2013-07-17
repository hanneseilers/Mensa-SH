package de.hanneseilers.mensa_sh;

import java.util.ArrayList;
import java.util.List;

import de.hanneseilers.mensa_sh.loader.AsyncCitiesLoader;
import de.hanneseilers.mensa_sh.loader.AsyncMensenLoader;
import de.hanneseilers.mensa_sh.loader.AsyncMenueLoader;
import de.mensa.sh.core.Mensa;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class Menue extends Activity implements OnItemSelectedListener {
	
	public static ArrayAdapter<String> adapterCity;
	public static ArrayAdapter<String> adapterMensa;
	public static WebView webView;
	
	public static List<Mensa> locations = new ArrayList<Mensa>();
	
	private Spinner spinnerCity;
	private Spinner spinnerMensa;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menue);
		
		// get spinners and webview
		spinnerCity = (Spinner) findViewById(R.id.lstCity);
		spinnerMensa = (Spinner) findViewById(R.id.lstMensa);
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		
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
	
	/**
	 * Adds cities to spinner list
	 */
	private void addCities(){
		new AsyncCitiesLoader(this).execute();
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
	private void loadMenue(String mensa){
		new AsyncMenueLoader(this).execute(mensa);
	}
	
	/**
	 * Loads the menue of the first mensa
	 */
	public void loadFirstMensaMenue(){
		if( spinnerMensa.getCount() > 0 ){
			System.out.println(">selected first item");
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

}
