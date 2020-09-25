package com.dvrblacktech.coronahelper.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dvrblacktech.coronahelper.R;



public class SlideshowFragment extends Fragment {

    public WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_slideshow, container, false);
        mWebView = (WebView) v.findViewById(R.id.webView3);
        mWebView.loadUrl("https://dvrblacktech.000webhostapp.com/corona%20precotions.png");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }
}
