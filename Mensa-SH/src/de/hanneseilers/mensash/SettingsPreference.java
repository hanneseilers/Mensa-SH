package de.hanneseilers.mensash;

import org.holoeverywhere.preference.Preference;
import org.holoeverywhere.preference.Preference.OnPreferenceClickListener;
import org.holoeverywhere.preference.PreferenceFragment;

import de.hanneseilers.mensash.R;
import android.os.Bundle;




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
		int cacheSize = CacheManager.getCacheSize(getActivity());
		Preference prefStat = findPreference("PREF_STATUS");
		
		// set text to preference
		prefStat.setSummary( cachedFiles + " cached files (" + cacheSize + "kB)" );
	}
	
}
