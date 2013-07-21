package de.hanneseilers.mensa_sh;

import android.app.Activity;
import android.os.Bundle;

public class SettingsPreferenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// add preferences
		getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsPreference()).commit();
	}
	
}
