package com.nialon.time2talk;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

public class Participant
{
    TextView m_tvName;
    TextView m_tvDuration;
    TextView m_tvPercentage;
    ImageButton m_imgStartStop;
    ImageButton m_imgRemove;

    Participant(String ParticipantName, Context cntxt)
    {
        m_tvName = new TextView(cntxt);
        m_tvName.setText(ParticipantName);
        m_tvName.setTextColor(cntxt.getResources().getColor(android.R.color.holo_blue_dark));
        m_tvName.setTextSize(24);
        m_tvDuration = new TextView(cntxt);
        m_tvPercentage = new TextView(cntxt);
        //img1.setImageResource(android.R.drawable.ic_media_play);
    }
}
