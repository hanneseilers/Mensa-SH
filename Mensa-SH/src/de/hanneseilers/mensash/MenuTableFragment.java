package de.hanneseilers.mensash;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		
		return vView;
	}
	
	public void setLoading(boolean loading){
		if( loading ){
			divLoading.setVisibility(View.VISIBLE);
			imgLoading.setAnimation(animLoading);
		} else {
			divLoading.setVisibility(View.GONE);
			imgLoading.setAnimation(null);
		}
	}
	
	private class SectionsPagerAdapter extends FragmentPagerAdapter{

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			return new MenuFragment(position);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			String[] vDays = getResources().getStringArray(R.array.tab_weekdays);
			if( position < vDays.length ){
				return vDays[position];
			}
			
			return getResources().getString(R.string.tab_none);
		}

		@Override
		public int getCount() {
			return getResources().getStringArray(R.array.tab_weekdays).length;
		}
		
	}
	
	@Override
	public void onPageSelected(int position) {
		getActivity().getActionBar().setTitle( mSectionsPagerAdapter.getPageTitle(position) );
		
		// notifiy other fragments to cancel background tasks
		for( int i=0; i < mSectionsPagerAdapter.getCount(); i++ ){
			((MenuFragment) mSectionsPagerAdapter.getItem(i)).cancelBackgroundTasks();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	
}
