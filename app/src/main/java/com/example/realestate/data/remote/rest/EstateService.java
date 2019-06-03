package com.example.realestate.data.remote.rest;

import com.example.realestate.data.remote.response.EstateListResponse;
import com.example.realestate.data.remote.response.LoginResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public interface EstateService {

    @FormUrlEncoded
    @POST("users/login_google")
    Observable<LoginResponse> loginGoogle(@Field("id_token") String idToken);


    @FormUrlEncoded
    @POST("projects/home")
    Observable<EstateListResponse> getEstatesByPosition(@Field("radius") String radius,
                                                        @Field("lat") String latitude,
                                                        @Field("long") String longitude);
}
