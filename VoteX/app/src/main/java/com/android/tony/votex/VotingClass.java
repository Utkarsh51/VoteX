package com.android.tony.votex;

public class VotingClass {
    String givenOn,givenTo,consituency,position;
    VotingClass(String givenOn,String givenTo,String consituency,String position)
    {
        this.givenOn = givenOn;
        this.givenTo = givenTo;
        this.consituency = consituency;
        this.position = position;
    }

    VotingClass()
    {

    }

    public String getGivenOn() {
        return givenOn;
    }

    public String getGivenTo() {
        return givenTo;
    }
}
