package de.hanneseilers.mensash.async;

import de.hanneseilers.mensash.MainActivity;
import de.hanneseilers.mensash.NavigationMensa;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.text.Html;

public class AsyncLunchTimeLoader extends AsyncTask<NavigationMensa, Void, String> {

	private NavigationMensa mNavigationMensa;
	
	@Override
	protected String doInBackground(NavigationMensa... params) {
		if( params.length > 0 ){
			mNavigationMensa = params[0];
			return mNavigationMensa.getMensa().getLunchTime(true);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if( result != null ){
			
			AlertDialog.Builder vBuilder = new AlertDialog.Builder( MainActivity.getInstance() );
			vBuilder.setMessage( Html.fromHtml(result) )
				.setTitle( mNavigationMensa.getMensa().getName() );
			
			vBuilder.create().show();
			
		}
		
		mNavigationMensa.setLoading(false);
	}

}
