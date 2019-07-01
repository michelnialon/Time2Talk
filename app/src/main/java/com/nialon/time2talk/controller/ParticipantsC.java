package com.nialon.time2talk.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nialon.time2talk.view.ParticipantV;

import java.util.ArrayList;

public class ParticipantsC
{
    private ArrayList allParticipants;
    private int totalTime;
    private int timeMax;
    private Boolean multipleSpeakers;
    private Boolean soundsignal;
    private String stringtone;
    private Ringtone ringtone;

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
        allParticipants.add(participantC);
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
            p.getParticipantV().displayDuration();
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
            infos += p.getParticipantM().getName();
            infos += " : ";
            infos += p.getParticipantM().getDuration();
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
            infos += p.getParticipantM().getName();
            infos += "</td><td>";
            infos += p.getParticipantM().getDuration();
            infos += "</td></tr>";
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
                    //participantM.setName(edtText.getText().toString());
                    Toast.makeText(ctxt, "Le nom a été modifié" + " :" +edtText.getText() , Toast.LENGTH_LONG).show();
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
}
