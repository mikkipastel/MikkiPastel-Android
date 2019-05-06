package com.mikkipastel.blog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mikkipastel.blog.R;
import com.mikkipastel.blog.fragment.AboutMeFragment;

/**
 * Created by acer on 7/1/2016.
 */
public class AboutMeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

        if (savedInstanceState == null) {
            //1st created
            //place fragment
            getSupportFragmentManager().beginTransaction()
                .add(R.id.contentContainer, AboutMeFragment.newInstance())
                 .commit();
        }
    }
}