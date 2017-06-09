package vn.edu.hcmus.familylocator.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vn.edu.hcmus.familylocator.fragments.FriendListFragment;
import vn.edu.hcmus.familylocator.fragments.FriendRequestedListFragment;
import vn.edu.hcmus.familylocator.fragments.FriendRequestingListFragment;

/**
 * Created by quangcat on 5/14/17.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FriendListFragment();
                break;
            case 1:
                fragment = new FriendRequestedListFragment();
                break;
            case 2:
                fragment = new FriendRequestingListFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Hiện có";
            case 1:
                return "Đang chờ";
            case 2:
                return "Bạn mới";
        }
        return null;
    }
}