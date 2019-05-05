package com.android.tony.votex;

public class ElectionDatabase {
    String consitutecy, dateOfElection, position;

    ElectionDatabase(String consitutecy, String dateOfElection, String position) {
        this.consitutecy = consitutecy;
        this.dateOfElection = dateOfElection;
        this.position = position;
    }

    ElectionDatabase()
    {

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
