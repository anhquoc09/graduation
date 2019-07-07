package com.reales.realestate.data.remote;

import com.reales.realestate.EstateApplication;
import com.reales.realestate.data.remote.rest.EstateService;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class ServiceProvider {

    private static RestClient sRestClient;

    private static EstateService sEstateService;

    private ServiceProvider() {
    }

    public static RestClient getRestClient() {
        RestClient client = sRestClient;
        if (client == null) {
            synchronized (ServiceProvider.class) {
                client = sRestClient;
                if (client == null) {
                    client = sRestClient = RestClient.getInstance(EstateApplication.getInstance());
                }
            }
        }
        return client;
    }

    public static synchronized EstateService getEstateService() {
        EstateService estateService = sEstateService;
        if (estateService == null) {
            synchronized (ServiceProvider.class) {
                estateService = sEstateService;
                if (estateService == null) {
                    estateService = sEstateService = getRestClient().create(EstateService.class);
                }
            }
        }
        return estateService;
    }
}