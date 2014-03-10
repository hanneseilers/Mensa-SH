package de.hanneseilers.mensash;

import java.util.ArrayList;

import org.holoeverywhere.app.Fragment;

import de.hanneseilers.mensash.activities.ActivityMain;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MenueFragmentPagerAdapter extends FragmentStatePagerAdapter {
 private ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();
 
 public MenueFragmentPagerAdapter(FragmentManager fm) {
     super(fm);
 }
 
 public Fragment addItem() {
	 Fragment fragment = new MenuFragment();
	 mFragmentsList.add(fragment);
	 this.notifyDataSetChanged();
	 return fragment;
 }

 @Override
 public Fragment getItem(int i) {
     Fragment fragment = mFragmentsList.get(i);
     return fragment;
 }

 @Override
 public int getCount() {
     return mFragmentsList.size();
 }

 @Override
 public CharSequence getPageTitle(int position) {
     return ActivityMain.getWeekdayString(position);
 }
 
 @Override
 public int getItemPosition(Object object) {
     return POSITION_NONE;
 }
}
