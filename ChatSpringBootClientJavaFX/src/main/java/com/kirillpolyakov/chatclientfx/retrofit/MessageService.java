package com.kirillpolyakov.chatclientfx.retrofit;

import com.kirillpolyakov.chatclientfx.dto.ResponseResult;
import com.kirillpolyakov.chatclientfx.model.Message;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MessageService {

    @POST("message/{id}")
    Call<ResponseResult<Message>> post(@Body Message training, @Path("id") long id);

    @GET("message")
    Call<ResponseResult<List<Message>>> getAll();

}
