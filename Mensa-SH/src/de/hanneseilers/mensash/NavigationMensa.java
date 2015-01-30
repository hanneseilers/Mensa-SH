package de.hanneseilers.mensash;

import de.hanneseilers.mensash.async.AsyncLunchTimeLoader;
import de.mensa.sh.core.Mensa;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NavigationMensa extends LinearLayout implements
	OnClickListener{
	
	private Mensa mMensa;
	private TextView mTextView;
	private ImageView mImageView;
	private ProgressBar mProgressBar;
	
	public NavigationMensa(Context context) {
		super(context);
		
		// get view
		View vView = MainActivity.getInstance().getLayoutInflater()
				.inflate(R.layout.navigation_mensa, this, false);		
		addView(vView);
		
		// get widgets
		mTextView = (TextView) findViewById(R.id.txtMensaName);
		mImageView = (ImageView) findViewById(R.id.imgMensaLunchTime);
		mProgressBar = (ProgressBar) findViewById(R.id.pgbNavigationMensaLoading);
		
		// add listener
		mImageView.setOnClickListener(this);
	}
	
	public synchronized void setMensa(Mensa aMensa){
		mMensa = aMensa;
		if( mMensa != null ){
			mTextView.setText( mMensa.getName() );
		}
	}
	
	public Mensa getMensa(){
		return mMensa;
	}
	
	public TextView getTextView(){
		return mTextView;
	}
	
	public void setLoading(boolean aLoading){
		if( aLoading ){
			mImageView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if( mMensa != null && mProgressBar.getVisibility() != View.VISIBLE ){
			setLoading(true);
			new AsyncLunchTimeLoader().execute(new NavigationMensa[]{this});
		}
	}

}
