package com.nialon.time2talk.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;

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

    public LinearLayout getParticipantLayout()
    {
        return participantLayout;
    }

    public ParticipantV(final ParticipantM participantM, final ParticipantsC allparticpants, Context context)
    {
        Typeface typeface;
        typeface = Typeface.createFromAsset(context.getAssets(), "LED.Font.ttf");
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        lparams.setMargins(5,0,5,0);
        //lparams.gravity = Gravity.CENTER_HORIZONTAL;
        this.participantLayout = new LinearLayout(context);

        participantLayout.setOrientation(LinearLayout.HORIZONTAL);
        //participantLayout.setBackgroundColor(Color.rgb(0,255,255));

        this.model = participantM;
        this.allparticipants = allparticpants;
        this.ctxt = context;
        this.selected = false;

        tvName=new TextView(context);
        tvName.setText(model.getName());
        tvName.setTextColor(ContextCompat.getColor(ctxt, android.R.color.holo_blue_dark));
        tvName.setTextSize(22);
        tvName.setTag(this);
        tvName.setOnClickListener(allparticipants.getListener1());

        participantLayout.addView(tvName);

        // Button Start/Stop
        imgStartStop = new ImageButton(ctxt);
        imgStartStop.setImageResource(android.R.drawable.ic_media_play);
        imgStartStop.setTag(this);
        imgStartStop.setOnClickListener(allparticipants.getListener2());
        participantLayout.addView(imgStartStop);

        // Duration
        tvDuration = new TextView(ctxt);
        tvDuration.setText(R.string.initvalue2);
        tvDuration.setTypeface(typeface);
        tvDuration.setTextSize(30);
        tvDuration.setTextColor(ContextCompat.getColor(ctxt, android.R.color.holo_green_light));
        participantLayout.addView(tvDuration);

        // Percentage
        /*
        progressBar = new ProgressBar(ctxt);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        progressBar.setProgress(1, false);
        */


        //participantLayout.addView(progressBar);

        tvPercentage=new TextView(ctxt);
        tvPercentage.setText(R.string.initvalue1);
        tvPercentage.setTextSize(24);
        participantLayout.addView(tvPercentage);



        LinearLayout.LayoutParams wparams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f);
        LinearLayout.LayoutParams wparams2= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f);
        LinearLayout.LayoutParams wparams3= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        LinearLayout.LayoutParams wparams4= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
        LinearLayout.LayoutParams wparams5= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f);


        // Remove participant
        imgRemove = new ImageButton(ctxt);
        imgRemove.setImageResource(android.R.drawable.ic_menu_delete);
        imgRemove.setTag(this);
        imgRemove.setOnClickListener(allparticipants.getListener3());

        participantLayout.addView(imgRemove);
        participantLayout.setLayoutParams(lparams);

        // todo : revoir  le dimensionnement des view
        tvName.setLayoutParams(wparams1);
        imgStartStop.setLayoutParams(wparams2);
        tvDuration.setGravity(Gravity.CENTER_HORIZONTAL);
        tvDuration.setLayoutParams(wparams3);
        tvPercentage.setLayoutParams(wparams4);
        //progressBar.setLayoutParams(wparams4);
        imgRemove.setLayoutParams(wparams5);
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
        if (selected)
        {
            System.out.println("selected");
            Toast.makeText(ctxt, "Participant " + tvName.getText() + " does not talk anymore.", Toast.LENGTH_LONG).show();
            selected = false;
            tvName.setTypeface(null, Typeface.NORMAL);
            this.imgStartStop.setImageResource(android.R.drawable.ic_media_play);
        }
        else
        {
            System.out.println("not selected");
            Toast.makeText(ctxt, "Participant " + tvName.getText() + " is starting to talk.", Toast.LENGTH_LONG).show();
            selected = true;

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

    public void displayDuration()
    {
        int val = model.getDuration();
        int tot = allparticipants.getTotal();
        tot = (tot==0?1:tot); // to avoid divide by 0
        if (val > 60 * allparticipants.getTimeMax())
        {
            tvDuration.setTextColor(Color.RED);
        }
        else
        {
            tvDuration.setTextColor(Color.GREEN);
        }

        tvDuration.setText((String.format(Locale.FRANCE,"%1d:%02d:%02d", val/3600, val/60, val%60)));
        tvPercentage.setText(String.format(Locale.FRANCE, "%3d%%", (val*100)/tot));
     //   progressBar.setProgress((val*100)/tot);
     //   progressBar.setProgress(50);
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
}
