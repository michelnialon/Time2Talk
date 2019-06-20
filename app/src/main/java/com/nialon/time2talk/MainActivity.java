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
import androidx.constraintlayout.widget.ConstraintLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.nialon.time2talk.controller.ParticipantC;
import com.nialon.time2talk.controller.ParticipantsC;
import com.nialon.time2talk.model.ParticipantM;
import com.nialon.time2talk.view.ParticipantV;

public class MainActivity extends AppCompatActivity
{
    Integer timeMax=6000;
    Typeface m_Typeface;
    private ParticipantsC allParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d("fun", "onCreate");
        setContentView(R.layout.activity_main);
        m_Typeface = Typeface.createFromAsset(this.getAssets(), "LED.Font.ttf");

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        String s = prefs.getString("maxTime", "15");
        timeMax = Integer.parseInt(s);
        allParticipants = new ParticipantsC(timeMax);

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

        LinearLayout allParticipantsLayout = findViewById(R.id.layout_root);

        ParticipantM participantM = new ParticipantM("---");
        ParticipantV participantV = new ParticipantV(participantM, allParticipants, this);
        ParticipantC participantC = new ParticipantC(participantM, participantV);
        allParticipantsLayout.addView(participantV.getParticipantLayout());
        allParticipants.add(participantC);
    }

    public void resetData(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Voulez-vous vraiment remettre toutes les données à zéro ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                allParticipants.reset();
                Toast.makeText(getApplicationContext(), "Données remises à zéro", Toast.LENGTH_SHORT).show();
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

