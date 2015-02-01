package de.hanneseilers.mensash;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hanneseilers.mensash.async.AsyncMealsLoader;
import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuTableFragment extends Fragment implements
	OnPageChangeListener{

	private LinearLayout divLoading;
	private ImageView imgLoading;
	private Animation animLoading;
	private ViewPager pager;
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	private Mensa mMensa;
	private List<Meal> mMeals;
	private Calendar mCalendar = Calendar.getInstance();
	private SimpleDateFormat mDateFormat;
	private AsyncMealsLoader mMealsLoader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// set date format
		mDateFormat = new SimpleDateFormat( getResources().getString(R.string.date_format_pattern), Locale.getDefault() );
		
		// get view
		View vView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		// get widgets
		divLoading = (LinearLayout) vView.findViewById(R.id.divLoading);
		imgLoading = (ImageView) vView.findViewById(R.id.imgLoading);		
		animLoading = AnimationUtils.loadAnimation(getActivity(), R.anim.loading_burger);
		pager = (ViewPager) vView.findViewById(R.id.pager);
		
		// set pager adapter
		mSectionsPagerAdapter = new SectionsPagerAdapter( getFragmentManager() );
		pager.setAdapter(mSectionsPagerAdapter);
		pager.setOnPageChangeListener(this);
		
		// set first title
		getActivity().getActionBar().setTitle( mSectionsPagerAdapter.getPageTitle(0) );
		
		return vView;
	}
	
	/**
	 * Enable loading animation
	 * @param loading	set {@code true} to enable and {@code false} to disable loading animation.
	 */
	private void setLoading(boolean loading){
		if( loading ){
			pager.setVisibility(View.GONE);
			divLoading.setVisibility(View.VISIBLE);
			imgLoading.setAnimation(animLoading);
		} else {
			pager.setVisibility(View.VISIBLE);
			divLoading.setVisibility(View.GONE);
			imgLoading.setAnimation(null);
		}
	}
	
	/**
	 * Interrupts running {@link AsyncMealsLoader}.
	 */
	private void interruptAsyncMealLoader(){
		if( mMealsLoader != null ){
			mMealsLoader.cancel(true);
			mMealsLoader = null;
		}
	}
	
	/**
	 * Stars a new {@link AsyncMealsLoader}.
	 */
	private void startAsyncMealLodaer(){
		// interrupt running meal loader
		interruptAsyncMealLoader();
		
		// load meals
		if( mMensa != null ){
			mMealsLoader = new AsyncMealsLoader();
			mMealsLoader.execute(new Mensa[]{mMensa});
			setLoading(true);
		}
	}
	
	/**
	 * Sets current mensa and updates menu table.
	 * @param aMensa	{@link Mensa} to set.
	 */
	public void setMensa(Mensa aMensa){
		if( aMensa != null ){
			mMensa = aMensa;
			startAsyncMealLodaer();			
		}
	}
	
	/**
	 * Sets list of meals.
	 * Only for internal callback. Do not set it manually.
	 * @param aMeals	{@link List} of {@link Meal}s to set.
	 */
	public void setMeals(List<Meal> aMeals){
		if( aMeals != null ){
			mMeals = aMeals;
			setLoading(false);
			mSectionsPagerAdapter.notifyDataSetChanged();
		} else {
			startAsyncMealLodaer();
		}
	}
	
	/**
	 * {@link FragmentPagerAdapter} class to show menu fragments.
	 * @author Hannes Eilers
	 *
	 */
	private class SectionsPagerAdapter extends FragmentPagerAdapter{

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			return new MenuFragment(position, mMeals);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			mCalendar.set( Calendar.DAY_OF_WEEK, mCalendar.getFirstDayOfWeek()
					+ (position > 4 ? position+2: position) );
			return mDateFormat.format(mCalendar.getTime());
		}

		@Override
		public int getCount() {
			return 10;
		}
		
	}
	
	@Override
	public void onPageSelected(int position) {
		getActivity().getActionBar().setTitle( mSectionsPagerAdapter.getPageTitle(position) );
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	
}
