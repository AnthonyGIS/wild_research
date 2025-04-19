package com.weng.FragMenu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weng.R;


/**
 * Created by liyuanjing on 2015/10/21.
 */
public class ResearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fp_research_frag_layout,container,false);
        return view;

    }
}
