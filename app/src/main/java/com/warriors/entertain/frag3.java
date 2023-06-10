package com.warriors.entertain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.warriors.R;

public class frag3 extends Fragment {

    private WebView netView;
    //显示布局
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (WebView)inflater.inflate(R.layout.frag3_layout, null);
        WebView netView = view.findViewById(R.id.news);
        netView.getSettings().setJavaScriptEnabled(true); // 开启JavaScript支持
        netView.setWebViewClient(new WebViewClient()); // 设置WebViewClient以在WebView中加载网页
        netView.loadUrl("https://sports.qq.com/kbsweb/qb/leagues.htm#/100000/schedule"); // 加载网页URL

        return view;
    }
}
