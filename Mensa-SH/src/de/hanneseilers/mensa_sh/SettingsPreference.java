package de.hanneseilers.mensa_sh;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Class for preferences
 * @author Hannes Eilers
 *
 */
public class SettingsPreference extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// load preferences from rescoure
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
