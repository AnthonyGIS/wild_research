package com.weng.WildResearch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.weng.R;


public class MainActivity extends Activity implements View.OnClickListener{
    private LinearLayout newsLayout;
    private LinearLayout readLayout;
    private LinearLayout videoLayout;
    private LinearLayout lampLayout;
    private LinearLayout pcLayout;

    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.newsLayout = (LinearLayout) findViewById(R.id.bottom_status_layout);
        this.readLayout = (LinearLayout) findViewById(R.id.bottom_map_layout);
        this.videoLayout = (LinearLayout) findViewById(R.id.bottom_discovery_layout);
        this.lampLayout = (LinearLayout) findViewById(R.id.bottom_research_layout);
        this.pcLayout = (LinearLayout) findViewById(R.id.bottom_my_layout);
        this.newsLayout.setOnClickListener(this);
        this.readLayout.setOnClickListener(this);
        this.videoLayout.setOnClickListener(this);
        this.lampLayout.setOnClickListener(this);
        this.pcLayout.setOnClickListener(this);
        this.newsLayout.setSelected(true);
        mFragments = new Fragment[5];
        this.fragmentManager = this.getFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragement_gps_status);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragement_map);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragement_discovery);
        mFragments[3] = fragmentManager.findFragmentById(R.id.fragement_research);
        mFragments[4] = fragmentManager.findFragmentById(R.id.fragement_my);
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);
        fragmentTransaction.show(mFragments[0]).commit();

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onClick(View v) {
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);

		int id = v.getId();
		if (id == R.id.bottom_status_layout) {
			this.newsLayout.setSelected(true);
			this.readLayout.setSelected(false);
			this.videoLayout.setSelected(false);
			this.lampLayout.setSelected(false);
			this.pcLayout.setSelected(false);
			fragmentTransaction.show(mFragments[0]).commit();
		}
		else if (id == R.id.bottom_map_layout) {
			this.newsLayout.setSelected(false);
			this.readLayout.setSelected(true);
			this.videoLayout.setSelected(false);
			this.lampLayout.setSelected(false);
			this.pcLayout.setSelected(false);
			fragmentTransaction.show(mFragments[1]).commit();
		}
		else if (id == R.id.bottom_discovery_layout) {
			this.newsLayout.setSelected(false);
			this.readLayout.setSelected(false);
			this.videoLayout.setSelected(true);
			this.lampLayout.setSelected(false);
			this.pcLayout.setSelected(false);
			fragmentTransaction.show(mFragments[2]).commit();
		}
		else if (id == R.id.bottom_research_layout) {
			this.newsLayout.setSelected(false);
			this.readLayout.setSelected(false);
			this.videoLayout.setSelected(false);
			this.lampLayout.setSelected(true);
			this.pcLayout.setSelected(false);
			fragmentTransaction.show(mFragments[3]).commit();
		}
		else if (id == R.id.bottom_my_layout) {
			this.newsLayout.setSelected(false);
			this.readLayout.setSelected(false);
			this.videoLayout.setSelected(false);
			this.lampLayout.setSelected(false);
			this.pcLayout.setSelected(true);
			fragmentTransaction.show(mFragments[4]).commit();
		}
    }
}
