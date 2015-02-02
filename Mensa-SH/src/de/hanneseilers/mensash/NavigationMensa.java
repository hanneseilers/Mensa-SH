package de.hanneseilers.mensash;

import de.hanneseilers.mensash.async.AsyncLunchTimeLoader;
import de.mensa.sh.core.Mensa;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * {@link LinearLayout} representing a navigation menu entry for a mensa.
 * @author Hannes Eilers
 *
 */
public class NavigationMensa extends LinearLayout implements
	OnClickListener{
	
	private Mensa mMensa;
	
	private View mRootView;
	private TextView mTextView;
	private ImageView mImageView;
	
	public NavigationMensa(Context context) {
		super(context);
		
		// get view
		mRootView = MainActivity.getInstance().getLayoutInflater()
				.inflate(R.layout.navigation_mensa, this, false);		
		addView(mRootView);
		
		// get widgets
		mTextView = (TextView) findViewById(R.id.txtMensaName);
		mImageView = (ImageView) findViewById(R.id.imgMensaLunchTime);
		
		// add listener
		mImageView.setOnClickListener(this);
		mTextView.setOnClickListener(this);
	}
	
	/**
	 * Set {@link Mensa} of this {@link NavigationMensa}.
	 * @param aMensa	{@link Mensa} to set.
	 */
	public synchronized void setMensa(Mensa aMensa){
		mMensa = aMensa;
		if( mMensa != null ){
			mTextView.setText( mMensa.getName() );
		}
	}
	
	/**
	 * @return	{@link Mensa} of this {@link NavigationMensa}.
	 */
	public Mensa getMensa(){
		return mMensa;
	}
	
	/**
	 * @return {@link TextView} of mensa name.
	 */
	public TextView getTextView(){
		return mTextView;
	}
	
	/**
	 * Sets if mensa is selected
	 * @param aSelected	Set {@code true} to highlight {@link NavigationMensa} as selected, {@code false} otherwise.
	 */
	public void setSelected(boolean aSelected){
		mRootView.setSelected(aSelected);
		mTextView.setSelected(aSelected);
	}
	
	/**
	 * Sets if to show loading progress bar.
	 * @param aLoading	Set {@code true} to show loading progress bar, {@code false} otherwise.
	 */
	public void setLoading(boolean aLoading){
		if( aLoading ){
			mImageView.setVisibility(View.GONE);
		} else {
			mImageView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if( mMensa != null ){
			
			// load lunch time
			if( v == mImageView && mImageView.getVisibility() == View.VISIBLE ){
				setLoading(true);
				new AsyncLunchTimeLoader(this).execute();
			} else if( v == mTextView ){
				MainActivity.getInstance().getNavigationDrawerFragment().selectMensa(this);
			}			
			
		}
	}

}
