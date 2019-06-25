package com.nialon.time2talk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class appPreferencesActivity extends PreferenceActivity  implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener

{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }
    @Override
    public boolean onPreferenceChange(Preference pref, Object val)
    {
        System.out.println("onPreferenceChange");
        pref.setTitle("test");

        return true;
    }
    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.appprefs);
        }
    }
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {

        System.out.println(key);
        /*
        Preference pref = findPreference(key);
        MyPreferenceFragment mpf = this.MyPreferenceFragment();
        mpf.findPreference(key);
        //if (pref instanceof EditTextPreference)
        {
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary("test");
        }
        */
    }

}

