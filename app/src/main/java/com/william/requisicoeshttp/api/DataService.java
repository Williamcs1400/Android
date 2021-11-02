package com.william.requisicoeshttp.api;

import com.william.requisicoeshttp.model.Photo;
import com.william.requisicoeshttp.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {

    @GET("/photos")
    Call<List<Photo>> getPhotos();

    @GET("/posts")
    Call<List<Post>> getPosts();

}
