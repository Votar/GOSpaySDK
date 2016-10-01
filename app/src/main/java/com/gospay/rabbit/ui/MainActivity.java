package com.gospay.rabbit.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.gospay.rabbit.gossdkrabbit.R;
import com.gospay.rabbit.ui.slide.add.AddCardFragment;
import com.gospay.rabbit.ui.slide.list.GetCardListFragment;
import com.gospay.rabbit.ui.slide.oneclick.OneClickDemoFragment;
import com.gospay.sdk.GosSdkManager;
import com.gospay.sdk.api.response.models.GosResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private GosSdkManager sdkManager;

    @BindView(R.id.pager)
    protected ViewPager mPager;

    @BindView(R.id.tabs)
    protected TabLayout tabs;


    private ScreenSlidePagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sdkManager = GosSdkManager.create(getApplicationContext());

        Fragment addCard = new AddCardFragment();
        Fragment getCardList = new GetCardListFragment();
        Fragment webViewFragment = new OneClickDemoFragment();

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPagerAdapter.addPage(addCard, R.string.title_add_card);
        mPagerAdapter.addPage(getCardList, R.string.title_card_list);
        mPagerAdapter.addPage(webViewFragment, R.string.title_web_view);

        mPager.setAdapter(mPagerAdapter);

        Log.d(TAG, new Gson().toJson(null, GosResponse.class));

        tabs.setupWithViewPager(mPager);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        private List<Fragment> slideList = new ArrayList<>();
        private List<Integer> titles = new ArrayList<>();

        public void addPage(Fragment fragment, int titleId){

            slideList.add(fragment);
            titles.add(titleId);

        }

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return slideList.get(position);
        }

        @Override
        public int getCount() {
            return slideList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(titles.get(position));
        }
    }




}
