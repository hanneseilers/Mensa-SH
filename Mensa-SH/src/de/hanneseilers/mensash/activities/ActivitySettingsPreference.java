package de.hanneseilers.mensash.activities;

import de.hanneseilers.mensash.SettingsPreference;
import android.app.Activity;
import android.os.Bundle;

public class ActivitySettingsPreference extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// add preferences
		getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsPreference()).commit();
	}
	
}
