package tsyew.crunchtime;

/**
 * Created by tzuoshuin on 1/17/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
// Code was adapted from http://www.truiton.com/2015/06/android-tabs-example-fragments-viewpager/
public class tabManager extends FragmentStatePagerAdapter {
    int tabCount;

    public tabManager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0) {
            return new AchievementFragment();
        } else if (position==1) {
            return new GoalFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}