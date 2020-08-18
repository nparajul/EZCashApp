const express = require('express')
const plaid = require('plaid')
const fs = require('fs')
const http = require('http')
const dwolla = require('dwolla-v2')
const bodyParser = require('body-parser');

var app = express()
app.use(bodyParser.json())

var urlencodedParser = bodyParser.urlencoded({ extended: true });
app.use(bodyParser.urlencoded({ extended: false }));
app.set('view engine', 'ejs');
app.use(express.static('public'));

app.listen(3000, ()=>{
    console.log("Listening on port 3000...")
})

const appKey = '2CNCN3gmQt3upGCGvTUrNBYqMAEWDrphRG9cZzPzrOG4WrnB7b';
const appSecret = 'WhrqGxz1ucY3fJM5N2DrszGJpuS1nG3OUxoKIGb9ItHDoakLC9';

const client = new dwolla.Client({
  key: appKey,
  secret: appSecret,
  environment: 'sandbox' // optional - defaults to production
});

const plaidClient = new plaid.Client({
    clientID: '5f1a0831b89a9900124d8d15',
    secret: 'f9df9903fd3f1383e95a25584f0b7a',
    env: plaid.environments.sandbox,
    options: { version: '2019-05-29' }
  })

app.post('/user', (req,res)=>{

    const requestBody = {
        firstName: req.body.firstName,
        lastName: req.body.lastName,
        email:req.body.email,
        type: 'personal',
        address1: '505 Ramapo Valley Road',
        city: 'Mahwah',
        state: 'NJ',
        postalCode : '07430',
        dateOfBirth: '1997-11-22',
        ssn: '1234' 

    }

    async function getUserUrl(){
        const appToken = await client.auth.client();
        const res = await appToken.post('customers',requestBody);
        const returnUrl = await res.headers.get('location');
        return returnUrl;
    }

    (async()=>{

        const returnUrl = await getUserUrl();
        const dataToApp = {
            customerUrl:returnUrl
        }
        console.log("Done");
        res.status(200).send(JSON.stringify(dataToApp));
    
    })()

})

app.post('/create_link_token', (req, response) => {
    // 1. Grab the client_user_id by searching for the current user in your database
    const clientUserId = req.body.muserId;
    console.log(clientUserId)
  
    // 2. Create a link_token for the given user
    plaidClient.createLinkToken({
      user: {
        client_user_id: clientUserId,
      },
      client_name: 'Nitesh',
      products: ['auth'],
      country_codes: ['US'],
      language: 'en',
      webhook: 'https://sample.webhook.com',
    }, (err, res) => {
        const dataToApp = {
            link_token:res.link_token
        }
    
  
      // 3. Send the data to the client
      console.log('LinkToken = '+ res.link_token);
      response.status(200).send(JSON.stringify(dataToApp));
    });
  })
  
  app.post('/item/public_token/exchange', (req,response)=>{
  
      const publicToken = req.body.publicToken;
      const accountId = req.body.accountId;
  
      console.log('AccountId = ' + accountId);
      console.log('PublicToken = ' + publicToken);
  
      plaidClient.exchangePublicToken(publicToken,function(err,res){
          const accessToken = res.access_token;
          console.log('AccessToken = ' + accessToken);
          const itemId = res.item_id;
          console.log('Item Id: ' + itemId);
  
          plaidClient.createProcessorToken(
              accessToken,
              accountId,
              'dwolla',
              function(err,res){
  
  
                      console.log("Resposne: " + res);
                      const processorToken = res.processor_token;
                      console.log('ProcessorToken = ' + processorToken);
  
                      const dataToApp = {
                          processorToken:processorToken
                      }
          
                      response.status(200).send(JSON.stringify(dataToApp));
              
  
              }
            
          )
  
      });
  })

  app.post('/bank', (req,res)=>{

    
        const customerUrl = req.body.customerUrl;

        var requestBody={
         plaidToken : req.body.plaidToken,
         name : req.body.name,
        };
    

    async function getBankUrl(){
        const appToken = await client.auth.client();
        const res = await appToken.post(`${customerUrl}/funding-sources`,requestBody);
        console.log(res);
        const returnUrl = await res.headers.get('location');
        return returnUrl;
    }

    (async()=>{

        const returnUrl = await getBankUrl();
        const dataToApp = {
            bankUrl:returnUrl
        }
        console.log("Bank Url Done");
        res.status(200).send(JSON.stringify(dataToApp));
    
    })()

})

app.post('/transfer',(req,response)=>{

    console.log("Sender URL: " + req.body.sender_bank_url)
    console.log("Receiver URL: " + req.body.receiver_bank_url)
    var requestBody = {
        _links:{
            source:{
                href: req.body.sender_bank_url
            },
            destination:{
                href:req.body.receiver_bank_url
            },
        },
        amount:{
            currency: "USD",
            value : req.body.amount,
        },
    };

    async function getTransferUrl(){
        const appToken = await client.auth.client();
        const res = await appToken.post("transfers",requestBody);
        const returnUrl = await res.headers.get('location');
        return returnUrl;
    }

    (async()=>{

        const returnUrl = await getTransferUrl();
        const appToken = await client.auth.client();
        const res = await appToken.get(returnUrl);
        const status = await res.body.status;
        const dataToApp = {
            status:status
        }
        console.log("Everything Done!!!");
        response.status(200).send(JSON.stringify(dataToApp));
    
    })()

})



