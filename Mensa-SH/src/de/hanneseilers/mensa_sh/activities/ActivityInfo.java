package de.hanneseilers.mensa_sh.activities;

import de.hanneseilers.mensa_sh.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ActivityInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

}
