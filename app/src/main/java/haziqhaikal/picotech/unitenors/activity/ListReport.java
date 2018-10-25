package haziqhaikal.picotech.unitenors.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import haziqhaikal.picotech.unitenors.R;
import haziqhaikal.picotech.unitenors.helper.SessionManager;

/**
 * Created by haziqhaikal on 11/28/2017.
 */

public class ListReport extends Fragment {


    private String TAG = MainActivity.class.getSimpleName();

    private SessionManager session;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View rootView;




    public static ListReport newInstance(){
        ListReport fragment = new ListReport();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState)
        {

        rootView = inflater.inflate(R.layout.list_report, container, false);

        //toolbar = rootView.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

            session = new SessionManager(getActivity());
            if (!session.isLoggedIn()) {
                // User is already logged in. Take him to main activity
                session.setLogin(false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }


        //OnClickbo
        return rootView;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PendingFrag(), "On Going");
        adapter.addFragment(new SolvedFrag(), "Solved");
        adapter.addFragment(new UnsolveFrag(), "Unresolved");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        rootView = null;
    }

}
