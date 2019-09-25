package com.nialon.time2talk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.Dimension;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.nialon.time2talk.controller.ParticipantC;
import com.nialon.time2talk.controller.ParticipantsC;
import com.nialon.time2talk.model.ParticipantM;
import com.nialon.time2talk.view.ParticipantV;

// todo: possibility to talk more than one attendee at a time
public class MainActivity extends AppCompatActivity
{
    private Integer timeMax=6000;
    private Typeface m_Typeface;
    private ParticipantsC allParticipants;
    private AdView adView;
    private String stringtone;
    private Ringtone ringtone;
    private Button txtButton1;
    private boolean silentmode=false;
    private ImageButton imgButton4;
    private ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
    private Handler handler;
    private Runnable runnable;
    private Display display;
    private Point size;
    private int scrwidth;
    private int scrheight;
    private LinearLayout svmain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();

        Log.d("On", "Create");
        display = getWindowManager().getDefaultDisplay();

        size = new Point();
        setContentView(R.layout.activity_main);
        //m_Typeface = Typeface.createFromAsset(this.getAssets(), "LED.Font.ttf");
        m_Typeface = Typeface.createFromAsset(this.getAssets(), "led_counter-7.ttf");

        txtButton1 = findViewById(R.id.txtButton1);
        txtButton1.setTypeface(m_Typeface);
        txtButton1.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light));

        imgButton4 = findViewById(R.id.imageButton4);
        svmain = findViewById(R.id.llmain);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        //TextView textView =findViewById(R.id.tvhelp);
        //textView.setTypeface(m_Typeface);
        adView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        //adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-3940256099942544~3347511713");

        adView.loadAd(adRequest);

        // set title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_ttt_round);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        String s = prefs.getString("maxTime", "15");
        timeMax = Integer.parseInt(s);
        boolean multipleSpeakers = prefs.getBoolean("multiplespeakers", false);
        boolean soundsignal = prefs.getBoolean("soundsignal", false);
        stringtone = prefs.getString("ringtone", "");
        Uri ringtoneUri = Uri.parse(stringtone);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
        allParticipants = new ParticipantsC(timeMax, this, multipleSpeakers, soundsignal, ringtone);

        if (savedInstanceState != null)
        {

        }

        handler = new Handler();
        runnable = new Runnable()
        {
            public void run()
            {
                //toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,100);

                if (allParticipants.getCount()==0)
                {
                    LinearLayout lhelp = findViewById(R.id.layout_help);
                    lhelp.setVisibility(View.VISIBLE);
                }
                else
                {
                    allParticipants.update();
                    allParticipants.displayAll();
                }
                if (allParticipants.nbselelected() > 0)
                {
                    handler.postDelayed(this, 1000);
                }
                else
                {
                    handler.removeCallbacks(runnable);
                }
            }
        };
        //handler.postDelayed(runnable, 1000);
    } // onCreate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnuPrefs:
                mnuPrefs();
                return true;
            case R.id.mnuAbout:
                intent = new Intent(this, about.class);
                startActivity(intent);
                return true;
                /*
            case R.id.mnuDonate:
                intent = new Intent(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=U6CX3ZNDSYFMQ")));
                startActivity(intent);
                return true;
                */
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    final private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
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
                Boolean ss = sharedPreferences.getBoolean("soundsignal", false);

                Log.d("soundsignal", ss.toString());
                allParticipants.setSoundSignal(ss);
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

        Log.d("On", "ConfigurationChanged");

        display.getSize(size);
        scrwidth = size.x;
        scrheight = size.y;
        scrwidth =svmain.getWidth();
        scrheight = svmain.getHeight();
        Log.d("Width", Integer.toString(scrwidth));
        Log.d("Height", Integer.toString(scrheight));

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            allParticipants.showProgressBar(true);
            allParticipants.setLandscape(true);
            //adView.setAdSize(AdSize.FULL_BANNER);
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            //adView.setAdSize(AdSize.BANNER);
            allParticipants.showProgressBar(false);
            allParticipants.setLandscape(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("On", "Start");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("On", "Resume");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("On", "Restart");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("On", "Pause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("On", "Stop");
    }
    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        allParticipants.reset();
        super.onDestroy();
        Log.d("On", "Destroy");
    }
    private  void mnuPrefs()
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

        ParticipantM participantM = new ParticipantM(getString(R.string.participant)+ " " + (allParticipants.getLast() + 1), allParticipants.getCount()+1);
        //participantM.setDuration(7200);
        ParticipantV participantV = new ParticipantV(participantM, allParticipants, this, m_Typeface, silentmode);
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
    public void soundOff(View v)
    {
        if (silentmode)
        {
            imgButton4.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
            silentmode = false;
            allParticipants.silentMode=false;
        }
        else
        {
            imgButton4.setImageResource(android.R.drawable.ic_lock_silent_mode);
            silentmode = true;
            allParticipants.silentMode=true;
        }
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

