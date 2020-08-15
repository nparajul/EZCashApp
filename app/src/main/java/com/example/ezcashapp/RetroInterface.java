package com.example.ezcashapp;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 * NAME: RetroInterface - The interface responsible to handle all requests made to backend nodeJS server.
 *
 * DESCRIPTION: This class handles executes all POST requests made to the backend server and communicates request/response with the backend nodeJS server.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */

public interface RetroInterface {

    @POST("/user")
    Call<DwollaLogin> executeDwollaLogin(@Body HashMap<String, String> map);

    @POST("/bank")
    Call<DwollaBank> executeDwollaBankReg(@Body HashMap<String, String> map);

    @POST("/create_link_token")
    Call<PlaidToken> executePlaidToken(@Body HashMap<String,String> map);

    @POST("/item/public_token/exchange")
    Call<PlaidToken> getProcessorToken(@Body HashMap<String, String> map);

    @POST("/transfer")
    Call<DwollaBank> makeTransfer(@Body HashMap<String, String> map);

}
