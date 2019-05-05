package com.android.tony.mastervotex;

public class CandidateDatabase {
    private String candidateAdhaarNumber,candidateVoterId,candidateName,candidateParty,candidateConsitunecy,cadidateDateOfBirth,candidatePosition;

    CandidateDatabase(String candidateAdhaarNumber,String candidateVoterId,String candidateName,String candidateParty,String candidateConsitunecy,String cadidateDateOfBirth,String candidatePosition)
    {
        this.candidateAdhaarNumber = candidateAdhaarNumber;
        this.candidateVoterId = candidateVoterId;
        this.candidateName = candidateName;
        this.candidateParty = candidateParty;
        this.candidateConsitunecy = candidateConsitunecy;
        this.cadidateDateOfBirth =cadidateDateOfBirth;
        this.candidatePosition = candidatePosition;
    }

    CandidateDatabase(String candidateAdhaarNumber,String candidateVoterId,String candidateName)
    {
        this.candidateAdhaarNumber = candidateAdhaarNumber;
        this.candidateName = candidateName;
        this.candidateVoterId = candidateVoterId;
    }

    CandidateDatabase(String candidateAdhaarNumber,String candidateVoterId)
    {
        this.candidateAdhaarNumber = candidateAdhaarNumber;
        this.candidateVoterId = candidateVoterId;
    }

    public String getCadidateDateOfBirth() {
        return cadidateDateOfBirth;
    }

    public String getCandidateConsitunecy() {
        return candidateConsitunecy;
    }

    public String getCandidateAdhaarNumber() {
        return candidateAdhaarNumber;
    }

    public String getCandidateVoterId() {
        return candidateVoterId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public String getCandidateParty() {
        return candidateParty;
    }

    public String getCandidatePosition() {
        return candidatePosition;
    }
}
