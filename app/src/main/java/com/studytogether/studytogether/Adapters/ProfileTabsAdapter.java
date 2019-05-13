package com.studytogether.studytogether.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.studytogether.studytogether.Fragments.EditProfileFragment;
import com.studytogether.studytogether.Fragments.GroupListFragment;

public class ProfileTabsAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public ProfileTabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                GroupListFragment groupListFragment = new GroupListFragment();
                return groupListFragment;
            case 1:
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                return editProfileFragment;
            default:
                return null;
        }
    }
}
