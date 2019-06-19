package com.nialon.time2talk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    int val[]={0,0,0,0,0,0,0,0,0,0,0,0};
    int select=99;
    Integer timeMax=6000;
    Typeface m_Typeface;
    Participant[] listParticpants = new Participant[100];
    int nbParticipants=0;

    TextView tv11;
    TextView tv12;
    TextView tv21;
    TextView tv31;
    TextView tv41;
    TextView tv51;
    TextView tv61;
    TextView tv71;
    TextView tv81;
    TextView tv91;
    TextView tva1;
    TextView tvb1;
    TextView tvc1;
    TextView tvd1;
    TextView tvd2;
    TextView tv22;
    TextView tv32;
    TextView tv42;
    TextView tv52;
    TextView tv62;
    TextView tv72;
    TextView tv82;
    TextView tv92;
    TextView tva2;
    TextView tvb2;
    TextView tvc2;
    TextView edd1;
    TextView ed11;
    ConstraintLayout l1;
    TextView tvall[];

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


        tv11 = findViewById(R.id.tv11);
        tv12 = findViewById(R.id.tv12);
        tv11.setTypeface(m_Typeface);
        /*
        tv21 = findViewById(R.id.tv21);
        tv22 = findViewById(R.id.tv22);
        tv32 = findViewById(R.id.tv32);
        tv42 = findViewById(R.id.tv42);
        tv52 = findViewById(R.id.tv52);
        tv62 = findViewById(R.id.tv62);
        tv72 = findViewById(R.id.tv72);
        tv82 = findViewById(R.id.tv82);
        tv92 = findViewById(R.id.tv92);
        tva2 = findViewById(R.id.tva2);
        tvb2 = findViewById(R.id.tvb2);
        tvc2 = findViewById(R.id.tvc2);
        */
//        l1 = findViewById(R.id.lay0);
/*
        tv21.setTypeface(myTypeface);
//        tv31 = findViewById(R.id.tv31);
        tv31.setTypeface(myTypeface);
        //tv41 = findViewById(R.id.tv41);
        tv41.setTypeface(myTypeface);
        //tv51 = findViewById(R.id.tv51);
        tv51.setTypeface(myTypeface);
        //tv61 = findViewById(R.id.tv61);
        tv61.setTypeface(myTypeface);
        //tv71 = findViewById(R.id.tv71);
        tv71.setTypeface(myTypeface);
        //tv81 = findViewById(R.id.tv81);
        tv81.setTypeface(myTypeface);
        //tv91 = findViewById(R.id.tv91);
        tv91.setTypeface(myTypeface);
        //tva1 = findViewById(R.id.tva1);
        tva1.setTypeface(myTypeface);
        //tvb1 = findViewById(R.id.tvb1);
        tvb1.setTypeface(myTypeface);
        //tvc1 = findViewById(R.id.tvc1);
        tvc1.setTypeface(myTypeface);
        //tvd1 = findViewById(R.id.tvd1);
        tvd1.setTypeface(myTypeface);
        //tvd2 = findViewById(R.id.tvd2);

        edd1 = findViewById(R.id.edd1);
        */

