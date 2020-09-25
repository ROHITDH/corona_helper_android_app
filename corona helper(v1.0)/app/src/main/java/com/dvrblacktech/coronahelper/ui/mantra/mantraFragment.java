package com.dvrblacktech.coronahelper.ui.mantra;


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



public class mantraFragment extends Fragment {

    public WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_mantra, container, false);
        mWebView = (WebView) v.findViewById(R.id.webView4);
        mWebView.loadUrl("https://dvrblacktech.000webhostapp.com/Amritha%20Sanjeevani%20Dhanvantari%20Stotram%20_%20Dhanvantri%20Mantra%20for%20Healing%20and%20Good%20Health.mp3");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }
}

