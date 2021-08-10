package com.papanews;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Adapter extends FragmentStateAdapter {

    private final String[] isnoksip = new String[]{"Profile", "Recommended", "Technology", "Politics",
            "Business", "Startup", "Entertainment", "Sports", "International", "Influencer", "Miscellaneous"};

    private final String[] isskip = new String[]{"Profile", "Technology", "Politics",
            "Business", "Startup", "Entertainment", "Sports", "International", "Influencer", "Miscellaneous"};

//    private String views_link[] = new String[]{"", "Tech", "Politics",
//            "Business", "Startup", "Entertain", "Sports", "International", "Influence", "Miscel"};

    private final String[] tabTitles = new String[]{};


    public Adapter(@NonNull final FragmentActivity fm) {
        super(fm);
        Log.e("fmprint ", String.valueOf(fm));
//        fm.beginTransaction().replace(R.id.viewPager,NewsFragment.class,null,"Politics").setReorderingAllowed(true).addToBackStack(null).commit();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("globalskip ", global.skipskip);

        if (!global.skipskip.equals("skip")) {
            Log.e("hbhbbhbh ", "yessss");

            if (position == 0) {
                return new Profile();
            }
            Fragment f1 = new NewsFragment2();
            Bundle args1 = new Bundle();
            args1.putString("frag_shared", isnoksip[position]);
            global.frag_shared = isnoksip[position];
            f1.setArguments(args1);

            return f1;
        } else {
            Log.e("hbhbbhbh ", position+" -  2");
            if (position == 0) {
                return new Profile();
            }
            Fragment f1 = new NewsFragment2();
            Bundle args1 = new Bundle();
            args1.putString("frag_shared", isskip[position]);
            global.frag_shared = isskip[position];
            f1.setArguments(args1);

            Log.e("fragtag - ", f1.getTag() + " - " + isskip[position]);

            return f1;
        }
    }

    @Override
    public int getItemCount() {
        return global.returnahowmuch;
    }
}
