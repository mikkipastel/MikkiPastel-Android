package com.mikkipastel.blog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mikkipastel.blog.DAO.Item;
import com.mikkipastel.blog.R;

/**
 * Created by acer on 4/30/2016.
 */
public class ContentFragment extends Fragment {

    static int position;

    WebView webviewContent;
    Item daoContent;

    public interface FragmentListener{
        void onListItemClick(Item dao, int pos);
    }

    public ContentFragment() {
        super();
    }

    public static ContentFragment newInstance() {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        //position = pos;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);
        initInstances(rootView);
        return rootView;
    }

    public void initInstances(View rootView){
        webviewContent = (WebView)rootView.findViewById(R.id.webViewContent);
        webviewContent.setWebViewClient(new WebViewClient());
        webviewContent.loadUrl(daoContent.getUrl());

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

}