//        edd1.setKeyListener(null);
        ed11 = findViewById(R.id.ed11);

        if (savedInstanceState != null)
        {
            for (int i = 0; i < 12; i++)
            {
                val[i] = 0;
            }
            select = 99;
        }

        final Handler handler = new Handler();
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                if (select != 99)
                {
                    Integer val1 = val[select];
                    val1++;
                    val[select] = val1;
                    Log.d("runnable", Integer.toString(select) + " : " + Integer.toString(val1) );

                    DisplayValues();
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
            }
        }
    };
    public void mnuPrefs()
    {
        Intent i = new Intent(this, appPreferencesActivity.class);
        startActivity(i);
    }

    int tot()
    {
        int s = 0;
        for (int i = 0; i < 12; i++)
        {
            s += val[i];
        }
        return (s>0 ? s : 1);
    }
    void DisplayValues()
    {
        ed11.setTypeface(ed11.getTypeface(), Typeface.BOLD);
        listParticpants[0].m_tvDuration.setText(String.format(Locale.FRANCE,"%1d:%02d:%02d", val[0]/3600, val[0]/60, val[0]%60));
        tv11.setText(String.format(Locale.FRANCE,"%1d:%02d:%02d", val[0]/3600, val[0]/60, val[0]%60));
        tv12.setText(String.format(Locale.FRANCE, "%3d%%", (val[0]*100)/tot()));
        if (val[0]>= timeMax*60)
        {
            tv11.setTextColor(Color.RED);
            tv12.setTextColor(Color.RED);
        }
        else
        {
           // tv11.setTextColor(Color.GREEN);
            //tv12.setTextColor(Color.GREEN);
        }
        /*
        tv21.setText(String.format("%03d:%02d", val[1]/60, val[1]%60));
        tv22.setText(String.format("%3d%%", (val[1]*100)/tot()));
        if (val[1]>= timeMax*60)
        {
            tv21.setTextColor(Color.RED);
            tv22.setTextColor(Color.RED);
        }
        else
        {
            //tv21.setTextColor(Color.GREEN);
            //tv22.setTextColor(Color.GREEN);
        }
        tv31.setText(String.format(Locale.FRANCE,"%03d:%02d", val[2]/60, val[2]%60));
        tv32.setText(String.format(Locale.FRANCE,"%3d%%", (val[2]*100)/tot()));

        tv41.setText(String.format(Locale.FRANCE,"%03d:%02d", val[3]/60, val[3]%60));
        tv42.setText(String.format(Locale.FRANCE,"%3d%%", (val[3]*100)/tot()));
        tv51.setText(String.format(Locale.FRANCE,"%03d:%02d", val[4]/60, val[4]%60));
        tv52.setText(String.format(Locale.FRANCE,"%3d%%", (val[4]*100)/tot()));
        tv61.setText(String.format(Locale.FRANCE,"%03d:%02d", val[5]/60, val[5]%60));
        tv62.setText(String.format(Locale.FRANCE,"%3d%%", (val[5]*100)/tot()));
        tv71.setText(String.format(Locale.FRANCE,"%03d:%02d", val[6]/60, val[6]%60));
        tv72.setText(String.format(Locale.FRANCE,"%3d%%", (val[6]*100)/tot()));
        tv81.setText(String.format(Locale.FRANCE,"%03d:%02d", val[7]/60, val[7]%60));
        tv82.setText(String.format(Locale.FRANCE,"%3d%%", (val[7]*100)/tot()));
        tv91.setText(String.format(Locale.FRANCE,"%03d:%02d", val[8]/60, val[8]%60));
        tv92.setText(String.format(Locale.FRANCE,"%3d%%", (val[8]*100)/tot()));
        tva1.setText(String.format(Locale.FRANCE,"%03d:%02d", val[9]/60, val[9]%60));
        tva2.setText(String.format(Locale.FRANCE,"%3d%%", (val[9]*100)/tot()));
        tvb1.setText(String.format(Locale.FRANCE,"%03d:%02d", val[10]/60, val[10]%60));
        tvb2.setText(String.format(Locale.FRANCE,"%3d%%", (val[10]*100)/tot()));
        tvc1.setText(String.format(Locale.FRANCE,"%03d:%02d", val[11]/60, val[11]%60));
        tvc2.setText(String.format(Locale.FRANCE,"%3d%%", (val[11]*100)/tot()));
        tvd1.setText(String.format(Locale.FRANCE,"%03d:%02d", tot()/60, tot()%60));
        tvd2.setText("100%");
        */
    }
    public void select(View v)    { select = Integer.parseInt(v.getTag().toString()); }

    public void addItem(View v)
    {
        Log.d("addItem", "Start");

        Participant new_participant = new Participant("<Name>", this);
        listParticpants[nbParticipants++] = new_participant;

        TextView tv1=new TextView(this);
        ImageButton img1 = new ImageButton(this);
        img1.setImageResource(android.R.drawable.ic_media_play);
        img1.setTag(nbParticipants);
        img1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You made a mess", Toast.LENGTH_LONG).show();
                select(v);
            }

        });
        tv1.setText(R.string.initvalue3);
        tv1.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tv1.setTextSize(24);

        TextView tv2=new TextView(this);
        tv2.setText(R.string.initvalue2);
        tv2.setTypeface(m_Typeface);
        tv2.setTextSize(32);
        tv2.setTextColor(getResources().getColor(android.R.color.holo_green_light));

        TextView tv3=new TextView(this);
        tv3.setText(R.string.initvalue1);
        tv3.setTextSize(24);
        LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
        tv1.setLayoutParams(lparams);
        tv2.setLayoutParams(lparams);
        tv3.setLayoutParams(lparams);

        LinearLayout newlay = new LinearLayout(this);
        newlay.setOrientation(LinearLayout.HORIZONTAL);
        //newlay.setBackgroundColor(Color.rgb(0,255,255));
        //newlay.addView(tv1);
        newlay.addView(new_participant.m_tvName);
        newlay.addView(img1);
        newlay.addView(tv2);
        newlay.addView(tv3);

        LinearLayout myRoot = findViewById(R.id.layout_root);
        myRoot.addView(newlay);

        /*
        LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        TextView tv=new TextView(this);
        tv.setText("TESTTESTTEST");

        //ConstraintLayout constraintLayout = findViewById(R.id.lay0);
        //tv.setLayoutParams(lparams);

        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setBackgroundColor(Color.rgb(0,255,255));
        l.setLayoutParams(lparams);
        */
/*
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(l.getId(), ConstraintSet.RIGHT,R.id.lay0,ConstraintSet.RIGHT,0);
        constraintSet.connect(l.getId(), ConstraintSet.TOP,R.id.layd,ConstraintSet.BOTTOM,0);
        constraintSet.applyTo(constraintLayout);

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = vi.inflate(l.getId(), null);
        container = (ViewGroup) findViewById(R.id.layoutInserPoint);
        container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        l.addView(tv);
        l1.addView(l);
*/

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
                for (int i =0; i<12;i++)
                {
                    val[i] = 0;
                }
                DisplayValues();
                select = 99;
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
    public void setName(final View v)
    {
        final EditText edtText = new EditText(this);
        edtText.setText(((TextView)v).getText());
        edtText.setPaintFlags(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification");
        builder.setMessage("Veuillez saisir le nom " );
        builder.setCancelable(true);
        builder.setView(edtText);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ((TextView)v).setText(edtText.getText());
                Toast.makeText(getApplicationContext(), "Le nom a été modifié " + edtText.getText() , Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}

