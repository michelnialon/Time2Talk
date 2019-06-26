package com.nialon.time2talk;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

public class appPreferencesActivity extends PreferenceActivity

{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("appPreferencesActivity.onCreate");
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            System.out.println("MyPreferenceFragment.onCreate");
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.appprefs);
            String mtime = this.findPreference("pref_key_category_1").getSharedPreferences().getString("maxTime", "15");

            this.findPreference("maxTime").setTitle("Durée maximale" + " (" + mtime  + " minute.s" + ")");
            Boolean ssignal = this.findPreference("soundsignal").getSharedPreferences().getBoolean("soundsignal", false);
            this.findPreference("ringtone").setEnabled(ssignal);
            this.findPreference("soundsignal").setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference,
                                                          Object newValue) {
                            System.out.println("onPreferenceChange"+ "soundsignal changed");
                            findPreference("ringtone").setEnabled((Boolean)newValue);
                            return true;
                        }
                    }
            );
            this.findPreference("maxTime").setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference,
                                                          Object newValue) {
                            System.out.println("onPreferenceChange"+ "maxTime changed");
                            findPreference("maxTime").setTitle("Durée maximale" + " (" + (String)newValue  + " minute.s" + ")");
                            return true;
                        }
                    }
            );
            this.findPreference("ringtone").setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference,
                                                          Object newValue) {
                            System.out.println("onPreferenceChange"+ "ringtone changed : " + newValue);

                            Uri ringtoneUri = Uri.parse((String)newValue);
                            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);
                            String rtname = ringtone.getTitle(getContext());
                            findPreference("ringtone").setTitle("Mélodie à utiliser " + " (" + rtname  +  ")");

                            return true;
                        }
                    }
            );
        }

    }
}

