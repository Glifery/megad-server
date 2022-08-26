#### MegaD adapter for smarthome

MegaD-2561 documentation: https://ab-log.ru/smart-house/ethernet/megad-2561

Megad-2561 API: https://ab-log.ru/smart-house/ethernet/megad-328-api

Node _rest_ lib documentation: https://github.com/cujojs/rest/blob/master/docs/interfaces.md#interface-request

##### How to start:
1. Put google sheet credentials to `src/main/resources/google-sheets-client-secret.json` 
2. Run java application

    > **NOTE!**  
    You could face Google API authorization exception on java application run
    > ````
    > com.google.api.client.auth.oauth2.TokenResponseException: 400 Bad Request
    > ...
    > ````
    > On startup application makes a request to google spreadsheet (if `spreadsheet.disabled = false`) The error means your access is expired.
    >
    > To fix it follow these steps:
    > 1. Clear `/google-tokens` directory if you have something in it
    > 2. Run java application. You should see request in terminal:
    >     ````
    >     Please open the following address in your browser:
    >       https://accounts.google.com/o/oauth2/auth....
    >     ````
    > 3. Open the link in browser and follow authorization process with your google account
    > 4. At the end your browser will be redirected to `http://localhost:8888/Callback` page with message:
    >     ````
    >     Received verification code. You may now close this window.
    >     ````
    >     which means authorization was successfully completed

3. Run webpack watch mode `npm run-script watch`
4. Open monitoring page http://localhost:8080/

##### How to deploy:

1. Deploy files to remote server via SSH:
   - `Dockerfile`
   - `target/smarthome-0.0.1-SNAPSHOT.jar app.jar`
2. Generate google credentials into `google-tokens` folder
3. Run `Dockerfile`