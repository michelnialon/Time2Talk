package com.nialon.time2talk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    private ImageButton imageButton1;
    private boolean silentmode=false;
    private ImageButton imgButton4;
    private Display display;
    private Point size;
    Rect windowRect;
    private int scrwidth;
    private int scrheight;
    private ScrollView scrvmain;
    //    private ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
//    private Handler handler;
//    private Runnable runnable;
// private String stringtone;
//private Ringtone ringtone;

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
        imageButton1 = findViewById(R.id.imageButton1);
        imgButton4 = findViewById(R.id.imageButton4);
        scrvmain = findViewById(R.id.scrvmain);
        //TextView textView =findViewById(R.id.tvhelp);
        //textView.setTypeface(m_Typeface);

        MobileAds.initialize(this, "ca-app-pub-4468029712209847~8644725633");
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

        //stringtone = prefs.getString("ringtone", "");
        //Uri ringtoneUri = Uri.parse(stringtone);
        //ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
        allParticipants = new ParticipantsC(timeMax, this, multipleSpeakers, soundsignal);

        if (savedInstanceState != null)
        {
            Toast.makeText(this, "savedInstance", Toast.LENGTH_SHORT).show();

        }

       /* handler = new Handler();
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
                    //allParticipants.update();
                    //allParticipants.displayAll();
                }
                if (allParticipants.nbselelected() > 0)
                {
                   // handler.postDelayed(this, 1000);
                }
                else
                {
                    handler.removeCallbacks(runnable);
                }
            }
        };*/
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
        String AppVersion="";
        try
        {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            AppVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnuPrefs:
                mnuPrefs();
                return true;
            case R.id.mnuAbout:
                intent = new Intent(this, about.class);
                startActivity(intent);
                return true;
            case R.id.mnu_check_version:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.nialon.time2talk"));
                startActivity(intent);
                return true;
            case R.id.mnu_sendcomment:
                intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "mnialon@gmail.com" });
                intent.putExtra(Intent.EXTRA_TEXT,
                        Html.fromHtml(new StringBuilder()
                                .append("<html><head></head><body>")
                                .append("<p>---</p>")
                                .append("<p>Version Application : " + AppVersion + "</p>")
                                .append("<p>Android Version : " + Build.VERSION.SDK_INT + " (" + Build.VERSION.RELEASE + ")</p>")
                                .append("<p>Device Type : " + Build.MANUFACTURER + " (" + Build.MODEL + ")</p>")
                                .append("</body></html>")
                                .toString())
                );
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.sendcomment)));
                return true;
            case R.id.mnu_share:
                intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_SUBJECT,  getResources().getString(R.string.app_name));

                intent.putExtra(Intent.EXTRA_TEXT,
                        Html.fromHtml(new StringBuilder()
                                .append("<html><head></head><body>")
                                .append("<p>" +  getResources().getString(R.string.sharecomment) + "</p>")
                                .append("<p>https://play.google.com/store/apps/details?id=com.nialon.time2talk</p>")
                                .append("<p>https://www.facebook.com/Time2Talk</p>")
                                .append("</body></html>")
                                .toString())
                );
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
                return true;
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
            /*
            if (key.equals("ringtone"))
            {
                String rt = sharedPreferences.getString("ringtone", "");

                Log.d("ringtone", rt);
                Uri ringtoneUri = Uri.parse(rt);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
                allParticipants.setRingtone(ringtone);
            }
            */
        }
    };
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d("On", "ConfigurationChanged");

        display.getSize(size);
        windowRect = new Rect();
        scrvmain.getWindowVisibleDisplayFrame(windowRect);
        scrwidth = windowRect.width();
        scrheight = windowRect.height();
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
        //handler.removeCallbacks(runnable);
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
        ParticipantV participantV = new ParticipantV(participantM, allParticipants, this, m_Typeface);
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
            imgButton4.setImageResource(getResources().getIdentifier("hp_on", "mipmap", getPackageName()));
            silentmode = false;
            allParticipants.silentMode=false;
        }
        else
        {
            imgButton4.setImageResource(getResources().getIdentifier("hp_off", "mipmap", getPackageName()));
            silentmode = true;
            allParticipants.silentMode=true;
        }
    }
    public void shareData(View v)
    {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("message/rfc822");
        //share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.meetinginfos));
        share.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(allParticipants.getInformationsHTML()));
        //  share.putExtra(Intent.EXTRA_HTML_TEXT, allParticipants.getInformationsHTML());

        startActivity(Intent.createChooser(share, getResources().getString(R.string.sendinfos)));
    }
}

