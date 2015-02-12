package de.hanneseilers.mensash;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PreferenceActivity extends Activity implements
	OnClickListener{

	private TextView txtPreferencesClose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		
		txtPreferencesClose = (TextView) findViewById(R.id.txtPreferencesClose);
		txtPreferencesClose.setOnClickListener(this);
		
		FragmentManager vFragmentManager = getFragmentManager();
		vFragmentManager.beginTransaction()
			.replace(R.id.preferencesContainer, new PreferencesFragment())
			.commit();
	}

	@Override
	public void onClick(View v) {
		finish();
	}
	
}
