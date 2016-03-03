package com.example.nano1.nytimes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nano1 on 2/10/2016.
 */


public interface NYTimesAPI {
    @GET("svc/topstories/v2/{section}.json?api-key=df07f0ef33849e12edf56f798c2fa288:6:74309049")
    Call<Article> getArticles(
            @Path("section")String section);
}
