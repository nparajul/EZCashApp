package com.example.ezcashapp;

/**
 *
 * NAME: PlaidToken - Data Model Class for Plaid Bank Verification API Request/Response
 *
 * DESCRIPTION: This class sends/retrieves data to/from the backend Nodejs server when a POST request is made to generate a link token.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */

public class PlaidToken {

    /**
     * member variable
     */
    private String muserId, link_token , processorToken, itemId;

    public PlaidToken() {
    }

    /**
     *
     * NAME: PlaidToken() - constructor for PlaidToken class
     *
     * SYNOPSIS: public PlaidToken(String muserId, String link_token, String processorToken, String itemId)
     *           mUserId -> user's Id
     *           link_token ->    token generated from a POST request (/create_link_token) made to backend server
     *           processorToken -> token generated from a POST request (/item/public_token/exchange) made to backend server
     *           itemId -> core identifier that map your end-users to their financial institutions. returned from POST (/item/public_token/exchange) request.
     *
     * DESCRIPTION: This is the constructor of the PlaidToken class
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public PlaidToken(String muserId, String link_token, String processorToken, String itemId) {
        this.muserId = muserId;
        this.link_token = link_token;
        this.processorToken = processorToken;
        this.itemId = itemId;
    }

    /**
     *
     * NAME: PlaidToken:getMuserId() - returns the id of the user
     *
     * SYNOPSIS:  public String getMuserId()
     *
     * DESCRIPTION: This is an accessor function that returns the user's id
     *
     * RETURNS: mUserId
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getMuserId() {
        return muserId;
    }


    /**
     *
     * NAME: Messages:setMuserId() - sets the user id
     *
     * SYNOPSIS:  public void setMuserId(String muserId)
     *           mUserId -> user's id
     *
     * DESCRIPTION: This is a setter function. It sets the muserId value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setMuserId(String muserId) {
        this.muserId = muserId;
    }

    /**
     *
     * NAME: PlaidToken:getMuserId() - returns the link_token from the server
     *
     * SYNOPSIS:  public String getLink_token()
     *
     * DESCRIPTION: This is an accessor function that returns the link_token
     *
     * RETURNS: link_token
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getLink_token() {
        return link_token;
    }

    /**
     *
     * NAME: Messages:setLink_token() - sets the link_token value
     *
     * SYNOPSIS: public void setLink_token(String link_token)
     *           link_token -> link token retrieved from server
     *
     * DESCRIPTION: This is a setter function. It sets the link_token value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setLink_token(String link_token) {
        this.link_token = link_token;
    }

    /**
     *
     * NAME: PlaidToken:getprocessorToken() - returns the processorToken from the server
     *
     * SYNOPSIS:  public String getprocessorToken()
     *
     * DESCRIPTION: This is an accessor function that returns the processorToken
     *
     * RETURNS: processorToken
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getprocessorToken() {
        return processorToken;
    }

    /**
     *
     * NAME: Messages:setprocessorToken() - sets the processorToken value
     *
     * SYNOPSIS: public void setprocessorToken(String processorToken)
     *           processorToken -> processor token retrieved from server
     *
     * DESCRIPTION: This is a setter function. It sets the processorToken value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setprocessorToken(String processorToken) {
        this.processorToken = processorToken;
    }

    /**
     *
     * NAME: PlaidToken:getItemId() - returns the itemId from the server
     *
     * SYNOPSIS:  public String getItemId() ()
     *
     * DESCRIPTION: This is an accessor function that returns the itemId
     *
     * RETURNS: itemId
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getItemId() {
        return itemId;
    }

    /**
     *
     * NAME: Messages:setItemId() - sets the itemId value
     *
     * SYNOPSIS: public void setItemId(String itemId)
     *           itemId -> item id retrieved from server
     *
     * DESCRIPTION: This is a setter function. It sets the itemId value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


}
