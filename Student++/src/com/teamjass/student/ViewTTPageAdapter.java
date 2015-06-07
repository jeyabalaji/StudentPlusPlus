package com.teamjass.student;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewTTPageAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 2;
	private String titles[] = new String[] { "Day", "Subject" };

	public ViewTTPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

			// Open Fragment_21_Day.java
		case 0:
			Fragment_21_Day fragmenttab1 = new Fragment_21_Day();
			return fragmenttab1;

			// Open Fragment_22_Subject.java
		case 1:
			Fragment_22_Subject fragmenttab2 = new Fragment_22_Subject();
			return fragmenttab2;
		}
		return null;
	}

	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}