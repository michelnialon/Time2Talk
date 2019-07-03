package com.nialon.time2talk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.nialon.time2talk.controller.ParticipantC;
import com.nialon.time2talk.controller.ParticipantsC;
import com.nialon.time2talk.model.ParticipantM;
import com.nialon.time2talk.view.ParticipantV;

// todo: possibility to talk more than one participant at a time
public class MainActivity extends AppCompatActivity
{
    Integer timeMax=6000;
    Typeface m_Typeface;
    private ParticipantsC allParticipants;
    private AdView adView;
    private String stringtone;
    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d("fun", "onCreate");
        setContentView(R.layout.activity_main);
        //m_Typeface = Typeface.createFromAsset(this.getAssets(), "LED.Font.ttf");
        m_Typeface = Typeface.createFromAsset(this.getAssets(), "led_counter-7.ttf");


        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        //TextView textView =findViewById(R.id.tvhelp);
        //textView.setTypeface(m_Typeface);
        adView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-3940256099942544~3347511713");

        adView.loadAd(adRequest);

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        String s = prefs.getString("maxTime", "15");
        timeMax = Integer.parseInt(s);
        Boolean multipleSpeakers = prefs.getBoolean("multiplespeakers", false);
        Boolean soundsignal = prefs.getBoolean("soundsignal", false);
        stringtone = prefs.getString("ringtone", "");
        Uri ringtoneUri = Uri.parse(stringtone);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
        allParticipants = new ParticipantsC(timeMax, this, multipleSpeakers, soundsignal, ringtone);

        if (savedInstanceState != null)
        {

        }

        final Handler handler = new Handler();
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                allParticipants.update();
                allParticipants.displayAll();
                if (allParticipants.getCount()==0)
                {
                    LinearLayout lhelp = findViewById(R.id.layout_help);
                    lhelp.setVisibility(View.VISIBLE);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    } // onCreate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnuPrefs:
                mnuPrefs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("maxTime"))
            {
                String s = sharedPreferences.getString("maxTime", "15");
                if (s != null)
                {
                    timeMax = Integer.valueOf(s);
                }
                else
                {
                    timeMax = 15;
                }

                Log.d("maxTime", String.valueOf(timeMax));
                allParticipants.setTimeMax(timeMax);
            }
            if (key.equals("multiplespeakers"))
            {
                Boolean ms = sharedPreferences.getBoolean("multiplespeakers", false);

                Log.d("multiplespealers", ms.toString());
                allParticipants.setMultipleSpeakers(ms);
            }
            if (key.equals("soundsignal"))
            {
                Boolean ms = sharedPreferences.getBoolean("soundsignal", false);

                Log.d("soundsignal", ms.toString());
                allParticipants.setSoundSignal(ms);
            }
            if (key.equals("ringtone"))
            {
                String rt = sharedPreferences.getString("ringtone", "");

                Log.d("ringtone", rt);
                Uri ringtoneUri = Uri.parse(rt);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
                allParticipants.setRingtone(ringtone);
            }
        }
    };
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            allParticipants.showProgressBar(true);
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            allParticipants.showProgressBar(false);
        }
    }
    public void mnuPrefs()
    {
        Intent intent = new Intent(this, appPreferencesActivity.class);

        startActivity(intent);
    }

    public void addItem(View v)
    {
        Log.d("addItem", "Start");
        LinearLayout lhelp = findViewById(R.id.layout_help);
        lhelp.setVisibility(View.GONE);

        LinearLayout allParticipantsLayout = findViewById(R.id.layout_root);

        ParticipantM participantM = new ParticipantM(getString(R.string.participant)+ " " + Integer.toString(allParticipants.getCount()+1), allParticipants.getCount()+1);
        ParticipantV participantV = new ParticipantV(participantM, allParticipants, this, ringtone, m_Typeface);
        ParticipantC participantC = new ParticipantC(participantM, participantV);
        allParticipantsLayout.addView(participantV.getParticipantLayout());
        allParticipants.add(participantC);
    }

    public void resetData(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirmation));
        builder.setMessage(getString(R.string.resetconfirmation));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                allParticipants.reset();
                Toast.makeText(getApplicationContext(), getString(R.string.dataset2zero), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.datanotset2zero), Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
    public void shareData(View v)
    {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/html");
        //share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.meetinginfos));
        share.putExtra(Intent.EXTRA_TEXT, allParticipants.getInformations());
        share.putExtra(Intent.EXTRA_HTML_TEXT, allParticipants.getInformationsHTML());

        startActivity(Intent.createChooser(share, getResources().getString(R.string.sendinfos)));
    }
}

