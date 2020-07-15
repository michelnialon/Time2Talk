package com.nialon.time2talk.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.nialon.time2talk.R;
import com.nialon.time2talk.model.ParticipantM;
import com.nialon.time2talk.view.ParticipantV;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class ParticipantsC
{
    private ArrayList allParticipantsList;
    private int totalTime;
    private int timeMax;
    private Boolean multipleSpeakers;
    private Boolean soundsignal;
    //public Ringtone ringtone;
    private boolean landscape;
    public boolean silentMode;
    private int lastParticipant = 0;
    private Handler handler1;
    private Runnable runnable1;

    private Context ctxt;

    public ParticipantsC(int timeMax, Context context, boolean multipleSpeakers, boolean soundsignal)
    {
        allParticipantsList = new ArrayList<ParticipantC>();
        totalTime = 0;
        this.timeMax = timeMax;
        this.multipleSpeakers = multipleSpeakers;
        this.soundsignal = soundsignal;
        //this.ringtone = ringtone;
        this.silentMode=false;
        this.ctxt = context;

        handler1 = new Handler();
        runnable1 = new Runnable()
        {
            public void run()
            {
                if (getCount() > 0)
                {
                    update();
                    displayAll();
                }
                if (nbselelected() > 0)
                {
                    handler1.postDelayed(this, 1000);
                }
                else
                {
                    handler1.removeCallbacks(runnable1);
                }
            }
        };
    }

    public void add(ParticipantC participantC)
    {
        try {
            allParticipantsList.add(participantC);
        }
        catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
        participantC.getParticipantV().setIndex(allParticipantsList.size()-1);
        lastParticipant++;
    }
    public ParticipantC addItem(Typeface typeface, Activity activity, String attendeeName, int id, int attendeeDuration, Boolean female)
    {
        Log.d("addItem", "ParticipantsC");

        ParticipantM participantM = new ParticipantM(attendeeName, id, attendeeDuration, female);
        ParticipantV participantV = new ParticipantV(participantM, this, activity, typeface);
        ParticipantC participantC = new ParticipantC(participantM, participantV);

        add(participantC);
        return participantC;
    }

    public void update()
    {
        System.out.println("update " + nbselelected() + " / " + allParticipantsList.size());
        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
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
    public int nbselelected()
    {
        int nb=0;
        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
            if (p.getParticipantV().isSelected())
            {
                nb ++;
            }
        }
        return nb;
    }
    private void unselectAllExcept(ParticipantV participantV)
    {
        System.out.println("unselectall ");
        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
            if (p.getParticipantV() != participantV)
            {
                p.getParticipantV().unselect();
            }
        }
    }
    public void displayAll()
    {
        //System.out.println("displayAll ");
        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
            p.getParticipantV().displayDuration(landscape, soundsignal);
        }
    }
    public void UpdateCounters()
    {
        System.out.println("UpdateCounters");
        handler1.removeCallbacks(runnable1);
        handler1.postDelayed(runnable1, 1000);
    }
    public void showProgressBar(Boolean visible)
    {
        //System.out.println("displayAll ");
        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
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
        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
            p.getParticipantM().setDuration(0);
        }
        totalTime = 0;
        unselectAllExcept(null);
        handler1.removeCallbacks(runnable1);
    }

    public String getInformations()
    {
        String infos = "";
        System.out.println("getInformations ");

        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
            infos += String.format(Locale.FRANCE,"%-20s : %s (%s)\n", p.getParticipantV().getParticipantM().getName(),
                    p.getParticipantV().formatDuration(p.getParticipantM().getDuration(), true),
                    p.getParticipantV().getPercentage()
            );

            infos += "\n";
        }
        return infos;
    }
    public String getInformationsHTML()
    {
        StringBuilder infos = new StringBuilder("");
        System.out.println("getInformations ");

        infos.append("<html><head></head><body>");
        infos.append("<table border=0>");

        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            infos.append("<tr>");
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
            infos.append("<td>");
            infos.append(String.format(Locale.FRANCE,"%-20s", p.getParticipantV().getParticipantM().getName()));
            infos.append("</td><td>");
            infos.append(p.getParticipantV().formatDuration(p.getParticipantM().getDuration(), true));
            infos.append("</td>");
            infos.append("<td>" + p.getParticipantV().getPercentage() + "</td>");
            infos.append("</tr></br>");
        }
        infos.append("</table>");
        infos.append("</body></html>");
        return infos.toString();
    }
    public void SaveMeeting(String meetingName)
    {
        SharedPreferences sp = ctxt.getSharedPreferences(meetingName, MODE_PRIVATE);
        SharedPreferences.Editor sedt = sp.edit();
        sedt.clear();
        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
            if (p.getParticipantM().getFemale()) {
                sedt.putString("F" + p.getParticipantV().getParticipantM().getName(), p.getParticipantM().getDuration().toString());
            }
            else {
                sedt.putString("M" + p.getParticipantV().getParticipantM().getName(), p.getParticipantM().getDuration().toString());
            }
        }
        sedt.apply();
    }
    public void ClearMeeting()
    {
        ParticipantV pV;

        for (int counter = 0; counter < allParticipantsList.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipantsList.get(counter);
            pV = p.getParticipantV();
            pV.getParticipantLayout().setVisibility(View.GONE);
        }
        allParticipantsList.clear();
        lastParticipant=0;
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
            edtText.setFocusable(true);
//            edtText.requestFocus();
//            InputMethodManager mngr = (InputMethodManager) ctxt.getSystemService(Context.INPUT_METHOD_SERVICE);
//            mngr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            final Switch swFemale = new Switch(ctxt);
            swFemale.setText(ctxt.getResources().getString(R.string.woman));
            swFemale.setChecked(((ParticipantV)v.getTag()).getParticipantM().getFemale());
            swFemale.setThumbTextPadding(10);

            AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
            builder.setTitle(ctxt.getResources().getString(R.string.modification));
           // builder.setMessage(ctxt.getResources().getString(R.string.entername));
            builder.setCancelable(true);
            builder.setView(edtText);
            LinearLayout relative=new LinearLayout(ctxt);
            relative.setOrientation(LinearLayout.VERTICAL);
           // relative.addView(edtText);
            relative.addView(swFemale);
            builder.setCustomTitle(relative);
            builder.setNeutralButton(ctxt.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
            {
                @Override
                @SuppressLint("ResourceType")
                public void onClick(DialogInterface dialog, int which)
                {
                    ((TextView)v).setText(edtText.getText().toString());
                    ((ParticipantV)v.getTag()).getParticipantM().setName(edtText.getText().toString());
                    if (swFemale.isChecked())
                    {
                        ((ParticipantV)v.getTag()).getParticipantM().setFemale();
                        ImageButton imageButton = ((ParticipantV)v.getTag()).getParticipantLayout().findViewById(1000);
                        imageButton.setImageResource(ctxt.getResources().getIdentifier("deluser_female_on", "mipmap", ctxt.getPackageName()));
                    }
                    else
                    {
                        ((ParticipantV)v.getTag()).getParticipantM().setMale();
                        ImageButton imageButton = ((ParticipantV)v.getTag()).getParticipantLayout().findViewById(1000);
                        imageButton.setImageResource(ctxt.getResources().getIdentifier("deluser_on", "mipmap", ctxt.getPackageName()));
                    }

                    //getparticipantM.setName(edtText.getText().toString());
                   // Toast.makeText(ctxt,  ctxt.getResources().getString(R.string.namemodified)+ " :" +edtText.getText() , Toast.LENGTH_LONG).show();
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
                    allParticipantsList.remove(pV.getIndex());
                    for (int i = pV.getIndex(); i < allParticipantsList.size(); i++)
                    {
                        ParticipantC pC = (ParticipantC)allParticipantsList.get(i);
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
    public View.OnClickListener getListener4()
    {
        return Listener4;
    }

    private View.OnClickListener Listener4 = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            System.out.println("listener4" + v.getParent() );
            final EditText edtText = new EditText(ctxt);
            edtText.setText(((ParticipantV)v.getTag()).getParticipantM().getDuration().toString());
            edtText.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtText.setPaintFlags(0);
            edtText.setSingleLine();

            AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
            builder.setTitle(ctxt.getResources().getString(R.string.modification));
            builder.setMessage(ctxt.getResources().getString(R.string.entervalue));
            builder.setCancelable(true);
            builder.setView(edtText);
            builder.setNeutralButton(ctxt.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    int newvalue = 0;
                    try
                    {
                        newvalue = Integer.parseInt(edtText.getText().toString());
                        if (newvalue > 35999) {newvalue=35999;}
                    }
                    catch(Exception e) {};
                    ((ParticipantV)v.getTag()).getParticipantM().setDuration(newvalue);
                    ((ParticipantV)v.getTag()).displayDuration(landscape,soundsignal);
                    //getparticipantM.setName(edtText.getText().toString());
                    //Toast.makeText(ctxt,  ctxt.getResources().getString(R.string.namemodified)+ " :" +edtText.getText() , Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
        }
    };
    public int getCount()
    {
        return allParticipantsList.size();
    }

    public int getLast()
    {
        return lastParticipant;
    }

   /* public void setRingtone(Ringtone ringtone)
    {
        this.ringtone = ringtone;
    }
*/
    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }
}
