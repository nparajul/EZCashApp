package com.example.ezcashapp;

/**
 *
 * NAME: Chats - Data Model Class for UsersActivity
 *
 * DESCRIPTION: This class retrieves data from the users Collection stored in the Firebase Database.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */


public class Users {

    String UID, name , status, image, online;

    public Users() {
    }

    /**
     *
     * NAME: AddedFriends() - constructor for AddedFriends class
     *
     * SYNOPSIS: Users(String uid, String name, String status, String image, String online)
     *           uid -> user id
     *           name -> username
     *           status -> user's status
     *           image -> user's profile image URL
     *           online -> user's online presence
     *
     * DESCRIPTION: This is the constructor of the Users class
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public Users(String uid, String name, String status, String image, String online) {
        this.UID = UID;
        this.name = name;
        this.status = status;
        this.image = image;
        this.online = online;
    }
    /**
     *
     * NAME: Users:getUid() - returns user's id
     *
     * SYNOPSIS: public String getUid()
     *
     * DESCRIPTION: This is an accessor function that returns the user's id as a string
     *
     * RETURNS: UID
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public String getUid() {
        return UID;
    }

    /**
     *
     * NAME: Users:setUid() - sets user's id
     *
     * SYNOPSIS: public String setUid(String UID)
     *           UID  -> user's id
     *
     * DESCRIPTION: This is a setter function. It sets the UID value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public void setUid(String UID) {
        this.UID = UID;
    }

    /**
     *
     * NAME: Users:getName() - returns user's name
     *
     * SYNOPSIS: public String getName()
     *
     * DESCRIPTION: This is an accessor function that returns the user's name as a string
     *
     * RETURNS: name
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public String getName() {
        return name;
    }
    /**
     *
     * NAME: Users:setName() - sets user's name
     *
     * SYNOPSIS: public String setName(String name)
     *           name  -> user's name
     *
     * DESCRIPTION: This is a setter function. It sets the name value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * NAME: Users:getStatus() - returns user's status
     *
     * SYNOPSIS: public String getStatus()
     *
     * DESCRIPTION: This is an accessor function that returns the user's status as a string
     *
     * RETURNS: status
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * NAME: Users:setStatus() - sets user's status
     *
     * SYNOPSIS: public String setStatus(String status)
     *           status  -> user's status
     *
     * DESCRIPTION: This is a setter function. It sets the status value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * NAME: Users:getName() - returns user's image URL
     *
     * SYNOPSIS: public String getImage()
     *
     * DESCRIPTION: This is an accessor function that returns the user's image URL as a string
     *
     * RETURNS: image
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * NAME: Users:setImage() - sets user's image URL
     *
     * SYNOPSIS: public String setImage(String image)
     *           image  -> user's image URL
     *
     * DESCRIPTION: This is a setter function. It sets the image value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * NAME: Users:getOnline() - returns user's online presence
     *
     * SYNOPSIS: public String getOnline()
     *
     * DESCRIPTION: This is an accessor function that returns the user's online presence as a string
     *
     * RETURNS: online
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public String getOnline() {
        return online;
    }

    /**
     *
     * NAME: Users:setOnline() - sets user's online presence
     *
     * SYNOPSIS: public String setOnline(String online)
     *           online  -> user's online presence
     *
     * DESCRIPTION: This is a setter function. It sets the online  value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 7/31/2020
     *
     */
    public void setOnline(String online) {
        this.online = online;
    }
}
