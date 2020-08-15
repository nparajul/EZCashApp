package com.example.ezcashapp;

/**
 *
 * NAME: DwollaLogin - Data Model Class for Dwolla customer Registration API Request/Response
 *
 * DESCRIPTION: This class sends/retrieves data to/from the backend Nodejs server when a POST request is made to register a user with Dwolla.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */
public class DwollaLogin {

    private String firstName, lastName, email, customerUrl;

    public DwollaLogin() {
    }


    /**
     *
     * NAME: DwollaLogin() - constructor for DwollaLogin class
     *
     * SYNOPSIS: DwollaLogin(String firstName, String lastName, String email, String customerUrl)
     *           firstName -> user's first Name
     *           lastName -> user's last Name
     *           email -> user's email address
     *           customerUrl -> user's customer URL received upon successful registration
     *
     *
     * DESCRIPTION: This is the constructor of the DwollaLogin class
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public DwollaLogin(String firstName, String lastName, String email, String customerUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.customerUrl = customerUrl;
    }

    /**
     *
     * NAME: DwollaLogin:getCustomerUrl() - returns user's customer URL received upon successful registration
     *
     * SYNOPSIS: public String getCustomerUrl()
     *
     * DESCRIPTION: This is an accessor function that returns the user's customer URL from Dwolla's API upon successful registration
     *
     * RETURNS: customerUrl
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getCustomerUrl() {
        return customerUrl;
    }

    /**
     *
     * NAME: DwollaLogin:setCustomerUrl() - sets user's customer URL received upon successful registration
     *
     * SYNOPSIS: public String setCustomerUrl(String customerUrl)
     *           customerUrl  ->  user's customer URL received upon successful registration
     *
     * DESCRIPTION: This is a setter function. It sets the customerUrl value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setCustomerUrl(String customerUrl) {
        this.customerUrl = customerUrl;
    }

    /**
     *
     * NAME: DwollaLogin:getFirstName() - returns user's first name
     *
     * SYNOPSIS: public String getFirstName()
     *
     * DESCRIPTION: This is an accessor function that returns the user's firstName used in Dwolla registration
     *
     * RETURNS: firstName
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * NAME: DwollaLogin:setFirstName() - sets user's first name
     *
     * SYNOPSIS: public String setFirstName(String firstName)
     *           firstName  -> user's first name
     *
     * DESCRIPTION: This is a setter function. It sets the firstName value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * NAME: DwollaLogin:getLastName() - returns user's last name
     *
     * SYNOPSIS: public String getLastName()
     *
     * DESCRIPTION: This is an accessor function that returns the user's last name used in Dwolla registration
     *
     * RETURNS: lastName
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * NAME: DwollaLogin:setLastName() - sets user's last name
     *
     * SYNOPSIS: public String setLastName(String lastName)
     *           lastName  -> user's last name
     *
     * DESCRIPTION: This is a setter function. It sets the lastName value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * NAME: DwollaLogin:getLastName() - returns user's email address
     *
     * SYNOPSIS: public String getEmail()
     *
     * DESCRIPTION: This is an accessor function that returns the user's email address used in Dwolla registration
     *
     * RETURNS: email
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * NAME: DwollaLogin:setEmail() - sets user's email
     *
     * SYNOPSIS: public String setEmail(String email)
     *           email  -> user's email
     *
     * DESCRIPTION: This is a setter function. It sets the email value
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
