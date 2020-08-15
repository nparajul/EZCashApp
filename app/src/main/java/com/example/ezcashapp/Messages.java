package com.example.ezcashapp;

/**
 *
 * NAME: Messages - Data Model Class for MessagesActivity
 *
 * DESCRIPTION: This class retrieves data from the messages Collection stored in the Firebase Database.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */

public class Messages {

    /**
     * member variables
     */
    private String message,time,type,from;
    Boolean seen;


    public Messages() {
    }

    /**
     *
     * NAME: Messages() - constructor for Messages class
     *
     * SYNOPSIS: public Messages(String message, String time, String type, Boolean seen, String from)
     *           message -> the message text/image
     *           time ->    timevalue of when the message was sent
     *           type -> type of message (text or image)
     *           seen -> value indicating if the message is seen
     *           from -> the user who sent the message
     *
     * DESCRIPTION: This is the constructor of the Messages class
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */

    public Messages(String message, String time, String type, Boolean seen) {
        this.message = message;
        this.time = time;
        this.type = type;
        this.seen = seen;
    }

    public Messages(String from) {
        this.from = from;
    }


    /**
     *
     * NAME: Messages:getFrom() - returns the user id of the sender
     *
     * SYNOPSIS: public String getFrom()
     *
     * DESCRIPTION: This is an accessor function that returns the user id of the sender
     *
     * RETURNS: sender's user id as a string
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * NAME: Messages:setFrom() - sets the user id of the sender
     *
     * SYNOPSIS: public void setFrom(String from)
     *           from -> sender's user id
     *
     * DESCRIPTION: This is a setter function. It sets the from value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setFrom(String from) {
        this.from = from;
    }


    /**
     *
     * NAME: Messages:getMessage() - returns the message text/image Url
     *
     * SYNOPSIS: public String getMessage()
     *
     * DESCRIPTION: This is an accessor function that returns the message body
     *
     * RETURNS: message
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * NAME: Messages:setMessage() - sets the message body
     *
     * SYNOPSIS: public void setMessage(String message)
     *           message -> the message text/image Url
     *
     * DESCRIPTION: This is a setter function. It sets the message value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020

     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * NAME: Messages:getMessage() - returns the timestamp of the message
     *
     * SYNOPSIS: public String getTime()
     *
     * DESCRIPTION: This is an accessor function that returns the timestamp of when the message was sent
     *
     * RETURNS: time
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * NAME: Messages:setTime() - sets the timestamp
     *
     * SYNOPSIS: public void setTime(String time)
     *           time -> the message timestamp
     *
     * DESCRIPTION: This is a setter function. It sets the time value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020

     */
    public void setTime(String time) {
        this.time = time;
    }


    /**
     *
     * NAME: Messages:getMessage() - returns the message type
     *
     * SYNOPSIS: public String getType()
     *
     * DESCRIPTION: This is an accessor function that returns the type of message
     *
     * RETURNS: type
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getType() {
        return type;
    }

    /**
     *
     * NAME: Messages:setType() - sets the message type
     *
     * SYNOPSIS: public void setType(String type)
     *           type -> the message type
     *
     * DESCRIPTION: This is a setter function. It sets the type value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020

     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * NAME: Messages:getMessage() - returns if the message was seen or not
     *
     * SYNOPSIS: public Boolean getSeen()
     *
     * DESCRIPTION: This is an accessor function that returns the seen value
     *
     * RETURNS: seen
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public Boolean getSeen() {
        return seen;
    }

    /**
     *
     * NAME: Messages:setSeen() - sets the seen type
     *
     * SYNOPSIS: public void setSeen(Boolean seen)
     *           seen -> value indicating if a message was seen
     *
     * DESCRIPTION: This is a setter function. It sets the seen value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020

     */
    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
