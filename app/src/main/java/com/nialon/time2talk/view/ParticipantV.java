package com.nialon.time2talk.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.nialon.time2talk.R;
import com.nialon.time2talk.controller.ParticipantsC;
import com.nialon.time2talk.model.ParticipantM;

import java.util.Locale;

public class ParticipantV
{
    private ParticipantM model;
    private ParticipantsC allparticipants;
    private TextView tvName;
    private LinearLayout participantLayout;
    private Context ctxt;
    private boolean selected;
    private ImageButton imgStartStop;
    private ImageButton imgRemove;
    private TextView tvDuration;
    private TextView tvPercentage;
    private ProgressBar progressBar;
    private int index;
    private Typeface m_Typeface;
    static private ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

    public LinearLayout getParticipantLayout()
    {
        return participantLayout;
    }

    @SuppressLint("ResourceType")
    public ParticipantV(final ParticipantM participantM, final ParticipantsC allparticpants, Activity context, Typeface typeface)
    {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        lparams.setMargins(20,0,20,0);
        //lparams.gravity = Gravity.CENTER_HORIZONTAL;
        this.participantLayout = new LinearLayout(context);

        participantLayout.setOrientation(LinearLayout.HORIZONTAL);
        //participantLayout.setBackgroundColor(Color.rgb(0,255,255));

        this.model = participantM;
        this.allparticipants = allparticpants;
        this.ctxt = context;
        this.m_Typeface = typeface;
        this.selected = false;

        tvName=new TextView(context);
        tvName.setText(model.getName());
        //tvName.setTextColor(ContextCompat.getColor(ctxt, android.R.color.holo_blue_dark));
        tvName.setTextColor(Color.rgb(0,116,255));
        tvName.setTextSize(20);
        tvName.setTag(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // tvName.setAutoSizeTextTypeUniformWithConfiguration(18, 22, 2, TypedValue.COMPLEX_UNIT_SP);
        }
        tvName.setOnClickListener(allparticipants.getListener1());

        participantLayout.addView(tvName);

        // Button Start/Stop
        imgStartStop = new ImageButton(ctxt);
        imgStartStop.setImageResource(android.R.drawable.ic_media_play);
        imgStartStop.setTag(this);
        imgStartStop.setBackground(null);
        imgStartStop.setOnClickListener(allparticipants.getListener2());
        participantLayout.addView(imgStartStop);

        // Duration
        tvDuration = new TextView(ctxt);
        tvDuration.setText(R.string.initvalue2);
        tvDuration.setTypeface(typeface);
        tvDuration.setTextSize(26);
        tvDuration.setTextColor(ContextCompat.getColor(ctxt, android.R.color.holo_green_light));
        tvDuration.setTag(this);
        tvDuration.setOnClickListener(allparticipants.getListener4());
        participantLayout.addView(tvDuration);

        // Percentage

        progressBar = new ProgressBar(ctxt, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        progressBar.getProgressDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

        if (Build.VERSION.SDK_INT > 24) {
            progressBar.setProgress(1, false);
        }

        participantLayout.addView(progressBar);

        tvPercentage=new TextView(ctxt);
        tvPercentage.setText(R.string.initvalue1);
        tvPercentage.setTextSize(18);
        participantLayout.addView(tvPercentage);

        LinearLayout.LayoutParams wparams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f);
        LinearLayout.LayoutParams wparams2= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f);
        LinearLayout.LayoutParams wparams3= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.2f);
        LinearLayout.LayoutParams wparams4= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
        LinearLayout.LayoutParams wparams5= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
        LinearLayout.LayoutParams wparams6= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.4f);

        // Remove participant
        imgRemove = new ImageButton(ctxt);
        if (participantM.getFemale())
        {
            imgRemove.setImageResource(ctxt.getResources().getIdentifier("deluser_female_on", "mipmap", ctxt.getPackageName()));
        }
        else
        {
            imgRemove.setImageResource(ctxt.getResources().getIdentifier("deluser_on", "mipmap", ctxt.getPackageName()));
        }

        imgRemove.setBackground(null);
        imgRemove.setTag(this);
        imgRemove.setId(1000);
        imgRemove.setOnClickListener(allparticipants.getListener3());

        participantLayout.addView(imgRemove);
        participantLayout.setLayoutParams(lparams);

        // todo : revoir  le dimensionnement des view
        tvName.setLayoutParams(wparams1);
        wparams1.gravity =Gravity.CENTER_VERTICAL;
        wparams3.gravity =Gravity.CENTER_VERTICAL;
        wparams4.gravity =Gravity.CENTER_VERTICAL;
        wparams6.gravity =Gravity.CENTER_VERTICAL;
        imgStartStop.setLayoutParams(wparams2);
        tvDuration.setGravity(Gravity.CENTER_HORIZONTAL);
        tvDuration.setLayoutParams(wparams3);
        tvPercentage.setLayoutParams(wparams4);
        progressBar.setLayoutParams(wparams6);
        imgRemove.setLayoutParams(wparams5);
        tvDuration.setTypeface(m_Typeface);
        int orientation = ctxt.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showProgressBar();
        } else {
            hideProgressBar();
        }
    }

    public TextView getTvName()
    {
        return tvName;
    }

    public void setTvName(TextView tvName)
    {
        this.tvName = tvName;
    }
    public void toggleselect()
    {
        //allparticipants.ringtone.stop();
        if (selected)
        {
            System.out.println("selected");
            Toast.makeText(ctxt, tvName.getText() + ctxt.getResources().getString(R.string.talknomore), Toast.LENGTH_LONG).show();
            selected = false;
            tvName.setTypeface(null, Typeface.NORMAL);
            this.imgStartStop.setImageResource(android.R.drawable.ic_media_play);
        }
        else
        {
            System.out.println("not selected");
            Toast.makeText(ctxt, tvName.getText() + ctxt.getResources().getString(R.string.starttalking), Toast.LENGTH_LONG).show();
            selected = true;
            allparticipants.UpdateCounters();

            tvName.setTypeface(tvName.getTypeface(), Typeface.BOLD);
            imgStartStop.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
    public void select()
    {
        System.out.println("select");
        selected = true;

        tvName.setTypeface(tvName.getTypeface(), Typeface.BOLD);
        imgStartStop.setImageResource(android.R.drawable.ic_media_pause);
    }
    public void unselect()
    {
        System.out.println("unselect");
        selected = false;
        tvName.setTypeface(null, Typeface.NORMAL);
        this.imgStartStop.setImageResource(android.R.drawable.ic_media_play);
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void displayDuration(boolean landscape, boolean soundsignal)
    {
        int val = model.getDuration();
        int tot = allparticipants.getTotal();

        //System.out.println(m_Typeface.toString());
        //tvDuration.setTypeface(m_Typeface);
        tot = (tot==0?1:tot); // to avoid divide by 0
        if (val > 60 * allparticipants.getTimeMax())
        {
            tvDuration.setTextColor(Color.RED);
            //tvName.setTextColor(Color.RED);
            //tvPercentage.setTextColor(Color.RED);
            if (selected && soundsignal)
            {
                PlayNotification();
            }
        }
        else
        {
            tvDuration.setTextColor(ContextCompat.getColor(ctxt, android.R.color.holo_green_light));
            //tvName.setTextColor(Color.GREEN);
            //tvPercentage.setTextColor(Color.GREEN);
            //StopNotification();
        }

        tvDuration.setText(formatDuration(val, landscape));
        tvPercentage.setText(String.format(Locale.FRANCE, "%3d%%", (val*100)/tot));
        progressBar.setProgress((val*100)/tot);
    }
    public String formatDuration(int val, boolean hour2d)
    {
        if (hour2d)
            return String.format(Locale.FRANCE,"%02d:%02d:%02d", val/3600, (val%3600)/60, val%60);
        else
            return String.format(Locale.FRANCE,"%1d:%02d:%02d", val/3600, (val%3600)/60, val%60);
    }
    public String getPercentage()
    {
        int tot = allparticipants.getTotal();
        int val = model.getDuration();
        tot = (tot==0?1:tot); // to avoid divide by 0
        return String.format(Locale.FRANCE, "%3d%%", (val*100)/tot);
    }

    public ParticipantM getParticipantM()
    {
        return model;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public Context getCtxt()
    {
        return ctxt;
    }

    @Override
    public String toString()
    {
        return "ParticipantV{" +
                "tvName=" + tvName.getText() +
                '}';
    }

    private void PlayNotification()
    {
        if (allparticipants.silentMode)
        {
//            allparticipants.ringtone.stop();
            toneGen1.stopTone();
        }
        else
        {
            toneGen1.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,150);
        }
//        if (!allparticipants.ringtone.isPlaying())
        {
//            allparticipants.ringtone.play();
        }
    }
    private  void StopNotification()
    {
        //if (allparticipants.ringtone.isPlaying())
        {
//            allparticipants.ringtone.stop();
            toneGen1.stopTone();
        }
    }
    public void showProgressBar()
    {
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar()
    {
        progressBar.setVisibility(View.GONE);
    }
}
