package com.nialon.time2talk.model;

public class ParticipantM
{
    private String  mName;
    private Integer mDuration;
    private Integer id;
    private Boolean mFemale;

    public Boolean getFemale() {
        return mFemale;
    }

    public ParticipantM(String mName, Integer id, Integer duration, Boolean female)
    {
        this.mName = mName;
        this.mDuration = duration;
        this.id = id;
        this.mFemale = female;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String mName)
    {
        this.mName = mName;
    }

    public void setFemale()
    {
        this.mFemale = true;
    }
    public void setMale()
    {
        this.mFemale = false;
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
        //System.out.println(mName + " : " + mDuration + " secondes.");
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public String toString()
    {
        return "ParticipantM{" +
                "Name='" + mName + '\'' +
                ", Duration=" + mDuration +
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
