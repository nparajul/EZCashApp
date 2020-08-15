package com.example.ezcashapp;

/**
 *
 * NAME: DwollaBank - Data Model Class for Dwolla Bank Verification API Request/Response
 *
 * DESCRIPTION: This class sends/retrieves data to/from the backend Nodejs server when a POST request is made to verify a user's bank account.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */
public class DwollaBank {

    /**
     * member variables
     */
    String name, bankUrl, transferUrl, status;

    /**
     *
     * NAME: DwollaBank() - constructor for DwollaBank class
     *
     * SYNOPSIS:  public DwollaBank(String name, String bankUrl, String transferUrl, String status)
     *           name -> user's bank account name
     *           bankUrl -> verified bankUrl
     *           transferUrl -> response string received upon a successful money transfer
     *           status -> status of the transfer
     *
     *
     * DESCRIPTION: This is the constructor of the DwollaBank class
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public DwollaBank(String name, String bankUrl, String transferUrl, String status) {
        this.name = name;
        this.bankUrl = bankUrl;
        this.transferUrl = transferUrl;
        this.status = status;
    }


    public DwollaBank() {
    }
    /**
     *
     * NAME: DwollaBank:getName() - returns user's bank account name
     *
     * SYNOPSIS: public String getName()
     *
     * DESCRIPTION: This is an accessor function that returns the user's bank account name as a string
     *
     * RETURNS: name
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getName() {
        return name;
    }

    /**
     *
     * NAME: DwollaBank:setName() - sets user's bank account name
     *
     * SYNOPSIS: public String setName(String name)
     *           name  -> user's bank account name
     *
     * DESCRIPTION: This is a setter function. It sets the name value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * NAME: DwollaBank:getBankUrl() - returns user's verified bank account URL
     *
     * SYNOPSIS: public String getBankUrl()
     *
     * DESCRIPTION: This is an accessor function that returns the user's bank account's URL after the bank is verified
     *
     * RETURNS: bankUrl
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getBankUrl() {
        return bankUrl;
    }

    /**
     *
     * NAME: DwollaBank:setName() - sets user's verified bank account URL
     *
     * SYNOPSIS: public String setBankUrl(String bankUrl)
     *           bankUrl  -> user's verified bank account URL
     *
     * DESCRIPTION: This is a setter function. It sets the bankUrl value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setBankUrl(String bankUrl) {
        this.bankUrl = bankUrl;
    }

    /**
     *
     * NAME: DwollaBank:getTransferUrl() - returns the URL after transferring money from one user to another
     *
     * SYNOPSIS: public String getTransferUrl()
     *
     * DESCRIPTION: This is an accessor function that returns the URL after transferring money from one user to another
     *
     * RETURNS: transferUrl
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getTransferUrl() {
        return transferUrl;
    }

    /**
     *
     * NAME: DwollaBank:setName() - sets the URL after transferring money from one user to another
     *
     * SYNOPSIS: public String setTransferUrl(String transferUrl)
     *           transferUrl  -> the URL after transferring money from one user to another
     *
     * DESCRIPTION: This is a setter function. It sets the transferUrl value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setTransferUrl(String transferUrl) {
        this.transferUrl = transferUrl;
    }

    /**
     *
     * NAME: DwollaBank:getStatus() - returns the status of a transfer
     *
     * SYNOPSIS: public String getStatus()
     *
     * DESCRIPTION: This is an accessor function that returns the status of a transfer
     *
     * RETURNS: status
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * NAME: DwollaBank:setStatus() - sets the status of a transfer
     *
     * SYNOPSIS: public String setStatus(String status)
     *           status  -> the status of a transfer
     *
     * DESCRIPTION: This is a setter function. It sets the status value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
