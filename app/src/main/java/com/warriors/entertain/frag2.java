package com.warriors.entertain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.warriors.R;

public class frag2 extends Fragment {
    //创建一个View
    private View zhihu;
    //显示布局
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        zhihu = inflater.inflate(R.layout.frag2_layout, null);
        WebView netView = zhihu.findViewById(R.id.news);
        netView.getSettings().setJavaScriptEnabled(true);
        netView.setWebViewClient(new WebViewClient());
        netView.loadUrl("https://www.zhihu.com/topic/19562832/hot");
        return zhihu;
    }
}
