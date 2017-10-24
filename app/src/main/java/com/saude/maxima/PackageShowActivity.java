package com.saude.maxima;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saude.maxima.fragments.packages.PackageContentFragment;
import com.saude.maxima.fragments.packages.PackageDescriptionFragment;

import org.json.JSONObject;

public class PackageShowActivity extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private JSONObject data_package;

    TextView name, description;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_show, container, false);



        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);


        Bundle args = getArguments();

        viewPager.setAdapter(
                new PackageFragmentStatePagerAdapter(
                        getFragmentManager(),
                        getResources().getStringArray(R.array.titles_tab),
                        args.getString("package")
                )
        );

        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);


        Bundle args = getIntent().getExtras();


        viewPager.setAdapter(
                new PackageFragmentStatePagerAdapter(
                        getSupportFragmentManager(),
                        getResources().getStringArray(R.array.titles_tab),
                        args.getString("package")
                )
        );

        tabLayout.setupWithViewPager(viewPager);





        *//*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*//*
    }



    @Override
    protected void onPause() {
        super.onPause();
        *//*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*//*
    }*/

    private class PackageFragmentStatePagerAdapter extends FragmentStatePagerAdapter{

        private String[] tabTitles;
        private String data_package;

        private PackageFragmentStatePagerAdapter(FragmentManager fm, String[] tabTitles, String data_package) {
            super(fm);
            this.tabTitles = tabTitles;
            this.data_package = data_package;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    PackageContentFragment packageContentFragment = new PackageContentFragment();
                    Bundle args = new Bundle();

                    args.putString("package", data_package);

                    packageContentFragment.setArguments(args);
                    return packageContentFragment;
                case 1: return new PackageDescriptionFragment();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return this.tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.tabTitles[position];
        }
    }

}
