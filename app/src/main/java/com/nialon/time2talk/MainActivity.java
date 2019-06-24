package com.nialon.time2talk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d("fun", "onCreate");
        setContentView(R.layout.activity_main);
        m_Typeface = Typeface.createFromAsset(this.getAssets(), "LED.Font.ttf");

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
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
        allParticipants = new ParticipantsC(timeMax, this);

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
                Log.d("maxTime", s);
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
        }
    };
    public void mnuPrefs()
    {
        Intent i = new Intent(this, appPreferencesActivity.class);
        startActivity(i);
    }

    public void addItem(View v)
    {
        Log.d("addItem", "Start");
        LinearLayout lhelp = findViewById(R.id.layout_help);
        lhelp.setVisibility(View.GONE);

        LinearLayout allParticipantsLayout = findViewById(R.id.layout_root);

        ParticipantM participantM = new ParticipantM(getString(R.string.initvalue3)+ " " + Integer.toString(allParticipants.getCount()+1), allParticipants.getCount()+1);
        ParticipantV participantV = new ParticipantV(participantM, allParticipants, this);
        ParticipantC participantC = new ParticipantC(participantM, participantV);
        allParticipantsLayout.addView(participantV.getParticipantLayout());
        allParticipants.add(participantC);
    }

    public void resetData(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage(getString(R.string.resetconfirmation));
        builder.setCancelable(false);
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                allParticipants.reset();
                Toast.makeText(getApplicationContext(), getString(R.string.datareset), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getApplicationContext(), "Données non remises à zéro", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}

