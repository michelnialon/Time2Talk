package com.nialon.time2talk.controller;

import com.nialon.time2talk.model.ParticipantM;
import com.nialon.time2talk.view.ParticipantV;

public class ParticipantC
{
    private ParticipantM participantM;
    private ParticipantV participantV;

    public ParticipantM getParticipantM()
    {
        return participantM;
    }

    public void setParticipantM(ParticipantM participantM)
    {
        this.participantM = participantM;
    }

    public ParticipantV getParticipantV()
    {
        return participantV;
    }

    public void setParticipantV(ParticipantV participantV)
    {
        this.participantV = participantV;
    }

    public ParticipantC(ParticipantM participantM, ParticipantV participantV)
    {
        this.participantM = participantM;
        this.participantV = participantV;
    }

    @Override
    public String toString()
    {
        return "ParticipantC{" +
                "participantM=" + participantM.toString() +
                '}';
    }
}
