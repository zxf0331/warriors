package com.warriors.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.warriors.R;

public class frag2 extends Fragment {
    //创建一个View
    private View zj;
    //显示布局
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        zj = inflater.inflate(R.layout.frag2_layout, null);
        return zj;
    }
}
