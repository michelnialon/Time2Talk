package com.nialon.time2talk.controller;

import java.util.ArrayList;

public class ParticipantsC
{
    private ArrayList allParticipants;
    private int total;
    private int timeMax;

    public ParticipantsC(int timeMax)
    {
        allParticipants = new ArrayList<ParticipantC>();
        total = 0;
        this.timeMax = timeMax;
    }

    public void add(ParticipantC participantC)
    {
        allParticipants.add(participantC);
    }

    public void update()
    {
        System.out.println("update "+ allParticipants.size());
        for (int counter = 0; counter < allParticipants.size(); counter++)
        {

            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            if (p.getParticipantV().isSelected())
            {
                p.getParticipantM().incDuration();
                total ++;
                System.out.println(p);
            }
        }
    }
    public void unselectAll()
    {
        System.out.println("unselectall ");
        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            p.getParticipantV().unselect();
        }
    }
    public void displayAll()
    {
        System.out.println("displayAll ");
        for (int counter = 0; counter < allParticipants.size(); counter++)
        {
            ParticipantC p  = (ParticipantC)allParticipants.get(counter);
            p.getParticipantV().displayDuration();
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
        total = 0;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public int getTimeMax()
    {
        return timeMax;
    }

    public void setTimeMax(int timeMax)
    {
        this.timeMax = timeMax;
    }
}
