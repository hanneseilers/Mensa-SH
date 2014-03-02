package de.hanneseilers.mensash;

import java.util.ArrayList;

import org.holoeverywhere.app.Fragment;

import de.hanneseilers.mensash.activities.ActivityMain;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//Since this is an object collection, use a FragmentStatePagerAdapter,
//and NOT a FragmentPagerAdapter.
public class MenueFragmentPagerAdapter extends FragmentStatePagerAdapter {
 private ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();
 
 public MenueFragmentPagerAdapter(FragmentManager fm) {
     super(fm);
 }
 
 public Fragment addItem() {
	 Fragment fragment = new MenuFragment();
     Bundle args = new Bundle();
     // Our object is just an integer :-P
//     args.putInt(MenuFragment.ARG_OBJECT, i + 1);
//     fragment.setArguments(args);
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
