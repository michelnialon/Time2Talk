package com.nialon.time2talk.model;

public class ParticipantM
{
    private String  mName;
    private Integer mDuration;
    private Integer id;

    public ParticipantM(String mName)
    {
        this.mName = mName;
        this.mDuration = 0;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String mName)
    {
        this.mName = mName;
    }

    public Integer getDuration()
    {
        return mDuration;
    }

    public void setDuration(Integer mDuration)
    {
        this.mDuration = mDuration;
    }

    public void incDuration()
    {
        this.mDuration++;
    }

    @Override
    public String toString()
    {
        return "ParticipantM{" +
                "mName='" + mName + '\'' +
                ", mDuration=" + mDuration +
                '}';
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}