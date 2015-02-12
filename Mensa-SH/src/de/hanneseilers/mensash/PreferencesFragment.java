package de.hanneseilers.mensash;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * {@link PreferenceFragment} to show app preferences.
 * @author Hannes Eilers
 *
 */
public class PreferencesFragment extends PreferenceFragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
