package co.meappdev.mikkipastel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.meappdev.mikkipastel.fragment.SettingFragment;

/**
 * Created by acer on 7/1/2016.
 */
public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (savedInstanceState == null) {
            //1st created
            //place fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, SettingFragment.newInstance())
                    .commit();
        }

    }
}
