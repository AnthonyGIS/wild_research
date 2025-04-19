package com.weng.FragMenu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.weng.R;


/**
 * Created by liyuanjing on 2015/9/28.
 */
public class ReadFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fp_read_fragment_main_layout,container,false);
        return view;
    }
}
