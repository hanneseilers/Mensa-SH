package de.hanneseilers.mensa_sh;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
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
		Preference cmdClearCache = findPreference("CMD_CLEAR_CACHE");
		
		// add listener for clearing cache
		updateCachedFiles();
		cmdClearCache.setOnPreferenceClickListener( new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				CacheManager.clearAll(getActivity());
				updateCachedFiles();
				return false;
			}
		} );
	}
	
	/**
	 * Updates number of cached files
	 */
	private void updateCachedFiles(){
		// get numbe rof cached files and preference
		int cachedFiles = CacheManager.getCachedFiles(getActivity()).length;
		Preference prefStat = findPreference("PREF_STATUS");
		
		// set text to preference
		prefStat.setSummary( cachedFiles + " cached files" );
	}
	
}
