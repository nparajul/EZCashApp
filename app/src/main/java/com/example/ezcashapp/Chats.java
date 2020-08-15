package com.example.ezcashapp;

/**
 *
 * NAME: Chats - Data Model Class for ChatActivity
 *
 * DESCRIPTION: This class retrieves data from the chat Collection stored in the Firebase Database.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */


public class Chats {

    /**
     * member variables
     */
    public boolean seen;
    public String timestamp;

    public Chats() {
    }

    /**
     *
     * NAME: Chats() - constructor for Chats class
     *
     * SYNOPSIS: Chats:Chats(boolean seen, String timestamp)
     *           seen -> boolean value of if a recent message was seen
     *           timestamp -> string value of when the recent message was seen
     *
     * DESCRIPTION: This is the constructor of the Chats class
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public Chats(boolean seen, String timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }

    /**
     *
     * NAME: Chats:isSeen() - returns seen value
     *
     * SYNOPSIS: public boolean isSeen()
     *
     * DESCRIPTION: This is an accessor function that returns the seen value as a boolean
     *
     * RETURNS: true if recent message is seen
     *          false if recent message is not seen
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     *
     * NAME: Chats:setSeen() - sets seen value
     *
     * SYNOPSIS: public void setSeen(boolean seen)
     *           seen -> boolean seen value
     *
     * DESCRIPTION: This is a setter function. It sets the seen value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    /**
     *
     * NAME: Chats:getTimestamp() - returns the timestamp
     *
     * SYNOPSIS: public String getTimestamp()
     *
     * DESCRIPTION: This is an accessor function that returns the timestamp of when the message was last seen
     *
     * RETURNS: timestamp
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * NAME: Chats:setTimestamp() - sets timestamp value
     *
     * SYNOPSIS: public void setTimestamp(String timestamp)
     *           timestamp -> timestamp value
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
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
