package com.android.tony.mastervotex;

public class ElectionDatabase {
    String consitutecy,dateOfElection,position;

    ElectionDatabase(String consitutecy, String dateOfElection, String position)
    {
        this.consitutecy = consitutecy;
        this.dateOfElection = dateOfElection;
        this.position = position;
    }

    public String getConsitutecy() {
        return consitutecy;
    }

    public String getDateOfElection() {
        return dateOfElection;
    }

    public String getPosition() {
        return position;
    }
}
