package co.meappdev.mikkipastel.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import co.meappdev.mikkipastel.R;

/**
 * Created by acer on 4/30/2016.
 */
public class SettingFragment extends DialogFragment {

    Switch notification_switch;
    LinearLayout aboutMe;

    public SettingFragment() {
        super();
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        //init(savedInstanceState);
        initInstances(rootView);
        return rootView;
    }

    /*private void init(Bundle savedInstanceState) {
        //build dummy file
        pref = getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
        //create editor : write value to key
        //SharedPreferences.Editor editor = pref.edit();
        //editor.putString("hello", "world");
        //commit data
        //editor.apply();

        //get value
        value = pref.getBoolean("push_notification", true);
    }*/

    public void initInstances(View rootView){
        notification_switch = (Switch)rootView.findViewById(R.id.switch1);
        aboutMe = (LinearLayout)rootView.findViewById(R.id.popup_aboutme);

        SharedPreferences pref = getContext().getSharedPreferences("setting",
                Context.MODE_PRIVATE);

        boolean value = pref.getBoolean("push_notification", true);
        notification_switch.setChecked(value);

        if (notification_switch.isChecked()) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("push_notification", true);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("push_notification", false);
            editor.commit();
        }

        aboutMe.setClickable(true);
        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Press", Toast.LENGTH_SHORT).show();
                showDialog();
            }
        });
    }

    public void showDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = AboutAppFragment.newInstance();
        newFragment.show(ft, "dialog");
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
