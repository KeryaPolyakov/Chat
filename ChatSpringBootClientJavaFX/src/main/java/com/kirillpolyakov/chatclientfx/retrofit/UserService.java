package com.kirillpolyakov.chatclientfx.retrofit;

import com.kirillpolyakov.chatclientfx.dto.ResponseResult;
import com.kirillpolyakov.chatclientfx.model.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserService {

    @POST("user")
    Call<ResponseResult<User>> add(@Body User user);

    @GET("user/authentication")
    Call<ResponseResult<User>> authenticate();

    @GET("user/online")
    Call<ResponseResult<List<String>>> getOnline();

}
