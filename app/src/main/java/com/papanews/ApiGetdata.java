package com.papanews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiGetdata {

    @GET("PapaNews/getPolitics.php")
    Call<List<Post>> getPosts();

}
