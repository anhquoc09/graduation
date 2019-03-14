package com.example.realestate.data.remote;

import com.example.realestate.EstateApplication;
import com.example.realestate.data.remote.rest.UserService;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class ServiceProvider {

    private static RestClient sRestClient;

    private static UserService sUserService;

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

    public static synchronized UserService getUserService() {
        UserService userService = sUserService;
        if (userService == null) {
            synchronized (ServiceProvider.class) {
                userService = sUserService;
                if (userService == null) {
                    userService = sUserService = getRestClient().create(UserService.class);
                }
            }
        }
        return userService;
    }
}