package com.teamjass.student;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewExamsPageAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 2;
	private String titles[] = new String[] { "Internals", "GPA"};

	public ViewExamsPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {


		case 0:
			Fragment_41_Internals fragmenttab1 = new Fragment_41_Internals();
			return fragmenttab1;


		case 1:
			Fragment_42_GPA fragmenttab2 = new Fragment_42_GPA();
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