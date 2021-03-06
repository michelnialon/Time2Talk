package com.nialon.time2talk;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.appcompat.app.AppCompatActivity;

public class appPreferencesActivity extends AppCompatActivity

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
            this.findPreference("maxTime").setTitle(getResources().getString(R.string.maxduration) + " (" + mtime  + " " + getResources().getString(R.string.minutes) + ")");

            /*
            String newValue = this.findPreference("pref_key_category_1").getSharedPreferences().getString("ringtone", "-");
            Uri ringtoneUri = Uri.parse(newValue);
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);
            String rtname = ringtone.getTitle(getContext());
            this.findPreference("ringtone").setTitle(getResources().getString(R.string.melodytouse2) + " (" + rtname  + ")");
            */
            boolean ssignal = this.findPreference("soundsignal").getSharedPreferences().getBoolean("soundsignal", false);
//            this.findPreference("ringtone").setEnabled(ssignal);
            this.findPreference("soundsignal").setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference,
                                                          Object newValue) {
                            System.out.println("onPreferenceChange"+ "soundsignal changed");
                            //findPreference("ringtone").setEnabled((Boolean)newValue);
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
                            findPreference("maxTime").setTitle(getResources().getString(R.string.maxduration) + " (" + newValue  +  " " + getResources().getString(R.string.minutes) + ")");
                            return true;
                        }
                    }
            );
            /*
            this.findPreference("ringtone").setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference,
                                                          Object newValue) {
                            System.out.println("onPreferenceChange"+ "ringtone changed : " + newValue);

                            Uri ringtoneUri = Uri.parse((String)newValue);
                            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);
                            String rtname = ringtone.getTitle(getContext());
                            findPreference("ringtone").setTitle(getResources().getString(R.string.melodytouse2) + " (" + rtname  +  ")");

                            return true;
                        }
                    }
            );
            */

        }

    }
}

