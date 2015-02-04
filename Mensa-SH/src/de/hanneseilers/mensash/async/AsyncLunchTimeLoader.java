package de.hanneseilers.mensash.async;

import de.hanneseilers.mensash.MainActivity;
import de.hanneseilers.mensash.NavigationMensa;
import de.hanneseilers.mensash.R;
import de.mensa.sh.core.Mensa;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * {@link AsyncTask} to load lunch times of a {@link Mensa}.
 * @author Hannes Eilers
 *
 */
public class AsyncLunchTimeLoader extends AsyncTask<Void, Void, String> {

	private NavigationMensa mNavigationMensa;
	private ProgressBar pgbDialogLunchTime;
	private TextView txtDialogLunchTime;
	
	public AsyncLunchTimeLoader(NavigationMensa aNavigationMensa) {
		mNavigationMensa = aNavigationMensa;
	}
	
	@SuppressLint("InflateParams")
	@Override
	protected void onPreExecute() {
		
		// get widgets
		LinearLayout vLayout = (LinearLayout) MainActivity.getInstance().getLayoutInflater()
				.inflate(R.layout.dialog_lunch_time, null);
		pgbDialogLunchTime = (ProgressBar) vLayout.findViewById(R.id.pgbDialogLunchTime);
		txtDialogLunchTime = (TextView) vLayout.findViewById(R.id.txtDialogLunchTime);
		
		// set text
		txtDialogLunchTime.setText( R.string.loading );
		
		// create dialog
		AlertDialog.Builder vBuilder = new AlertDialog.Builder( MainActivity.getInstance() );
		vBuilder.setTitle( mNavigationMensa.getMensa().getName() )
			.setView(vLayout)
			.setPositiveButton( R.string.close, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {}
			});
		
		vBuilder.create().show();
		
	}
	
	@Override
	protected String doInBackground(Void... params) {
		if(mNavigationMensa != null ){
			return mNavigationMensa.getMensa().getLunchTime(true);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if( result != null ){
			pgbDialogLunchTime.setVisibility(View.GONE);
			txtDialogLunchTime.setText( Html.fromHtml(result) );
		}
		
		mNavigationMensa.setLoading(false);
	}

}
