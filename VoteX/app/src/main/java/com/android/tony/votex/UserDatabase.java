package com.android.tony.votex;

// UserDataBase Modal Class, for storing and retriving user data

public class UserDatabase {
    String userAdhaarNumber, userName, userVoterId, userEmail, userDateOfBirth, userPhoneNumber, userCity, userState, userConstitunecy;

    UserDatabase(String userAdhaarNumber, String userName, String userVoterId, String userEmail, String userDateOfBirth, String userPhoneNumber, String userCity, String userState,String userConstitunecy) {
        this.userAdhaarNumber = userAdhaarNumber;
        this.userName = userName;
        this.userVoterId = userVoterId;
        this.userEmail = userEmail;
        this.userDateOfBirth = userDateOfBirth;
        this.userPhoneNumber = userPhoneNumber;
        this.userCity = userCity;
        this.userState = userState;
        this.userConstitunecy = userConstitunecy;
    }

    UserDatabase(String userConstitunecy)
    {
        this.userConstitunecy = userConstitunecy;
    }

    UserDatabase()
    {

    }

    public String getUserState() {
        return userState;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserAdhaarNumber() {
        return userAdhaarNumber;
    }

    public String getUserVoterId() {
        return userVoterId;
    }

    public String getUserConstitunecy() {
        return userConstitunecy;
    }
}
