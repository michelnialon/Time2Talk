package com.nialon.time2talk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public LinearLayout getParticipantLayout()
    {
        return participantLayout;
    }

    public ParticipantV(final ParticipantM participantM, final ParticipantsC allparticpants, Context context)
    {
        Typeface typeface;
        typeface = Typeface.createFromAsset(context.getAssets(), "LED.Font.ttf");
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        this.participantLayout = new LinearLayout(context);

        participantLayout.setOrientation(LinearLayout.HORIZONTAL);
        //participantLayout.setBackgroundColor(Color.rgb(0,255,255));
        participantLayout.setPadding(5,0,5,0);

        this.model = participantM;
        this.allparticipants = allparticpants;
        this.ctxt = context;
        this.selected = false;

        tvName=new TextView(context);
        tvName.setText(model.getName());
        tvName.setLayoutParams(lparams);
        tvName.setTextColor(ctxt.getResources().getColor(android.R.color.holo_blue_dark));
        tvName.setTextSize(24);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final EditText edtText = new EditText(ctxt);
                edtText.setText(((TextView)v).getText());
                edtText.setPaintFlags(0);

                AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
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
                        participantM.setName(edtText.getText().toString());
                        Toast.makeText(ctxt, "Le nom a été modifié " + edtText.getText() , Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });
        participantLayout.addView(tvName);

        // Button Start/Stop
        imgStartStop = new ImageButton(ctxt);
        imgStartStop.setImageResource(android.R.drawable.ic_media_play);
        imgStartStop.setTag(0);
        imgStartStop.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                allparticpants.unselectAll();
                toggleselect();
            }

        });
        participantLayout.addView(imgStartStop);

        // Duration
        tvDuration = new TextView(ctxt);
        tvDuration.setText(R.string.initvalue2);
        tvDuration.setTypeface(typeface);
        tvDuration.setTextSize(32);
        tvDuration.setTextColor(ctxt.getResources().getColor(android.R.color.holo_green_light));
        participantLayout.addView(tvDuration);

        // Percentage
        tvPercentage=new TextView(ctxt);
        tvPercentage.setText(R.string.initvalue1);
        tvPercentage.setTextSize(24);
        participantLayout.addView(tvPercentage);
        LinearLayout.LayoutParams wparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        LinearLayout.LayoutParams wparams2= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
        LinearLayout.LayoutParams wparams3= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f);


        // Remove participant
        imgRemove = new ImageButton(ctxt);
        imgRemove.setImageResource(android.R.drawable.ic_menu_delete);
        imgRemove.setTag(0);
        imgRemove.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(ctxt, "Participant " + tvName.toString() + " removed ", Toast.LENGTH_LONG).show();
            }

        });
        participantLayout.addView(imgRemove);

        tvName.setLayoutParams(wparams3);
        imgStartStop.setLayoutParams(wparams2);
        tvDuration.setLayoutParams(wparams);
        tvPercentage.setLayoutParams(wparams2);
        imgRemove.setLayoutParams(wparams2);
    }

    public TextView getTvName()
    {
        return tvName;
    }

    public void setTvName(TextView tvName)
    {
        this.tvName = tvName;
    }
    private void toggleselect()
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
        System.out.println("not selected");
        selected = true;

        tvName.setTypeface(tvName.getTypeface(), Typeface.BOLD);
        imgStartStop.setImageResource(android.R.drawable.ic_media_pause);
    }
    public void unselect()
    {
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
    }
}
