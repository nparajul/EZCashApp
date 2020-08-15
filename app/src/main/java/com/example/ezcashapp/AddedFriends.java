package com.example.ezcashapp;

/**
 *
 * NAME: AddedFriends - Data Model Class for AddedFriends Activity
 *
 * DESCRIPTION: This class retrieves data from the friends Collection stored in the Firebase Database.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */

public class AddedFriends {

    /**
     * variable for the timestamp value
     */
    public String date;

    public AddedFriends() {

    }
    /**
     *
     * NAME: AddedFriends() - constructor for AddedFriends class
     *
     * SYNOPSIS: AddedFriends:AddedFriends(String date)
     *           date -> timestamp
     *
     * DESCRIPTION: This is the constructor of the AddedFriends class
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public AddedFriends(String date) {
        this.date = date;
    }

    /**
     *
     * NAME: AddedFriends:getDate() - returns timestamp value
     *
     * SYNOPSIS: public String getDate()
     *
     * DESCRIPTION: This is an accessor function that returns the timestamp as a string
     *
     * RETURNS: timestamp
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public String getDate() {
        return date;
    }


    /**
     *
     * NAME: AddedFriends:setDate() - sets timestamp value
     *
     * SYNOPSIS: public String setDate(String date)
     *           date  -> timestamp value
     *
     * DESCRIPTION: This is a setter function. It sets the timestamp value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public void setDate(String date) {
        this.date = date;
    }
}
