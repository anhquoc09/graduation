package com.example.realestate.data.remote.rest;

import com.example.realestate.data.model.CodeList;
import com.example.realestate.data.model.SaveEstateResponse;
import com.example.realestate.data.model.SavedEstateListResponse;
import com.example.realestate.data.model.UnSaveEstateResponse;
import com.example.realestate.data.remote.response.EstateListResponse;
import com.example.realestate.data.remote.response.LoginResponse;
import com.example.realestate.data.remote.response.SubmitEstateResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @FormUrlEncoded
    @POST("projects")
    Observable<SubmitEstateResponse> submitEstate(@Header("authorization") String accessToken,
                                                  @Field("name") String title,
                                                  @Field("investor") String investor,
                                                  @Field("price") float price,
                                                  @Field("unit") String unit,
                                                  @Field("area") float square,
                                                  @Field("type") int type,
                                                  @Field("address") String address,
                                                  @Field("info") String description,
                                                  @Field("lat") double latitude,
                                                  @Field("long") double longitude,
                                                  @Field("ownerid") String ownerId,
                                                  @Field("statusProject") int status,
                                                  @Field("createTime") long createTime,
                                                  @Field("updateTime") long updateTime,
                                                  @Field("fullname") String contactName,
                                                  @Field("phone") String contactPhone,
                                                  @Field("email") String contactEmail,
                                                  @Field("avatar") String avatarUrl,
                                                  @Field("url") String[] urls,
                                                  @Field("publicId") String[] publicIds,
                                                  @Field("codelist") CodeList[] codeList);

    @GET("projects/all/{page}")
    Observable<UserListEstateResponse> getListNewEstate(@Path("page") int page);

    @GET("users/listSaved")
    Observable<SavedEstateListResponse> getSavedList(@Header("authorization") String accessToken);

    @GET("users/danhsachproject/{page}")
    Observable<UserListEstateResponse> getCurrentUserListEstate(@Header("authorization") String accessToken,
                                                                @Path("page") int page);

    @GET("users/profile/{user_id}")
    Observable<UserProfileEstateResponse> getProfileById(@Path("user_id") String id);

    @GET("users/projectlist/{id}/{page}")
    Observable<UserListEstateResponse> getUserListEstateById(@Path("id") String id,
                                                             @Path("page") int next);

    @FormUrlEncoded
    @POST("users/follow")
    Observable<SaveEstateResponse> saveProject(@Header("authorization") String accessToken,
                                               @Field("fullname") String name,
                                               @Field("projectid") String projectId,
                                               @Field("createTime") long createTime);

    @FormUrlEncoded
    @POST("users/unfollow")
    Observable<UnSaveEstateResponse> unSaveProject(@Header("authorization") String accessToken,
                                                   @Field("projectid") String projectId);
}
