package de.hanneseilers.mensash.activities;

import org.holoeverywhere.app.Activity;

import de.hanneseilers.mensash.SettingsPreference;
import android.os.Bundle;

public class ActivitySettingsPreference extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// add preferences
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsPreference()).commit();
	}
	
}
