package com.nialon.time2talk.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nialon.time2talk.R;
import com.nialon.time2talk.view.ParticipantV;

import java.util.ArrayList;
import java.util.Locale;

public class ParticipantsC
{
    private ArrayList allParticipants;
    private int totalTime;
    private int timeMax;
    private Boolean multipleSpeakers;
    private Boolean soundsignal;
    private String stringtone;
    public Ringtone ringtone;
    private boolean landscape;
    public boolean silentMode;

    private Context ctxt;

    public ParticipantsC(int timeMax, Context context, boolean multipleSpeakers, boolean soundsignal, Ringtone ringtone)
    {
        allParticipants = new ArrayList<ParticipantC>();
        totalTime = 0;
        this.timeMax = timeMax;
        this.multipleSpeakers = multipleSpeakers;
        this.soundsignal = soundsignal;
        this.ringtone = ringtone;
        this.ctxt = context;
    }

    public void add(ParticipantC participantC)
    {
        try {
            allParticipants.add(participantC);
        }
        catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
        participantC.getParticipantV().setIndex(allParticipants.size()-1);
    }

    public void update()
    {
        System.out.println("update "+ allParticipants.size());
        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            //ParticipantV p  = (ParticipantV)allParticipants.get(counter);
            if (p.getParticipantV().isSelected())
            //if (p.isSelected())
            {
                p.getParticipantM().incDuration();
                totalTime ++;
                //System.out.println(p);
            }
        }
    }
    private void unselectAllExcept(ParticipantV participantV)
    {
        System.out.println("unselectall ");
        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            if (p.getParticipantV() != participantV)
            {
                p.getParticipantV().unselect();
            }
        }
    }
    public void displayAll()
    {
        //System.out.println("displayAll ");
        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            p.getParticipantV().displayDuration(landscape, soundsignal);
        }
    }
    public void showProgressBar(Boolean visible)
    {
        //System.out.println("displayAll ");
        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            if (visible)
            {
                p.getParticipantV().showProgressBar();
            }
            else
            {
                p.getParticipantV().hideProgressBar();
            }
        }
    }
    public void reset()
    {
        System.out.println("reset ");
        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            p.getParticipantM().setDuration(0);
        }
        totalTime = 0;
        unselectAllExcept(null);
    }

    public String getInformations()
    {
        String infos = "";
        System.out.println("getInformations ");

        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            infos += String.format(Locale.FRANCE,"%-20.20s : %s (%s)", p.getParticipantV().getParticipantM().getName(),
                    p.getParticipantV().formatDuration(p.getParticipantM().getDuration(), true),
                    p.getParticipantV().getPercentage()
            );

            infos += "\n";
        }
        return infos;
    }
    public String getInformationsHTML()
    {
        String infos = "";
        System.out.println("getInformations ");

        infos += "<table border=0>";

        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            infos += "<tr>";
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            infos += "<td>";
            infos += String.format(Locale.FRANCE,"%-20s", p.getParticipantV().getParticipantM().getName());
            infos += "</td><td>";
            infos += p.getParticipantV().formatDuration(p.getParticipantM().getDuration(), true);
            infos += "</td>";
            infos += "<td>" + p.getParticipantV().getPercentage() + "/td>";
            infos += "</tr>";
        }
        infos += "</table>";
        return infos;
    }

    public int getTotal()
    {
        return totalTime;
    }

    public void setTotal(int total)
    {
        this.totalTime = total;
    }

    public int getTimeMax()
    {
        return timeMax;
    }

    public void setTimeMax(int timeMax)
    {
        this.timeMax = timeMax;
    }

    public void setMultipleSpeakers(Boolean ms)
    {
        this.multipleSpeakers = ms;
    }

    public void setSoundSignal(Boolean ss)
    {
        this.soundsignal = ss;
    }

    public View.OnClickListener getListener1()
    {
        return Listener1;
    }

    private View.OnClickListener Listener1 = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            System.out.println("listener1" + v.getParent() );
            final EditText edtText = new EditText(ctxt);
            edtText.setText(((TextView)v).getText());
            edtText.setPaintFlags(0);
            edtText.setSingleLine();

            AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
            builder.setTitle(ctxt.getResources().getString(R.string.modification));
            builder.setMessage(ctxt.getResources().getString(R.string.entername));
            builder.setCancelable(true);
            builder.setView(edtText);
            builder.setNeutralButton(ctxt.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ((TextView)v).setText(edtText.getText().toString());
                    ((ParticipantV)v.getTag()).getParticipantM().setName(edtText.getText().toString());
                    //getparticipantM.setName(edtText.getText().toString());
                    Toast.makeText(ctxt,  ctxt.getResources().getString(R.string.namemodified)+ " :" +edtText.getText() , Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
        }
    };

    public View.OnClickListener getListener2()
    {
        return Listener2;
    }

    private View.OnClickListener Listener2 = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            ParticipantV pV;
            System.out.println("listener2" + v.getParent() );

            pV = (ParticipantV)v.getTag();
            if (!multipleSpeakers)
            {
                unselectAllExcept(pV);
            }
            pV.toggleselect();
        }
    };
    public View.OnClickListener getListener3()
    {
        return Listener3;
    }

    private View.OnClickListener Listener3 = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
            builder.setTitle(ctxt.getResources().getString(R.string.confirmation));
            builder.setMessage(ctxt.getResources().getString(R.string.confirmparticipantsupression));
            builder.setCancelable(false);
            builder.setPositiveButton(ctxt.getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ParticipantV pV;
                    pV = (ParticipantV)v.getTag();
                    System.out.println("Remove " + pV.toString() + " " + pV.getIndex());
                    pV.getParticipantLayout().setVisibility(View.GONE);
                    allParticipants.remove(pV.getIndex());
                    for (int i = pV.getIndex(); i < allParticipants.size(); i++)
                    {
                        ParticipantC pC = (ParticipantC)allParticipants.get(i);
                        pC.getParticipantV().setIndex(pC.getParticipantV().getIndex()-1);
                    }
                   Toast.makeText(ctxt, ctxt.getResources().getString(R.string.participantsuppressed), Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton(ctxt.getResources().getString(R.string.no), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.show();
        }
    };

    public int getCount()
    {
        return allParticipants.size();
    }

    public void setRingtone(Ringtone ringtone)
    {
        this.ringtone = ringtone;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }
}
