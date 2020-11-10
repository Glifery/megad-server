package org.glifery.smarthome.adapter.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import org.glifery.smarthome.application.configuration.SpreadsheetConfig;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SheetsServiceUtil {
    private static final String APPLICATION_NAME = "MegaD config reader";

    public static Sheets getSheetsService(SpreadsheetConfig spreadsheetConfig) throws IOException, GeneralSecurityException {
        Credential credential = GoogleAuthorizeUtil.authorize(spreadsheetConfig);
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
